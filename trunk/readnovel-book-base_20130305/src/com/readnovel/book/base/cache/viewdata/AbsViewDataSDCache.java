package com.readnovel.book.base.cache.viewdata;

import com.readnovel.book.base.cache.Cache;
import com.readnovel.book.base.utils.StorageUtils;



/**
 * SD卡缓存抽象类
 * 
 * 
 */
public abstract class AbsViewDataSDCache<T> implements Cache<T> {

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
