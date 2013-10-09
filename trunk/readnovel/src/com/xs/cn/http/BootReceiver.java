package com.xs.cn.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.eastedge.readnovel.common.LocalStore;

public class BootReceiver extends BroadcastReceiver {
	private final String TAG = "BootReceiver" ;
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "BootReceiver complete") ;
		if(LocalStore.getIsSetPush(context)){
			Intent mService = new Intent(context, UpdateService.class);
			mService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(mService);
		}
	}
}
