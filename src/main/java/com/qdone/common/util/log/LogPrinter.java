package com.qdone.common.util.log;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.qdone.common.util.MacUtils;
import com.qdone.framework.annotation.Function;
import com.qdone.framework.core.constant.Constants;
import com.qdone.framework.core.page.CoreUtil;
import com.qdone.framework.exception.RRException;

/**
 * 日志打印
 * @author 傅为地
 * 通用日志打印，针对传入多样参数兼容处理
 *  如果传入参数为空，或者只有response。日志不会写入数据库
 */
@Component
@Aspect
public class LogPrinter {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String userName = null; // 用户名
	private String inputArgString="";//传入参数格式化字符串
	private String className = null;// 执行类名
	private String methodName = null;// 执行方法名称
	private String functionName = "";// function注解描述的方法名称
	@Resource
	private Properties mailInfo;
	private static final ThreadLocal<Long> startTimeThreadLocal =new NamedThreadLocal<Long>("ThreadLocal StartTime");
	
	/*是否开启通用日志*/
	@Value("${auto.log.enabled:true}")
	private Boolean isEnabled;
    

	/*是否开启记录数据库日志*/
	@Value("${auto.log.database.enabled:true}")
	private Boolean isDatabaseEnabled;
    
	
	@Autowired
	TaskExecutor taskExecutor;
	

	@Resource(name="logUtil")
	LogUtil logUtil;
	
	/* 切入日志打印 */
	@Pointcut("execution(public * com.qdone.module..*.*(..)) && @annotation(com.qdone.framework.annotation.Function)")
	public void printLog() {
	}

