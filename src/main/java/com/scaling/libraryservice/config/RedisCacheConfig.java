package com.scaling.libraryservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.entity.Library;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisCacheConfig extends CachingConfigurerSupport {

	private final LibraryRepository libraryRepo;
	private final RedisConnectionFactory connectionFactory;

	@Bean
	public RedisCacheConfiguration cacheConfiguration() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		RedisSerializer<String> redisSerializer = new StringRedisSerializer();
		RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

		RedisSerializationContext.SerializationPair<String> keySerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer);
		RedisSerializationContext.SerializationPair<Object> valueSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer);

		return RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(keySerializationPair)
			.serializeValuesWith(valueSerializationPair)
			.entryTtl(Duration.ofMinutes(5));
	}

	@Bean
	public CacheManager redisCacheManager() {
		RedisCacheManagerBuilder builder = RedisCacheManager.builder(connectionFactory)
			.cacheDefaults(cacheConfiguration());

		// Cache names for libraries and other caches
		List<String> cacheNames = Arrays.asList("libraryCache");

		// Create caches for specified cache names
		builder.initialCacheNames(Collections.singleton("libraryCache"));

		// Create RedisCacheManager with caches
		RedisCacheManager cacheManager = builder.build();

		return cacheManager;
	}

	CacheKey generateCacheKey(Object[] arguments) throws UnsupportedOperationException {

		for(Object obj : arguments){

			if(obj instanceof CacheKey){
				return (CacheKey) obj;
			}
		}

		throw new UnsupportedOperationException(
			"No suitable CacheKey implementation found for class: ");
	}
}
