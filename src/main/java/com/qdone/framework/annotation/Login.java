package com.qdone.framework.annotation;

import java.lang.annotation.*;

/**
 * app登录效验
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
	String value() default "";
}
