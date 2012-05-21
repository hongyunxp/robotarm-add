/**
 * 
 */
package robot.arm.provider.cache;

import java.util.Arrays;
import java.util.List;

/**
 * @author li.li
 * 
 *         Apr 28, 2012
 * 
 *         三级缓存
 * 
 *         接口穿透
 * 
 */
public class CacheProvider implements Cache {
	private static final CacheProvider instance = new CacheProvider();
	private static final List<? extends Cache> caches = Arrays.asList(//

			// new RAMCache(),// 内存缓存
			// new PMCache(),// 手机存储缓存
			new SDCache()// sd卡缓存

			);

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
	public String put(String imageUrl) {
		String result=null;
		
		for (Cache cache : caches) {
			if (cache.available())
				result = cache.put(imageUrl);
		}
		
		return result;
	}

	@Override
	public String get(String imageUrl) {
		for (Cache cache : caches) {
			if (cache.available())
				return cache.get(imageUrl);
		}

		return null;
	}

	@Override
	public long getTotalMemorySize() {
		for (Cache cache : caches) {
			if (cache.available())
				return cache.getTotalMemorySize();
		}

		return 0;
	}

	@Override
	public long getAvailableMemorySize() {
		for (Cache cache : caches) {
			if (cache.available())
				return cache.getAvailableMemorySize();
		}

		return 0;
	}
}
