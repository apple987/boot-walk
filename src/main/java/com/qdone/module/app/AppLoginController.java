package com.qdone.module.app;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.qdone.common.util.JwtUtils;
/*import com.rainsoft.mvc.controller.Result;
import com.rainsoft.mvc.test.User;*/
import com.qdone.common.util.Result;
import com.qdone.framework.annotation.Function;
import com.qdone.framework.annotation.Login;
import com.qdone.module.model.Student;
import com.qdone.module.model.User;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * APP登录授权
 *  app模块的路径前缀都是app开头，AppInterceptor针对app/**路径拦截处理
 */
@Api(tags = "APP登录",description = "演示服务接口")
@RestController
@RequestMapping("/app")
public class AppLoginController {
	
    @Autowired
    private JwtUtils jwtUtils;
    
    
    /**
     * 登录
     *   相同账号，每一次登录生成的token都是不一样的
     *     比如说第一次生成一个token，虽然token还没有过期，但是第二次继续登录，又重新生成一个token
     *     这就会导致，相同账户两个token都是有效的，那么获取数据的时候，使用第一次生成的token，也可以拿到数据吗？
     *     这显然不合适，这里需要加一个处理
     *     简单点就是以最后一次登录生成的token为有效，生成最后一次token时，销毁当前账户，以前的token，保证最后一次登录的token才有效果。
     *     方案优化版本：某一次登录操作，需要生成token时，先判断上一次获取token是否有效，如果依然有效，那就继续返回上次生成token给用户
     *               如果上次token已经失效，那就重新生成新token给用户
     *        
     */
    @ApiOperation(value = "APP登录", httpMethod = "POST", notes = "手机端登录", response = Result.class)
    @PostMapping("login")
    public Result<HashMap<String, Object>> login(@ApiParam(required = true, value = "账户名称", name = "userId") @RequestParam(value = "userId") String userId,
    		@ApiParam(required = true, value = "账户密码", name = "password") @RequestParam(value = "password")  String password){
    	Assert.isTrue(StringUtils.isNotEmpty(userId), "账户名称不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(password), "账户密码不能为空");
    	Result<HashMap<String, Object>> res=new Result<HashMap<String, Object>>();
        /*
         * 用户登录
         * 1.正常流程实际上，先根据用户名密码，数据库查询符合要求的用户信息
         *   如果存在用户，就走后续逻辑
         *   如果不存在用户，就直接抛出异常信息给前端 
         *  本处为了模拟这个操作，暂时直接new一个对象，存储对应信息 
         */
    	String token = jwtUtils.generateToken(userId);
    	System.err.println("生成token是:"+token);
    	HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        res.setData(map);
        //本次登录成功APP用户信息，存入redis中
        Claims claims =jwtUtils.getClaimByToken(token);
        /*保证两个token在redis和生成时，完全一致的过期，剩余过期时间(秒)*/
        User usr=new User();
    	usr.setName(userId);
    	usr.setPassword(password);
    	usr.setToken(token);
    	usr.setSex(1);
    	usr.setAge(20);
        long remain=jwtUtils.getExpire()-(new Date().getTime()-claims.getIssuedAt().getTime())/1000;
    	jwtUtils.set((jwtUtils.AppTokenPrefix+userId).getBytes(), (int)remain, usr);
        return res;
    }

    /**
     * 输入token和userId
     * @param token
     * @param userId
     * @return
     */
    @Login
    @GetMapping("userId")
    public Result<User> userInfo(
    		@ApiParam(required = false, value = "令牌", name = "token") @RequestHeader(value = "token")  String token,
    		@ApiParam(required = false, value = "用户名", name = "userId") @RequestHeader(value = "userId")  String userId){
    	System.err.println(token);
   	    Result<User> res=new Result<User>();
        /*本处拦截器已经验证了token和userId的唯一对应关系，所以可以直接去userId*/
    	User usr1=jwtUtils.get((jwtUtils.AppTokenPrefix+userId).getBytes(), User.class);
    	System.err.println("序列化拿到的用户信息是:"+JSON.toJSONString(usr1));
    	res.setData(usr1);
    	return res;
    }
    
    /***********************************模拟多种方式传递参数，特殊性针对request,map,mutisort三种******************************************************************/
    @ApiOperation(value = "logParam", httpMethod = "POST", notes = "logParam", response = Result.class)
    @PostMapping("testParam")
    @Function("logParam")
    public Result<HashMap<String, Object>> testParam(@ApiParam(required = true, value = "账户名称", name = "userId") @RequestParam(value = "userId") String userId,
    		@ApiParam(required = true, value = "账户密码", name = "password") @RequestParam(value = "password")  String password
    		,HttpServletRequest request,HttpServletResponse resp,
    		@ApiParam(name = "学生对象", value = "传入json格式", required = true) @RequestBody Student entity,
    		@ApiParam(name = "map", value = "map", required = true) @RequestParam Map<String,Object> mp
    		){
    	Assert.isTrue(StringUtils.isNotEmpty(userId), "账户名称不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(password), "账户密码不能为空");
    	Result<HashMap<String, Object>> res=new Result<HashMap<String, Object>>();
    	return res;
    }
    
    @ApiOperation(value = "logParam1", httpMethod = "POST", notes = "logParam1", response = Result.class)
    @PostMapping("testParam1")
    @Function("logParam1")
    public Result<HashMap<String, Object>> testParam1(@ApiParam(required = true, value = "账户名称", name = "userId") @RequestParam(value = "userId") String userId,
    		@ApiParam(required = true, value = "账户密码", name = "password") @RequestParam(value = "password")  String password
    		,HttpServletResponse resp,
    		@ApiParam(name = "学生对象", value = "传入json格式", required = true) @RequestBody Student entity,
    		@ApiParam(name = "map", value = "map", required = true) @RequestParam Map<String,Object> mp
    		){
    	Assert.isTrue(StringUtils.isNotEmpty(userId), "账户名称不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(password), "账户密码不能为空");
    	Result<HashMap<String, Object>> res=new Result<HashMap<String, Object>>();
    	return res;
    }
    
    @ApiOperation(value = "logParam2", httpMethod = "POST", notes = "logParam2", response = Result.class)
    @PostMapping("testParam2")
    @Function("logParam2")
    public Result<HashMap<String, Object>> testParam2(@ApiParam(required = true, value = "账户名称", name = "userId") @RequestParam(value = "userId") String userId,
    		@ApiParam(required = true, value = "账户密码", name = "password") @RequestParam(value = "password")  String password
    		,HttpServletResponse resp,
    		@ApiParam(name = "学生对象", value = "传入json格式", required = true) @RequestBody Student entity
    		){
    	Assert.isTrue(StringUtils.isNotEmpty(userId), "账户名称不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(password), "账户密码不能为空");
    	Result<HashMap<String, Object>> res=new Result<HashMap<String, Object>>();
    	return res;
    }
    
    @ApiOperation(value = "logParam3", httpMethod = "POST", notes = "logParam3", response = Result.class)
    @PostMapping("testParam3")
    @Function("logParam3")
    public Result<HashMap<String, Object>> testParam3(@ApiParam(required = true, value = "账户名称", name = "userId") @RequestParam(value = "userId") String userId,
    		@ApiParam(required = true, value = "账户密码", name = "password") @RequestParam(value = "password")  String password
    		,HttpServletResponse resp
    		){
    	/*Assert.isTrue(StringUtils.isNotEmpty(userId), "账户名称不能为空");*/
        Assert.isTrue(StringUtils.isNotEmpty(password), "账户密码不能为空");
    	Result<HashMap<String, Object>> res=new Result<HashMap<String, Object>>();
    	return res;
    }
    
    
    @ApiOperation(value = "logParam4", httpMethod = "POST", notes = "logParam4", response = Result.class)
    @PostMapping("testParam4")
    @Function("logParam4")
    public Result<HashMap<String, Object>> testParam4(HttpServletResponse resp){
    	Result<HashMap<String, Object>> res=new Result<HashMap<String, Object>>();
    	return res;
    }
    
    @ApiOperation(value = "logParam5", httpMethod = "POST", notes = "logParam5", response = Result.class)
    @PostMapping("testParam5")
    @Function("logParam5")
    public Result<HashMap<String, Object>> testParam5(){
    	Result<HashMap<String, Object>> res=new Result<HashMap<String, Object>>();
    	return res;
    }
    
}
