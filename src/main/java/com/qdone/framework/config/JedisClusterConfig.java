package com.qdone.framework.config;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ConditionalOnClass({ JedisCluster.class })
public class JedisClusterConfig {

	@Value("${spring.redis.cluster.nodes}")
	private String clusterNodes;
	@Value("${spring.redis.cluster.timeout:2000}")
	private int timeout;
	@Value("${spring.redis.cluster.so-time-out:60}")
	private int soTimeOut;
	@Value("${spring.redis.cluster.max-redirections}")
	private int maxRedirections;
	@Value("${spring.redis.cluster.password}")
	private String password;
	/* redis-pool */
	//最大连接数
	@Value("${redis.pool.max-total:30}")
	private int maxTotal;
	//最大空闲连接数,默认8
	@Value("${spring.redis.pool.max-idle:8}")
	private int maxIdle;
	//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
	@Value("${spring.redis.pool.max-wait:-1}")
	private long maxWaitMillis;
	//最小空闲连接数, 默认0
	@Value("${spring.redis.pool.min-idle:0}")
	private int minIdle;
	//在获得链接的时候检查有效性，本处配置true
	@Value("${spring.redis.testOnBorrow:true}")
	private Boolean testOnBorrow;
	
	/**
	 * 创建JedisCluster集群
	 */
	@Bean
	public JedisCluster createJedisCluster(@Qualifier("jedisPoolConfig") JedisPoolConfig jedisPoolConfig) {
		String[] cNodes = clusterNodes.split(",");
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		// 分割出集群节点
		for (String node : cNodes) {
			String[] hp = node.split(":");
			nodes.add(new HostAndPort(hp[0].trim(), Integer.parseInt(hp[1].trim())));
		}
		if (StringUtils.isEmpty(password)) {
			// 创建集群对象
			return new JedisCluster(nodes, timeout, jedisPoolConfig);
		} else {
			return new JedisCluster(nodes, timeout, soTimeOut, maxRedirections, password, new GenericObjectPoolConfig().clone());
		}
	}

	/**
	 * 集群配置redssion
	 */
	@Bean
	public RedissonClient createRedissionCluster() {
		String[] cNodes = clusterNodes.split(",");
		List<String> arr = new ArrayList<String>();
		// 分割出集群节点
		for (String node : cNodes) {
			arr.add("redis://" + node);
		}
		Config config = new Config();
		if (StringUtils.isNotEmpty(password)) {
			config.useClusterServers().setPassword(password).setScanInterval(2000) // 集群状态扫描间隔时间，单位是毫秒
					// 可以用"rediss://"来启用SSL连接
					.addNodeAddress(arr.toArray(new String[0]));
		} else {
			config.useClusterServers().setScanInterval(2000) // 集群状态扫描间隔时间，单位是毫秒
					// 可以用"rediss://"来启用SSL连接
					.addNodeAddress(arr.toArray(new String[0]));
		}

		return Redisson.create(config);
	}
	
	/**
	 * 配置集群RedisConnectionFactory
	 */
	@Bean(name="redisConnectionFactory")
	@Primary
	@ConditionalOnMissingBean
	public RedisConnectionFactory connectionFactory(
			@Qualifier("redisClusterConfiguration") RedisClusterConfiguration redisClusterConfiguration,
			@Qualifier("jedisPoolConfig") JedisPoolConfig jedisPoolConfig) {
		return new JedisConnectionFactory(redisClusterConfiguration,jedisPoolConfig);
	}
	/**
	 * 配置集群RedisClusterConfiguration
	 */
	@Bean(name="redisClusterConfiguration")
	@Primary
	@ConditionalOnMissingBean
    public RedisClusterConfiguration createRedisClusterConfiguration(){
		RedisClusterConfiguration bean=new RedisClusterConfiguration();
		bean.setMaxRedirects(maxRedirections);
		bean.setMaxRedirects(maxRedirections);
		Set<RedisNode> nodes=new HashSet<RedisNode>();
		String[] cNodes = clusterNodes.split(",");
		for (String node : cNodes) {
			String[] hosts=node.split(":");
			notNull(hosts, "HostAndPort need to be seperated by  ':'.");
			isTrue(hosts.length == 2, "Host and Port String needs to specified as host:port");
			nodes.add(new RedisNode(hosts[0],Integer.parseInt(hosts[1])));
		}
		bean.setClusterNodes(nodes);
		if(StringUtils.isNotEmpty(password)){
			bean.setPassword(RedisPassword.of(password));
		}
		return bean;
    }
	
	/**
	 * 配置jedis连接池配置
	 */
	@Bean(name="jedisPoolConfig")
	@Primary
	@ConditionalOnMissingBean
	public JedisPoolConfig poolCofig() { 
		    JedisPoolConfig poolCofig = new JedisPoolConfig();
		    poolCofig.setMaxTotal(maxTotal);
		    poolCofig.setMaxIdle(maxIdle); 
		    poolCofig.setMaxWaitMillis(maxWaitMillis); 
		    poolCofig.setMinIdle(minIdle);
		    poolCofig.setTestOnBorrow(testOnBorrow); 
		    return poolCofig; 
	} 
}