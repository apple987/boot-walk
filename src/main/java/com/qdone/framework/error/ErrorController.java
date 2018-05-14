package com.qdone.framework.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 付为地 错误跳转页面
 */
@Controller
@RequestMapping("/")
public class ErrorController {

	/* 错误提示页跳转 */
	@RequestMapping("/400")
	public String error400() {
		return "error/500";
	}

	@RequestMapping("/401")
	public String error401() {
		return "error/401";
	}

	@RequestMapping("/404")
	public String error404() {
		return "error/404";
	}

	@RequestMapping("/406")
	public String error406() {
		return "error/500";
	}

	@RequestMapping("/500")
	public String error500() {
		return "error/500";
	}

}
