package com.qdone.framework.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 
 * @author 付为地
 *  springBoot配置http自动跳转功能，客户访问http://localhost:8080/springBoot
 *        将会自动跳转到:https://localhost:9090/springBoot
 *  参考资料:https://github.com/spring-projects/spring-boot/issues/9836
 *         https://blog.csdn.net/u012702547/article/details/53790722
 *         https://github.com/lenve/JavaEETest/tree/master/Test20-Thymeleaf1 
 */
@Configuration
public class HttpsConfig {
	
	@Value("${server.port:8080}")
	private int serverPort;

    /**
     * 针对springBoot2.0版本配置
     */
	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(httpConnector());
		return tomcat;
	}

    @Bean
    public Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        //Connector监听的http的端口号
        connector.setPort(80);
        connector.setSecure(false);
        //监听到http的端口号后转向到的https的端口号
        /*connector.setRedirectPort(8443);*/
        connector.setRedirectPort(serverPort);
        return connector;
    }
    
    
    
  /*  
   * 针对1.4.2版本配置
   *
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint constraint = new SecurityConstraint();
                constraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }*/
	
}
