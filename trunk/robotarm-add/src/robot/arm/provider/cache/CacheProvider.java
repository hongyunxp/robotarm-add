/**
 * 
 */
package robot.arm.provider.cache;

import java.util.Arrays;
import java.util.List;

import android.graphics.Bitmap;

/**
 * @author li.li
 * 
 *         Apr 28, 2012
 *         
 *         接口穿透
 * 
 */
public class CacheProvider implements Cache {
	private static final CacheProvider instance = new CacheProvider();
	private static final List<? extends Cache> caches = Arrays.asList(new CardCache(), new MemoryCache());

	private CacheProvider() {
	}

	public static CacheProvider getInstance() {
		return instance;
	}

	@Override
	public String getRootPath() {

		for (Cache cache : caches) {
			if (cache.available())
				return cache.getRootPath();
		}

		return null;
	}

	@Override
	public boolean available() {

		for (Cache cache : caches) {
			return cache.available();
		}

		return false;
	}

	@Override
	public void put(Bitmap bm, String imageUrl) {
		for (Cache cache : caches) {
			if (cache.available())
				cache.put(bm, imageUrl);
		}
	}

	@Override
	public Bitmap get(String imageUrl) {
		for (Cache cache : caches) {
			if (cache.available())
				return cache.get(imageUrl);
		}

		return null;
	}
	@Override
	public long getTotalExternalMemorySize() {
		for (Cache cache : caches) {
			if (cache.available())
				return cache.getTotalExternalMemorySize();
		}
		
		return 0;
	}
}
