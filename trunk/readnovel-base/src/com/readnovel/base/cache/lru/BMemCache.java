package com.readnovel.base.cache.lru;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION_CODES;
import android.support.v4.util.LruCache;

import com.readnovel.base.common.CommonApp;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.PhoneUtils;

/**
 * Bitmap内存缓存
 * 
 * the baseline Android memory class is 16 (which happens to be the Java heap limit of those devices); 
 * some device with more memory may return 24 or even higher numbers.
 * 
 * @author li.li
 *
 * Dec 10, 2012
 */
public class BMemCache implements LruCacheFace<Bitmap> {
	private static final BMemCache instance = new BMemCache();
	private static final int BYTE = 1;
	private static final int KB = BYTE * 1024;
	private static final int MB = KB * 1024;
	private static final int DEFAULT_SIZE = 16 * MB;

	//lru缓存组件
	private LruCache<String, Bitmap> lruCache;
	//应用运行内存分配大小
	private int memClass;

	private BMemCache() {
		init();
	}

	@Override
	public void put(String key, Bitmap bm) {
		if (get(key) == null)
			lruCache.put(key, bm);

	}

	@Override
	public Bitmap get(String key) {
		return lruCache.get(key);
	}

	private void init() {

		if (PhoneUtils.getVersionCode() > VERSION_CODES.DONUT) {//api大于4时获得真正的可运行应用内存

			memClass = new Object() {

				@TargetApi(5)
				public int getMemClass() {
					return ((ActivityManager) CommonApp.getInstance().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
				}

			}.getMemClass() * MB;
		} else {//应用运行内存大小,默认最小16MB

			memClass = DEFAULT_SIZE;
		}

		// Use 1/8th of the available memory for this memory cache.
		// 和默认大小对比取最大的
		int cacheSize = Math.max(memClass / 8, DEFAULT_SIZE);

		LogUtils.info("内存缓存分配空间字节数（总空间的1/8）：" + cacheSize);

		init(cacheSize);

	}

	private void init(int cacheSize) {

		lruCache = new LruCache<String, Bitmap>(cacheSize) {

			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in bytes rather than number of items.
				//Returns the number of bytes used to store this bitmap's pixels.
				int byteCount = bitmap.getRowBytes() * bitmap.getHeight();

				LogUtils.info("当前图片大小|" + byteCount);

				return byteCount;

			}

		};
	}

	public static BMemCache getInstance() {
		return instance;
	}

}
