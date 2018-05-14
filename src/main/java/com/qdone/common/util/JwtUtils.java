package com.qdone.common.util;

import java.util.Date;
import java.util.Map;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.qdone.framework.exception.RRException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * @author 付为地 jwt工具类 也可以考虑,采用redis做tonken生成 采用token实现,服务器无状态,分布式等方式都方便
 * 
 *         比如说第一次生成一个token，虽然token还没有过期，但是第二次继续登录，又重新生成一个token
 *         这就会导致，相同账户两个token都是有效的，那么获取数据的时候，使用第一次生成的token，也可以拿到数据吗？
 *         这显然不合适，这里需要加一个处理
 *         简单点就是以最后一次登录生成的token为有效，生成最后一次token时，销毁当前账户，以前的token，保证最后一次登录的token才有效果
 */
@Component
public class JwtUtils {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${jwt.token.secret:b65f414eaa7caf8914faacb2a211570f}")
	private String secret;

	@Value("${jwt.token.expire:604800}")
	private int expire;

	@Value("${jwt.token.header:token}")
	private String header;

	@Autowired
	private JedisCluster jedisCluster;

	/* 工具生成的app模块的token前缀 */
	public final String AppTokenPrefix = "app_token_prefix_";

	/**
	 * 生成jwt token 因为存储的是key=AppTokenPrefix+userId value=User方式 本处可以如下简单处理
	 */
	public String generateToken(String userId) {
		Date nowDate = new Date();
		// 过期时间
		Date expireDate = new Date(nowDate.getTime() + expire * 1000);
		/* 销毁当前账户，本次登录之前的token信息 */
		/*
		 * TreeSet<String> ts = keys(AppTokenPrefix+"*"); Iterator<String> it =
		 * ts.iterator(); while (it.hasNext()) { String key=it.next(); User
		 * usr=get(key.getBytes(), User.class);
		 * if(!ObjectUtils.isEmpty(usr)&&StringUtils.isNotEmpty(usr.getName())&&
		 * usr.getName().equals(String.valueOf(userId))){ jedisCluster.del(key);
		 * } }
		 */
		/* 因为存储的是key=AppTokenPrefix+userId value=User方式本处可以如下简单处理 */
		if (exists(AppTokenPrefix + userId)) {
			jedisCluster.del(AppTokenPrefix + userId);
		}
		String token = Jwts.builder().setHeaderParam("typ", "JWT").setSubject(userId + "").setIssuedAt(nowDate)
				.setExpiration(expireDate).signWith(SignatureAlgorithm.HS512, secret).compact();
		/*
		 * 本处模拟，用户登录信息处理，实际上是先根据用户userId查询到user信息，然后后续处理存储到redis中，同时生成token,
		 * 本处部分代码放在login方法实现
		 */
		/*
		 * User usr=new User(); usr.setName(userId); usr.setPassword(userId);
		 * usr.setToken(token); //生成token，存入redis,这部分功能暂时
		 * set((AppTokenPrefix+userId).getBytes(), expire,usr);
		 */
		return AESUtil.encrypt(token, null);
	}

	/**
	 * 验证token
	 * 
	 * @param token
	 * @return
	 */
	public Claims getClaimByToken(String token) {
		try {
			return Jwts.parser().setSigningKey(secret).parseClaimsJws(AESUtil.decrypt(token, null)).getBody();
		} catch (Exception e) {
			logger.debug("validate is token error ", e);
			return null;
		}
	}

	/**
	 * token是否过期
	 * 
	 * @return true：过期
	 */
	public boolean isTokenExpired(Date expiration) {
		return expiration.before(new Date());
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	/********************************* 辅助方法 ******************************************************/
	/**
	 * 获取集群keys
	 */
	public TreeSet<String> keys(String pattern) {
		logger.debug("开始JwtUtils.keys获取集群keys传入表达式:" + pattern);
		TreeSet<String> keys = new TreeSet<>();
		Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
		for (String k : clusterNodes.keySet()) {
			JedisPool jedisPool = clusterNodes.get(k);
			Jedis jedis = jedisPool.getResource();
			try {
				keys.addAll(jedis.keys(pattern));
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("执行JwtUtils.keys获取集群keys异常:" + e);
				throw new RRException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value());
			} finally {
				if (jedis != null) {
					jedis.close();// 用完一定要close这个链接！！！
				}
			}
		}
		return keys;
	}

	/**
	 * 判断key是不是存在
	 */
	public Boolean exists(String key) {
		return jedisCluster.exists(key);
	}

	/**
	 * 设置元素 存活时间 存入序列化对象
	 */
	public String set(byte[] key, int timeout, Object value) {
		return jedisCluster.setex(key, timeout, SerializeUtils.serialize(value));
	}

	/**
	 * 根据Key获取元素 针对存入序列化对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(byte[] key, Class<T> type) {
		return (T) SerializeUtils.deserialize(jedisCluster.get(key));
	}

}
