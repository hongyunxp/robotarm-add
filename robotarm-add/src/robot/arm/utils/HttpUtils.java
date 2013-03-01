package robot.arm.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import robot.arm.common.Constants;
import robot.arm.provider.http.HttpProvider;
import robot.arm.provider.http.HttpResult;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 
 */

/**
 * Http请求工具类
 * 
 * @author li.li
 * 
 * 
 */
public class HttpUtils {
	public static final String ENCODING = "utf-8";
	public static final int COMMON_CONNECT_TIMEOUT = 30 * 1000;//连接超时
	public static final int COMMON_SO_TIMEOUT = 60 * 1000;//读取超时

	public static HttpResult post(String url, Map<String, String> headers, Map<String, String> params, String encoding) throws Throwable {

		final DefaultHttpClient client = new DefaultHttpClient();

		final List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (String temp : params.keySet()) {
			list.add(new BasicNameValuePair(temp, params.get(temp)));
		}

		final HttpPost post = new HttpPost(url);

		if (null != headers)
			post.setHeaders(assemblyHeader(headers));

		post.setEntity(new UrlEncodedFormEntity(list, encoding));

		return execute(client, post);
	}

	public static HttpResult post(String url, Map<String, String> headers, Map<String, String> params, String encoding, DefaultHttpClient client)
			throws Throwable {

		final List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (String temp : params.keySet()) {
			list.add(new BasicNameValuePair(temp, params.get(temp)));
		}

		final HttpPost post = new HttpPost(url);

		if (null != headers)
			post.setHeaders(assemblyHeader(headers));

		post.setEntity(new UrlEncodedFormEntity(list, encoding));

		return execute(client, post);
	}

	public static HttpResult get(String url, Map<String, String> headers, Map<String, String> params, String encoding) throws Throwable {

		final DefaultHttpClient client = new DefaultHttpClient();

		url = url + (null == params ? "" : assemblyParameter(params));

		final HttpGet get = new HttpGet(url);

		if (null != headers)
			get.setHeaders(assemblyHeader(headers));

		return execute(client, get);
	}

	public static HttpResult get(String url, Map<String, String> headers, Map<String, String> params) throws Throwable {

		return get(url, headers, params, HTTP.UTF_8);
	}

	public static HttpResult get(String url, Map<String, String> headers, Map<String, String> params, String encoding, DefaultHttpClient client)
			throws Throwable {

		url += (null == params ? "" : assemblyParameter(params));

		final HttpGet get = new HttpGet(url);

		if (null != headers)
			get.setHeaders(assemblyHeader(headers));

		return execute(client, get);
	}

	public static HttpResult execute(AbstractHttpClient client, HttpUriRequest request) throws Throwable {
		long startTime = System.currentTimeMillis();

		final HttpResponse response = client.execute(request);
		final HttpResult result = new HttpResult();

		result.setStatusCode(response.getStatusLine().getStatusCode());
		result.setHeaders(response.getAllHeaders());
		result.setCookie(assemblyCookie(client.getCookieStore().getCookies()));
		result.setCookieMap(assemblyCookieMap(client.getCookieStore().getCookies()));
		result.setHttpEntity(response.getEntity());
		result.setRequest(request);

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;

		LogUtils.info(request.getURI().getPath() + "|" + duration);

		return result;
	}

	public static Header[] assemblyHeader(Map<String, String> headers) {
		final Header[] allHeader = new BasicHeader[headers.size()];
		int i = 0;
		for (String str : headers.keySet()) {
			allHeader[i] = new BasicHeader(str, headers.get(str));
			i++;
		}

		return allHeader;
	}

	public static String assemblyCookie(List<Cookie> cookies) {
		final StringBuffer sbu = new StringBuffer();

		for (Cookie cookie : cookies) {
			sbu.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
		}

		if (sbu.length() > 0)
			sbu.deleteCharAt(sbu.length() - 1);

		return sbu.toString();
	}

	public static Map<String, String> assemblyCookieMap(List<Cookie> cookies) {
		final Map<String, String> cookieMap = new HashMap<String, String>(cookies.size());

		for (Cookie cookie : cookies) {
			cookieMap.put(cookie.getName(), cookie.getValue());
		}

		return cookieMap;
	}

	public static String assemblyParameter(Map<String, String> parameters) {
		String para = "?";
		for (String str : parameters.keySet()) {
			para += str + "=" + parameters.get(str) + "&";
		}
		return para.substring(0, para.length() - 1);
	}

	/**
	 * 从网络上读取图片返回
	 * 
	 */
	public static Bitmap loadImage(String imageUrl) {
		HttpProvider httpProvider = null;
		InputStream is = null;

		try {
			httpProvider = HttpProvider.newInstance(Constants.IMG_CONNECT_TIMEOUT, Constants.IMG_SO_TIMEOUT);
			HttpResult httpResult = httpProvider.get(imageUrl, null, null, HttpUtils.ENCODING);
			is = httpResult.getHttpEntity().getContent();

			return BitmapFactory.decodeStream(is);
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			closeStream(is);//关闭流

			if (httpProvider != null)
				httpProvider.shutdown();//关闭会话
		}

		return null;
	}

	/**
	 * 从网络上读取图片并下载到文件
	 * 
	 */
	public static String loadImage(String picPath, String imageUrl) {
		HttpProvider httpProvider = null;
		InputStream is = null;
		OutputStream out = null;

		try {

			httpProvider = HttpProvider.newInstance(Constants.IMG_CONNECT_TIMEOUT, Constants.IMG_SO_TIMEOUT);
			HttpResult httpResult = httpProvider.get(imageUrl, null, null, HttpUtils.ENCODING);
			is = httpResult.getHttpEntity().getContent();

			File file = new File(picPath);
			out = new FileOutputStream(file);

			byte[] buf = new byte[512];
			int length = 0;
			while ((length = is.read(buf)) != -1) {
				out.write(buf, 0, length);
			}
			out.flush();

			return picPath;

		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			closeStream(is);
			closeStream(out);

			if (httpProvider != null)
				httpProvider.shutdown();
		}

		return null;
	}

	public static boolean saveImage(String picPath, Bitmap bm) {
		FileOutputStream out = null;

		try {
			File file = new File(picPath);

			if (!file.exists())
				file.createNewFile();

			out = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();

			return true;
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			closeStream(out);
		}

		return false;

	}

	private static void closeStream(Closeable is) {
		if (is == null)
			return;
		try {
			is.close();
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		}
	}

}
