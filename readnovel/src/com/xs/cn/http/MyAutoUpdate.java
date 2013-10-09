package com.xs.cn.http;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.KeyEvent;
import android.widget.Toast;

import com.eastedge.readnovel.common.CloseActivity;
import com.readnovel.base.R;
import com.readnovel.base.util.LogUtils;

public class MyAutoUpdate {

	private Activity activity;
	private int versionCode;
	private String strURL;
	private String content; //提示内容
	private int nowversionCode;
	private boolean toast;
	private boolean isforce;
	private int[] wrongversion;
	private boolean dLock;//锁

	public MyAutoUpdate(Activity activity, int nowversionCode, String url, String content, boolean isforce, int[] wrongversion) {
		this(activity, nowversionCode, url, true, content, isforce, wrongversion);
	}

	public MyAutoUpdate(Activity activity, int nowversionCode, String url, boolean toast, String content, boolean isforce, int[] wrongversion) {

		this.activity = activity;
		this.nowversionCode = nowversionCode;
		this.strURL = url;
		this.toast = toast;
		this.content = content;
		this.isforce = isforce;
		this.wrongversion = wrongversion;
		getCurrentVersion();
	}

	/**
	 * 检测版本
	 */
	public void check() {
		if (isNetworkAvailable(this.activity) == false) {
			// 网络不可用
			return;
		}
		if (isforce == true)
			forceshowUpdateDialog();
		else if (iswrongversion(wrongversion, versionCode))
			forceshowUpdateDialog();
		else {
			//最里面的层次  提示更新层
			if (nowversionCode > versionCode) {// Check version.
				showUpdateDialog();
			} else {
				if (toast)
					Toast.makeText(activity, "当前已是最新版本 不需要更新", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private boolean iswrongversion(int[] a, int b) {
		if (a != null)
			for (int i = 0; i < a.length; i++) {
				if (a[i] == b) {
					return true;
				}
			}
		return false;
	}

	/**
	 * 当到最新版本信息
	 */
	private void getCurrentVersion() {
		try {
			PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
			this.versionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			LogUtils.error(e.getMessage(), e);
		}
	}

	/**
	 * 检测网络
	 * @param ctx
	 * @return
	 */
	private static boolean isNetworkAvailable(Context ctx) {
		try {
			ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			return (info != null && info.isConnected());
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 显示升级信息
	 */
	private void showUpdateDialog() {
		new AlertDialog.Builder(this.activity).setTitle("发现新版本").setMessage(content).setPositiveButton("现在升级", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(activity, UpdateAPKService.class);
				intent.putExtra("url", strURL);
				activity.startService(intent);
			}
		}).setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();

	}

	private void forceshowUpdateDialog() {

		AlertDialog.Builder tDialog = new AlertDialog.Builder(activity);
		tDialog.setTitle("发现新版本");
		tDialog.setMessage(content);
		tDialog.setInverseBackgroundForced(false);
		tDialog.setCancelable(false);
		tDialog.setPositiveButton("现在升级", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(activity, UpdateAPKService.class);
				intent.putExtra("url", strURL);
				activity.startService(intent);

				final ProgressDialog pd = new ProgressDialog(activity);
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pd.setTitle("更新提示");
				pd.setMessage("正在下载中，请稍等.....");
				pd.setIndeterminate(false);
				pd.setInverseBackgroundForced(false);
				pd.setCancelable(false);

				pd.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialoginterface, int i, KeyEvent keyevent) {
						if (i == KeyEvent.KEYCODE_BACK) {
							//退出应用
							if (!dLock) {
								dLock = true;//加锁

								new AlertDialog.Builder(activity).setTitle("温馨提示").setMessage("您确定退出小说阅读网？")
										.setPositiveButton(activity.getString(R.string.ensure), new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												CloseActivity.close();
											}
										}).setNegativeButton(activity.getString(R.string.cacel), new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												dLock = false;//解锁
											}
										}).show();
							}

							return true;
						}
						return false;
					}

				});

				pd.show();

			}
		});
		tDialog.setNegativeButton("退出", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				CloseActivity.close();
			}

		});

		tDialog.show();

	}
}
