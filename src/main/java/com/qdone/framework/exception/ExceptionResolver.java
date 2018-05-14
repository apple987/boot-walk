package com.qdone.framework.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.qdone.framework.util.AjaxResult;

/**
 * 全局异常处理器
 * 
 * @author 付为地
 */
public class ExceptionResolver extends SimpleMappingExceptionResolver {
	
	static Logger logger=LoggerFactory.getLogger(ExceptionResolver.class);
	
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		response.setCharacterEncoding("utf-8");
		String viewName = determineViewName(ex, request);
		if (viewName != null) {
			if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request
					.getHeader("X-Requested-With") != null && request
					.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
				// 非异步方式返回
				Integer statusCode = determineStatusCode(request, viewName);
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				logger.error("异常处理", ex);
				// 跳转到提示页面
				AjaxResult result = new AjaxResult();
				result.addError(ex.getMessage());
				request.getSession().setAttribute("result", result);
				return getModelAndView(viewName, ex, request);
			} else {
				// 异步方式返回
				try {
					AjaxResult result = new AjaxResult();
					result.addError(ex.getMessage());
					request.getSession().setAttribute("result", result);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("ajax异常:", e);
				}
				logger.error("ajax异常处理:", ex);
				return getModelAndView(viewName, ex, request);
			}
		} else {
			logger.error("异常处理:没有找到异常处理信息");
			return getModelAndView("/500", ex, request);
		}
	}
	
}
