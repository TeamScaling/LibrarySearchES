package com.scaling.libraryservice.commons.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.utils.CircuitBreakerUtil;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitConfig {

	@Bean
	public CircuitBreakerConfig config() {
		return CircuitBreakerConfig.custom()
			.minimumNumberOfCalls(5) // 5번 호출 이후에는 fallback 메서드
//		.failureRateThreshold(50)
//		.waitDurationInOpenState(Duration.ofMillis(1000))
//		.slowCallRateThreshold(50)
//		.permittedNumberOfCallsInHalfOpenState(2)
//		.slidingWindowSize(2)
			.recordExceptions(IndexOutOfBoundsException.class, TimeoutException.class)
			.build();
	}

	@Bean
	public CircuitBreakerRegistry circuitBreakerRegistry() {
		CircuitBreakerConfig config = CircuitBreakerConfig.custom()
			.minimumNumberOfCalls(5) // 5번 호출 이후에는 fallback 메서드
			.recordExceptions(IndexOutOfBoundsException.class, TimeoutException.class)
			.build();
		return CircuitBreakerRegistry.of(config);
	}

	@Bean
	public CircuitBreaker customCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
		return circuitBreakerRegistry.circuitBreaker("customCircuitBreaker");
	}

}
