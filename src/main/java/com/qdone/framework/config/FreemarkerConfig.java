package com.qdone.framework.config;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import freemarker.template.utility.XmlEscape;

/**
 * Freemarker配置
 *   抛弃WebMvcConfigurationSupport，这个会导致一系列找不到页面错误
 */
@Configuration
public class FreemarkerConfig{

	/**
	 * 配置freemaker视图处理
	 */
	@Bean
    public ViewResolver viewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setViewClass(org.springframework.web.servlet.view.freemarker.FreeMarkerView.class);
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setSuffix(".html");
        resolver.setOrder(0);
        resolver.setCache(true);
        resolver.setExposeRequestAttributes(true);
        resolver.setExposeSessionAttributes(true);
        resolver.setExposeSpringMacroHelpers(true);
        resolver.setAllowRequestOverride(true);
        resolver.setAllowSessionOverride(true);
        resolver.setRequestContextAttribute("request");
        return resolver;
    }
	
	/**
	 * 配置freemaker
	 */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(@Value("${web.path}") String staticPath,
    		@Value("${freemarker.template.path:classpath:/templates/}") String templatePath
    		){
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setDefaultEncoding("UTF-8");
        /*configurer.setTemplateLoaderPath("classpath:/templates/view");*/
        /*模板文件所在路径*/
        configurer.setTemplateLoaderPath(templatePath);
        Map<String, Object> variables = new HashMap<>(1);
        /*variables.put("shiro", shiroTag);*/
        /*xml转码器*/
        variables.put("xml_escape", new XmlEscape());
        /*动静分离时，可以把静态文件的路径配置在这里，页面直接可以取${static_path}css/share.css,其他属性类似写法就好 */
        variables.put("static_path", staticPath);
        configurer.setFreemarkerVariables(variables);
        Properties settings = new Properties();
        settings.setProperty("default_encoding", "UTF-8");
        settings.setProperty("number_format", "0.##");
        settings.setProperty("classic_compatible", "true");
        settings.setProperty("url_escaping_charset", "UTF-8");
        settings.setProperty("template_update_delay", "0");
        settings.setProperty("output_encoding", "UTF-8");
        settings.setProperty("locale", "zh_CN");
        settings.setProperty("boolean_format", "true,false");
        settings.setProperty("date_format", "yyyy-MM-dd");
        settings.setProperty("datetime_format", "yyyy-MM-dd HH:mm:ss");
        configurer.setFreemarkerSettings(settings);
        return configurer;
    }
    
    
    

}
