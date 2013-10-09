package com.eastedge.readnovel.threads;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.eastedge.readnovel.beans.NewBook;
import com.eastedge.readnovel.common.Util;
import com.xs.cn.http.HttpImpl;

public class PaihangDetailThread extends Thread {
	private Context context;
	private Handler handler;
	private String id;
	public ArrayList<NewBook> arrayList;
	
	public PaihangDetailThread(Context context, Handler handler, String id) {
		super();
		this.context = context;
		this.handler = handler;
		this.id = id;
	}


	@Override
	public void run() {
		super.run();
		arrayList = HttpImpl.paihangItemList(id);
		if(arrayList == null){
			return;
		}
		handler.sendEmptyMessage(1);
	}
	
}
