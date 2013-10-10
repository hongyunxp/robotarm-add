package com.readnovel.book.base;

import android.app.Application;

import com.readnovel.book.base.common.CommonApp;
import com.readnovel.book.base.utils.LogUtils;

public class BookApp extends CommonApp {
	private static BookApp instance = new BookApp();

	public static BookApp getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtils.info("应用程序启动");
	}
}
