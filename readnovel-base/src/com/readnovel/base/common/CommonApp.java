package com.readnovel.base.common;

import android.app.Application;

public class CommonApp extends Application {
	private static Application instance;

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;

		//启动全局异常处理器
		CrashHandler.getInstance().init(getApplicationContext());

	}

	public static Application getInstance() {
		return instance;
	}

}
