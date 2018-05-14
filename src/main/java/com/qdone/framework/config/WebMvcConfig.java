
package com.qdone.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.qdone.framework.filter.AppInterceptor;

/**
 * @author 付为地
 * @since 2018-5-1 20:50 WebMvcConfigurerAdapter在2.0.1高版本已经被抛弃，
 *        测试发现WebMvcConfigurationSupport页面redirect跳转无法找到 本处直接使用implements
 *        WebMvcConfigurer方式
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private AppInterceptor appInterceptor;

	@Value("${swagger.open:true}")
	private Boolean isOpen;
	
	/**
	 * 系统欢迎页面
	 *  类似于web.xml里面的welecome-file
	 */
	@Override
    public void addViewControllers( ViewControllerRegistry registry ) {
        /*registry.addViewController( "/" ).setViewName("redirect:/swagger-ui.html");*/
        registry.addViewController( "/" ).setViewName("forward:/student/init");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE );
    } 

	/**
	 * 配置资源放行
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/*
		 * 配置静态资源路径
		 */
		registry.addResourceHandler("/page/static/**").addResourceLocations("classpath:/page/static/");
		/*registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");*/
		/**
		 * 配置swagger映射路径
		 */
		if (isOpen) {
			registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
	}

	/**
	 * 配置CORS跨域支持
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowCredentials(true)
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").maxAge(3600);
	}

	/**
	 * 添加api interceptor
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(appInterceptor).addPathPatterns("/app/**");
	}
	
   
}
