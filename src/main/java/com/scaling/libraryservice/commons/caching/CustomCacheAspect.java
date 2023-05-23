package com.scaling.libraryservice.commons.caching;

import com.scaling.libraryservice.mapBook.cacheKey.HasBookCacheKey;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.util.MapBookApiHandler;
import com.scaling.libraryservice.recommend.cacheKey.RecCacheKey;
import com.scaling.libraryservice.recommend.service.RecommendService;
import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import com.scaling.libraryservice.search.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

@Aspect
@Component @Slf4j
@RequiredArgsConstructor
public class CustomCacheAspect<T> {
    private final CustomCacheManager<T> cacheManager;

    @Pointcut("@annotation(CustomCacheable)")
    public void customCacheablePointcut() {}

    @Around("customCacheablePointcut()")
    public Object cacheAround(ProceedingJoinPoint joinPoint) throws Throwable {

//        C customer = (C) joinPoint.getTarget();
        Class<?> clazz = joinPoint.getTarget().getClass();
        Object[] arguments = joinPoint.getArgs();
        CacheKey cacheKey = cacheManager.generateCacheKey(joinPoint.getArgs());

        if (cacheManager.isContainItem(clazz, cacheKey)) {
            return cacheManager.get(clazz, cacheKey);
        }

        Object result = joinPoint.proceed();
        cacheManager.put(clazz, cacheKey, result);
        return result;

    }
}