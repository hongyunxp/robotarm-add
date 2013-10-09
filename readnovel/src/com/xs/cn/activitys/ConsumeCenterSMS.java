package com.xs.cn.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.SixbtnClickListener;
import com.eastedge.readnovel.task.ContactInfoTask;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

/** 
 * @author ninglv 
 * @version Time：2012-3-22 下午4:37:42 
 */
public class ConsumeCenterSMS extends Activity implements OnClickListener {

	private Button left1;
	private TextView topTv;

	private Button sms, phone, czk, zfb, qb, wy;
	private RelativeLayout rl;
	private LinearLayout ll;
	private Button sms10, sms20;
	private User user;
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.consume_sms);
		CloseActivity.add(this);

		setTopBar();

		ll = (LinearLayout) findViewById(R.id.consume_sixbtn);
		sms = (Button) ll.findViewById(R.id.consume_sms);
		sms.setTextColor(Color.WHITE);
		sms.setBackgroundResource(R.drawable.consume2);

		SixbtnClickListener mListener = new SixbtnClickListener(ConsumeCenterSMS.this);
		phone = (Button) ll.findViewById(R.id.consume_phone);
		phone.setOnClickListener(mListener);
		czk = (Button) ll.findViewById(R.id.consume_czk);
		czk.setOnClickListener(mListener);
		zfb = (Button) ll.findViewById(R.id.consume_zfb);
		zfb.setOnClickListener(mListener);
		qb = (Button) ll.findViewById(R.id.consume_qb);
		qb.setOnClickListener(mListener);
		wy = (Button) ll.findViewById(R.id.consume_wy);
		wy.setOnClickListener(mListener);

		sms10 = (Button) findViewById(R.id.consume_sms_10);
		String source = "发送短信充值10元\n（获得450个阅读币）";
		SpannableString ss1 = new SpannableString(source);
		ss1.setSpan(new AbsoluteSizeSpan(16), 10, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		sms10.setText(ss1);
		sms10.setOnClickListener(this);

		sms20 = (Button) findViewById(R.id.consume_sms_20);
		String source2 = "发送短信充值20元\n（获得900个阅读币）";
		SpannableString ss2 = new SpannableString(source2);
		ss2.setSpan(new AbsoluteSizeSpan(16), 10, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		sms20.setText(ss2);
		sms20.setOnClickListener(this);

		user = BookApp.getUser();
		if (user != null && user.getUid() != null) {
			uid = user.getUid();
		} else {
			Toast.makeText(ConsumeCenterSMS.this, "您尚未登录，请先登录！", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(ConsumeCenterSMS.this, LoginActivity.class));
			finish();
		}

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
		// TODO Auto-generated method stub
		if (v.getId() == R.id.title_btn_left2) {
			//			Intent i = new Intent(ConsumeCenterSMS.this, PersonCenterActivity.class);
			//			startActivity(i);
			finish();
		}
		if (v.getId() == R.id.consume_sms_10) {
			Uri uri = Uri.parse("smsto:1065800883147");
			Intent i = new Intent(Intent.ACTION_SENDTO, uri);
			i.putExtra("sms_body", "100#" + uid);
			startActivity(i);

			//			finish();
		}
		if (v.getId() == R.id.consume_sms_20) {
			Uri uri = Uri.parse("smsto:1065800883147");
			Intent i = new Intent(Intent.ACTION_SENDTO, uri);
			i.putExtra("sms_body", "200#" + uid);
			startActivity(i);

			//			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			//			startActivity(new Intent(ConsumeCenterSMS.this, PersonCenterActivity.class));
			finish();
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
}
