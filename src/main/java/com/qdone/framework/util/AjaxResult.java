package com.qdone.framework.util;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;


/**
 * Ajax处理结果对象
 * @author付为地
 */
public class AjaxResult implements Serializable {
	/**
	 * @fields serialVersionUID 
	 */
	private static final long serialVersionUID = 5905715228490291386L;
	/**
	 * @fields status  状态信息，正确返回OK，否则返回 ERROR，如果返回ERROR则需要填写Message信息
	 */
	private Status status;
	/**
	 * @fields record 消息对象
	 */
	private Object message;

	public AjaxResult() {
		super();
	}

	/**
	 * @description 
	 * @param status 状态
	 * @param message 消息
	 */
	public AjaxResult(Status status, Object message) {
		this.status = status;
		this.message = message;
	}

	/**
	 * 结果类型信息
	 * @author fuiou
	 * @date 2014年3月7日下午4:20:23
	 */
	public enum Status {
		OK, ERROR
	}

	/**
	 * 添加成功结果信息
	 * @param record
	 */
	public void addOK(Object message) {
		this.message = message;
		this.status = Status.OK;
	}

	/**
	 * 添加错误消息
	 * @param message
	 */
	public void addError(Object message) {
		this.message = message;
		this.status = Status.ERROR;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}
	
	public String toJsonString(){
	/*	JSONObject json = new JSONObject();  
        json.put("status", this.status);  
        json.put("message", this.message);  */
        Map<String,Object> parMap=new HashMap<String,Object>();
        parMap.put("status", this.status);  
        parMap.put("message", this.message);
        return JSON.toJSONString(parMap);
	}
	
	
	

}
