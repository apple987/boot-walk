package com.qdone.framework.exception;

import java.util.Properties;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
/*import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;*/

/**
 * 异常配置
 */
@Configuration
public class ExceptionConfig implements ErrorPageRegistrar{

	/**
	 * 异常处理
	 */
	@Bean
	public ExceptionResolver exceptionResolver() {
		ExceptionResolver resolver = new ExceptionResolver();
		resolver.setDefaultErrorView("/500");
		resolver.setDefaultStatusCode(404);
		Properties statusCodes = new Properties();
		statusCodes.setProperty("/500", "500");
		statusCodes.setProperty("/404", "404");
		resolver.setStatusCodes(statusCodes);
		Properties mappings = new Properties();
		mappings.setProperty("java.sql.SQLException", "/500");
		mappings.setProperty("org.springframework.web.bind.ServletRequestBindingException", "/500");
		mappings.setProperty("java.lang.IllegalArgumentException", "/500");
		mappings.setProperty("java.lang.Exception", "/500");
		mappings.setProperty("java.lang.ArithmeticException", "/500");
		mappings.setProperty("org.springframework.web.multipart.MaxUploadSizeExceededException", "/500");
		mappings.setProperty("com.example.util.RRException", "/500");
		resolver.setExceptionMappings(mappings);
		return resolver;
	}


    /**
     *  异常页面处理
     *    ErrorPage相当于mvc的web配置文件里面默认配置
     *    本处特殊处理404页面错误
     */
	@Override
	public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
        /*1、按错误的类型显示错误的网页*/
        /*错误类型为404，找不到网页的，默认显示404.html网页*/
        ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
        /*错误类型为500，表示服务器响应错误，默认显示500.html网页*/
        /*ErrorPage e500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
        errorPageRegistry.addErrorPages(e404, e500);*/
        errorPageRegistry.addErrorPages(e404);
    }
	
	
  /*  @Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/page/404.html"));
			}
		};
	}*/
}
