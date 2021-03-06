package com.readnovel.book.base.cache.card;

import com.readnovel.book.base.cache.Cache;
import com.readnovel.book.base.utils.StorageUtils;

/**
 * 缓存接口实现
 * 
 */
public abstract class AbsImgSDCache<T> implements Cache<T> {

	@Override
	public String getRootPath() {
		return StorageUtils.externalMemoryRootPath();
	}

	@Override
	public boolean available() {
		return StorageUtils.externalMemoryAvailable();
	}

	@Override
	public long getTotalMemorySize() {
		return StorageUtils.getTotalExternalMemorySize();
	}

	@Override
	public long getAvailableMemorySize() {
		return StorageUtils.getAvailableExternalMemorySize();
	}

}
