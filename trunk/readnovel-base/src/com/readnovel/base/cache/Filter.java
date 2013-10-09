package com.readnovel.base.cache;
/**
 * 存缓存过滤器
 * 
 * @author li.li
 *
 * Aug 15, 2012
 */
public interface Filter<T> {
	boolean accept(T obj);
}
