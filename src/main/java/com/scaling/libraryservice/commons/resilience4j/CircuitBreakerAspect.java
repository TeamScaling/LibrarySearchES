package com.scaling.libraryservice.commons.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CircuitBreakerAspect {

	private final CircuitBreaker customCircuitBreaker;

	@Pointcut("@annotation(Substitute)")
    public void customCircuitBreakerPointcut() {}

	@Around("customCircuitBreakerPointcut()")
	public Object substituteAround(ProceedingJoinPoint joinPoint) throws Throwable {

		customCircuitBreaker.acquirePermission();

		try {
			log.info("API요청 성공 @@@@@@@@@@@@@@@@@@@@@@@@");
			Object result = joinPoint.proceed();
			customCircuitBreaker.onSuccess(3, TimeUnit.SECONDS);
			return result;
		} catch (Exception e) {
			log.info("API요청 실패 @@@@@@@@@@@@@@@@@@@@@@@@");
			customCircuitBreaker.onError(5, TimeUnit.SECONDS, e);
			throw e;
		}}

}
