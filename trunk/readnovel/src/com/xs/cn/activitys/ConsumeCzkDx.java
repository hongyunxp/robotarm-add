package com.xs.cn.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastedge.readnovel.beans.CardConsumeBean;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.common.SixbtnClickListener;
import com.eastedge.readnovel.task.ContactInfoTask;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

/** 
 * @author ninglv 
 * @version Time：2012-3-26 下午4:00:05 
 */
public class ConsumeCzkDx extends Activity implements OnClickListener {

	private Button left1;
	private TextView topTv;

	private Button phone, sms, czk, zfb, qb, wy;
	private RelativeLayout rl;
	private LinearLayout ll;

	private Button money50, money100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.consume_czk_dx);
		CloseActivity.add(this);

		setTopBar();

		ll = (LinearLayout) findViewById(R.id.consume_sixbtn);
		czk = (Button) ll.findViewById(R.id.consume_czk);
		czk.setBackgroundResource(R.drawable.consume2);
		czk.setTextColor(Color.WHITE);

		SixbtnClickListener mlistener = new SixbtnClickListener(ConsumeCzkDx.this);
		sms = (Button) ll.findViewById(R.id.consume_sms);
		sms.setOnClickListener(mlistener);
		phone = (Button) ll.findViewById(R.id.consume_phone);
		phone.setOnClickListener(mlistener);
		czk = (Button) ll.findViewById(R.id.consume_czk);
		czk.setOnClickListener(mlistener);
		zfb = (Button) ll.findViewById(R.id.consume_zfb);
		zfb.setOnClickListener(mlistener);
		qb = (Button) ll.findViewById(R.id.consume_qb);
		qb.setOnClickListener(mlistener);
		wy = (Button) ll.findViewById(R.id.consume_wy);
		wy.setOnClickListener(mlistener);

		money50 = (Button) findViewById(R.id.czk_dx_50);
		money50.setOnClickListener(this);
		money100 = (Button) findViewById(R.id.czk_dx_100);
		money100.setOnClickListener(this);

		//创建和启动异步任务，取客服信息
		TextView aboutMeQQ = (TextView) findViewById(R.id.about_me_qq);
		TextView aboutMeTel = (TextView) findViewById(R.id.about_me_tel);
		new ContactInfoTask(this, aboutMeTel, aboutMeQQ).execute();
	}

	private void setTopBar() {
		rl = (RelativeLayout) findViewById(R.id.consume_top);
		left1 = (Button) rl.findViewById(R.id.title_btn_left2);
		topTv = (TextView) rl.findViewById(R.id.title_tv);
		left1.setText("  返回");
		left1.setVisibility(View.VISIBLE);

		left1.setOnClickListener(this);

		topTv.setText("充值中心");
	}

	public void onClick(View v) {
		if (v.getId() == R.id.title_btn_left2) {//后退按钮
			goBack();
		} else if (v.getId() == R.id.czk_dx_50) {
			CardConsumeBean cardConsumeBean = LocalStore.getCzk(this);

			cardConsumeBean.setCartMoney(50);
			cardConsumeBean.setPayMoney(50);

			LocalStore.setCzk(this, cardConsumeBean);

			Intent intent = new Intent(this, ConsumeCzk.class);
			startActivity(intent);
			finish();
		} else if (v.getId() == R.id.czk_dx_100) {
			CardConsumeBean cardConsumeBean = LocalStore.getCzk(this);

			cardConsumeBean.setCartMoney(100);
			cardConsumeBean.setPayMoney(100);

			LocalStore.setCzk(this, cardConsumeBean);

			Intent intent = new Intent(this, ConsumeCzk.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
		}
		return super.onKeyDown(keyCode, event);
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

	//后退
	private void goBack() {
		startActivity(new Intent(this, ConsumeCenterForCzk.class));
		finish();
	}
}
