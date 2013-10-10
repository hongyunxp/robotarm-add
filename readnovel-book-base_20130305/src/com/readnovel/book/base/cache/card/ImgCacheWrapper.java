package com.readnovel.book.base.cache.card;

import java.util.Arrays;
import java.util.List;

import android.graphics.Bitmap;

import com.readnovel.book.base.cache.Cache;
import com.readnovel.book.base.cache.Filter;
import com.readnovel.book.base.cache.KeyCreater;

public class ImgCacheWrapper implements Cache<Bitmap> {
	private static volatile ImgCacheWrapper instance;
	private final List<? extends Cache<Bitmap>> caches;

	private ImgCacheWrapper(String picPath) {
		caches = Arrays.asList(new ImgSDCache(picPath));
	}

	/**
	 * picPath 缓存相对路径。例：String READNOVEL_IMGCACHE = "/readnovel/imgCache/"
	 * @param picPath
	 * @return
	 */
	public static ImgCacheWrapper getInstance(String picPath) {
		if (instance == null) {
			synchronized (ImgCacheWrapper.class) {
				if (instance == null) {
					instance = new ImgCacheWrapper(picPath);
				}
			}
		}

		return instance;
	}

	@Override
	public String getRootPath() {

		for (Cache<Bitmap> cache : caches) {
			if (cache.available())
				return cache.getRootPath();
		}

		return null;
	}

	@Override
	public boolean available() {

		for (Cache<Bitmap> cache : caches) {
			return cache.available();
		}

		return false;
	}

	@Override
	public boolean put(String imageUrl, Bitmap bm) {
		boolean result = false;

		for (Cache<Bitmap> cache : caches) {
			if (cache.available()) {
				result = cache.put(imageUrl, bm);
				break;
			}
		}

		return result;
	}

	@Override
	public boolean put(String imageUrl, Bitmap bm, Filter<Bitmap> filter) {
		boolean result = false;

		for (Cache<Bitmap> cache : caches) {
			if (cache.available()) {
				result = cache.put(imageUrl, bm, filter);
				break;
			}
		}

		return result;
	}

	@Override
	public boolean put(String imageUrl, Bitmap bm, Filter<Bitmap> filter, KeyCreater keyCreater) {
		boolean result = false;

		for (Cache<Bitmap> cache : caches) {
			if (cache.available()) {
				result = cache.put(imageUrl, bm, filter, keyCreater);
				break;
			}
		}

		return result;
	}

	@Override
	public Bitmap get(String key) {
		Bitmap bm = null;

		for (Cache<Bitmap> cache : caches) {
			if (cache.available()) {
				bm = cache.get(key, null);
				break;
			}
		}

		return bm;
	}

	@Override
	public Bitmap get(String key, KeyCreater keyCreater) {
		Bitmap bm = null;

		for (Cache<Bitmap> cache : caches) {
			if (cache.available()) {
				bm = cache.get(key, keyCreater);
				break;
			}
		}

		return bm;
	}

	@Override
	public Bitmap get(String key, KeyCreater keyCreater, long timeOut) {
		Bitmap bm = null;

		for (Cache<Bitmap> cache : caches) {
			if (cache.available()) {
				bm = cache.get(key, keyCreater, timeOut);
				break;
			}
		}

		return bm;
	}

	@Override
	public long getTotalMemorySize() {
		for (Cache<Bitmap> cache : caches) {
			if (cache.available())
				return cache.getTotalMemorySize();
		}

		return 0;
	}

	@Override
	public long getAvailableMemorySize() {
		for (Cache<Bitmap> cache : caches) {
			if (cache.available())
				return cache.getAvailableMemorySize();
		}

		return 0;
	}

}
