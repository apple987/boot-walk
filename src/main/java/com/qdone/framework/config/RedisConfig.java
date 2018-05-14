package com.qdone.framework.config;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qdone.framework.util.RedisCache;
/**
 * 配置redis缓存
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport{
	
	@Bean
	public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

	/**
	 * 自定义缓存策略
	 */
	@SuppressWarnings({ "rawtypes"})
	@Bean
    public SimpleCacheManager simpleCacheManager(RedisTemplate redisTemplate) {
		    SimpleCacheManager simple = new SimpleCacheManager();
		    Set<RedisCache> data=new HashSet<RedisCache>();
		    data.add(new RedisCache(redisTemplate,"view",60));
		    data.add(new RedisCache(redisTemplate,"defaultCache",1800));
		    simple.setCaches(data);
	        return simple;
	 }
	/**
	 * 定义redisTemplate
	 *  可以定义多个RedisTemplate,
	 *   本处强制全部使用集群版本RedisTemplate
	 */
	@Bean
    public RedisTemplate<String, String> redisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
    

}