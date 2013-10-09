package com.xs.cn.activitys;

import android.app.Activity;
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
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;

/** 
 * @author ninglv 
 * @version Time：2012-3-26 下午5:22:01 
 */
public class ConsumeWy extends Activity implements OnClickListener, OnCheckedChangeListener {
	private Button phone, sms, czk, zfb, qb, wy;
	private LinearLayout ll;

	private Button left1;
	private TextView topTv;
	private RelativeLayout rl;

	private Button go;
	private EditText input;

	private RadioGroup rg_wy;
	private RadioButton wyXykButton;// 信用卡
	private RadioButton wyCckWapButton;// 存储卡

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//	setContentView(R.layout.consume_wy) ; 改成网银支付
		setContentView(R.layout.consume_xyk);

		CloseActivity.add(this);
		setTopBar();
		rg_wy = (RadioGroup) findViewById(R.id.wy);
		rg_wy.setOnCheckedChangeListener(this);

		wyXykButton = (RadioButton) findViewById(R.id.wy_xyk_wap);
		wyCckWapButton = (RadioButton) findViewById(R.id.wy_cck_wap);

		int smallSize = (int) getResources().getDimension(R.dimen.alipay_radiobutton_small_text_size);

		//设置textview样式
		SpannableString mspApp = new SpannableString(Html.fromHtml(getString(R.string.xinyongka_app)));
		mspApp.setSpan(new AbsoluteSizeSpan(smallSize), 5, mspApp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		wyXykButton.setText(mspApp);

		SpannableString mspWap = new SpannableString(Html.fromHtml(getString(R.string.cunchuka_app)));
		mspWap.setSpan(new AbsoluteSizeSpan(smallSize), 5, mspWap.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		wyCckWapButton.setText(mspWap);
	}

	private void setTopBar() {
		rl = (RelativeLayout) findViewById(R.id.consume_top);
		left1 = (Button) rl.findViewById(R.id.title_btn_left2);
		topTv = (TextView) rl.findViewById(R.id.title_tv);
		left1.setText("  返回");
		left1.setVisibility(View.VISIBLE);

		ll = (LinearLayout) findViewById(R.id.consume_sixbtn);
		wy = (Button) ll.findViewById(R.id.consume_wy);
		wy.setBackgroundResource(R.drawable.consume2);
		wy.setTextColor(Color.WHITE);

		left1.setOnClickListener(this);

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

		topTv.setText("充值中心");

		go = (Button) findViewById(R.id.consume_wy_go);
		go.setOnClickListener(this);

		input = (EditText) findViewById(R.id.consume_wy_input);
	}

	public void onClick(View v) {

		if (v.getId() == R.id.title_btn_left2) {
			goBack();
		}

		if (v.getId() == R.id.consume_wy_go) {//确认支付
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

			int checkedId = rg_wy.getCheckedRadioButtonId();

			if (R.id.wy_xyk_wap == checkedId) {//信用卡支付

				Intent intent = new Intent(this, ConsumeWyWap.class);
				intent.putExtra("tag", "wy");
				intent.putExtra("isXXk", true);
				intent.putExtra("uid", user.getUid());
				intent.putExtra("fee", money);

				startActivity(intent);

			} else {//储蓄卡支付
				Intent intent = new Intent(this, ConsumeWyWap.class);
				intent.putExtra("tag", "wy");
				intent.putExtra("isXXk", false);
				intent.putExtra("uid", user.getUid());
				intent.putExtra("fee", money);
				startActivity(intent);

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

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
	}

}
