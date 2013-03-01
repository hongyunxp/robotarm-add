/**
 * 
 */
package robot.arm.provider.http;

import java.util.Map;

import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import robot.arm.utils.HttpUtils;

/**
 * Http请求
 * 
 * @author li.li
 * 
 */
public class HttpProvider {
	private DefaultHttpClient client;

	private HttpProvider(int conTimeout, int soTimeout) {
		HttpParams httpParams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, conTimeout);
		HttpConnectionParams.setSoTimeout(httpParams, soTimeout);
		httpParams.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);

		client = new DefaultHttpClient(httpParams);

	}

	/**
	 * 创建建使用默认超时时间的实例 
	 * @return
	 */
	public static HttpProvider newInstance() {
		return new HttpProvider(HttpUtils.COMMON_CONNECT_TIMEOUT, HttpUtils.COMMON_SO_TIMEOUT);
	}

	/**
	 * 创建实例，自定义超时时间
	 * @param conTimeout
	 * @param soTimeout
	 * @return
	 */
	public static HttpProvider newInstance(int conTimeout, int soTimeout) {
		return new HttpProvider(conTimeout, soTimeout);
	}

	/**
	 * 发送get请求
	 * @param url
	 * @param headers 
	 * @param params
	 * @param encoding
	 * @return
	 * @throws Throwable
	 */
	public HttpResult get(String url, Map<String, String> headers, Map<String, String> params, String encoding) throws Throwable {

		HttpResult result = HttpUtils.get(url, headers, params, encoding, client);

		return result;
	}

	/**
	 * 发送post请求
	 * @param url
	 * @param headers
	 * @param params
	 * @param encoding
	 * @return
	 * @throws Throwable
	 */
	public HttpResult post(String url, Map<String, String> headers, Map<String, String> params, String encoding) throws Throwable {

		HttpResult result = HttpUtils.post(url, headers, params, encoding, client);

		return result;
	}

	/**
	 * 关闭会话
	 */
	public void shutdown() {
		if (client != null)
			client.getConnectionManager().shutdown();
	}

}
