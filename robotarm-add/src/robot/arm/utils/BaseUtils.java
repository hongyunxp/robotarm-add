package robot.arm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import robot.arm.R;
import robot.arm.common.RobotArmApp;
import robot.arm.provider.asyc.AsycTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 
 * @author li.li
 * 
 *         Mar 15, 2012
 * 
 */
public class BaseUtils {
	private static final int TIME_OUT = 30000;
	private static final String TAG = BaseUtils.class.getName();
	public static final String ENCODING = "utf-8";
	private static volatile DisplayMetrics display;

	public static void confirm(final Context context, int message, OnClickListener pl, OnClickListener nl) {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setTitle(R.string.confirm_title);
		tDialog.setMessage(message);
		tDialog.setPositiveButton(R.string.Ensure, pl);
		tDialog.setNegativeButton(R.string.Cancel, nl);
		tDialog.show();
	}

	public static DisplayMetrics getScreen(Activity act) {
		if (display == null) {
			synchronized (BaseUtils.class) {
				if (display == null) {
					DisplayMetrics metrics = new DisplayMetrics();
					act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
					display = metrics;
				}
			}
		}

		return display;
	}

	public static int setListViewHeight(ListView lv) {
		ViewGroup.LayoutParams lp = lv.getLayoutParams();
		lp.height = getListViewHeight(lv);
		lv.setLayoutParams(lp);

		return lp.height;
	}

	public static void setViewGroupHeight(ViewGroup vg, int height) {
		ViewGroup.LayoutParams lp = vg.getLayoutParams();
		lp.height = height;
		vg.setLayoutParams(lp);
	}

	public static int getListViewHeight(ListView lv) {
		ListAdapter la = lv.getAdapter();
		if (null == la)
			return 0;
		int h = 0;
		int cnt = la.getCount();
		View item = null;
		for (int i = 0; i < cnt; i++) {
			item = la.getView(i, null, lv);
			item.measure(0, 0);
			h += item.getMeasuredHeight();
		}

		return h + (lv.getDividerHeight() * (cnt - 1));
	}

	// 获取联系人
	public static List<Contact> getContact(Context ctx) {
		List<Contact> contactList = new ArrayList<Contact>();

		// 获得所有的联系人
		Cursor contacts = ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
				ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");

		// 循环遍历
		if (contacts.moveToFirst()) {

			int idColumn = contacts.getColumnIndex(ContactsContract.Contacts._ID);
			int displayNameColumn = contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

			do {

				Contact contact = new Contact();

				// 获得联系人的ID号
				String contactId = contacts.getString(idColumn);
				// 获得联系人姓名
				String disPlayName = contacts.getString(displayNameColumn);
				Log.i("名称", disPlayName);
				contact.setName(disPlayName);

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

							if ("1".equals(phoneType))
								contact.setPhone(phoneNumber);
							else if ("2".equals(phoneType))
								contact.setMobile(phoneNumber);

						} while (phones.moveToNext());
					}
				} else {
					Log.i("电话|类型", "无电话号码");
				}

				contactList.add(contact);

			} while (contacts.moveToNext());

		}

		return contactList;
	}

	public static class Contact {
		private String name;
		private String phone;
		private String mobile;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

	}

	// 这是模拟post请求
	public static Result post(String url, Map<String, String> headers, Map<String, String> params, String encoding) throws ClientProtocolException, IOException {

		DefaultHttpClient client = new DefaultHttpClient();

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (String temp : params.keySet()) {
			list.add(new BasicNameValuePair(temp, params.get(temp)));
		}

		HttpPost post = new HttpPost(url);

		if (null != headers)
			post.setHeaders(assemblyHeader(headers));

		post.setEntity(new UrlEncodedFormEntity(list, encoding));

		return request(client, post);
	}

	private static Result request(AbstractHttpClient client, HttpUriRequest request) throws ClientProtocolException, IOException {
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();

		Result result = new Result();
		result.setStatusCode(response.getStatusLine().getStatusCode());
		result.setHeaders(response.getAllHeaders());
		result.setCookie(assemblyCookie(client.getCookieStore().getCookies()));
		result.setHttpEntity(entity);

		return result;
	}

	// 这是组装cookie
	public static String assemblyCookie(List<Cookie> cookies) {
		StringBuffer sbu = new StringBuffer();
		for (Cookie cookie : cookies) {
			sbu.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
		}
		if (sbu.length() > 0)
			sbu.deleteCharAt(sbu.length() - 1);
		return sbu.toString();
	}

	// 这是组装头部
	public static Header[] assemblyHeader(Map<String, String> headers) {
		Header[] allHeader = new BasicHeader[headers.size()];
		int i = 0;
		for (String str : headers.keySet()) {
			allHeader[i] = new BasicHeader(str, headers.get(str));
			i++;
		}
		return allHeader;
	}

	public static class Result {

		private int statusCode;
		private HashMap<String, Header> headerAll;
		private HttpEntity httpEntity;
		private String cookie;

		public String getCookie() {
			return cookie;
		}

		public void setCookie(String cookie) {
			this.cookie = cookie;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}

		public HashMap<String, Header> getHeaders() {
			return headerAll;
		}

		public void setHeaders(Header[] headers) {
			headerAll = new HashMap<String, Header>();
			for (Header header : headers) {
				headerAll.put(header.getName(), header);
			}
		}

		public HttpEntity getHttpEntity() {
			return httpEntity;
		}

		public void setHttpEntity(HttpEntity httpEntity) {
			this.httpEntity = httpEntity;
		}

		public String httpEntityContent() {

			try {
				return EntityUtils.toString(httpEntity, ENCODING);
			} catch (Throwable e) {
				e.printStackTrace();
			}

			return null;
		}
	}

	public static void closeInputStream(InputStream is) {
		if (is == null)
			return;
		try {
			is.close();
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
		}
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

		return !"".equals(Proxy.getDefaultHost());
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

	public static void loadImageSync(Activity act, final String imageUrl, final ImageView imageView) {

		loadImageSync(act, imageUrl, imageView, true);// 走本地存储

	}

	public static void loadImageSync(Activity act, final String imageUrl, final ImageView imageView, final boolean local) {

		// 创建并执行异步任务
		new AsycTask<Activity>(act) {
			Bitmap bm;

			@Override
			public void doCall() {
				if (local)
					bm = SDCardUtil.getPicToSd(imageUrl);

				if (bm == null) {
					bm = loadImage(imageUrl);
					if (local)
						SDCardUtil.savePicToSd(bm, imageUrl);// 将图片存到SD卡
				}
			}

			@Override
			public void doResult() {
				if (bm != null)
					imageView.setImageBitmap(bm);

			}

		}.execute();

	}

	/**
	 * 从网络上读取图片
	 */
	private static Bitmap loadImage(String imageUrl) {
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

//		if (checkGPRS_WAP()) {
//			httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(Proxy.getDefaultHost(), Proxy.getDefaultPort(), "http"));
//		}
		return new DefaultHttpClient(httpParams);

	}

}