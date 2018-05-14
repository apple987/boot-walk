package com.qdone.common.util;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 响应数据
 */
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 3997124446365032582L;
	/**
	 * 错误码
	 */
	@ApiModelProperty(value = "错误码", required = true)
	private Integer code = 200;
	@ApiModelProperty(value = "数据", required = true)
	private T data;
	@ApiModelProperty(value = "消息提示")
	private String msg="请求成功";
	/**
	 * 业务码
	 */
	@ApiModelProperty(value = "业务码")
	private Integer bizCode=200;

	public Result() {
		super();
	}

	public Result(Integer code, Integer bizCode, String msg) {
		super();
		this.code = code;
		this.bizCode = bizCode;
		this.msg = msg;
	}

	public Result(Integer code, Integer bizCode, String msg, T data) {
		super();
		this.code = code;
		this.bizCode = bizCode;
		this.msg = msg;
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getBizCode() {
		return bizCode;
	}

	public void setBizCode(Integer bizCode) {
		this.bizCode = bizCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
