package com.eastedge.readnovel.threads;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.common.PhoneInfo;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.PhoneUtils;
import com.xs.cn.http.HttpImpl;

/** 
 * @author ninglv 
 * @version Time：2012-4-23 上午11:37:49 
 */
public class SendInstallInfo extends Thread {

	public static final String TAG = "SendPhoneInfo";

	private Context context;

	public SendInstallInfo(Context context) {
		super();
		this.context = context;
	}

	public void run() {
		super.run();

		PhoneInfo phone = new PhoneInfo((Activity) context);
		//		String imei = phone.getImei();
		String imei = PhoneUtils.getPhoneImei(context);
		String model = phone.getModel();
		String pix = phone.getScreenPix();

		JSONObject obj = new JSONObject();
		try {
			obj.put("imei", imei);
			obj.put("screenpix", pix);
			obj.put("model", model);
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		LogUtils.info("手机信息为：" + obj.toString());
		String channel = CommonUtils.getChannel((Activity) context);

		boolean result = HttpImpl.sendPhoneInfoInstall(pix, model, imei, channel);

		if (result) {//发送成功
			LocalStore.setActivate(context, true);
		}

	}
}
