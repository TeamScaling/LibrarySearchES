package com.scaling.libraryservice.commons.caching;

import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.entity.Library;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;

@RequiredArgsConstructor
public class LibraryCache {

	private final LibraryRepository libraryRepo;
	private final RedisCacheManager cacheManager;
	private static final String CACHE_NAME = "libraryCache";

	@PostConstruct
	private void init() {
		loadLibraryCache();
	}

	public List<LibraryDto> getLibraries() {
		Cache cache = cacheManager.getCache(CACHE_NAME);
		if (cache != null) {
			Cache.ValueWrapper valueWrapper = cache.get(CACHE_NAME);
			if (valueWrapper != null) {
				return (List<LibraryDto>) valueWrapper.get();
			}
		}
		return null;
	}

	public void clearLibraryCache() {
		Cache cache = cacheManager.getCache(CACHE_NAME);
		if (cache != null) {
			cache.evict(CACHE_NAME);
		}
	}

	private void loadLibraryCache() {
		List<Library> libraries = libraryRepo.findAll();
		Cache cache = cacheManager.getCache(CACHE_NAME);
		if (cache != null) {
			cache.put(CACHE_NAME, libraries);
		}
	}
}

