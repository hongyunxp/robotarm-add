package com.readnovel.book.base.common;

import android.app.Application;

public class CommonApp extends Application{
	private static Application instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance=this;
	}
	
	public static Application getInstance(){
		return instance;
	}

}
