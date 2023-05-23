package com.scaling.libraryservice.commons.resilience4j;

public @interface Retry {

	String name();

//	String fallbackMethod() default "";

}
