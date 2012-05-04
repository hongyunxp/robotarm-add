package robot.arm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
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
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
	private static final int TIMEOUT = 5000;
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
			if (item.getVisibility() != View.GONE)
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

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);

		DefaultHttpClient client = new DefaultHttpClient(httpParams);

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

	// 这是模拟get请求
	public static Result get(String url, Map<String, String> headers, Map<String, String> params, String encoding) throws ClientProtocolException, IOException {

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);

		DefaultHttpClient client = new DefaultHttpClient(httpParams);

		url = url + (null == params ? "" : assemblyParameter(params));

		HttpGet get = new HttpGet(url);

		if (null != headers)
			get.setHeaders(assemblyHeader(headers));

		return request(client, get);
	}

	// 这是组装参数
	private static String assemblyParameter(Map<String, String> parameters) {
		String para = "?";
		for (String str : parameters.keySet()) {
			para += str + "=" + parameters.get(str) + "&";
		}
		return para.substring(0, para.length() - 1);
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

	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			Log.e(TAG, e.toString());

		}
		return version;
	}

	public static int getAppVersion() {
		int appVersion = 0;
		PackageManager manager = RobotArmApp.getApp().getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(RobotArmApp.getApp().getPackageName(), 0);
			appVersion = info.versionCode; // 版本号
		} catch (Throwable e) {
			Log.e(TAG, e.toString());
		}

		return appVersion;
	}

	public static String getPackageName() {
		return RobotArmApp.getApp().getPackageName();
	}

}