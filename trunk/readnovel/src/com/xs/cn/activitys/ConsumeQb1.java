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

import com.eastedge.readnovel.beans.ConsumeQQBean;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.common.SixbtnClickListener;
import com.eastedge.readnovel.task.ContactInfoTask;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

/** 
 * @author ninglv 
 * @version Time：2012-3-26 下午4:52:59 
 */
public class ConsumeQb1 extends Activity implements OnClickListener {

	private Button left1;
	private TextView topTv;

	private Button phone, sms, czk, qb, wy, zfb;
	private RelativeLayout rl;
	private LinearLayout ll;

	private Button money5, money10, money15, money20, money30, money60;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.consume_qb1);
		CloseActivity.add(this);

		setTopBar();

		ll = (LinearLayout) findViewById(R.id.consume_sixbtn);
		qb = (Button) ll.findViewById(R.id.consume_qb);
		qb.setBackgroundResource(R.drawable.consume2);
		qb.setTextColor(Color.WHITE);

		SixbtnClickListener mlistener = new SixbtnClickListener(ConsumeQb1.this);
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

		money5 = (Button) findViewById(R.id.qb_5);
		money5.setOnClickListener(this);
		money10 = (Button) findViewById(R.id.qb_10);
		money10.setOnClickListener(this);
		money15 = (Button) findViewById(R.id.qb_15);
		money15.setOnClickListener(this);
		money20 = (Button) findViewById(R.id.qb_20);
		money20.setOnClickListener(this);
		money30 = (Button) findViewById(R.id.qb_30);
		money30.setOnClickListener(this);
		money60 = (Button) findViewById(R.id.qb_60);
		money60.setOnClickListener(this);

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
		if (v.getId() == R.id.title_btn_left2) {
			goBack();
		} else {
			Intent i1 = new Intent(this, ConsumeQb2.class);

			ConsumeQQBean qqConsumeBean = new ConsumeQQBean();
			qqConsumeBean.setuId(Integer.parseInt(BookApp.getUser().getUid()));
			qqConsumeBean.setUserName(BookApp.getUser().getUsername());

			if (v.getId() == R.id.qb_5) {
				qqConsumeBean.setPayMoney(5);
			} else if (v.getId() == R.id.qb_10) {
				qqConsumeBean.setPayMoney(10);
			} else if (v.getId() == R.id.qb_15) {
				qqConsumeBean.setPayMoney(15);
			} else if (v.getId() == R.id.qb_20) {
				qqConsumeBean.setPayMoney(20);
			} else if (v.getId() == R.id.qb_30) {
				qqConsumeBean.setPayMoney(30);
			} else if (v.getId() == R.id.qb_60) {
				qqConsumeBean.setPayMoney(60);
			}

			LocalStore.setConsumeQQ(this, qqConsumeBean);

			startActivity(i1);
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
			goBack();

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
		finish();
	}
}
