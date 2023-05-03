package com.scaling.libraryservice.commons.circuitBreaker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Substitutable {

    Class<?> origin();

    String substitute() default "";

}
