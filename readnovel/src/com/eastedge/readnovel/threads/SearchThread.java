package com.eastedge.readnovel.threads;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.os.Handler;

import com.eastedge.readnovel.beans.SearchResult;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.http.HttpImpl;

public class SearchThread extends Thread {
	public Handler handler;
	public Context ctx;
	public String word;
	public SearchResult list;
	public int i;

	public SearchThread(Context ctx, Handler handler, String word, int i) {
		this.handler = handler;
		this.ctx = ctx;
		// this.word = word;
		try {
			this.word = URLEncoder.encode(word, "gbk");
		} catch (UnsupportedEncodingException e) {
			LogUtils.error(e.getMessage(), e);
		}
		this.i = i;

	}

	public void run() {
		list = HttpImpl.seachList(word, i);
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
