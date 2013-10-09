package com.eastedge.readnovel.threads;

import com.eastedge.readnovel.beans.Shuping_huifu;
import com.xs.cn.http.HttpImpl;

import android.content.Context;
import android.os.Handler;

public class ShupinghuifuThread extends Thread
{
	public Handler handler;
	public Context ctx;
	public String id;
	public int tid;
	public int page;
	public Shuping_huifu sphf;

	public ShupinghuifuThread(Context ctx, Handler handler, String id, int tid,
	        int page)
	{
		this.handler = handler;
		this.ctx = ctx;
		this.id = id;
		this.tid = tid;
		this.page = page;
	}

	public void run()
	{
		sphf = HttpImpl.Shuping_huifu(id, tid, page);
		if (sphf == null)
		{
			return;
		}
		if (page == 1)
		{
			handler.sendEmptyMessage(1);
		}
		else
		{
			handler.sendEmptyMessage(2);
		}
	}
}
