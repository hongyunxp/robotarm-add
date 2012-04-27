/**
 * 
 */
package robot.arm.common;

import org.apache.commons.collections.map.LRUMap;

/**
 * @author li.li
 * 
 *         Apr 27, 2012
 *         
 * LRU内存缓存
 * 
 */
public class LRUMemCache<T> {
	private final int cache_size;
	private final LRUMap cache;// 缓存

	public LRUMemCache(int size) {
		cache_size = size;
		cache = new LRUMap(cache_size);
	}

	public void putCache(Object key, T value) {
		cache.put(key, value);// 存缓存

	}

	@SuppressWarnings("unchecked")
	public T getCache(Object key) {
		T value = null;

		final Object o = cache.get(key);

		if (o != null) {
			value = (T) o;
		}

		return value;
	}
}
