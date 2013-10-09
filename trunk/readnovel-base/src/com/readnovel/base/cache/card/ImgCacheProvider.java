package com.readnovel.base.cache.card;

import android.graphics.Bitmap;

import com.readnovel.base.cache.Cache;
import com.readnovel.base.cache.Filter;
import com.readnovel.base.cache.KeyCreater;

/**
 * 缓存包装
 * 
 * @author li.li
 * 
 */
public class ImgCacheProvider implements Cache<Bitmap> {
	private static volatile ImgCacheProvider instance;
	private final ImgCacheWrapper cacheWrapper;

	private ImgCacheProvider(String picPath) {
		cacheWrapper = ImgCacheWrapper.getInstance(picPath);
	}

	public static ImgCacheProvider getInstance(String picPath) {
		if (instance == null) {
			synchronized (ImgCacheProvider.class) {
				if (instance == null) {
					instance = new ImgCacheProvider(picPath);
				}
			}
		}

		return instance;
	}

	@Override
	public boolean put(String key, Bitmap value) {
		return cacheWrapper.put(key, value);
	}

	@Override
	public boolean put(String key, Bitmap value, Filter<Bitmap> filter) {
		return cacheWrapper.put(key, value, filter);
	}

	@Override
	public boolean put(String key, Bitmap value, Filter<Bitmap> filter, KeyCreater keyCreater) {
		return cacheWrapper.put(key, value, filter, keyCreater);
	}

	@Override
	public Bitmap get(String key) {
		return cacheWrapper.get(key, null);
	}

	@Override
	public Bitmap get(String key, KeyCreater keyCreater) {
		return cacheWrapper.get(key, keyCreater);
	}

	@Override
	public Bitmap get(String key, KeyCreater keyCreater, long timeOut) {
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
