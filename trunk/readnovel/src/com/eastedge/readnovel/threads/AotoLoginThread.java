package com.eastedge.readnovel.threads;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.JsonToBean;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.http.HttpImpl;

/** 
 * 自动登录
 */
public class AotoLoginThread extends Thread {

	private Context context;
	private Handler handler;

	//	private int time;

	public AotoLoginThread(Context context, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
	}

	@Override
	public void run() {
		try {
			/**
			 * 自动登陆使用用户数据数据同步接口
			 */
			//			JSONObject json = HttpImpl.login(name, pwd, true);
			//得到登陆uid
			String uid = LocalStore.getLastUid(context);

			//使用用户信息同步实现重登陆(得到用户信息)
			JSONObject json = HttpImpl.syncUserInfo(uid, CommonUtils.logInToken(uid));

			User user = JsonToBean.JsonToUser(json);

			//登陆成功
			if (user != null && User.LOGIN_SUCCESS.equals(user.getCode())) {
				user.setToken(CommonUtils.logInToken(user.getUid()));
				//登录成功后记录登陆状态
				CommonUtils.saveLoginStatus(context, user.getUid());
				//登录成功后缓存用户
				BookApp.setUser(user);
			}

		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			handler.sendEmptyMessage(9);//异常返回登陆完成
		}

	}

}
