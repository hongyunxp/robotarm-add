package com.eastedge.readnovel.threads;

import java.util.ArrayList;

import com.eastedge.readnovel.beans.BookType;
import com.xs.cn.http.HttpImpl;

import android.content.Context;
import android.os.Handler;

public class FenleiThread extends Thread
{
	private Handler handler;
	private Context ctx;
	public ArrayList<BookType> list;

	public FenleiThread(Context ctx, Handler handler)
	{
		this.handler = handler;
		this.ctx = ctx;
	}

	public void run()
	{
		list = HttpImpl.fenleiList();
		if (list == null)
		{
			handler.sendEmptyMessage(2);
			return ;
		}
		handler.sendEmptyMessage(1);
	}
}
