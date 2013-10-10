package com.readnovel.book.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mobclick.android.MobclickAgent;
import com.readnovel.book.base.common.Constants;
import com.readnovel.book.base.sp.StyleSaveUtil;
import com.readnovel.book.base.task.InitTask;
import com.readnovel.book.base.utils.BookListProvider;
import com.readnovel.book.base.utils.EventUtils;
import com.readnovel.book.base.utils.PhoneUtils;

public class FirstActivity extends BaseActivity {
	private StyleSaveUtil util;
	private AnimationDrawable anim;
	private InitTask initTask;
	int intLevel;
	int intScale;
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				intLevel = intent.getIntExtra("level", 0);
				if (sst != null) {
					sst.setlight(intLevel);
				} else {
				}
			}
		}
	};

	/* 拦截到ACTION_BATTERY_CHANGED时要执行的method */
	public void onBatteryInfoReceiver(int intLevel, int intScale) {
		unregisterReceiver(mBatInfoReceiver);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.first0);

		ImageView img = (ImageView) findViewById(R.id.dhimg);
		//设置动画
		img.setImageResource(R.anim.dhld);
		//获取动画
		anim = (AnimationDrawable) img.getDrawable();
		util = new StyleSaveUtil(this);
		initTask = new InitTask(this);
		//安装应用事件
		if (!util.installed()) {
			util.installed(true);
			// 添加快捷方式
			PhoneUtils.addShortcutToDesktop(this, R.drawable.icon, R.string.app_name);
			// 添加友盟统计 
			String channelName = getResources().getString(R.string.umeng_channel_value);
			int bookId = BookListProvider.getInstance(this).getBook().getId();
			EventUtils.installForChannel(channelName, bookId);
			MobclickAgent.onEvent(this, Constants.UMENG_EVENT_INSTALLED, getString(R.string.app_name));
		}
		MobclickAgent.onEvent(this, Constants.UMENG_EVENT_BOOK_LAUNCH, getString(R.string.app_name));
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//注册电量
		registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

	}

	@Override
	protected void onResume() {
		super.onResume();
		initTask.execute();//执行初始化
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//取消电量注册
		onBatteryInfoReceiver(intLevel, intScale);

	}

	public AnimationDrawable getAnim() {
		return anim;
	}

	public StyleSaveUtil getSaveUtil() {
		return util;
	}
}