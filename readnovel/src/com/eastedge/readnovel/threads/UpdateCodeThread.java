package com.eastedge.readnovel.threads;

import org.json.JSONObject;

import android.os.Handler;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.JsonToBean;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.http.HttpImpl;

/** 
 * @author ninglv 
 * @version Time：2012-3-19 下午4:57:47 
 */
public class UpdateCodeThread extends Thread {

	private String oldWord, newWord;
	private Handler handler;

	private static final String TAG = "UpdateCode";

	public UpdateCodeThread(String oldWord, String newWord, Handler handler) {
		super();
		this.oldWord = oldWord;
		this.newWord = newWord;
		this.handler = handler;
	}

	@Override
	public void run() {

		User user = BookApp.getUser();

		if (user != null) {
			JSONObject json = HttpImpl.updatePass(user.getUid(), oldWord, newWord, user.getToken());
			if (json == null) {
				return;
			}

			User u = JsonToBean.JsonToUser(json);
			if (u == null) {
				return;
			}

			String code = u.getCode();

			if (code.equals("1")) {
				handler.sendEmptyMessage(1);
			} else if (code.equals("2")) {
				handler.sendEmptyMessage(2);
			} else if (code.equals("3")) {
				handler.sendEmptyMessage(3);
			} else if (code.equals("4")) {
				handler.sendEmptyMessage(4);
			}
			return;
		} else {
			return;
		}

	}

}
