package com.readnovel.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * 
 * 手机工具类
 * 
 * @author li.li
 *
 */
public class PhoneUtils {

	/**
	 * 获取手机型号         
	 * @return
	 */
	public static String getPhoneModel() {
		try {
			return URLEncoder.encode(android.os.Build.MODEL, "utf-8");
		} catch (UnsupportedEncodingException e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 获取手机机器码
	 * @param act
	 * @return
	 */
	public static String getPhoneImei(Context ctx) {
		TelephonyManager telMan = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		return telMan.getDeviceId();
		//		return "2012110712312312iu9fgdfdffg";
	}

	/**
	 * 获取屏幕大小
	 * @param act
	 * @return
	 */
	public static int[] getScreenPix(Activity act) {
		DisplayMetrics dm = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int[] pixs = { dm.widthPixels, dm.heightPixels };

		return pixs;
	}

	/**
	 * 系统应用版本号
	 * @return
	 */
	public static int getVersionCode() {
		int version = 0;
		try {
			version = android.os.Build.VERSION.SDK_INT;
		} catch (NumberFormatException e) {
			LogUtils.error(e.getMessage(), e);
		}
		return version;
	}

	/**

	  * 添加快捷方式到桌面 要点：  

	  * 1.给Intent指定action="com.android.launcher.INSTALL_SHORTCUT"

	  * 2.给定义为Intent.EXTRA_SHORTCUT_INENT的Intent设置与安装时一致的action(必须要有)  

	  * 3.添加权限:com.android.launcher.permission.INSTALL_SHORTCUT

	  */

	public static void addShortcutToDesktop(Activity act, int iconResourceId, int appNameResourceId) {

		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

		// 不允许重建

		shortcut.putExtra("duplicate", false);

		// 设置名字

		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, act.getString(appNameResourceId));

		// 设置图标
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(act, iconResourceId));

		// 设置意图和快捷方式关联程序

		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(act, act.getClass()).setAction(Intent.ACTION_MAIN));

		// 发送广播

		act.sendBroadcast(shortcut);

	}

	/**
	 * 解析出短信
	 * @param intent 从onReceive方法的参数
	 * @return
	 */
	public static final SmsMessage[] getMessagesFromIntent(Intent intent) {

		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");

		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++)

		{

			pduObjs[i] = (byte[]) messages[i];

		}

		byte[][] pdus = new byte[pduObjs.length][];

		int pduCount = pdus.length;

		SmsMessage[] msgs = new SmsMessage[pduCount];

		for (int i = 0; i < pduCount; i++)

		{

			pdus[i] = pduObjs[i];

			msgs[i] = SmsMessage.createFromPdu(pdus[i]);

		}

		return msgs;

	}

	//得到当前运行的Activity名
	public static String getCurActivity(Activity act) {
		ActivityManager manager = (ActivityManager) act.getSystemService(Activity.ACTIVITY_SERVICE);
		RunningTaskInfo info = manager.getRunningTasks(1).get(0);
		String shortClassName = info.topActivity.getShortClassName(); //类名 

		return shortClassName;
	}

	// 判断是否是飞行模式
	public static boolean getAirplaneMode(Context context) {
		int isAirplaneMode = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
		return (isAirplaneMode == 1) ? true : false;
	}

	/**
	 * 得到包信息
	 */
	public static PackageInfo getPackageInfo(Context ctx, String packageName) {
		PackageManager manager = ctx.getPackageManager();
		List<PackageInfo> pkgList = manager.getInstalledPackages(0);

		for (int i = 0; i < pkgList.size(); i++) {
			PackageInfo pI = pkgList.get(i);

			if (pI.packageName.equalsIgnoreCase(packageName))
				return pI;
		}

		return null;
	}
}
