package com.scaling.libraryservice.commons.caching;

import java.util.Objects;
import javax.cache.Cache;

public interface CacheKey {

	boolean equals(Object o);

	int hashCode();
}
