package com.Springboot.miaosha.xianliu;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.persistence.Inheritance;


@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inheritance
@Documented
public @interface HttpCountLimit {
     int count();
     int second();
     boolean Signin() default true;
}
