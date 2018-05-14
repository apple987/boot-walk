package com.qdone.module.controller;

import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qdone.module.model.Solr;
import com.qdone.module.model.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import redis.clients.jedis.JedisCluster;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "测试接口", description = "简单测试接口")
@RestController
public class TestController {
	
	@Autowired
	JedisCluster jedisCluster;

	@ApiOperation(value = "模拟拿postData数据", notes = "测试postData", httpMethod = "POST", response = Solr.class)
	@RequestMapping(value = "/test/postData", method = RequestMethod.POST)
	@ApiResponse(code = 200, message = "TestController响应请求成功", response = Solr.class, responseContainer = "Set")
	public Solr getData(@ApiParam(name = "Solr对象", value = "传入json格式", required = true) @RequestBody Solr entity) {
		System.err.println("传入对象名称name:" + entity.getName());
		jedisCluster.set("apple", entity.getName());
		Solr sr = new Solr();
		sr.setId("123456");
		sr.setPrice(250);
		sr.setName("简单restTemplate测试");
		sr.setTitle("简单restTemplate测试,标题文件");
		sr.setCreatetime(new Date());
		return sr;
	}

	@ApiIgnore
	@ApiOperation(value = "模拟拿getJSP数据", position = 0, notes = "测试getJSP", response = Solr.class)
	@RequestMapping(value = "/getJSP", method = RequestMethod.GET)
	public String getJSP(@RequestParam Map<String, Object> param) {
		System.err.println("传入参数:" + param);
		System.err.println("通过jedis集群获取参数是:" + jedisCluster.get("apple"));
		return "solr/selectSolr";
	}

	 /**
     *  创建用户
     *    处理 "/users" 的 POST 请求，用来获取用户列表
     *    通过 @ModelAttribute 绑定参数，也通过 @RequestParam 从页面中传递参数
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String postUser(ModelMap map,
                           @ModelAttribute @Valid User user,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
        	System.err.println(bindingResult.getFieldError());
        	System.err.println(bindingResult.getFieldError().getDefaultMessage());
            map.addAttribute("action", "create");
            return "userForm";
        }


        return "redirect:/";
    }

}
