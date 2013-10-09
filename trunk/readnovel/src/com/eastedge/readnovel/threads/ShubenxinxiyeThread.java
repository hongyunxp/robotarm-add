package com.eastedge.readnovel.threads;

import com.eastedge.readnovel.beans.Shubenxinxiye;
import com.xs.cn.http.HttpImpl;

import android.content.Context;
import android.os.Handler;

public class ShubenxinxiyeThread extends Thread
{
	public Handler handler;
	public Context ctx;
	public String id;
	public Shubenxinxiye sbxxy = null;

	public ShubenxinxiyeThread(Context ctx, Handler handler, String id)
	{
		this.handler = handler;
		this.ctx = ctx;
		this.id = id;
	}

	public void run()
	{
		sbxxy = HttpImpl.Shubenxinxiye(id);
		if (sbxxy != null)
		{
			handler.sendEmptyMessage(1);
		}

	}
}
