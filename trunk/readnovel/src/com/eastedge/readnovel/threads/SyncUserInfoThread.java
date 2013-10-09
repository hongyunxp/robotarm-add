package com.eastedge.readnovel.threads;

import org.json.JSONObject;

import android.os.Handler;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.JsonToBean;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.http.HttpImpl;

/** 
 * @author ninglv 
 * @version Time：2012-3-27 下午4:52:47 
 */
public class SyncUserInfoThread extends Thread {
	private Handler handler;
	private String uid, token;
	public static User user;

	public SyncUserInfoThread(Handler handler, String uid, String token) {
		super();
		this.handler = handler;
		this.uid = uid;
		this.token = token;
	}

	@Override
	public void run() {
		JSONObject json = HttpImpl.syncUserInfo(uid, token);

		if (json == null) {
			handler.sendEmptyMessage(4);
			return;
		}
		user = JsonToBean.JsonToUser(json);
		if (user.getCode() == "2") {
			handler.sendEmptyMessage(2);
			return;
		}
		user.setUid(uid);
		user.setToken(token);

		BookApp.setUser(user);
		handler.sendEmptyMessage(1);
	}

}
