package com.scaling.libraryservice.commons.resilience4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Substitute {

	Class<?> name();

	String fallbackMethod() default "";
}
