package com.readnovel.base.cache.card;

import com.readnovel.base.cache.Cache;
import com.readnovel.base.util.StorageUtils;

/**
 * 缓存接口实现
 * 
 * @author li.li
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
