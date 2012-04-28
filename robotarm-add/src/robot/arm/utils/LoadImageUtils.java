/**
 * 
 */
package robot.arm.utils;

import java.io.InputStream;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import robot.arm.common.RobotArmApp;
import robot.arm.provider.asyc.AsycTask;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * @author li.li
 * 
 *         Apr 19, 2012
 * 
 */
public class LoadImageUtils {
	private static final String TAG = LoadImageUtils.class.getName();

	private static final int TIME_OUT = 30000;
	private static final int CACHE_SIZE = 100;// 内存缓存100张图
	private static final LRUMemCache<Bitmap> cache = new LRUMemCache<Bitmap>(CACHE_SIZE);// 图片bitmap缓存

	public static void loadImageSync(Activity act, final String imageUrl, final ImageView imageView) {

		loadImageSync(act, imageUrl, imageView, true);// 走本地存储

	}

	public static void loadImageSync(Activity act, final String imageUrl, final ImageView imageView, final boolean local) {

		// 创建并执行异步任务
		new AsycTask<Activity>(act) {
			Bitmap bm;

			@Override
			public void doCall() {
				// bm = cache.getCache(imageUrl);// 取缓存

				if (bm == null) {
					if (local)
						bm = MemoryUtils.getPicToSd(imageUrl);

					if (bm == null) {
						bm = loadImage(imageUrl);
						if (local)
							MemoryUtils.savePicToSd(bm, imageUrl);// 将图片存到SD卡
					}

					// cache.putCache(imageUrl, bm);// 存缓存
				}

			}

			@Override
			public void doResult() {
				if (bm != null) {
					imageView.setImageBitmap(bm);
					imageView.setScaleType(ScaleType.CENTER_CROP);

				}

			}

		}.execute();

	}

	/**
	 * 从网络上读取图片
	 */
	public static Bitmap loadImage(String imageUrl) {
		HttpClient client = null;
		HttpGet get = new HttpGet(imageUrl);
		InputStream is = null;
		try {
			client = getHttpClient();
			HttpResponse response = client.execute(get);
			is = response.getEntity().getContent();

			return BitmapFactory.decodeStream(is);
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			BaseUtils.closeInputStream(is);
			if (client != null)
				client.getConnectionManager().shutdown();
		}
	}

	private static HttpClient getHttpClient() throws Throwable {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);

		if (checkGPRS_WAP()) {
			httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(Proxy.getDefaultHost(), Proxy.getDefaultPort(), "http"));
		}
		return new DefaultHttpClient(httpParams);

	}

	/**
	 * 检查手机网络连接类型是否为GPRS WAP
	 */
	private static boolean checkGPRS_WAP() {
		if (!checkNetIsAvailable())
			return false;

		NetworkInfo info = ((ConnectivityManager) RobotArmApp.getApp().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		// WIFI连接
		if (ConnectivityManager.TYPE_WIFI == info.getType())
			return false;

		// GPRS方式连接
		if (ConnectivityManager.TYPE_MOBILE != info.getType())
			return false;

		return Proxy.getDefaultHost() != null && !"".equals(Proxy.getDefaultHost());
	}

	/**
	 * 检查手机网络是否可用 true：可用 false:不可用
	 */
	public static boolean checkNetIsAvailable() {
		// 获取手机所有连接管理对象
		ConnectivityManager cm = (ConnectivityManager) RobotArmApp.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;

		// 获取网络连接管理的对象
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null || !info.isAvailable() || !info.isConnected() || info.getState() != NetworkInfo.State.CONNECTED)
			return false;
		return true;
	}
}
