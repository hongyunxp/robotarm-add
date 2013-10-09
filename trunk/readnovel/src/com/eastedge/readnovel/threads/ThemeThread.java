package com.eastedge.readnovel.threads;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.eastedge.readnovel.beans.NewBook;
import com.eastedge.readnovel.beans.Theme;
import com.xs.cn.http.HttpImpl;

public class ThemeThread extends Thread {
	private Handler handler;
	private Context context;
	public Theme theme;
	private String aid;
	public Drawable img;
	public ArrayList<NewBook> al;
	public String topTitle;
	public String discountsorted;
	public Boolean isdis = false;

	public ThemeThread() {
	}

	private int page;

	public ThemeThread(Handler handler, Context context, String aid, int page) {
		super();
		this.handler = handler;
		this.context = context;
		this.aid = aid;
		this.page = page;
	}

	public ThemeThread(Handler handler, Context context, String aid, int page, String sorted, Boolean isdis) {
		super();
		this.handler = handler;
		this.context = context;
		this.aid = aid;
		this.page = page;
		this.discountsorted = sorted;
		this.isdis = isdis;
	}

	@Override
	public void run() {

		theme = HttpImpl.theme(aid, page, discountsorted);

		if (theme == null) {
			handler.sendEmptyMessage(44);
			return;
		}
		al = theme.getBookList();
		if (isdis) {
			isdis = false;
			handler.sendEmptyMessage(5);
		} else {
			if (page == 1) {
				handler.sendEmptyMessage(1);
			} else {
				handler.sendEmptyMessage(2);
			}
		}
	}
}
