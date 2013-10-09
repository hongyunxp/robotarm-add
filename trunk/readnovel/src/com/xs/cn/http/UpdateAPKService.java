package com.xs.cn.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.readnovel.base.util.LogUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.FinishActivity;

public class UpdateAPKService extends Service {

	// 标题
	//	private int titleId = 0;
	// 文件存储
	private File updateDir = null;
	private File updateFile = null;
	private String url = null;
	// 通知栏
	private NotificationManager updateNotificationManager = null;
	private Notification updateNotification = null;
	private PendingIntent updatePendingIntent;
	// 通知栏跳转Intent
	private Intent updateIntent = null;

	private final static int DOWNLOAD_COMPLETE = 0;
	private final static int DOWNLOAD_FAIL = 1;

	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		// 获取传值
		url = intent.getStringExtra("url");
		LogUtils.info("启动下载   :" + url);
		// 创建文件
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
			updateDir = new File(Environment.getExternalStorageDirectory(), "readnovel");
			updateFile = new File(updateDir, "readnovel-up.apk");
			if (updateFile.exists()) {
				updateFile.delete();
			}
		}

		updateIntent = new Intent(this, UpdateAPKService.class);

		updateNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		//状态栏提醒内容
		this.updateNotification = new Notification(R.drawable.icon, "小说阅读网提醒", System.currentTimeMillis());
		updatePendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, FinishActivity.class), 0);
		//状态栏提醒内容
		updateNotification.contentView = new RemoteViews(getApplication().getPackageName(), R.layout.progress);
		updateNotification.contentView.setProgressBar(R.id.progressBar1, 100, 0, false);
		updateNotification.contentView.setTextViewText(R.id.textView1, "进度" + 0 + "%");

		updateNotification.contentIntent = updatePendingIntent;
		//		updateNotification.setLatestEventInfo(getApplicationContext(), "小说阅读网" , "0%", updatePendingIntent);
		//		updateNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		//		updateNotification.defaults |= Notification.DEFAULT_SOUND;

		// 发出通知
		updateNotificationManager.notify(0, updateNotification);
		// 开启一个新的线程下载
		new Thread(new UpdateRunnable()).start();
	}

	private class UpdateRunnable implements Runnable {
		Message message = updateHandler.obtainMessage();

		public void run() {
			message.what = DOWNLOAD_COMPLETE;
			try {
				if (!updateDir.exists()) {
					updateDir.mkdirs();
				}
				if (!updateFile.exists()) {
					updateFile.createNewFile();
				}
				long downloadSize = downloadUpdateFile(url, updateFile);
				if (downloadSize > 0) {
					// 下载成功
					updateHandler.sendMessage(message);
				}
			} catch (Exception e) {
				LogUtils.error(e.getMessage(), e);
				message.what = DOWNLOAD_FAIL;
				// 下载失败
				updateHandler.sendMessage(message);
			}
		}
	}

	public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception {
		int downloadCount = 0;
		int currentSize = 0;
		long totalSize = 0;
		int updateTotalSize = 0;
		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(downloadUrl);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
			if (currentSize > 0) {
				httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
			}
			httpConnection.setConnectTimeout(20000);
			httpConnection.setReadTimeout(120000);
			updateTotalSize = httpConnection.getContentLength();
			if (httpConnection.getResponseCode() == 404) {
				throw new Exception("fail!");
			}
			is = httpConnection.getInputStream();
			fos = new FileOutputStream(saveFile, false);
			byte buffer[] = new byte[4096];
			int readsize = 0;
			while ((readsize = is.read(buffer)) > 0) {
				fos.write(buffer, 0, readsize);
				totalSize += readsize; // 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
				if ((downloadCount == 0) || (int) (totalSize * 100 / updateTotalSize) - 10 > downloadCount) {
					downloadCount += 10;
					updateNotification.contentView.setProgressBar(R.id.progressBar1, 100, (int) totalSize * 100 / updateTotalSize, false);
					updateNotification.contentView.setTextViewText(R.id.textView1, "进度" + (int) totalSize * 100 / updateTotalSize + "%");
					updateNotification.contentIntent = updatePendingIntent;

					//					updateNotification.setLatestEventInfo(UpdateAPKService.this,"正在下载", (int) totalSize * 100 / updateTotalSize+ "%", updatePendingIntent);
					updateNotificationManager.notify(0, updateNotification);
					//					Thread.sleep(500);
				}
			}
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			if (is != null) {
				is.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return totalSize;
	}

	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_COMPLETE:
				// 点击安装PendingIntent
				Uri uri = Uri.fromFile(updateFile);
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
				startActivity(installIntent);
				//				updatePendingIntent = PendingIntent.getActivity(UpdateAPKService.this, 0, installIntent, 0);
				//				updateNotification.defaults = Notification.DEFAULT_SOUND;
				//				// 铃声提醒
				//				updateNotification.setLatestEventInfo(UpdateAPKService.this,"小说阅读网", "下载完成,点击安装。", updatePendingIntent);
				//				updateNotificationManager.notify(1, updateNotification);
				//				// 停止服务
				updateNotificationManager.cancel(0);
				stopService(updateIntent);
				break;
			case DOWNLOAD_FAIL:
				// 下载失败
				updateNotification.setLatestEventInfo(UpdateAPKService.this, "小说阅读网", "下载失败", updatePendingIntent);
				updateNotificationManager.notify(0, updateNotification);
				stopService(updateIntent);
				break;
			default:
				stopService(updateIntent);
				break;
			}
		}
	};
}
