package com.eastedge.readnovel.threads;

import android.app.Activity;
import android.content.Context;

import com.eastedge.readnovel.common.PhoneInfo;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.util.PhoneUtils;
import com.xs.cn.http.HttpImpl;

/** 
 * @author ninglv 
 * @version Time：2012-4-23 下午4:41:50 
 */
public class SendLogRegInfo extends Thread {
	private Context context;
	private String uid, token;
	private int flag;
	public static final String TAG = "SendLogRegInfo";

	public SendLogRegInfo(Context context, String uid, String token, int flag) {
		super();
		this.context = context;
		this.uid = uid;
		this.flag = flag;
		this.token = token;
	}

	@Override
	public void run() {
		super.run();
		PhoneInfo phone = new PhoneInfo((Activity) context);
		String channel = CommonUtils.getChannel((Activity) context);
		String imei = PhoneUtils.getPhoneImei(context);
		HttpImpl.sendPhoneInfoFeedBack(uid, token, phone.getScreenPix(), phone.getModel(), imei, channel, flag);

	}
}
