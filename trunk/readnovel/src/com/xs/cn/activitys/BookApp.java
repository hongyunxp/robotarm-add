package com.xs.cn.activitys;

import android.app.Activity;
import android.content.DialogInterface;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.HCData;
import com.eastedge.readnovel.common.LocalStore;
import com.readnovel.base.common.CommonApp;
import com.readnovel.base.util.ViewUtils;

/**
 * 代表当前应用
 * @author li.li
 *
 * May 20, 2013
 */
public class BookApp extends CommonApp {
	public static volatile boolean isInit = false;//程序初始化标识

	private static volatile User user;//登陆用户状态保存，和应用的生命周期相同，断网或其它情况不丢失

	public static User getUser() {
		if (user == null)//内容用户信息为空时从本地持久化数据中取
			user = LocalStore.getCurLoginUser(getInstance());

		return user;
	}

	public static void setUser(User user) {
		if (user != null)//更新本地持久化用户信息
			LocalStore.setCurLoginUser(getInstance(), user);

		BookApp.user = user;
	}

	/**
	 * 清空用户登陆状态
	 */
	public static void cleanUser() {
		LocalStore.setCurLoginUser(getInstance(), null);
		BookApp.user = null;

	}

	public static void exitApp(Activity act) {
		ViewUtils.confirm(act, "温馨提示", "您确定退出小说阅读网？", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				HCData.clearAll();
				CloseActivity.close();
			}

		}, null);
	}

}
