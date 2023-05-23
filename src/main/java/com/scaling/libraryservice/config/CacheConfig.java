//package com.scaling.libraryservice.config;
//
//import java.time.Duration;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.CachingConfigurer;
//import org.springframework.cache.CacheManager;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableCaching
//public class CacheConfig extends CachingConfigurerSupport {
//
//	@Bean
//	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//		RedisSerializer<String> redisSerializer = new StringRedisSerializer();
//
//		RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
//			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
//			.entryTtl(Duration.ofMinutes(5)); // 캐시된 데이터의 TTL 설정
//
//		return RedisCacheManager.builder(connectionFactory)
//			.cacheDefaults(cacheConfiguration)
//			.build();
//	}
//
//}
