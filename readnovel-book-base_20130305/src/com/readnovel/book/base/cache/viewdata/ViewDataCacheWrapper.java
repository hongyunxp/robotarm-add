package com.readnovel.book.base.cache.viewdata;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import com.readnovel.book.base.cache.Cache;
import com.readnovel.book.base.cache.Filter;
import com.readnovel.book.base.cache.KeyCreater;

/**
 * 缓存适配器
 * 
 * Aug 7, 2012
 */
public class ViewDataCacheWrapper implements Cache<JSONObject> {
	private static volatile ViewDataCacheWrapper instance;
	private final List<? extends Cache<JSONObject>> caches;

	private ViewDataCacheWrapper(String path) {
		caches = Arrays.asList(ViewDataSDCache.getInstance(path));
	}

	public static ViewDataCacheWrapper getInstance(String path) {
		if (instance == null) {
			synchronized (ViewDataCacheWrapper.class) {
				if (instance == null) {
					instance = new ViewDataCacheWrapper(path);
				}
			}
		}

		return instance;
	}

	@Override
	public boolean put(String key, JSONObject value) {

		//成功即返回
		for (Cache<JSONObject> cache : caches) {
			if (cache.available() && cache.put(key, value))
				return true;
		}

		return false;//无可用的或不成功返回
	}

	@Override
	public boolean put(String key, JSONObject value, Filter<JSONObject> filter) {
		//成功即返回
		for (Cache<JSONObject> cache : caches) {
			if (cache.available() && cache.put(key, value, filter))
				return true;
		}

		return false;//无可用的或不成功返回
	}

	@Override
	public boolean put(String key, JSONObject value, Filter<JSONObject> filter, KeyCreater keyCreater) {
		//成功即返回
		for (Cache<JSONObject> cache : caches) {
			if (cache.available() && cache.put(key, value, filter, keyCreater))
				return true;
		}

		return false;//无可用的或不成功返回
	}

	@Override
	public JSONObject get(String url) {
		return get(url, null);
	}

	@Override
	public JSONObject get(String key, KeyCreater keyCreater) {

		//成功即返回
		JSONObject jo = null;
		for (Cache<JSONObject> cache : caches) {
			if (cache.available() && (jo = cache.get(key, keyCreater)) != null)
				return jo;
		}

		return jo;//无可用的或不成功返回
	}

	@Override
	public JSONObject get(String key, KeyCreater keyCreater, long timeOut) {
		//成功即返回
		JSONObject jo = null;
		for (Cache<JSONObject> cache : caches) {
			if (cache.available() && (jo = cache.get(key, keyCreater, timeOut)) != null)
				return jo;
		}

		return jo;//无可用的或不成功返回
	}

	@Override
	public String getRootPath() {
		for (Cache<JSONObject> cache : caches) {
			if (cache.available())
				return cache.getRootPath();
		}

		return null;
	}

	@Override
	public boolean available() {
		for (Cache<JSONObject> cache : caches) {
			if (cache.available())
				return true;
		}

		return false;
	}

	@Override
	public long getTotalMemorySize() {
		for (Cache<JSONObject> cache : caches) {
			if (cache.available())
				return cache.getTotalMemorySize();
		}

		return 0;
	}

	@Override
	public long getAvailableMemorySize() {
		for (Cache<JSONObject> cache : caches) {
			if (cache.available())
				return cache.getAvailableMemorySize();
		}

		return 0;
	}

}
