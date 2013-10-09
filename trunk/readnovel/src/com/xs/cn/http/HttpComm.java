package com.xs.cn.http;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.eastedge.readnovel.common.Constants;
import com.readnovel.base.cache.Cache;
import com.readnovel.base.cache.Filter;
import com.readnovel.base.cache.KeyCreater;
import com.readnovel.base.cache.viewdata.JSONObjectSDCache;
import com.readnovel.base.common.NetType;
import com.readnovel.base.http.HttpProvider;
import com.readnovel.base.http.HttpResult;
import com.readnovel.base.util.HttpUtils;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.NetUtils;
import com.readnovel.base.util.StringUtils;
import com.xs.cn.activitys.BookApp;

public class HttpComm {
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String TAG = "HttpComm";

	public static JSONObject sendJSONToServer(String u) {
		return sendJSONToServer(u, 0);
	}

	/**
	 * get请求
	 * @param u
	 * @param flag
	 * @return
	 */
	public static JSONObject sendJSONToServer(String u, int flag) {
		NetType netType = NetUtils.checkNet(BookApp.getInstance());

		if (NetType.TYPE_NONE.equals(netType)) {
			LogUtils.info("无网络，不发送网络请求");
			return null;
		}

		HttpProvider httpProvider = null;
		String result = null;
		try {
			httpProvider = HttpProvider.newInstance(Constants.COMMON_CONNECT_TIMEOUT, Constants.COMMON_SO_TIMEOUT);
			LogUtils.info("请求开始时间：" + format.format(new Date()) + "|" + u);
			HttpResult httpResult = httpProvider.get(u, null, null, HttpUtils.ENCODING);
			result = httpResult.httpEntityContent();

			if (result != null && "".equals(result.trim()))
				return null;

			if (flag == 1 && result != null && result.indexOf("{") != 0)
				result = "{root:" + result + "}";

			JSONObject retJsonObject = new JSONObject(result);
			LogUtils.info("请求结束时间：" + format.format(new Date()) + "|" + retJsonObject + "|" + u);

			return retJsonObject;

		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			if (httpProvider != null) {// 释放连接
				httpProvider.shutdown();
			}
		}

		return null;
	}

	/**
	 * post请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static JSONObject post(String url, Map<String, String> params) {
		NetType netType = NetUtils.checkNet(BookApp.getInstance());

		if (NetType.TYPE_NONE.equals(netType)) {
			LogUtils.info("无网络，不发送网络请求");
			return null;
		}

		HttpProvider httpProvider = null;
		String result = null;
		try {
			httpProvider = HttpProvider.newInstance(Constants.COMMON_CONNECT_TIMEOUT, Constants.COMMON_SO_TIMEOUT);
			LogUtils.info("postRequest请求开始时间：" + format.format(new Date()) + "|" + url);
			HttpResult httpResult = httpProvider.post(url, null, params, HttpUtils.ENCODING);
			result = httpResult.httpEntityContent();

			if (StringUtils.isBlank(result))
				return null;

			JSONObject retJsonObject = new JSONObject(result);
			LogUtils.info("postRequest请求结束时间：" + format.format(new Date()) + "|" + retJsonObject + "|" + url);

			return retJsonObject;

		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			if (httpProvider != null) {// 释放连接
				httpProvider.shutdown();
			}
		}

		return null;
	}

	/**
	 * 带缓存的get请求
	 * @param u 请求url
	 * @param flag 
	 * @param filter 过滤器
	 * @return
	 */
	public static JSONObject sendJSONToServerWithCache(String u, int flag, Filter<JSONObject> getFilter, Filter<JSONObject> filter,
			KeyCreater keyCreater, long timeOut) {
		Cache<JSONObject> cache = JSONObjectSDCache.getInstance(Constants.READNOVEL_VIEW_DATA_CACHE_ABS);

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
	 * 带缓存的get请求
	 * @param u 请求url
	 * @param flag 
	 * @param getFilter 缓存结果过滤器
	 * @param filter 过滤器
	 * @return
	 */
	public static JSONObject sendJSONToServerWithCache(String u, int flag, Filter<JSONObject> getFilter, Filter<JSONObject> filter,
			KeyCreater keyCreater) {

		return sendJSONToServerWithCache(u, flag, getFilter, filter, keyCreater, 0);
	}

	public static JSONObject sendJSONToServerWithCache(String u) {
		return sendJSONToServerWithCache(u, 0, null, null, null);
	}

	public static JSONObject sendJSONToServerWithCache(String u, long timeOut) {
		return sendJSONToServerWithCache(u, 0, null, null, null, timeOut);
	}

	public static JSONObject sendJSONToServerWithCache(String u, int flag) {

		return sendJSONToServerWithCache(u, flag, null, null, null);
	}

	public static JSONObject sendJSONToServerWithCache(String u, int flag, long timeOut) {
		return sendJSONToServerWithCache(u, flag, null, null, null, timeOut);
	}

	public static JSONObject sendJSONToServerWithCache(String u, Filter<JSONObject> filter) {
		return sendJSONToServerWithCache(u, 0, null, filter, null);
	}

	public static JSONObject sendJSONToServerWithCache(String u, Filter<JSONObject> getFilter, Filter<JSONObject> filter) {
		return sendJSONToServerWithCache(u, 0, getFilter, filter, null);
	}

	public static JSONObject sendJSONToServerWithCache(String u, Filter<JSONObject> filter, KeyCreater keyCreater) {
		return sendJSONToServerWithCache(u, 0, null, filter, keyCreater);
	}

	/**
	 * 数据统计，安装量等
	 * @param u
	 * @param v1
	 * @param v2
	 * @param v3
	 */
	public static boolean postMsg(String u, String v1, String v2, String v3, String v4) {
		NetType netType = NetUtils.checkNet(BookApp.getInstance());
		if (NetType.TYPE_NONE.equals(netType)) {
			LogUtils.info("无网络，不发送网络请求");
		}

		HttpProvider httpProvider = null;

		try {

			httpProvider = HttpProvider.newInstance(Constants.COMMON_CONNECT_TIMEOUT, Constants.COMMON_SO_TIMEOUT);

			Map<String, String> params = new HashMap<String, String>();
			params.put("screenpix", v1);
			params.put("model", v2);
			params.put("imei", v3);
			params.put("from_channel", v4);
			HttpResult httpResult = httpProvider.post(u, null, params, "utf-8");
			String result = httpResult.httpEntityContent();
			JSONObject jo = new JSONObject(result);
			LogUtils.info("发送结果：" + result + "|" + !jo.isNull("code"));
			if (!jo.isNull("code")) {
				String code = jo.getString("code");
				boolean isSuccess = "1".equals(code);

				LogUtils.info("是否发成功：" + isSuccess);

				if (isSuccess) {
					return true;
				}

			}

		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
			if (httpProvider != null) {// 释放连接
				httpProvider.shutdown();
			}
		}

		return false;

	}
}
