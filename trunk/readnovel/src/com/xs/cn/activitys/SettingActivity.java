package com.xs.cn.activitys;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.task.ContactInfoTask;
import com.eastedge.readnovel.task.PhoneCleanTask;
import com.eastedge.readnovel.task.SDCleanTask;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;
import com.xs.cn.http.UpdateService;

/**
 * 系统设置
 */
public class SettingActivity extends Activity implements OnClickListener {
	private View novel_set_gxrjbb;
	private View novel_set_bbgxsz;
	private View novel_set_aboutwe;
	private Button left1;
	private CheckBox pushSet;
	private CheckBox setKeepScreenOn;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novel_set);
		CloseActivity.add(this);

		novel_set_bbgxsz = (View) findViewById(R.id.novel_set_bbgxsz);// 版本更新设置
		novel_set_gxrjbb = (View) findViewById(R.id.novel_set_gxrjbb);// 更新软件版本
		novel_set_aboutwe = (View) findViewById(R.id.novel_set_aboutwe);// 关于我们

		pushSet = (CheckBox) findViewById(R.id.set_push);
		setKeepScreenOn = (CheckBox) findViewById(R.id.set_keepScreenOn);

		if (LocalStore.getIsSetPush(this)) {
			pushSet.setChecked(true);
			Intent intentService = new Intent(this, UpdateService.class);
			startService(intentService);
		} else {
			pushSet.setChecked(false);
		}

		if (LocalStore.getKeepScreenOn(this)) {
			setKeepScreenOn.setChecked(true);
		} else {
			setKeepScreenOn.setChecked(false);
		}

		pushSet.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					LocalStore.setPush(SettingActivity.this, true);
					pushSet.setChecked(true);
					Intent intentService = new Intent(SettingActivity.this, UpdateService.class);
					startService(intentService);
				} else {
					LocalStore.setPush(SettingActivity.this, false);
					pushSet.setChecked(false);
					Intent intentService = new Intent(SettingActivity.this, UpdateService.class);
					stopService(intentService);
				}
			}
		});

		setTopBar();

		//创建和启动异步任务，取电话等数据
		//创建和启动异步任务，取客服信息
		TextView aboutMeQQ = (TextView) findViewById(R.id.about_me_qq);
		TextView aboutMeTel = (TextView) findViewById(R.id.about_me_tel);
		new ContactInfoTask(this, aboutMeTel, aboutMeQQ).execute();

		novel_set_bbgxsz.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent2 = new Intent(SettingActivity.this, SettingBBActivity.class);
				startActivity(intent2);
			}
		});
		novel_set_gxrjbb.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent2 = new Intent(SettingActivity.this, BanbenxinxiActivity.class);
				startActivity(intent2);
			}
		});
		novel_set_aboutwe.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent2 = new Intent(SettingActivity.this, AboutweActivity.class);
				startActivity(intent2);
			}
		});
	}

	private void setTopBar() {
		left1 = (Button) findViewById(R.id.title_btn_left2);
		left1.setText("返回");
		left1.setGravity(Gravity.CENTER);

		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText("设置");

		left1.setVisibility(View.VISIBLE);

		left1.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == left1.getId()) {
			startActivity(new Intent(SettingActivity.this, MainActivity.class));
			finish();
		}
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		CloseActivity.curActivity = this;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}

	/**
	 * 推荐应用
	 * @param view
	 */
	public void goToApp(View view) {
		Intent intent = new Intent(this, SettingAppActivity.class);
		startActivity(intent);
	}

	/**
	 * 清理数据
	 */
	public void cleanData(View view) {
		ViewUtils.confirm(this, "清理当前应用SD卡数据", "确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				new SDCleanTask(SettingActivity.this).execute();

			}
		});

	}

	/**
	 * 清理缓存
	 */
	public void cleanCache(View view) {
		ViewUtils.confirm(this, "将清空所有用户相关数据", "确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				new PhoneCleanTask(SettingActivity.this).execute();

			}
		});

	}

	/**
	 * 屏幕常亮
	 * @param view
	 */
	public void keepScreenOn(View view) {

		CheckBox cBox = (CheckBox) view;

		if (cBox.isChecked())
			LocalStore.setKeepScreenOn(this, true);
		else
			LocalStore.setKeepScreenOn(this, false);

	}

}
