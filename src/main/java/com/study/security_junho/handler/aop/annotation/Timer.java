package com.study.security_junho.handler.aop.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME) // 언제 적용 시킬 것인가(? 할 때 적용)
@Target({ TYPE, METHOD })
public @interface Timer {
	
}
