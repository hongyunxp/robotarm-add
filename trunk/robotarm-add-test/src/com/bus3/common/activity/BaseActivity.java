package com.bus3.common.activity;

import robot.arm.core.TabInvHandler;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.bus3.common.net.NetType;
import com.bus3.common.utils.BaseUtils;

public class BaseActivity extends Activity {
	private String TAG = getClass().getSimpleName();

	private boolean isChild;// 是否存在父级
	private TabInvHandler tabInvHandler;// 父级,当不存在时为空
	private NetType nt;// 当前网络类型

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		init();
	}

	private void init() {
		tabInvHandler = (TabInvHandler) getParent();
		isChild = tabInvHandler == null ? false : true;
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();

		nt = BaseUtils.checkNet();
		Log.d(TAG, "当前网络连接类型：" + nt.getDesc() + "|是否可用：" + nt.available + "|是否有父级：" + isChild);
		Log.d(TAG, "当前版本号：" + BaseUtils.getAndroidSDKVersion());
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	public TabInvHandler tabInvHandler() {
		return tabInvHandler;
	}

	protected NetType nt() {
		nt = BaseUtils.checkNet();
		return nt;
	}

	protected void setSoftInputMode(int softInputMode) {
		tabInvHandler().getWindow().setSoftInputMode(softInputMode);
	}

}
