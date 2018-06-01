package com.qdone.common.util;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
/**
 * 登陆成功用来
 *   存储登录人及其他信息
 * @author user
 *
 */
public class SessionUtil {

	public static Object getSessionObject(String key){
		HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession(); 
		return session.getAttribute(key);
	}
	
	public static void setSessionObject(String key,Object value){
		HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession(); 
		session.setAttribute(key, value);
	}
	
	public static HttpServletRequest getRequest(){
		HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	public static HttpServletResponse getReponse(){
		HttpServletResponse response =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		return response;
	}
	
	public static Object getSession(){
		return getRequest().getSession();
	}
}
