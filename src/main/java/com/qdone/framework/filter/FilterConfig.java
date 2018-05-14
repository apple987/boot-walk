package com.qdone.framework.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;

import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextListener;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.BeanTypeAutoProxyCreator;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.qdone.framework.util.xss.XssFilter;

import net.bull.javamelody.MonitoringFilter;
import net.bull.javamelody.SessionListener;

/**
 * 配置过滤器 druib监控
 */
@Configuration
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FilterConfig {

	
	/**
	 * 配置XSS
	 * @throws IOException 
	 */
	@Bean
	public FilterRegistrationBean xssFilterRegistration() throws IOException {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setFilter(new XssFilter());
		registration.addUrlPatterns("/*");
		registration.setName("xssFilter");
		registration.setOrder(Integer.MAX_VALUE);
		return registration;
	}

	/**
	 * RequestContextListener注册
	 */
	@Bean
	public ServletListenerRegistrationBean<RequestContextListener> requestContextListenerRegistration() {
		return new ServletListenerRegistrationBean<>(new RequestContextListener());
	}

	/**
	 * druidServlet注册
	 */
	@Bean
	public ServletRegistrationBean druidServletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new StatViewServlet());
		registration.addUrlMappings("/druid/*");
		return registration;
	}

	/**
	 * druid监控 配置URI拦截策略
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean druidStatFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		// 添加过滤规则.
		filterRegistrationBean.addUrlPatterns("/*");
		// 添加不需要忽略的格式信息.
		filterRegistrationBean.addInitParameter("exclusions",
				"/static/*,*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid,/druid/*,/monitoring,/monitoring/*");
		// 用于session监控页面的用户名显示 需要登录后主动将username注入到session里
		filterRegistrationBean.addInitParameter("principalSessionName", "username");
		return filterRegistrationBean;
	}

	/**
	 * druid数据库连接池监控
	 */
	@Bean
	public DruidStatInterceptor druidStatInterceptor() {
		return new DruidStatInterceptor();
	}

	@Bean
	public JdkRegexpMethodPointcut druidStatPointcut() {
		JdkRegexpMethodPointcut druidStatPointcut = new JdkRegexpMethodPointcut();
		String patterns = "com.qdone.module.*.service.*";
		// 可以set多个
		druidStatPointcut.setPatterns(patterns);
		return druidStatPointcut;
	}

	/**
	 * druid数据库连接池监控
	 */
	@Bean
	public BeanTypeAutoProxyCreator beanTypeAutoProxyCreator() {
		BeanTypeAutoProxyCreator beanTypeAutoProxyCreator = new BeanTypeAutoProxyCreator();
		beanTypeAutoProxyCreator.setTargetBeanType(DruidDataSource.class);
		beanTypeAutoProxyCreator.setInterceptorNames("druidStatInterceptor");
		return beanTypeAutoProxyCreator;
	}

	/**
	 * druid 为druidStatPointcut添加拦截
	 * 
	 * @return
	 */
	@Bean
	public Advisor druidStatAdvisor() {
		return new DefaultPointcutAdvisor(druidStatPointcut(), druidStatInterceptor());
	}

	/**
	 * 配置javamelody监控
	 * spring boot 会按照order值的大小，从小到大的顺序来依次过滤
	 */
	@Bean
	@Order(Integer.MAX_VALUE-1)
	public FilterRegistrationBean monitoringFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new MonitoringFilter());
		registration.addUrlPatterns("/*");
		registration.setName("monitoring");
		return registration;
	}
	
    /**
     *  配置javamelody监听器sessionListener
     */
	@Bean
	public ServletListenerRegistrationBean servletListenerRegistrationBean() {
		ServletListenerRegistrationBean slrBean = new ServletListenerRegistrationBean();
		slrBean.setListener(new SessionListener());
		return slrBean;
	}

}
