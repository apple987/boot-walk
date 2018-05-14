package com.qdone.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 *  自定义配置redis缓存
 *  自定义生成key策略，目前分开了不同的缓存name区：
 *  如当前cacheName的名字是defalut：那么就增删改查时，都会生成的key带上前缀:projectName+"_fn_"+cacheName+"_"],类似于ehcache功能
 */
@Scope("prototype")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RedisCache implements Cache {  
	
	private RedisTemplate redisTemplate;
    private String name;  
    private long timeout;
    /*适应最新版本*/
    private RedisConnectionFactory connectionFactory;
    
	public RedisConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public RedisCache(RedisTemplate redisTemplate, String name, long timeout) {
		super();
		this.redisTemplate = redisTemplate;
		this.name = name;
		this.timeout = timeout;
	}

	public RedisTemplate<String, Object> getRedisTemplate() {  
        return redisTemplate;  
    }  
  
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {  
        this.redisTemplate = redisTemplate;  
    }  
  
    public void setName(String name) {
        this.name = name;  
    }  
  
    @Override  
    public String getName() {  
        return this.name;  
    }  
    
    /*@Override  
    public Object getNativeCache() {  
        return new RedisCacheManager(this.redisTemplate).getCache(name);  
    }  */
    
    /*适应最新版本*/
    @Override  
    public Object getNativeCache() {  
        return RedisCacheManager.create(connectionFactory).getCache(name);  
    }  
    
    
	@Override  
    public ValueWrapper get(Object key) { 
		final String keyf = getUkPrfex(key.toString()); 
        Object object = null;  
        object = redisTemplate.execute(new RedisCallback<Object>() {  
            public Object doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                byte[] key = keyf.getBytes();  
                byte[] value = connection.get(key);  
                if (value == null) {  
                    return null;  
                }  
                return toObject(value);  
  
            }  
        });  
        return (object != null ? new SimpleValueWrapper(object) : null); 
    }  
	      
    @Override  
    public void put(Object key, Object value) {  
    	final String keyf = getUkPrfex(key.toString()); 
        final Object valuef = value;  
        final long liveTime = timeout;  
        redisTemplate.execute(new RedisCallback<Long>() {  
            public Long doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                byte[] keyb = keyf.getBytes();  
                byte[] valueb = toByteArray(valuef);  
                connection.set(keyb, valueb);  
                if (liveTime > 0) {  
                    connection.expire(keyb, liveTime);  
                }  
                return 1L;  
            }  
        }); 
    }
    
    /**
    * 清除
    */
    @Override  
    public void evict(Object key) {  
        redisTemplate.delete(getUkPrfex(key.toString()));
    } 
    
    /**
    * 清除的时候，只会清除缓存名称为name前缀的缓存
    */
    @Override 
    public void clear() { 
    	/*redisTemplate.delete(redisTemplate.keys("_fn_"+name+"_*"));*/
    	redisTemplate.delete(redisTemplate.keys(getUkPrfex("*")));
    	
    }

	@Override
	public <T> T get(Object key, Class<T> type) {
		Object object = null;  
        try {  
        	ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
            object = valueops.get(getUkPrfex(key.toString()));
        }catch (IllegalStateException  e) {  
            e.printStackTrace();  
        }catch (Exception  e) {  
            e.printStackTrace();  
        }   
        return isEmpty(object)?null:(T)object;  
	}
	
	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		Object object = null;
		try {
			object= get(key,valueLoader.call().getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T)object;
	}
	
    /**
     *自动将指定值在缓存中指定的键是否已经设置
     */
	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		ValueWrapper vw=get(key);
		if(isEmpty(vw)){
			put(key, value);
			return null;
		}else{
			return vw;
		}
	} 
	/***************************************************************************************/
	/*保证生成的key唯一前缀*/
	private String getUkPrfex(String key){
		/*String path = SessionUtil.getRequest().getContextPath().replaceFirst("/", "");
		return key.startsWith(path+"_fn_"+name+"_")?key:(path+"_fn_"+name+"_"+key);
		*/
		return key.startsWith("_fn_"+name+"_")?key:("_fn_"+name+"_"+key);
	}
	/*判断对象是否为空*/
	private  boolean isEmpty(Object obj){
		return obj==null||obj.toString().trim().equals("")||obj.toString().trim().length()==0;
	}
    /** 
     * 描述 : <Object转byte[]>
     */  
    private byte[] toByteArray(Object obj) {  
        byte[] bytes = null;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        try {  
            ObjectOutputStream oos = new ObjectOutputStream(bos);  
            oos.writeObject(obj);  
            oos.flush();  
            bytes = bos.toByteArray();  
            oos.close();  
            bos.close();  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        }  
        return bytes;  
    }  
  
    /** 
     * 描述 : <byte[]转Object>. <br> 
     */  
    private Object toObject(byte[] bytes) {  
        Object obj = null;  
        try {  
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);  
            ObjectInputStream ois = new ObjectInputStream(bis);  
            obj = ois.readObject();  
            ois.close();  
            bis.close();  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        } catch (ClassNotFoundException ex) {  
            ex.printStackTrace();  
        }  
        return obj;  
    }
    
    /**
    * 默认存活时间
    */
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	
	
}  