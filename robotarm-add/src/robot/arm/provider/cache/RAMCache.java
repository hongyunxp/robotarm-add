
package robot.arm.provider.cache;

import org.apache.commons.collections.map.LRUMap;
import android.graphics.Bitmap;

/**
 * @author li.li
 * 
 *         Apr 28, 2012
 * 
 */
public class RAMCache implements Cache {
	private static final int CACHE_SIZE = 100;// 内存缓存100张图
	
	private final LRUMap cache;
	
	public RAMCache() {
		cache = new LRUMap(CACHE_SIZE);
	}

	@Override
	public void put(String key, Bitmap value) {
		cache.put(key, value);
		
	}
	
	@Override
	public Bitmap get(String key) {
		Bitmap value = null;

		final Object o = cache.get(key);

		if (o != null) {
			value = (Bitmap) o;
		}
		return value;
	}
	
	@Override
	public boolean available() {
		return true;
	}

	
	////////////////////////////以下不用
	@Override
	public String getRootPath() {
		return null;
	}

	@Override
	public long getTotalExternalMemorySize() {
		return 0;
	}

}
