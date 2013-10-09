package com.eastedge.readnovel.threads;

import java.util.ArrayList;

import com.xs.cn.http.HttpImpl;

import android.content.Context;
import android.os.Handler;

public class HottahThread extends Thread
{
	public ArrayList<String> list;
	public Handler mhandler;
	public Context ctx;

	public HottahThread(Context ctx, Handler mhandler)
	{
		this.mhandler = mhandler;
		this.ctx = ctx;
	}

	public void run()
	{
		list = HttpImpl.taglist();
		mhandler.sendEmptyMessage(1);
		if (list == null)
		{
			mhandler.sendEmptyMessage(2);
			return;
		}

	}
}
