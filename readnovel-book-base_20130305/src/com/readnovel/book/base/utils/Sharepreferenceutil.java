package com.readnovel.book.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Sharepreferenceutil {
	SharedPreferences sp;
	SharedPreferences.Editor editor;
	int fontsize;
	int fontcolor;
	int a, b;
	int bg;

	public Sharepreferenceutil(Context context) {
		sp = context.getSharedPreferences("danbensp", Context.MODE_PRIVATE);

	}
	
	public void setpay(Boolean b) {
		Editor editor = sp.edit();
		editor.putBoolean("ispay", b);
		editor.commit();
	}

	// 获取电量值
	public Boolean getpay() {
		return sp.getBoolean("ispay", false);
	}
}
