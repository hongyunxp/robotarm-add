package com.xs.cn.activitys;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eastedge.readnovel.beans.AliPayCallParams;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.Constants.LoginType;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.task.AliPayCallTask;
import com.eastedge.readnovel.threads.AotoLoginThread;
import com.eastedge.readnovel.threads.SendInstallInfo;
import com.eastedge.readnovel.utils.CommonUtils;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.util.PhoneUtils;
import com.readnovel.base.util.StringUtils;
import com.xs.cn.R;
import com.xs.cn.http.UpdateService;

/**
 *  加载页
 */
public class LoadingActivity extends Activity {

	private LinearLayout ly;
	private AotoLoginThread alt; //自动登录线程
	private static final int EVERY_START = 1; //是否登录过
	private AnimationDrawable anim; //加载动画
	private volatile boolean flag1, flag2; //flag1为默认显示2秒 ，flag2登陆成功后

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 5:
				//默认显示2秒后
				flag1 = true;
				if (flag1 && flag2) {
					handler.sendEmptyMessage(8);
				}
				break;
			case 9:
				//登陆成功后
				flag2 = true;
				if (flag1 && flag2) {
					handler.sendEmptyMessage(8);
				}
				break;
			case 8:
				//判断是否是第1次启动，第一次进入书城，后面进入书架
				if (LocalStore.getfirstload(LoadingActivity.this).equals("1")) {
					//					Intent intent = new Intent(LoadingActivity.this, BookshelfActivity.class);
					Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("id", R.id.main_bookcity);
					startActivity(intent);
				} else {
					Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("id", R.id.main_bookshelf);
					startActivity(intent);
				}
				finish();
				break;
			case 7:
				//启动加载动画
				anim.start();
				break;
			}
			;
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		CloseActivity.add(this);

		int cpd = CommonUtils.getChannelPartnerDrawable(this);
		if (cpd != 0) {//得到渠道资源id，不存在时为0
			View indexImg = findViewById(R.id.index);
			indexImg.setBackgroundResource(cpd);
		}

		ImageView img = (ImageView) findViewById(R.id.dhimg);

		//解决支付宝唤起请求
		AliPayCallParams aliPayCallParams = CommonUtils.parseAlipayCallParams(this);
		if (aliPayCallParams != null) {
			if (Constants.ALIPAY_CHANNEL_NAME.equals(CommonUtils.getChannel(this)))
				img.setVisibility(View.GONE);

			new AliPayCallTask(this, aliPayCallParams.getAlipayUserId(), aliPayCallParams.getAuthCode(), LoginType.alipay).execute();
			return;
		}

		//设置动画
		img.setImageResource(R.anim.dhld);
		//获取动画
		anim = (AnimationDrawable) img.getDrawable();

		//延迟0.5秒启动动画
		handler.sendEmptyMessageDelayed(7, 500);

		//延迟2秒加载完成
		handler.sendEmptyMessageDelayed(5, 2000);

		//判断是否推送
		if (LocalStore.getIsSetPush(this)) {
			Intent mService = new Intent(this, UpdateService.class);
			mService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startService(mService);
		}

		//自动登录
		aotoLogin();

		//第一次
		if (LocalStore.getFirstInstall(LoadingActivity.this) == 0) {
			LocalStore.setFirstInstall(LoadingActivity.this, 1);

			//创建快捷方式
			if (!Constants.ALIPAY_CHANNEL_NAME.equals(CommonUtils.getChannel(this)))
				PhoneUtils.addShortcutToDesktop(this, R.drawable.icon, R.string.app_name);

			//初始化设置
			LocalStore.setPush(LoadingActivity.this, true);
			LocalStore.setUpdate(LoadingActivity.this, EVERY_START);

			//记住检查更新时间
			Date date = new Date();
			String upTime = new SimpleDateFormat("yyyyMMddHH").format(date);
			LocalStore.setUptime(LoadingActivity.this, upTime);

		}

		if (!LocalStore.getActivate(this)) {
			//发送手机机器码等信息
			new SendInstallInfo(LoadingActivity.this).start();
		}

		//调用初始化书服务.
		//		Intent intentService = new Intent(this, PreInstallService.class);
		//		startService(intentService);
		//程序重新启动
		LocalStore.setIsFullStart(LoadingActivity.this, true);
	}

	private void aotoLogin() {
		//判断自动登录的时间  自动登录保存1个月
		String endTime = LocalStore.getStime(LoadingActivity.this);
		String now = new SimpleDateFormat("yyyyMMdd").format(new Date());
		if (StringUtils.isNotBlank(endTime) && Long.parseLong(now) <= Long.parseLong(endTime)) {
			// 自动登录
			alt = new AotoLoginThread(LoadingActivity.this, handler);
			alt.start();
		} else {
			//当无用户登陆信息
			flag2 = true;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		CloseActivity.curActivity = this;
	}

	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}
}
