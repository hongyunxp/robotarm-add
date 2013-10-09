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
 * @version Time：2012-3-26 下午1:42:08 
 */
public class ConsumeCzkYd extends Activity implements OnClickListener {

	private Button left1;
	private TextView topTv;

	private Button phone, sms, czk, zfb, qb, wy;
	private RelativeLayout rl;
	private LinearLayout ll;

	private Button money20, money30, money50, money100, money300, money500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.consume_czk_yd);
		CloseActivity.add(this);

		setTopBar();

		ll = (LinearLayout) findViewById(R.id.consume_sixbtn);
		czk = (Button) ll.findViewById(R.id.consume_czk);
		czk.setBackgroundResource(R.drawable.consume2);
		czk.setTextColor(Color.WHITE);

		SixbtnClickListener mlistener = new SixbtnClickListener(ConsumeCzkYd.this);
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

		money20 = (Button) findViewById(R.id.czk_yd_20);
		money20.setOnClickListener(this);
		money30 = (Button) findViewById(R.id.czk_yd_30);
		money30.setOnClickListener(this);
		money50 = (Button) findViewById(R.id.czk_yd_50);
		money50.setOnClickListener(this);
		money100 = (Button) findViewById(R.id.czk_yd_100);
		money100.setOnClickListener(this);
		money300 = (Button) findViewById(R.id.czk_yd_300);
		money300.setOnClickListener(this);
		money500 = (Button) findViewById(R.id.czk_yd_500);
		money500.setOnClickListener(this);

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
		} else {
			CardConsumeBean cardConsumeBean = LocalStore.getCzk(ConsumeCzkYd.this);
			Intent intent = new Intent(this, ConsumeCzk.class);

			if (v.getId() == R.id.czk_yd_20) {
				cardConsumeBean.setCartMoney(20);
				cardConsumeBean.setPayMoney(20);
			} else if (v.getId() == R.id.czk_yd_30) {
				cardConsumeBean.setCartMoney(30);
				cardConsumeBean.setPayMoney(30);
			} else if (v.getId() == R.id.czk_yd_50) {
				cardConsumeBean.setCartMoney(50);
				cardConsumeBean.setPayMoney(50);
			} else if (v.getId() == R.id.czk_yd_100) {
				cardConsumeBean.setCartMoney(100);
				cardConsumeBean.setPayMoney(100);
			} else if (v.getId() == R.id.czk_yd_300) {
				cardConsumeBean.setCartMoney(300);
				cardConsumeBean.setPayMoney(300);
			} else if (v.getId() == R.id.czk_yd_500) {
				cardConsumeBean.setCartMoney(500);
				cardConsumeBean.setPayMoney(500);
			}

			LocalStore.setCzk(ConsumeCzkYd.this, cardConsumeBean);

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
