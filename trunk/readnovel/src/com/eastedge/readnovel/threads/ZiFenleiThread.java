package com.eastedge.readnovel.threads;

import java.io.IOException;

import com.eastedge.readnovel.beans.ZiFenleiList;
import com.eastedge.readnovel.common.Util;
import com.xs.cn.http.HttpImpl;

import android.content.Context;
import android.os.Handler;

public class ZiFenleiThread extends Thread {
	public Handler mhandler;
	public Context ctx;
	public ZiFenleiList list;
	public int i;
	public String SortId;

	public ZiFenleiThread(Context ctx, Handler mhandler, int i, String SortId) {
		this.mhandler = mhandler;
		this.ctx = ctx;
		this.i = i;
		this.SortId = SortId;
	}

	public void run() {
		list = HttpImpl.ZifenleiItemList(SortId, i);
		if (i == 1) {
			mhandler.sendEmptyMessage(1);
		} else {
			mhandler.sendEmptyMessage(2);
		}
	}
}