	/* 日志打印 方法执行(前/后) */
	@Around("printLog()")
	public Object doAround(ProceedingJoinPoint pjp) {
			if(isEnabled){
				final SysLog sysLog=new SysLog();
				className = pjp.getTarget().getClass().getName();
				methodName = pjp.getSignature().getName();
				ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				HttpServletRequest request = attributes.getRequest();
				/*SysUsers user = (SysUsers) SessionUtil.getSessionObject(Constant.CURRENT_USER);
				if (user != null && user.getLoginid() != null && !"".equals(user.getLoginid())) {
					userName = user.getLoginid();
				} else {
					userName = "未登录";
				}*/
				Object[] args = pjp.getArgs();
				Method method = ((org.aspectj.lang.reflect.MethodSignature) pjp.getSignature()).getMethod();
				Class<?>[] paremClassTypes = method.getParameterTypes();
				inputArgString=CoreUtil.buildInputParamToString(args,paremClassTypes);
			    //获取注解信息
				Function f = method.getAnnotation(Function.class);
				functionName = f == null ? "" : f.value();
				/*线程池绑定log打印时间*/
				long beginTime = System.currentTimeMillis();//1、开始时间  
			    startTimeThreadLocal.set(beginTime);		//线程绑定变量（该数据只有当前请求的线程可见）
				logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S").format(beginTime)+" 用户:" + userName + " [" + functionName + "] 开始:" + className + "." + methodName + "() 参数:"+ inputArgString);
				// 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行
				Object result=null;
				try {
					//删除线程变量中的数据，防止内存泄漏
				    startTimeThreadLocal.remove();
				    //开启日志记录数据库
				    if(isDatabaseEnabled){
							//数据库记录日志
						    sysLog.setTitle(functionName);
						    sysLog.setType(Constants.LogType.NORMAL.getVal());
						    sysLog.setUserId(userName);
						    sysLog.setDatabase(logUtil.getDataBase());
						    sysLog.setCreateDate(new Date());
						    sysLog.setUserAgent(request.getHeader("user-agent"));
						    sysLog.setRemoteIp(request.getRemoteHost());
						    sysLog.setRequestUri(request.getRequestURI());//访问路径
						    sysLog.setRequestMethod(request.getMethod());//请求方式
						    sysLog.setRequestParams(inputArgString);
						    sysLog.setRequestMac(MacUtils.getMac());
						    sysLog.setException(null);
						    sysLog.setClassName(className);
						    sysLog.setFunctionName(functionName);
						    sysLog.setMethodName(methodName);
						    sysLog.setActionThread(Thread.currentThread().getName());
						    sysLog.setActionStartTime(new Date(beginTime));
							result = pjp.proceed();// result的值就是被拦截方法的返回值
							long endTime = System.currentTimeMillis(); 	//2、结束时间
							if (result == null) {
								logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S").format(endTime)+" 用户:" + userName + " [" + functionName + "] 结束:" + className + "." + methodName + "() 返回:{}");
							} else {
								if (result instanceof String) {
									logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S").format(endTime)+" 用户:" + userName + " [" + functionName + "] 结束:" + className + "." + methodName + "() 返回:"+ result);
								} else {
									logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S").format(endTime)+" 用户:" + userName + " [" + functionName + "] 结束:" + className + "." + methodName + "() 返回:"+ JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss"));
								}
							}
							sysLog.setActionEndTime(new Date(endTime));
						    sysLog.setActionTime(endTime-beginTime);
							/*日志写入数据库*/
							FutureTask<Object> task = new FutureTask<Object>(new Callable<Object>() {
								@Override
								public Object call() throws Exception {
									try {
										if(!CoreUtil.isEmpty(sysLog.getRequestParams())){
											logger.info("线程池异步[开始] 数据库记录[正常]日志 starting.................................................");
											logUtil.insert(sysLog);
											logger.info("线程池异步[结束] 数据库记录[正常]日志 ending.................................................");
										}
									} catch (Throwable e) {
										logger.error("线程池异步 记录日志失败", e);
									}
									return "ok";
								}
							});
							taskExecutor.execute(task);	//为提升访问速率, 日志记录采用异步的方式进行.
					
					}else{//不开启日志记录数据库，只记录文件
						result = pjp.proceed();// result的值就是被拦截方法的返回值
						long endTime = System.currentTimeMillis(); 	//2、结束时间
						if (result == null) {
							logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S").format(endTime)+" 用户:" + userName + " [" + functionName + "] 结束:" + className + "." + methodName + "() 返回:{}");
						} else {
							if (result instanceof String) {
								logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S").format(endTime)+" 用户:" + userName + " [" + functionName + "] 结束:" + className + "." + methodName + "() 返回:"
										+ result);
							} else {
								logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S").format(endTime)+" 用户:" + userName + " [" + functionName + "] 结束:" + className + "." + methodName + "() 返回:"+ JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss"));
										
							}
						}
					}
					return result;
				} catch (Throwable e1) {
					e1.printStackTrace();
					logger.error("LogPrinter执行代码块时异常："+e1);
					if(isDatabaseEnabled){
						sysLog.setActionEndTime(new Date());
					    sysLog.setActionTime(System.currentTimeMillis()-beginTime);
					    sysLog.setType(Constants.LogType.ABNORMAL.getVal());
					    sysLog.setException(e1.toString());
						/*日志写入数据库*/
						FutureTask<Object> task = new FutureTask<Object>(new Callable<Object>() {
							@Override
							public Object call() throws Exception {
								try {
									if(!CoreUtil.isEmpty(sysLog.getRequestParams())){
										logger.error("线程池异步[开始] 数据库记录[异常]日志 starting.................................................");
										logUtil.insert(sysLog);
										logger.error("线程池异步[结束] 数据库记录[异常]日志 ending.................................................");
									}
								} catch (Throwable e) {
									logger.error("线程池异步 记录日志失败", e);
								}
								return "ok";
							}
						});
						taskExecutor.execute(task);	//为提升访问速率, 日志记录采用异步的方式进行.
					}
					/*继续让Spring全局异常处理，跳转页面*/
					throw new RRException(e1.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value());
				}
			}else{
				try {
					return pjp.proceed();
				} catch (Throwable e) {
					e.printStackTrace();
					logger.error("LogPrinter执行代码块时异常："+e);
					/*继续让Spring全局异常处理，跳转页面*/
					throw new RRException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value());
				}
			}
	}
  
}
