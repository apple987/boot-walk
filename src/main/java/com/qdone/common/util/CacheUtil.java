package com.qdone.common.util;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
/**
 *@author 傅为地
 *        缓存工具
 *所有人只允许自定义操作：名称为default的缓存
 *前缀才有_cu_default_:cu表示CacheUtil大写缩写
 */
@Component
public class CacheUtil {
	
	@Autowired
	@SuppressWarnings("rawtypes")
	private RedisTemplate redisTemplate;
	 
	private final String name="default";//默认使用cacheName="default"的缓存区
	private final String keyPrfx="_cu_"+name+"_";//默认生成的key前缀：_cu_default_
	
    /**
     * 设置key
     */
	@SuppressWarnings("unchecked")
	public void put(String key,Object value,final long liveTime) {
		ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
		valueops.set(getUkPrfex(key), value,liveTime);
	}
	/**
     * 设置key
     */
	@SuppressWarnings("unchecked")
	public void put(String key,Object value) {
		ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
		valueops.set(getUkPrfex(key), value);
	}
	/**
	 * 设置key对应元素存活时间
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void set(final  String key, final  String value, final long liveTime) {
		redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(getUkPrfex(key).getBytes(), value.getBytes());
                if (liveTime > 0) {
                    connection.expire(getUkPrfex(key).getBytes(), liveTime);
                }
                return 1L;
            }
        });
    }
	/**
	 *  设置key对应元素存活时间
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void set(final  String key, final  Object value, final long liveTime) {
		redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(getUkPrfex(key).getBytes(), value.toString().getBytes());
                if (liveTime > 0) {
                    connection.expire(getUkPrfex(key).getBytes(), liveTime);
                }
                return 1L;
            }
        });
    }
	/**
	 * 设置key存活时间
	 */
	@SuppressWarnings("unchecked")
	public Boolean set(final  String key, final long liveTime) {
		return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) {
				return connection.expire(getUkPrfex(key).getBytes(), liveTime);
			}
		}, true);
    }
	/**
	 * 设置key元素到endDate时刻到期
	 */
	@SuppressWarnings("unchecked")
	public Boolean set(String key,  Date endDate) {
		return redisTemplate.expireAt(getUkPrfex(key),endDate);
	}
	
	
	/**
	 * 持久化key对应元素
	 */
	@SuppressWarnings("unchecked")
	public void persist(String key) {
		redisTemplate.persist(getUkPrfex(key));
	}
	/**
	 * 修改key值
	 */
	@SuppressWarnings("unchecked")
	public void rename(String oldKey,String newKey) {
		redisTemplate.rename(getUkPrfex(oldKey),getUkPrfex(newKey));
	}
	
	/**
	 * 根据key获得元素
	 */
	@SuppressWarnings("unchecked")
	public Object get(String key) {
		ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
		/*return key.startsWith(keyPrfx)?valueops.get(key):valueops.get(keyPrfx+key);*/
		return valueops.get(getUkPrfex(key));
	}
	/**
	 * 获得key对应元素存活时间
	 */
	@SuppressWarnings("unchecked")
	public long getKeyExpire(Object key) {
		return redisTemplate.getExpire(getUkPrfex(getNotNullStr(key)));
	}
	
	/**
	 * 移除key
	 */
	@SuppressWarnings("unchecked")
	public void remove(String key){
		redisTemplate.delete(getUkPrfex(key));
	}
	/**
	 * 移除keys
	 */
	@SuppressWarnings("unchecked")
	public void removeAll(List<String> keys){
		for (int i = 0; i < keys.size(); i++) {
			keys.set(i, getUkPrfex(keys.get(i)));
		}
		redisTemplate.delete(keys);
	}
   /**
    * 是否含有key
	*/
	@SuppressWarnings("unchecked")
	public Boolean hasKey(String key) {
		return redisTemplate.hasKey(getUkPrfex(key));
	}
	/**
	 * 返回所有keys
	 */
	@SuppressWarnings("unchecked")
	public Set<?> getAllKeys(){
		/*return redisTemplate.keys(keyPrfx+"*");*/
		
		return redisTemplate.keys(getUkPrfex("*"));
	}
	/**
	 * 返回所有values
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getAllValues(){
		Map<String,Object> result=new HashMap<String,Object>();
		Set<String> keys=(Set<String>) getAllKeys();
		for (String key : keys) {
			result.put(key, get(key));
		}
		return result;
	}
	/**
	 * 清除所有
	 */
	@SuppressWarnings("unchecked")
	public void clear() {
		redisTemplate.delete(getAllKeys());
	}
	/**
	 * 检查是否连接成功
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String ping() {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.ping();
            }
        });
    }
	/**
	 * 查看redis里有多少数据
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public long dbSize() {
	        return (long) redisTemplate.execute(new RedisCallback() {
	            public Long doInRedis(RedisConnection connection) throws DataAccessException {
	                return connection.dbSize();
	            }
	        });
	 }
	/**依赖构造器**/
	@SuppressWarnings("rawtypes")
	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}
	public void setRedisTemplate(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	/*保证生成的key唯一前缀*/
	private String getUkPrfex(String key){
		return key.startsWith(keyPrfx)?key:(keyPrfx+key);
		/*String path = SessionUtil.getRequest().getContextPath().replaceFirst("/", "");
		return key.startsWith(path+keyPrfx)?key:(path+keyPrfx+key);*/
	}
	/*得到非空字符串*/
	private  String getNotNullStr(Object obj){
		return isEmpty(obj)?"":obj.toString();
	}
	/*判断对象是否为空*/
	private  boolean isEmpty(Object obj){
		return obj==null||obj.toString().trim().equals("")||obj.toString().trim().length()==0;
	}
}
