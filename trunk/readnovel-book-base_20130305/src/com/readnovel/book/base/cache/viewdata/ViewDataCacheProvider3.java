package com.readnovel.book.base.cache.viewdata;

import org.json.JSONArray;

import com.readnovel.book.base.cache.Cache;
import com.readnovel.book.base.cache.Filter;
import com.readnovel.book.base.cache.KeyCreater;

public class ViewDataCacheProvider3 implements Cache<JSONArray> {
	private static volatile ViewDataCacheProvider3 instance;
	private final ViewDataCacheWrapper3 cacheWrapper;

	private ViewDataCacheProvider3(String path) {
		cacheWrapper = ViewDataCacheWrapper3.getInstance(path);
	}

	public static ViewDataCacheProvider3 getInstance(String path) {
		if (instance == null) {
			synchronized (ViewDataCacheProvider3.class) {
				if (instance == null) {
					instance = new ViewDataCacheProvider3(path);
				}
			}
		}
		return instance;
	}

	@Override
	public boolean put(String key, JSONArray value) {
		return cacheWrapper.put(key, value);
	}

	@Override
	public boolean put(String key, JSONArray value, Filter<JSONArray> filter) {
		return cacheWrapper.put(key, value, filter);
	}

	@Override
	public boolean put(String key, JSONArray value, Filter<JSONArray> filter, KeyCreater keyCreater) {
		return cacheWrapper.put(key, value, filter, keyCreater);
	}

	@Override
	public JSONArray get(String url) {
		return cacheWrapper.get(url, null);
	}

	@Override
	public JSONArray get(String key, KeyCreater keyCreater) {
		return cacheWrapper.get(key, keyCreater);
	}

	@Override
	public JSONArray get(String key, KeyCreater keyCreater, long timeOut) {
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
