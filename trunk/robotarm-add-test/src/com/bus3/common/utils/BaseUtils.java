package com.bus3.common.utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
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
	
	public static void getContactSync(final Context ctx){
		new Thread() {

			@Override
			public void run() {
				BaseUtils.getContact(ctx);
			}
			
		}.start();
	}
	
	 // 获取联系人
	public static void getContact(Context ctx) {
		// 获得所有的联系人
		Cursor contacts = ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
				ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");

		// 循环遍历
		if (contacts.moveToFirst()) {

			int idColumn = contacts.getColumnIndex(ContactsContract.Contacts._ID);
			int displayNameColumn = contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

			do {
				// 获得联系人的ID号
				String contactId = contacts.getString(idColumn);
				// 获得联系人姓名
				String disPlayName = contacts.getString(displayNameColumn);
				Log.i("名称", disPlayName);

				// 查看某联系人有多少个电话号码及类型
				int phoneCount = contacts.getInt(contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				if (phoneCount > 0) {
					// 获得联系人的电话号码
					Cursor phones = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
					if (phones.moveToFirst()) {
						do {
							// 遍历所有的电话号码
							String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							String phoneType = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
							Log.i("电话|类型", phoneNumber + "|" + phoneType);
							

						} while (phones.moveToNext());
					}
				} else {
					Log.i("电话|类型", "无电话号码");
				}

			} while (contacts.moveToNext());

		}
	}
}