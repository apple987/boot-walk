/*package com.qdone.framework.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;*/
/**
 * @author 付为地
 *   本处针对，SqlSessionFactoryBean配置setMapperLocations路径
 *    mybatis的sql的xml文件存在src/main/java包路径里面时，比如:
 *     src
 *        main
 *           java 
 *              com
 *                qdone
 *                    mapper
 *                       solr.xml
 *           resources
 *              mapper
 *                 solr.xml
 *    针对sql的xml文件放在，java包路径里面无法访问，此处强制指定setMapperLocations配置                        
 */
/*@Configuration
public class MyBatisConfig {*/

/*	@Bean(name = "sqlSessionFactory")
	@ConditionalOnMissingBean // 当容器里没有指定的Bean的情况下创建该对象
	@Primary
	public SqlSessionFactoryBean sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/qdone/module/mapper/*.xml"));
		return sqlSessionFactoryBean;
	}*/

	/*@Bean(name = "transactionManager")
	@ConditionalOnMissingBean // 当容器里没有指定的Bean的情况下创建该对象
	@Primary
	public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}*/

	/*@Bean(name = "dataSource")
	@ConditionalOnMissingBean // 当容器里没有指定的Bean的情况下创建该对象
	@ConfigurationProperties(prefix = "spring.datasource")
	@Primary
	public DataSource testDataSource() {
		return DataSourceBuilder.create().build();
	}*/

	
	/*@Bean(name = "sqlSessionTemplate")
	@Primary
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory)
			throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}*/
	
	/*@Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.*")
    public DataSource druidDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setRemoveAbandoned(removeAbandoned);
        datasource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        datasource.setLogAbandoned(logAbandoned);

        datasource.setQueryTimeout(queryTimeOut);
        datasource.setTransactionQueryTimeout(transactionQueryTimeout);

        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        return datasource;
    }*/
/*
}*/
