package com.eastedge.readnovel.threads;

import java.io.IOException;
import java.util.ArrayList;

import com.eastedge.readnovel.beans.FenleiList;
import com.eastedge.readnovel.beans.NewBook;
import com.eastedge.readnovel.common.Util;
import com.xs.cn.http.HttpImpl;

import android.content.Context;
import android.os.Handler;

public class FenleiItemThread extends Thread
{
	private Handler handler;
	private Context ctx;
	private String SortId;
	public FenleiList list;
	public int i;

	public FenleiItemThread(Context ctx, Handler handler, String SortId, int i)
	{
		this.handler = handler;
		this.ctx = ctx;
		this.SortId = SortId;
		this.i = i;
	}

	public void run()
	{
		list = HttpImpl.fenleiItemList(SortId, i);
		if (list == null)
		{
			handler.sendEmptyMessage(333);
			return;
		}

		if (i == 1)
		{
			handler.sendEmptyMessage(111);
		}
		else
		{
			handler.sendEmptyMessage(222);
		}
	}
}
