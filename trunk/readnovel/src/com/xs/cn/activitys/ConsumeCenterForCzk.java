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
import com.eastedge.readnovel.utils.CommonUtils;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

/** 
 * @author ninglv 
 * @version Time：2012-3-26 下午12:49:39 
 */
public class ConsumeCenterForCzk extends Activity implements OnClickListener {

	private Button left1;
	private TextView topTv;

	private Button phone, sms, czk, zfb, qb, wy;
	private RelativeLayout rl;
	private LinearLayout ll;

	private Button yd, lt, dx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.consume_center_czk);
		CloseActivity.add(this);

		setTopBar();

		ll = (LinearLayout) findViewById(R.id.consume_sixbtn);
		czk = (Button) ll.findViewById(R.id.consume_czk);
		czk.setBackgroundResource(R.drawable.consume2);
		czk.setTextColor(Color.WHITE);

		SixbtnClickListener mlistener = new SixbtnClickListener(ConsumeCenterForCzk.this);
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

		yd = (Button) findViewById(R.id.consume_czk_yd);
		yd.setOnClickListener(this);
		lt = (Button) findViewById(R.id.consume_czk_lt);
		lt.setOnClickListener(this);
		dx = (Button) findViewById(R.id.consume_czk_dx);
		dx.setOnClickListener(this);

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
			String tag = getIntent().getStringExtra("Tag");
			if ("readbook".equals(tag)) {
				finish();
			} else {
				goBack();
			}
		} else {
			CardConsumeBean cardConsumeBean = new CardConsumeBean();

			cardConsumeBean.setuId(Integer.parseInt(BookApp.getUser().getUid()));
			cardConsumeBean.setUserName(BookApp.getUser().getUsername());
			cardConsumeBean.setOrderId(CommonUtils.createCardPayNo());//订单号

			if (v.getId() == R.id.consume_czk_yd) {//移动充值

				cardConsumeBean.setCardType(CardConsumeBean.CMCC);
				LocalStore.setCzk(this, cardConsumeBean);

				Intent intent = new Intent(this, ConsumeCzkYd.class);
				startActivity(intent);
				finish();

			} else if (v.getId() == R.id.consume_czk_lt) {//联通充值 

				cardConsumeBean.setCardType(CardConsumeBean.UNICOM);
				LocalStore.setCzk(this, cardConsumeBean);

				Intent intent = new Intent(this, ConsumeCzkLt.class);
				startActivity(intent);
				finish();

			} else if (v.getId() == R.id.consume_czk_dx) {//电信充值

				cardConsumeBean.setCardType(CardConsumeBean.TELECOM);
				LocalStore.setCzk(this, cardConsumeBean);

				Intent intent = new Intent(this, ConsumeCzkDx.class);
				startActivity(intent);
				finish();
			}

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
		MobclickAgent.onPause(this);
		super.onPause();
	}

	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}

	private void goBack() {
		finish();
	}
}
