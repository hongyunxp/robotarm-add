package com.eastedge.readnovel.threads;

import android.content.Context;
import android.os.Handler;

import com.xs.cn.http.HttpImpl;

/** 
 * @author ninglv 
 * @version Time：2012-5-10 下午12:34:02 
 */
public class UpdateInfoThread extends Thread {
	private Context context;
	private String uid, token, email, phone;
	private Handler handler;
	public String code;
	public UpdateInfoThread(Context context, Handler handler, String uid, String token,
			String email, String phone) {
		super();
		this.context = context;
		this.handler = handler;
		this.uid = uid;
		this.token = token;
		this.email = email;
		this.phone = phone;
	}
	@Override
	public void run() {
		super.run();
		
		code = HttpImpl.updateInfo(uid, token, phone, email);
		if(code == null){
			handler.sendEmptyMessage(123);
			return;
		}
		if("1".equals(code)){
			handler.sendEmptyMessage(1);
		}
		if("2".equals(code)){
			handler.sendEmptyMessage(2);
		}
	}
	
	
}
