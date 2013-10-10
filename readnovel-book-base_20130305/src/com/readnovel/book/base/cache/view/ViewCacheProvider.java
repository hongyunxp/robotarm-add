package com.readnovel.book.base.cache.view;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.view.View;

import com.readnovel.book.base.cache.Filter;
import com.readnovel.book.base.cache.KeyCreater;
import com.readnovel.book.base.utils.StringUtils;

/**
 * 视图缓存
 * 
 */
public class ViewCacheProvider extends AbsViewCache<View> {
	private static final ViewCacheProvider instance = new ViewCacheProvider();
	public static final Map<String, WeakReference<View>> VIEW_CACHE = new ConcurrentHashMap<String, WeakReference<View>>();

	private ViewCacheProvider() {
	}

	public static ViewCacheProvider getInstance() {
		return instance;
	}

	@Override
	public View get(String url) {
		return get(url, null);
	}

	// 从缓存中取
	@Override
	public View get(String url, KeyCreater keyCreater) {
		if (StringUtils.isBlank(url))
			return null;

		String key = null;
		if (keyCreater != null)
			key = keyCreater.create();
		else
			key = url;

		WeakReference<View> cacheView = VIEW_CACHE.get(key);
		View curView = null;

		if (cacheView != null)
			if (cacheView.get() != null)
				curView = cacheView.get();// 缓存中取当前View
			else
				VIEW_CACHE.remove(cacheView);// 失效时移除

		return curView;
	}
	
	@Override
	public View get(String key, KeyCreater keyCreater, long timeOut) {
		return null;
	}

	// 放入缓存
	@Override
	public boolean put(String key, View view) {

		if (StringUtils.isBlank(key) || view == null)
			return false;

		WeakReference<View> weakWiew = VIEW_CACHE.put(key, new WeakReference<View>(view));// View加入缓存

		return weakWiew == null ? false : true;
	}

	@Override
	public boolean put(String key, View view, Filter<View> filter) {

		return put(key, view, filter, null);
	}

	@Override
	public boolean put(String key, View view, Filter<View> filter, KeyCreater keyCreater) {
		//当过滤器为空，或接受时，加入缓存
		if (keyCreater != null)
			key = keyCreater.create();

		if (filter == null || filter.accept(view))
			return put(key, view);

		return false;
	}

}
