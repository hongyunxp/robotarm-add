package com.readnovel.book.base.cache.viewdata;

import org.json.JSONObject;

import com.readnovel.book.base.cache.Cache;
import com.readnovel.book.base.cache.Filter;
import com.readnovel.book.base.cache.KeyCreater;



/**
 * 视图数据缓存提供者
 * 
 */
public class ViewDataCacheProvider implements Cache<JSONObject> {
	private static volatile ViewDataCacheProvider instance;
	private final ViewDataCacheWrapper cacheWrapper;

	private ViewDataCacheProvider(String path) {
		cacheWrapper = ViewDataCacheWrapper.getInstance(path);
	}

	public static ViewDataCacheProvider getInstance(String path) {
		if (instance == null) {
			synchronized (ViewDataCacheProvider.class) {
				if (instance == null) {
					instance = new ViewDataCacheProvider(path);
				}
			}
		}

		return instance;
	}

	@Override
	public boolean put(String key, JSONObject value) {
		return cacheWrapper.put(key, value);
	}

	@Override
	public boolean put(String key, JSONObject value, Filter<JSONObject> filter) {
		return cacheWrapper.put(key, value, filter);
	}

	@Override
	public boolean put(String key, JSONObject value, Filter<JSONObject> filter, KeyCreater keyCreater) {
		return cacheWrapper.put(key, value, filter, keyCreater);
	}

	@Override
	public JSONObject get(String url) {
		return cacheWrapper.get(url, null);
	}

	@Override
	public JSONObject get(String key, KeyCreater keyCreater) {
		return cacheWrapper.get(key, keyCreater);
	}

	@Override
	public JSONObject get(String key, KeyCreater keyCreater, long timeOut) {
		return cacheWrapper.get(key, keyCreater, timeOut);
	}

	@Override
	public String getRootPath() {
		return cacheWrapper.getRootPath();
	}

	@Override
	public boolean available() {
		return cacheWrapper.available();
	}

	@Override
	public long getTotalMemorySize() {
		return cacheWrapper.getTotalMemorySize();
	}

	@Override
	public long getAvailableMemorySize() {
		return cacheWrapper.getAvailableMemorySize();
	}

}
