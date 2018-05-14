package com.qdone.module.controller;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.qdone.common.util.ZxingQRCode;
import com.qdone.module.model.User;

import springfox.documentation.annotations.ApiIgnore;


/**
 * Created by Gensis on 2016/9/9.
 */
@ApiIgnore
@Controller
public class HelloController {

    // 从 application.properties 中读取配置，如取不到默认值为Hello
    @Value("${application.hello:Hello}")
    private String hello;

    @Value("${qrcode.logo}")
    private String logo;
    
   /* @RequestMapping("/")
    public String welcome() {
    	System.err.println("你好啊");
        return "redirect:/swagger-ui.html";
    }*/
    @RequestMapping("/helloJsp")
    public String helloJsp(Map<String, Object> map) {
        System.out.println("HelloController.helloJsp().hello=" + hello);
        map.put("hello", hello);
        return "helloJsp";

    }
    
    @RequestMapping("/page1")
    public ModelAndView page1(){  
        // 页面位置 /WEB-INF/jsp/page/page.jsp  
        ModelAndView mav = new ModelAndView("page");  
        mav.addObject("content", "哈哈哈第一种");  
        return mav;  
    }  
    
    @RequestMapping("/pageTest")
    @ResponseBody
    /*@Cacheable(value="defaultCache",key="#root.targetClass.toString().substring(#root.targetClass.toString().lastIndexOf('.')+1).concat('.'+#root.method.name).concat('.'+#user.getName()+'.'+#user.getAge())")*/
    @Cacheable(value="defaultCache",key="#user.getName()")
    public String pageTest(User user){  
    	System.err.println("进入pageTest方法，本次传入参数:name="+user.getName()+",age="+user.getAge());
    	return user.getName();
    }  
    
    
    @RequestMapping("/pageTest1")
    @ResponseBody
    /*@Cacheable(value="defaultCache",key="#root.targetClass.toString().substring(#root.targetClass.toString().lastIndexOf('.')+1).concat('.'+#root.method.name).concat('.'+#user.getName()+'.'+#user.getAge())")*/
    @Cacheable(value="view",key="#user.getName()")
    public String pageTest1(User user){  
    	System.err.println("进入pageTest1方法，本次传入参数:name="+user.getName()+",age="+user.getAge());
    	return user.getName();
    }  
	

    @RequestMapping("/toQrcode")
	public String toQrcode(HttpServletRequest request,
			HttpServletResponse response) {
		String content="apple95272591234";
		request.getSession().setAttribute("qrdata", content);
		return "qr_code";
	}
	
	
	
	@RequestMapping("/toZxingQrcode")
	public String toZxingQrcode(HttpServletRequest request,
			HttpServletResponse response) {
		String content="apple95272591234";
		request.getSession().setAttribute("qrdata", content);
		return "zxing_qr_code";
	}
	
	@RequestMapping("/getZxingQrcode")
	public void getZxingQrcode(HttpServletRequest request,
			HttpServletResponse response) {
		System.err.println("执行生成二维码: StaffController.getZxingQrcode()");
		try {
			String content=(String) request.getSession().getAttribute("qrdata");
			//生成带图标的二维码
			BufferedImage img=ZxingQRCode.genBarcode(content, 38,logo);
			//生成二维码QRCode图片  
            ImageIO.write(img, "jpg", response.getOutputStream());
		} catch (Exception e) {
			System.err.println("执行生成二维码: StaffController.getZxingQrcode() 异常:"+e.getMessage());
		}
		System.err.println("执行生成二维码: StaffController.getZxingQrcode() 完毕");
	}
	
	 @RequestMapping("/testJson")
	 @ResponseBody
	 public Map<String,Object> testJson(){  
	    	System.err.println("进入testJson方法");
	    	/*Map<String,Object> result=new ConcurrentHashMap<String,Object>();*/
	    	Map<String,Object> result=new HashMap<String,Object>();
	    	result.put("a", null);
	    	result.put("b", "");
	    	return result;
	 }  
}