package com.eastedge.readnovel.threads;

import com.eastedge.readnovel.beans.Subed_chapters_info;
import com.xs.cn.http.HttpImpl;

import android.content.Context;
import android.os.Handler;

public class SubedchaptersinfoThread extends Thread
{
	private Handler mhandler;
	private Context ctx;
	private String aid, uid, token;
	public Subed_chapters_info scif;

	
	public SubedchaptersinfoThread(Context ctx, Handler mhandler, String aid,
	        String uid, String token)
	{
		this.mhandler = mhandler;
		this.ctx = ctx;
		this.aid = aid;
		this.uid = uid;
		this.token = token;
	}

	private boolean isRunning = true ;
	public void stopRun(){
		isRunning = false ;
	}
	
	public void run()
	{
		scif = HttpImpl.Subed_chapters_info(aid, uid, token);
		if (scif != null)
		{
			if(isRunning)
			mhandler.sendEmptyMessage(1);
		}
	}
}
