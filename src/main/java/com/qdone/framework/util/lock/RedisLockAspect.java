package com.qdone.framework.util.lock;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * redis分布式锁切面处理
 * @author 付为地
 */
@Component
@Aspect
public class RedisLockAspect {
	private static Logger logger= LoggerFactory.getLogger(RedisLockAspect.class);
	@Autowired
    private RedissonClient redissonClient;

	/**
	 * 切面处理分布式锁
	 * @param point
	 * @throws Throwable
	 */
    @Around("execution(public * com.qdone.module..*(..)) && @annotation(com.qdone.framework.util.lock.RedisLock)")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        RLock lock = null;
        Object object = null;
        try {
            RedisLock redisLock = getDistRedisLockInfo(point);
            String lockKey = getLockKey(point, redisLock.lockKey());
            lock =redissonClient.getFairLock(lockKey);
            if (lock != null) {
                Boolean status = lock.tryLock(redisLock.maxSleepMills(), redisLock.keepMills(), TimeUnit.MILLISECONDS);
                if (status) {
                	logger.info("==============获得分布式锁成功===============");
                    object = point.proceed();
                }else{
                	logger.info("==============获得分布式锁失败===============");
                }
            }else{
            	throw new IllegalArgumentException("==============分布式锁配置参数错误===============");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("==============处理分布式锁异常==============="+e.getMessage());
        }finally {
            if (lock != null) {
            	if(lock.isHeldByCurrentThread()){
                	logger.info("==============释放分布式锁成功===============");
                    lock.unlock();
            	}else{
            		logger.info("==============系统已回收分布式锁=============");
            	}
            }else{
            	logger.info("==============系统已回收分布式锁=============");
            }
        }
        return object;
    }
    /**
     * 切面获得RedisLock注解信息
     * @param point
     * @return
     */
    private RedisLock getDistRedisLockInfo(ProceedingJoinPoint point) {
        try {
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            Method method = methodSignature.getMethod();
            return method.getAnnotation(RedisLock.class);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
    }
    
    /**
     * 获取包括方法参数上的key
     * redis key的拼写规则为 "DistRedisLock+" + lockKey + @DistRedisLockKey<br/>
     *
     * @param point
     * @param lockKey
     * @return
     */
	private  String getLockKey(ProceedingJoinPoint point, String lockKey) {
        try {
            lockKey = "DistRedisLock:" + lockKey;
            Object[] args = point.getArgs();
            if (args != null && args.length > 0) {
                MethodSignature methodSignature = (MethodSignature)point.getSignature();
                Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
                SortedMap<Integer, String> keys = new TreeMap<>();
                for (int i = 0; i < parameterAnnotations.length; i ++) {
                    RedisLockKey redisLockKey = getAnnotation(RedisLockKey.class, parameterAnnotations[i]);
                    if (redisLockKey != null) {
                        Object arg = args[i];
                        if (arg != null) {
                            keys.put(redisLockKey.order(), arg.toString());
                        }
                    }
                }
                if (keys != null && keys.size() > 0){
                    for (String key : keys.values()) {
                        lockKey += key;
                    }
                }
            }

            return lockKey;
        } catch (Exception e) {
            logger.error("getLockKey error.", e);
        }
        return null;
    }

    /**
     * 获取注解类型
     * @param annotationClass
     * @param annotations
     * @param <T>
     * @return
     */
	@SuppressWarnings("unchecked")
	private  <T extends Annotation> T getAnnotation(final Class<T> annotationClass, final Annotation[] annotations) {
        if (annotations != null && annotations.length > 0) {
            for (final Annotation annotation : annotations) {
                if (annotationClass.equals(annotation.annotationType())) {
                    return (T) annotation;
                }
            }
        }
        return null;
    }
}
