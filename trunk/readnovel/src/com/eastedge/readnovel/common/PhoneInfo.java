package com.eastedge.readnovel.common;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * <uses-permission android:name="READ_PHONE_STATE" /> 
 * @author ninglv 
 * @version Time：2012-4-20 上午10:31:20 
 */
public class PhoneInfo {

	private Activity activity;
	public static final String TAG = "PhoneInfo";

	public PhoneInfo(Activity activity) {
		super();
		this.activity = activity;
	}

	//获取屏幕大小
	public String getScreenPix() {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels + "*" + dm.heightPixels;
	}

	//获取手机型号          
	public String getModel() {
		return android.os.Build.MODEL;
	}

	//获取手机机器码
	public String getImei() {
		TelephonyManager telMan = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		return telMan.getDeviceId();
	}
}
