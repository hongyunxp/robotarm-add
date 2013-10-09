package com.xs.cn.activitys;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.SixbtnClickListener;
import com.eastedge.readnovel.task.AlipayTask;
import com.eastedge.readnovel.task.ContactInfoTask;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.alipay.Alipay;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;

/**
 * 支付宝
 * 
 * @author li.li
 *
 * Dec 13, 2012
 */
public class ConsumeZfb extends Activity implements OnClickListener, OnCheckedChangeListener {

	private Button left1, go;
	private EditText input;
	private LinearLayout inputLayout;
	private TextView topTv;

	private Button phone, sms, czk, zfb, qb, wy;
	private RelativeLayout rl;
	private LinearLayout ll;
	private RadioGroup rg;
	private RadioButton alipayAppButton;
	private RadioButton alipayWapButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.consume_zfb);
		CloseActivity.add(this);

		setTopBar();

		input = (EditText) findViewById(R.id.consume_zfb_input);
		inputLayout = (LinearLayout) findViewById(R.id.consume_zfb_input_layout);

		ll = (LinearLayout) findViewById(R.id.consume_sixbtn);
		zfb = (Button) ll.findViewById(R.id.consume_zfb);
		zfb.setBackgroundResource(R.drawable.consume2);
		zfb.setTextColor(Color.WHITE);

		SixbtnClickListener mListener = new SixbtnClickListener(this);
		phone = (Button) ll.findViewById(R.id.consume_phone);
		phone.setOnClickListener(mListener);
		sms = (Button) ll.findViewById(R.id.consume_sms);
		sms.setOnClickListener(mListener);
		czk = (Button) ll.findViewById(R.id.consume_czk);
		czk.setOnClickListener(mListener);
		zfb = (Button) ll.findViewById(R.id.consume_zfb);
		zfb.setOnClickListener(mListener);
		qb = (Button) ll.findViewById(R.id.consume_qb);
		qb.setOnClickListener(mListener);
		wy = (Button) ll.findViewById(R.id.consume_wy);
		wy.setOnClickListener(mListener);

		go = (Button) findViewById(R.id.consume_zfb_go);
		go.setOnClickListener(this);

		rg = (RadioGroup) findViewById(R.id.alipay);
		rg.setOnCheckedChangeListener(this);

		alipayAppButton = (RadioButton) findViewById(R.id.alipay_app);
		alipayWapButton = (RadioButton) findViewById(R.id.alipay_wap);

		//		alipayAppButton.setText(Html.fromHtml(getString(R.string.alipay_app)));
		//		alipayWapButton.setText(Html.fromHtml(getString(R.string.alipay_wap)));

		int smallSize = (int) getResources().getDimension(R.dimen.alipay_radiobutton_small_text_size);

		//设置textview样式
		SpannableString mspApp = new SpannableString(Html.fromHtml(getString(R.string.alipay_app)));
		mspApp.setSpan(new AbsoluteSizeSpan(smallSize), 8, mspApp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		alipayAppButton.setText(mspApp);

		SpannableString mspWap = new SpannableString(Html.fromHtml(getString(R.string.alipay_wap)));
		mspWap.setSpan(new AbsoluteSizeSpan(smallSize), 7, mspWap.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		alipayWapButton.setText(mspWap);

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
		if (v.getId() == R.id.title_btn_left2) {//返回
			//			Intent i = new Intent(ConsumeZfb.this, PersonCenterActivity.class);
			//						startActivity(i);
			goBack();
		} else if (v.getId() == R.id.consume_zfb_go) {//确认支付
			//校验最小值
			User user = BookApp.getUser();
			double money = 0;
			if (StringUtils.isNotBlank(input.getText().toString()) && user != null)
				money = Double.parseDouble(input.getText().toString());

			if (money < Constants.CONSUME_PAY_MIN) {
				ViewUtils.showDialog(this, "请输入" + Constants.CONSUME_PAY_MIN + "以上的整数", R.drawable.infoicon, null);
				input.requestFocus();
				return;
			}

			int checkedId = rg.getCheckedRadioButtonId();

			if (R.id.alipay_app == checkedId) {//app支付

				Alipay alipay = new Alipay(this, new DialogInterface.OnClickListener() {//支付成功跳到用户中心

							@Override
							public void onClick(DialogInterface dialog, int which) {

								Intent intent = new Intent(ConsumeZfb.this, MainActivity.class);
								intent.putExtra("id", R.id.main_usercenter);
								ConsumeZfb.this.startActivity(intent);

							}

						});

				// 检测安全支付服务是否安装
				if (!alipay.checkIsInstall())
					return;

				AlipayTask alipayTask = new AlipayTask(this, alipay);
				alipayTask.execute();

			} else {//wap支付
				Intent intent = new Intent(this, ConsumeAlipayWap.class);
				intent.putExtra("tag", "zfb");

				intent.putExtra("uid", user.getUid());
				intent.putExtra("fee", money);

				startActivity(intent);

			}

		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		//		if (R.id.alipay_app == checkedId)
		//			inputLayout.setVisibility(View.VISIBLE);
		//		else
		//			inputLayout.setVisibility(View.GONE);

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

	private void goBack() {
		finish();
	}

}
