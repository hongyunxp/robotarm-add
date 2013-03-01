package robot.arm.provider.http;

import java.util.Map;

import robot.arm.common.CommonApp;
import robot.arm.utils.HttpUtils;
import robot.arm.utils.JsonUtils;
import robot.arm.utils.LogUtils;
import robot.arm.utils.NetType;
import robot.arm.utils.NetUtils;
import robot.arm.utils.StringUtils;

/**
 * http请求帮助类
 * 
 * 提供将一个请求的结果返回一个对象
 * 如果返回的json数据是空的则返回空对象
 * 
 * @author li.li
 *
 * Dec 27, 2012
 */
public class HttpHelper {

	/**
	 * post请求
	 * @param url 请求地址
	 * @param params 参数
	 * @param clazz 返回的对象实例类型
	 * @return
	 */
	public static <T> T post(String url, Map<String, String> params, Class<T> clazz) {

		String result = post(url, params);

		if (StringUtils.isBlank(result))
			return null;

		T obj = JsonUtils.fromJson(result, clazz);

		return obj;
	}

	/**
	 * get请求
	 * @param url 请求地址
	 * @param params 参数
	 * @param clazz 返回的对象实例类型
	 * @return
	 */
	public static <T> T get(String url, Map<String, String> params, Class<T> clazz) {

		String result = get(url, params);

		if (StringUtils.isBlank(result))
			return null;

		T obj = JsonUtils.fromJson(result, clazz);

		return obj;
	}

	/**
	 * post请求
	 * @param url 请求地址
	 * @param params 参数
	 * @return
	 */
	public static String post(String url, Map<String, String> params) {
		NetType netType = NetUtils.checkNet(CommonApp.getInstance());

		if (NetType.TYPE_NONE.equals(netType)) {
			LogUtils.info("无网络，不发送网络请求");
			return null;
		}

		HttpProvider httpProvider = null;
		String result = null;

		try {
			httpProvider = HttpProvider.newInstance();
			HttpResult httpResult = httpProvider.post(url, null, params, HttpUtils.ENCODING);
			result = httpResult.httpEntityContent();

			return result;

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
	 * get请求
	 * @param url 请求地址
	 * @param params 参数
	 * @return
	 */
	public static String get(String url, Map<String, String> params) {
		NetType netType = NetUtils.checkNet(CommonApp.getInstance());

		if (NetType.TYPE_NONE.equals(netType)) {
			LogUtils.info("无网络，不发送网络请求");
			return null;
		}

		HttpProvider httpProvider = null;
		String result = null;

		try {
			httpProvider = HttpProvider.newInstance();
			HttpResult httpResult = httpProvider.get(url, null, params, HttpUtils.ENCODING);
			result = httpResult.httpEntityContent();

			return result;

		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			if (httpProvider != null) {// 释放连接
				httpProvider.shutdown();
			}
		}

		return null;
	}
}
