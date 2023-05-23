package com.scaling.libraryservice.commons.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.scaling.libraryservice.mapBook.cacheKey.HasBookCacheKey;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.recommend.cacheKey.RecCacheKey;
import com.scaling.libraryservice.recommend.service.RecommendService;
import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import com.scaling.libraryservice.search.service.BookSearchService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j @Component
public class CustomCacheManager<T> {

    private final Map<Class<?>, Cache<CacheKey,T>> anonymousCache = new HashMap<>();

    public void registerCaching(Cache<CacheKey,T> cache,Class<?> customer){
        log.info("[{}] is registered in caching System",customer);
        anonymousCache.put(customer,cache);

    }

    public void put(Class<?> customer, CacheKey personalKey,Object item){

        if(anonymousCache.containsKey(customer)){
            log.info("CacheManger put item for [{}]",customer);
            anonymousCache.get(customer).put(personalKey,(T)item);
        }
    }

    public T get(Class<?> customer,CacheKey personalKey){
        log.info("CacheManger find item for [{}]",customer);

        return anonymousCache.get(customer).getIfPresent(personalKey);
    }

    public void removeCaching(Class<?> customer){

        if (anonymousCache.containsKey(customer)){
            anonymousCache.remove(customer);
        }else{
            log.error("해지 하고자 하는 캐싱 정보가 없습니다. [{}]",customer);
            throw new IllegalArgumentException();
        }
    }

    public boolean isUsingCaching(Class<?> customer){

        return anonymousCache.containsKey(customer);
    }

    public boolean isContainItem(Class<?> customer,CacheKey personalKey){

        if(isUsingCaching(customer)){
            return anonymousCache.get(customer).getIfPresent(personalKey) != null;
        }else{

            throw new IllegalArgumentException(customer+"is not registered for caching");
        }
    }

    public CacheKey generateCacheKey(Object[] arguments) {

        log.info("캐시키 생성=======");
        for (Object obj : arguments) {

            if (obj instanceof CacheKey){
                return (CacheKey) obj;
            }
        }

        throw new UnsupportedOperationException("No suitable CacheKey implementation found for class");
    }
}
