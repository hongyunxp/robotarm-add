package robot.arm.provider.cache.card;

import robot.arm.provider.cache.Cache;
import robot.arm.provider.cache.Filter;
import robot.arm.provider.cache.KeyCreater;
import android.graphics.Bitmap;

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
