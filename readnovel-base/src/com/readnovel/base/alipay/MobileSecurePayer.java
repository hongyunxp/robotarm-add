/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.readnovel.base.alipay;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.alipay.android.app.IAlixPay;
import com.alipay.android.app.IRemoteServiceCallback;
import com.readnovel.base.util.PhoneUtils;

/**
 * 和安全支付服务通信，发送订单信息进行支付，接收支付宝返回信息
 * 
 */
public class MobileSecurePayer {
	private static String TAG = "MobileSecurePayer";

	private Integer lock = 0;
	private IAlixPay mAlixPay = null;
	private boolean mbPaying = false;

	private Context context = null;

	// 和安全支付服务建立连接
	private ServiceConnection mAlixPayConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			//
			// wake up the binder to continue.
			// 获得通信通道
			synchronized (lock) {
				mAlixPay = IAlixPay.Stub.asInterface(service);
				lock.notify();
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			mAlixPay = null;
		}
	};

	/**
	 * 向支付宝发送支付请求
	 * 
	 * @param strOrderInfo
	 *            订单信息
	 * @param callback
	 *            回调handler
	 * @param myWhat
	 *            回调信息
	 * @param activity
	 *            目标activity
	 * @return
	 */
	public boolean pay(final String strOrderInfo, final Handler callback, final int myWhat, final Context context) {
		if (mbPaying)
			return false;
		mbPaying = true;

		//
		this.context = context;

		// bind the service.
		// 绑定服务
		if (mAlixPay == null) {
			// 绑定安全支付服务需要获取上下文环境，
			// 如果绑定不成功使用mActivity.getApplicationContext().bindService
			// 解绑时同理

			//老绑定方式
			//			context.getApplicationContext().bindService(new Intent(IAlixPay.class.getName()), mAlixPayConnection, Context.BIND_AUTO_CREATE);

			PackageInfo packageInfo = PhoneUtils.getPackageInfo(context, "com.eg.android.AlipayGphone");

			if (packageInfo != null && packageInfo.versionCode >= 37) {//有钱包，并且可用
				Intent intent = new Intent("com.eg.android.AlipayGphone.IAlixPay");//钱包
				context.getApplicationContext().bindService(intent, mAlixPayConnection, Context.BIND_AUTO_CREATE);
			} else {
				Intent intent = new Intent("com.alipay.android.app.IAlixPay"); //快捷插件
				context.getApplicationContext().bindService(intent, mAlixPayConnection, Context.BIND_AUTO_CREATE);
			}

		}
		// else ok.

		// 实例一个线程来进行支付
		new Thread(new Runnable() {
			public void run() {
				try {
					// wait for the service bind operation to completely
					// finished.
					// Note: this is important,otherwise the next mAlixPay.Pay()
					// will fail.
					// 等待安全支付服务绑定操作结束
					// 注意：这里很重要，否则mAlixPay.Pay()方法会失败
					synchronized (lock) {
						if (mAlixPay == null)
							lock.wait();
					}

					// register a Callback for the service.
					// 为安全支付服务注册一个回调
					mAlixPay.registerCallback(mCallback);

					// call the MobileSecurePay service.
					// 调用安全支付服务的pay方法
					String strRet = mAlixPay.Pay(strOrderInfo);
					BaseHelper.log(TAG, "After Pay: " + strRet);

					// set the flag to indicate that we have finished.
					// unregister the Callback, and unbind the service.
					// 将mbPaying置为false，表示支付结束
					// 移除回调的注册，解绑安全支付服务
					mbPaying = false;
					mAlixPay.unregisterCallback(mCallback);
					context.getApplicationContext().unbindService(mAlixPayConnection);

					// send the result back to caller.
					// 发送交易结果
					Message msg = new Message();
					msg.what = myWhat;
					msg.obj = strRet;
					callback.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();

					// send the result back to caller.
					// 发送交易结果
					Message msg = new Message();
					msg.what = myWhat;
					msg.obj = e.toString();
					callback.sendMessage(msg);
				}
			}
		}).start();

		return true;
	}

	/**
	 * This implementation is used to receive callbacks from the remote service.
	 * 实现安全支付的回调
	 */
	private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
		/**
		 * This is called by the remote service regularly to tell us about new
		 * values. Note that IPC calls are dispatched through a thread pool
		 * running in each process, so the code executing here will NOT be
		 * running in our main thread like most other things -- so, to update
		 * the UI, we need to use a Handler to hop over there. 通过IPC机制启动安全支付服务
		 */
		public void startActivity(String packageName, String className, int iCallingPid, Bundle bundle) throws RemoteException {
			Intent intent = new Intent(Intent.ACTION_MAIN, null);

			if (bundle == null)
				bundle = new Bundle();
			// else ok.

			try {
				bundle.putInt("CallingPid", iCallingPid);
				intent.putExtras(bundle);
			} catch (Exception e) {
				e.printStackTrace();
			}

			intent.setClassName(packageName, className);

			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}

		/**
		 * when the msp loading dialog gone, call back this method.
		 */
		@Override
		public boolean isHideLoadingScreen() throws RemoteException {
			return false;
		}

		/**
		 * when the current trade is finished or cancelled, call back this method.
		 */
		@Override
		public void payEnd(boolean arg0, String arg1) throws RemoteException {

		}

	};
}