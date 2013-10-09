/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.readnovel.base.cache.lru;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

import com.readnovel.base.util.LogUtils;

/**
 * 硬盘缓存
 * @author li.li
 *
 * Apr 17, 2013
 */
public abstract class DiskLruCache<T> {
	private static final int MAX_REMOVALS = 4;
	private static final int INITIAL_CAPACITY = 32;
	private static final float LOAD_FACTOR = 0.75f;
	private static final int DEF_MAX_CACHE_ITEM_SIZE = 64;// 64 item default
	private static final long DEF_MAX_CACHE_BYTE_SIZE = 1024 * 1024 * 5;// 5MB default

	private final File mCacheDir;
	private int cacheSize;
	private int cacheByteSize;

	private final int maxCacheItemSize = DEF_MAX_CACHE_ITEM_SIZE; // 64 item default
	private long maxCacheByteSize = DEF_MAX_CACHE_BYTE_SIZE; // 5MB default

	private final Map<String, String> mLinkedHashMap = Collections.synchronizedMap(new LinkedHashMap<String, String>(INITIAL_CAPACITY, LOAD_FACTOR,
			true));

	/**
	 * Constructor that should not be called directly, instead use {@link DiskLruCache#openCache(Context, File, long)} which runs some extra checks before creating a DiskLruCache
	 * instance.
	 * 
	 * @param cacheDir
	 * @param maxByteSize
	 */
	public DiskLruCache(File cacheDir) {
		mCacheDir = cacheDir;
	}

	/**
	 * Constructor that should not be called directly, instead use {@link DiskLruCache#openCache(Context, File, long)} which runs some extra checks before creating a DiskLruCache
	 * instance.
	 * 
	 * @param cacheDir
	 * @param maxByteSize
	 */
	public DiskLruCache(File cacheDir, long maxByteSize) {
		mCacheDir = cacheDir;
		maxCacheByteSize = maxByteSize;
	}

	/**
	 * Add a bitmap to the disk cache.
	 * 
	 * @param key
	 *            A unique identifier for the bitmap.
	 * @param data
	 *            The bitmap to store.
	 */
	public void put(String key, T obj) {
		synchronized (mLinkedHashMap) {
			if (mLinkedHashMap.get(key) == null) {
				try {
					final String file = createFilePath(mCacheDir, key);
					if (writeToFile(obj, file)) {
						put(key, file);
						flushCache();
					}
				} catch (Throwable e) {
					LogUtils.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * Get an image from the disk cache.
	 * 
	 * @param key
	 *            The unique identifier for the bitmap
	 * @return The bitmap or null if not found
	 */
	public T get(String key) {
		synchronized (mLinkedHashMap) {
			try {
				final String file = mLinkedHashMap.get(key);
				if (file != null) {
					return readFromFile(file);
				} else {
					final String existingFile = createFilePath(mCacheDir, key);
					if (new File(existingFile).exists()) {
						put(key, existingFile);
						return readFromFile(existingFile);
					}
				}
			} catch (Throwable e) {
				LogUtils.error(e.getMessage(), e);
			}
			return null;
		}
	}

	/**
	 * Removes all disk cache entries from this instance cache dir
	 */
	public void clearCache() {
		DiskLruCache.clearCache(mCacheDir);
	}

	/**
	 * Writes a Object to a file.
	 * 
	 * @param bitmap
	 * @param file
	 * @return
	 */
	protected abstract boolean writeToFile(T obj, String file) throws Throwable;

	/**
	 * Read a Object from a file.
	 * @param file
	 * @return
	 * @throws OutOfMemoryError
	 */
	protected abstract T readFromFile(String file) throws Throwable;

	/**
	 * *********************************************************************************************
	 * 私有方法，不对外
	 * *********************************************************************************************
	 */

	/**
	 * Creates a constant cache file path given a target cache directory and an image key.
	 * 
	 * @param cacheDir
	 * @param key
	 * @return
	 */
	private static String createFilePath(File cacheDir, String key) {

		return cacheDir.getAbsolutePath() + File.separator + key;

	}

	/**
	 * Removes all disk cache entries from the given directory. This should not be called directly, call {@link DiskLruCache#clearCache(Context, String)} or
	 * {@link DiskLruCache#clearCache()} instead.
	 * 
	 * @param cacheDir
	 *            The directory to remove the cache files from
	 */
	private static void clearCache(File cacheDir) {
		final File[] files = cacheDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}

	private void put(String key, String file) {
		mLinkedHashMap.put(key, file);
		cacheSize = mLinkedHashMap.size();
		cacheByteSize += new File(file).length();
	}

	/**
	 * Flush the cache, removing oldest entries if the total size is over the specified cache size. Note that this isn't keeping track of stale files in the cache directory that
	 * aren't in the HashMap. If the images and keys in the disk cache change often then they probably won't ever be removed.
	 */
	private void flushCache() {
		Entry<String, String> eldestEntry;
		File eldestFile;
		long eldestFileSize;
		int count = 0;

		while (count < MAX_REMOVALS && (cacheSize > maxCacheItemSize || cacheByteSize > maxCacheByteSize)) {
			eldestEntry = mLinkedHashMap.entrySet().iterator().next();
			eldestFile = new File(eldestEntry.getValue());
			eldestFileSize = eldestFile.length();
			mLinkedHashMap.remove(eldestEntry.getKey());
			eldestFile.delete();
			cacheSize = mLinkedHashMap.size();
			cacheByteSize -= eldestFileSize;
			count++;
		}
	}
}
