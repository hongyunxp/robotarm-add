package com.xs.cn.http;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.Vector;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.db.DBAdapter;
import com.eastedge.readnovel.threads.CheckUpdateBookThread;
import com.readnovel.base.util.DateUtils;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.LoadingActivity;

/**
 * 章节推送
 */
public class UpdateService extends Service {
	private AlarmManager alarmManager;
	private PendingIntent alarmSender;
	private Timer timer;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				doUp();
				break;
			case 3:
				NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				//状态栏提醒内容
				Notification notification = new Notification(R.drawable.icon, "小说阅读网提醒", System.currentTimeMillis());
				//通知栏提醒内容
				notification.setLatestEventInfo(getApplicationContext(), "小说阅读网提醒", "您正在阅读的作品有" + msg.arg1 + "部已有更新，请随时阅读！",
						PendingIntent.getActivity(UpdateService.this, 0, new Intent(UpdateService.this, LoadingActivity.class), 0));
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification.defaults |= Notification.DEFAULT_SOUND;
				manager.notify(1, notification);
				break;
			}
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtils.info("UpdateService启动了 onCreate");

		//执行的时间
		Date upTime = getUpBookTime();
		//		Calendar cal = Calendar.getInstance();
		//		cal.add(Calendar.SECOND, 30);
		//		Date upTime = cal.getTime();
		LogUtils.info("章节更新时间|" + DateUtils.format(upTime));

		timer = DateUtils.schedule(new Runnable() {

			@Override
			public void run() {
				DBAdapter dbAdapter = new DBAdapter(UpdateService.this);
				dbAdapter.open();
				Vector<BFBook> bflist = dbAdapter.queryMyBFData(LocalStore.getLastUid(UpdateService.this));//书架的书
				Vector<BFBook> viplist = dbAdapter.queryAllVipData(LocalStore.getLastUid(UpdateService.this));//VIP书架的书

				//书架有书时执行
				if (bflist != null && !bflist.isEmpty() || viplist != null && !viplist.isEmpty())
					doUp();//执行任务

			}
		}, upTime);

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		LogUtils.info("UpdateService启动了 onStart");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtils.info("UpdateService启动了 onDestroy");
		if (timer != null) {
			LogUtils.info("章节更新时间timer销毁");
			timer.cancel();
		}
	}

	/**
	 * 执行更新提示
	 */
	private void doUp() {
		new Thread() {
			public void run() {
				LogUtils.info("执行章节更新提示线程");
				CheckUpdateBookThread ck = new CheckUpdateBookThread(UpdateService.this);
				ck.run();
				if (ck.hasup) {
					Message msg = new Message();
					msg.arg1 = ck.count;
					msg.what = 3;
					handler.sendMessage(msg);
				}
			};
		}.start();

	};

	/**
	 * 得到需更新的时间
	 * @return
	 */
	private Date getUpBookTime() {
		Date upBookStartTime = getUpBookSTime();
		Date upBookEndTime = getUpBookETime();

		Calendar cal = Calendar.getInstance();
		//在时间区间内随机一个时间长度
		int randomTime = new Random().nextInt((int) (upBookEndTime.getTime() - upBookStartTime.getTime()));
		cal.setTime(upBookStartTime);
		cal.add(Calendar.MILLISECOND, randomTime);

		return cal.getTime();
	}

	/**
	 * 更新章节开始时间9点
	 * @return
	 */
	private Date getUpBookSTime() {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.HOUR_OF_DAY, 9);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal.getTime();
	}

	/**
	 * 更新书结束时间21点
	 * @return
	 */
	private Date getUpBookETime() {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.HOUR_OF_DAY, 21);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal.getTime();
	}

}
