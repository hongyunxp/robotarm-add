package com.readnovel.book.base;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.readnovel.book.base.common.Constants;
import com.readnovel.book.base.task.CheckVipTask;
import com.readnovel.book.base.utils.LogUtils;
import com.readnovel.book.base.utils.NetUtils;
import com.readnovel.book.base.utils.NetUtils.NetType;

public class PayCheckService extends Service {
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
	}
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		try{
			if (intent == null)
				return;
			Bundle bundle = intent.getExtras();
			final int chapterId = bundle.getInt("chapterId");
			LogUtils.info("启动PayCheckService服务");
			NetType netType = NetUtils.checkNet(this);
			if (NetType.TYPE_NONE.equals(netType))
				return;//无网络环境下直接返回
			CheckVipTask checkVipTask = new CheckVipTask(this,Constants.yanzhengjiange_onehour);
			checkVipTask.execute(chapterId);
		}catch(Throwable e){
			LogUtils.error(e.getMessage(), e);
		}
	}

}
