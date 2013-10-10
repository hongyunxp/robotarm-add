package com.readnovel.book.base.cache.view;

import com.readnovel.book.base.cache.Cache;

public abstract class AbsViewCache<T> implements Cache<T> {

	@Override
	public String getRootPath() {
		throw new RuntimeException("不支持调用此方法ViewCache.getRootPath");
	}

	@Override
	public boolean available() {
		return true;
	}

	@Override
	public long getTotalMemorySize() {
		return Long.MAX_VALUE;
	}

	@Override
	public long getAvailableMemorySize() {
		return Long.MAX_VALUE;
	}

}
