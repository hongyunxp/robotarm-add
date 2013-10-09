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
 * 登陆线程
 * @author ninglv 
 * @version Time：2012-3-19 下午3:57:12 
 */
public class LoginThread extends Thread {
	private String name, passwd;

	private Handler handler;
	private Context context;

	public LoginThread(Context context, String name, String password, Handler handler) {
		super();
		this.context = context;
		this.name = name;
		this.passwd = password;
		this.handler = handler;
	}

	@Override
	public void run() {
		//登陆
		JSONObject json = HttpImpl.login(name, passwd, false);
		//解析用户信息
		User user = JsonToBean.JsonToUser(json);

		if (user == null) {
			handler.sendEmptyMessage(5);
			return;
		}

		//登陆成功
		if (user != null && User.LOGIN_SUCCESS.equals(user.getCode())) {
			//登录成功后记录登陆状态
			CommonUtils.saveLoginStatus(context, user.getUid(), LoginType.def);
			//设置登陆用户状态
			BookApp.setUser(user);

			handler.sendEmptyMessage(1);
		} else if ("2".equals(user.getCode())) {
			handler.sendEmptyMessage(2);
		} else if ("3".equals(user.getCode())) {
			handler.sendEmptyMessage(3);
		} else if ("4".equals(user.getCode())) {
			handler.sendEmptyMessage(4);
		}

		return;
	}
}
