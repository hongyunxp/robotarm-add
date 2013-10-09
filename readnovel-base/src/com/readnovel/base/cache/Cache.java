package com.readnovel.base.cache;

/**
 * 缓存接口
 * 
 * @author li.li
 * 
 */
public interface Cache<T> {
	/**
	 * 存数据
	 * @param key
	 * @param value
	 * @return
	 */
	boolean put(String key, T value);
	
	/**
	 * 存数据
	 * @param key
	 * @param value
	 * @param filter
	 * @return
	 */
	boolean put(String key, T value, Filter<T> filter);
	
	/**
	 * 存数据
	 * @param key
	 * @param value
	 * @param filter
	 * @return
	 */
	boolean put(String key, T value, Filter<T> filter,KeyCreater keyCreater);
	
	/**
	 * 取数据
	 * @param key
	 * @return
	 */
	T get(String key);
	
	/**
	 * 取数据
	 * @param key
	 * @return
	 */
	T get(String key,KeyCreater keyCreater);
	
	/**
	 * 取数据
	 * @param key
	 * @return
	 */
	T get(String key,KeyCreater keyCreater,long timeOut);
	
	/**
	 * 根路径
	 * @return
	 */
	String getRootPath();
	
	/**
	 * 当前缓存是否可用
	 * @return
	 */
	boolean available();
	
	/**
	 * 缓存总大小
	 * @return
	 */
	long getTotalMemorySize();
	
	/**
	 * 缓存可用大小
	 * @return
	 */
	long getAvailableMemorySize();

}
