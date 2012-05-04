package robot.arm.provider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import robot.arm.R;
import robot.arm.common.RobotArmApp;
import robot.arm.utils.BaseUtils;
import robot.arm.utils.BaseUtils.Result;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

/**
 * 更新程序
 */
public class AppUpdateProvider {
	private static final String TAG = AppUpdateProvider.class.getName();
	
	private static final String UPPDATE_URL = RobotArmApp.getApp().getString(R.string.upload_url);
	private static final AppUpdateProvider instance = new AppUpdateProvider();
	private static final String UPDATE_SAVENAME = "look-beautiful.apk";
	private static final File DOWNLOAD_LOCATION = new File(Environment.getExternalStorageDirectory(), UPDATE_SAVENAME);

	private Handler mHandler = new Handler();
	private final DecimalFormat df = new DecimalFormat("0.##");
	private ProgressDialog pBar;
	private VersionInfo versionInfo;

	private AppUpdateProvider() {
	}

	public static AppUpdateProvider getInstance() {
		return instance;
	}

	public boolean start(final Context context) {
		try {
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+UPPDATE_URL);
			Result result = BaseUtils.get(UPPDATE_URL, null, null, HTTP.UTF_8);
			String content = result.httpEntityContent();
			JSONObject jo = new JSONObject(content);

			versionInfo = new VersionInfo(jo.getInt("versionCode"), jo.getString("versionName"), jo.getInt("type"), jo.getString("desc"), jo.getString("url"));
			if (BaseUtils.getAppVersion() < versionInfo.versionCode) {

				updateNewVersion(context, versionInfo.url);

				return true;
			}

		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
		}

		return false;
	}

	/**
	 * 新版本处理
	 */
	public void updateNewVersion(final Context context, final String downloadURL) {
		StringBuffer sb = new StringBuffer();
		sb.append("发现新版本, 请更新.");
		dialog(context, "软件更新", sb.toString(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				pBar = new ProgressDialog(context);
				pBar.setTitle("正在下载");
				pBar.setMessage("请稍候...");
				pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				downFile(context, downloadURL);
			}
		});
	}

	/**
	 * 整理http请求客户端
	 */
	private HttpClient getHttpClient() throws Throwable {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		HttpConnectionParams.setSoTimeout(httpParams, 30000);
		return new DefaultHttpClient(httpParams);
	}

	/**
	 * 自定义弹出框
	 */
	private void dialog(Context context, String title, String message, OnClickListener pl) {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setTitle(title);
		tDialog.setMessage(message);
		tDialog.setPositiveButton("更新", pl);
		tDialog.show();
	}

	/**
	 * 下载应用程序包 并提醒安装
	 */
	private void downFile(final Context context, final String downloadURL) {
		pBar.show();
		new Thread() {
			public void run() {
				try {
					doDownFile(downloadURL, DOWNLOAD_LOCATION);
					updateApp(context);
				} catch (Throwable e) {
				}
			}
		}.start();
	}

	/**
	 * 更新应用程序
	 */
	private void updateApp(Context context) {
		pBar.cancel();
		File file = new File(Environment.getExternalStorageDirectory(), UPDATE_SAVENAME);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 下载新版本程序 从服务器下载软件包到指定路径
	 * Environment.getExternalStorageDirectory()+Config.UPDATE_SAVENAME
	 */
	private void doDownFile(final String downloadURL, final File file) throws Throwable {
		loadData(downloadURL, new ResponseCallback() {
			public void callback(HttpResponse response) throws Throwable {
				InputStream is = null;
				FileOutputStream fos = null;
				try {
					is = response.getEntity().getContent();
					fos = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
					int len;
					int total = 0;
					while ((len = is.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
						total += len;
						// 更新下载进度
						updateDownLoading(total);
					}
				} finally {
					fos.close();
					is.close();
				}
			}
		});
	}

	/**
	 * 从网络上读取数据
	 */
	private void loadData(String url, ResponseCallback responseCallBack) {
		HttpClient client = null;
		try {
			client = getHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			responseCallBack.callback(response);
		} catch (Throwable e) {
		} finally {
			if (client != null)
				client.getConnectionManager().shutdown();
		}
	}

	/**
	 * 更新下载进度
	 */
	private void updateDownLoading(int total) {
		final int tempTotal = total;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				pBar.setMessage(df.format(tempTotal * 1.0 / 1024) + "KB");
			}
		});
	}

	/**
	 * 网络强求响应回调
	 */
	private interface ResponseCallback {
		void callback(HttpResponse response) throws Throwable;
	}

	public static class VersionInfo {
		public int versionCode;
		public String versionName;
		public int type;
		public String desc;
		public String url;

		public VersionInfo(int versionCode, String versionName, int type, String desc, String url) {
			this.versionCode = versionCode;
			this.versionName = versionName;
			this.type = type;
			this.desc = desc;
			this.url = url;
		}
	}
}
