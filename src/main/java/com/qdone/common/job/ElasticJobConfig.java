package com.qdone.common.job;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

@Configuration
@ConditionalOnExpression("'${spring.elasticjob.serverList}'.length() > 0")
public class ElasticJobConfig {
	
	@Autowired
    private DataSource dataSource;
	
	
	@Bean(initMethod = "init")
	public ZookeeperRegistryCenter regCenter(@Value("${spring.elasticjob.serverList}") final String serverList, @Value("${spring.elasticjob.namespace}") final String namespace) {
	        return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList, namespace));
	}
	
    @Bean
    public JobEventConfiguration jobEventConfiguration() {
        return new JobEventRdbConfiguration(dataSource);
    }
}
