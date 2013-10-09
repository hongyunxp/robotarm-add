package com.eastedge.readnovel.threads;

import com.eastedge.readnovel.beans.Banbenxinxi;
import com.xs.cn.http.HttpImpl;

import android.content.Context;
import android.os.Handler;

public class BanbenxinThread extends Thread
{
	private Handler handler;
	private Context ctx;
	public Banbenxinxi bbxx;

	public BanbenxinThread(Context ctx, Handler handler)
	{
		this.handler = handler;
		this.ctx = ctx;
	}

	public void run()
	{
		bbxx = HttpImpl.banbenxinxi();
		if (bbxx == null)
		{
			handler.sendEmptyMessage(2);
		}
		else
		{
			handler.sendEmptyMessage(1);
		}
	}
}
