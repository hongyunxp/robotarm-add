package com.eastedge.readnovel.threads;

import android.content.Context;
import android.os.Handler;

import com.eastedge.readnovel.beans.Shubenmulu;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.http.HttpImpl;

public class ShubenmuluThread extends Thread {
	public Handler handler;
	public Context ctx;
	public String id;
	public Shubenmulu sbml = null;
	public int page;
	public int times;

	public ShubenmuluThread(Context ctx, Handler handler, String id) {
		this.handler = handler;
		this.ctx = ctx;
		this.id = id;
	}

	private boolean isRunning = true ;
	public void stopRun(){
		isRunning = false ;
	}
	
	public void run() {
//		sbml = null;
		sbml = HttpImpl.ShubenmuluAll(id);
		if(sbml==null){
			if(times<5){
				times++;
				try {
					sleep(300);
				} catch (InterruptedException e) {
					LogUtils.error(e.getMessage(), e);
				}
				run();
				return;
			}
			handler.sendEmptyMessage(3);
			return;
		}
		
		if (sbml != null && isRunning) {
			handler.sendEmptyMessage(1);
		}
	}
}
