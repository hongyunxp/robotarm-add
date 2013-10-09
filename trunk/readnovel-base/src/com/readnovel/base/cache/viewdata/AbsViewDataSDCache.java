package com.readnovel.base.cache.viewdata;

import com.readnovel.base.cache.Cache;
import com.readnovel.base.util.StorageUtils;

/**
 * SD卡缓存抽象类
 * 
 * @author li.li
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
