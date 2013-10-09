package com.xs.cn.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastedge.readnovel.beans.ConsumeQQBean;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.common.SixbtnClickListener;
import com.eastedge.readnovel.task.ConsumeQQTask;
import com.eastedge.readnovel.task.ContactInfoTask;
import com.eastedge.readnovel.utils.CommonUtils;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;

/** consume_qb_go
 * @author ninglv 
 * @version Time：2012-3-26 下午5:11:18 
 */
public class ConsumeQb2 extends Activity implements OnClickListener {

	private Button left1, go;
	private TextView topTv;

	private Button phone, sms, czk, zfb, qb, wy;
	private RelativeLayout rl;
	private LinearLayout ll;

	private EditText cardNo;
	private EditText cardPwd;

	private TextView payMoney;
	private TextView readBean;

	private ConsumeQQBean qqConsumeBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.consume_qb2);
		CloseActivity.add(this);

		setTopBar();

		ll = (LinearLayout) findViewById(R.id.consume_sixbtn);
		qb = (Button) ll.findViewById(R.id.consume_qb);
		qb.setBackgroundResource(R.drawable.consume2);
		qb.setTextColor(Color.WHITE);

		SixbtnClickListener mlistener = new SixbtnClickListener(ConsumeQb2.this);
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

		go = (Button) findViewById(R.id.consume_qb_go);
		go.setOnClickListener(this);

		cardNo = (EditText) findViewById(R.id.card_no);
		cardPwd = (EditText) findViewById(R.id.card_pwd);

		payMoney = (TextView) findViewById(R.id.pay_money);
		readBean = (TextView) findViewById(R.id.pay_read_bean);

		cardNo.setInputType(InputType.TYPE_CLASS_NUMBER);
		cardPwd.setInputType(InputType.TYPE_CLASS_NUMBER);

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
		} else if (v.getId() == R.id.consume_qb_go) {
			//执行支付
			qqConsumeBean.setOrderId(CommonUtils.createCardPayNo());//订单号
			qqConsumeBean.setCardId(cardNo.getText().toString());
			qqConsumeBean.setCardPwd(cardPwd.getText().toString());

			LocalStore.setConsumeQQ(this, qqConsumeBean);

			//充值卡检查
			if (StringUtils.isBlank(qqConsumeBean.getCardId())) {
				ViewUtils.showDialog(this, "请输入Q币充值卡卡号", R.drawable.infoicon, null);
				cardNo.requestFocus();//定位焦点
				return;
			} else if (StringUtils.isBlank(qqConsumeBean.getCardPwd())) {
				ViewUtils.showDialog(this, "请输入Q币充值卡密码", R.drawable.infoicon, null);
				cardPwd.requestFocus();//定位焦点
				return;
			}

			//执行充值异步任务
			new ConsumeQQTask(this).execute();
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

		qqConsumeBean = LocalStore.getConsumeQQ(this);

		payMoney.setText(qqConsumeBean.getPayMoney() + "元");
		readBean.setText(String.valueOf(qqConsumeBean.getPayMoney() * 80));
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
		startActivity(new Intent(this, ConsumeQb1.class));
		finish();
	}
}
