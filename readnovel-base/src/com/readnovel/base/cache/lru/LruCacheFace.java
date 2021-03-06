package com.readnovel.base.cache.lru;

/**
 * lru缓存接口
 * 
 * 
 * @author li.li
 *
 */
public interface LruCacheFace<T> {
	void put(String key, T obj);

	T get(String key);
}
