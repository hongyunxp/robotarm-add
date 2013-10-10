package com.readnovel.book.base.http;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.readnovel.book.base.cache.Cache;
import com.readnovel.book.base.cache.Filter;
import com.readnovel.book.base.cache.KeyCreater;
import com.readnovel.book.base.cache.viewdata.ViewDataCacheProvider;
import com.readnovel.book.base.cache.viewdata.ViewDataCacheProvider3;
import com.readnovel.book.base.common.NetType;
import com.readnovel.book.base.http.HttpProvider;
import com.readnovel.book.base.http.HttpResult;
import com.readnovel.book.base.utils.HttpUtils;
import com.readnovel.book.base.utils.NetUtils;
import com.readnovel.book.base.BookApp;

public class HttpComm {
	public static JSONObject sendJSONToServerWithCache(String u, long timeOut) {
		return sendJSONToServerWithCache(u, 0, null, null, null, timeOut);
	}

	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	//////////////////////////////////////////////
	public static JSONObject sendJSONToServer(String u) {
		return sendJSONToServer(u, 0);
	}
	//////////////////////////////////////////////
	
	
	/**
	 * 带缓存的get请求
	 * @param u 请求url
	 * @param flag 
	 * @param filter 过滤器
	 * @return
	 */
	public static JSONObject sendJSONToServerWithCache(String u, int flag, Filter<JSONObject> getFilter, Filter<JSONObject> filter,
			KeyCreater keyCreater, long timeOut) {
		Cache<JSONObject> cache = ViewDataCacheProvider.getInstance("/readnovela/viewdataCache/");
		JSONObject jo = null;

		if (timeOut > 0)
			jo = cache.get(u, keyCreater, timeOut);//从缓存里取，指定失效时间
		else
			jo = cache.get(u, keyCreater);//从缓存里取

		if (getFilter != null && jo != null) {//就否使用缓存的结果
			boolean accept = getFilter.accept(jo);
			if (!accept)
				jo = null;
		}
		if (jo == null) {//当缓存为空时从网络加载，并放入缓存
			jo = sendJSONToServer(u, flag);
			cache.put(u, jo, filter, keyCreater);
		}
		return jo;
	}

	/**
	 * get请求
	 * @param u
	 * @param flag
	 * @return
	 */
	public static JSONObject sendJSONToServer(String u, int flag) {
		com.readnovel.book.base.utils.NetUtils.NetType netType = NetUtils.checkNet(BookApp.getInstance());

		if (NetType.TYPE_NONE.equals(netType)) {
			return null;
		}

		HttpProvider httpProvider = null;
		String result = null;
		try {
			httpProvider = HttpProvider.newInstance(30 * 1000, 60 * 1000);
			HttpResult httpResult = httpProvider.get(u, null, null, HttpUtils.ENCODING);
			result = httpResult.httpEntityContent();

			if (result != null && "".equals(result.trim()))
				return null;

			if (flag == 1 && result != null && result.indexOf("{") != 0)
				result = "{root:" + result + "}";

			JSONObject retJsonObject = new JSONObject(result);

			return retJsonObject;
		} catch (Throwable e) {
		} finally {
			if (httpProvider != null) {// 释放连接
				httpProvider.shutdown();
			}
		}

		return null;
	}

	public static JSONArray sendJSONArrayToServerWithCache(String u, long timeOut) {
		return sendJSONArrayToServerWithCache(u, 0, null, null, null, timeOut);
	}

	public static JSONArray sendJSONArrayToServerWithCache(String u, int flag, Filter<JSONArray> getFilter, Filter<JSONArray> filter,
			KeyCreater keyCreater, long timeOut) {
		Cache<JSONArray> cache = ViewDataCacheProvider3.getInstance("/readnovela/viewdataCache/");

		JSONArray jo = null;

		if (timeOut > 0)
			jo = cache.get(u, keyCreater, timeOut);//从缓存里取，指定失效时间
		else
			jo = cache.get(u, keyCreater);//从缓存里取

		if (getFilter != null && jo != null) {//就否使用缓存的结果
			boolean accept = getFilter.accept(jo);
			if (!accept)
				jo = null;
		}

		if (jo == null) {//当缓存为空时从网络加载，并放入缓存
			jo = sendJSONArrayToServer(u, flag);
			cache.put(u, jo, filter, keyCreater);
		}

		return jo;
	}

	public static JSONArray sendJSONArrayToServer(String u, int flag) {
		com.readnovel.book.base.utils.NetUtils.NetType netType = NetUtils.checkNet(BookApp.getInstance());

		if (NetType.TYPE_NONE.equals(netType)) {
			return null;
		}

		HttpProvider httpProvider = null;
		String result = null;
		try {
			httpProvider = HttpProvider.newInstance(30 * 1000, 60 * 1000);
			HttpResult httpResult = httpProvider.get(u, null, null, HttpUtils.ENCODING);
			result = httpResult.httpEntityContent();

			if (result != null && "".equals(result.trim()))
				return null;

			if (flag == 1 && result != null && result.indexOf("{") != 0)
				result = "{root:" + result + "}";

			JSONArray retJsonObject = new JSONArray(result);
			return retJsonObject;

		} catch (Throwable e) {
		} finally {
			if (httpProvider != null) {// 释放连接
				httpProvider.shutdown();
			}
		}

		return null;
	}
}
