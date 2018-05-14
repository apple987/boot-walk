package com.qdone.framework.util.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisLock {
	/**
	 * 锁的key 如果想添加非固定锁，可以在参数上添加@P4jSynKey注解，但是本参数是必写选项<br/>
	 * redis key的拼写规则为 "DistRedisLock+" + lockKey + @RedisLOckKey<br/>
	 */
	String lockKey();

	/**
	 * 持锁时间 单位毫秒,默认15秒<br/>
	 */
	long keepMills() default 15 * 1000;

	/**
	 * 没有获取到锁时，等待时间60秒
	 * @return
	 */
	long maxSleepMills() default 60 * 1000;
}