package com.eastedge.readnovel.threads;

import android.content.Context;
import android.os.Handler;

import com.eastedge.readnovel.beans.NewBookList;
import com.xs.cn.http.HttpImpl;

public class NewBookThread extends Thread {
	public Handler handler;
	public Context ctx;
	public NewBookList list;
	public int i;
	public int type;

	public NewBookThread(Context ctx, Handler handler, int i, int type) {
		this.handler = handler;
		this.ctx = ctx;
		this.i = i;
		this.type = type;
	}

	public void run() {
		list = HttpImpl.newbook(i, type);

		if (list == null) {
			handler.sendEmptyMessage(333);
			return;
		}

		if (i == 1) {
			handler.sendEmptyMessage(111);
		} else {
			handler.sendEmptyMessage(222);
		}
	}
}
