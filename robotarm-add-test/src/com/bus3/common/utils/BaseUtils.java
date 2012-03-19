package com.bus3.common.utils;

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

}