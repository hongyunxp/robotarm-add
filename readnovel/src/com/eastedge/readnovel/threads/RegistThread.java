package com.eastedge.readnovel.threads;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.Constants.LoginType;
import com.eastedge.readnovel.common.JsonToBean;
import com.eastedge.readnovel.utils.CommonUtils;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.http.HttpImpl;

/** 
 * @author ninglv 
 * @version Time：2012-3-19 下午1:47:01 
 */
public class RegistThread extends Thread {

	private String username, passwdhash;
	private Handler handler;
	private Context context;

	public RegistThread(Context context, String username, String passwdhash, Handler handler) {
		super();
		this.username = username;
		this.passwdhash = passwdhash;
		this.handler = handler;
		this.context = context;
	}

	@Override
	public void run() {
		JSONObject json = HttpImpl.regist(username, passwdhash);

		if (json == null) {
			handler.sendEmptyMessage(0);
			return;
		}

		User user = JsonToBean.JsonToUser(json);

		BookApp.setUser(user);

		String code = user.getCode();

		if (code.equals("1")) {
			//记录登陆状态
			CommonUtils.saveLoginStatus(context, user.getUid(), LoginType.def);

			handler.sendEmptyMessage(1);
		} else if (code.equals("2")) {
			handler.sendEmptyMessage(2);
		} else if (code.equals("3")) {
			handler.sendEmptyMessage(3);
		} else if (code.equals("4")) {
			handler.sendEmptyMessage(4);
		}
		return;
	}

}
