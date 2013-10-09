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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastedge.readnovel.beans.CardConsumeBean;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.common.SixbtnClickListener;
import com.eastedge.readnovel.task.CZKTask;
import com.eastedge.readnovel.task.ContactInfoTask;
import com.eastedge.readnovel.utils.CommonUtils;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;

/** 
 * @author ninglv 
 * @version Time：2012-3-26 下午2:58:53 
 */
public class ConsumeCzk extends Activity implements OnClickListener {

	private Button left1;
	private TextView topTv;

	private Button phone, sms, czk, zfb, qb, wy;
	private RelativeLayout rl;
	private LinearLayout ll;

	private Button ensure;
	//用户名
	private TextView userName;
	//金额
	private TextView consumeSum;
	//订单编号
	private TextView sumNo;
	//阅读币
	private TextView readCoin;

	private CardConsumeBean cardConsumeBean;

	private EditText cardNo;
	private EditText cardPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.consume_czk);
		CloseActivity.add(this);

		setTopBar();

		ll = (LinearLayout) findViewById(R.id.consume_sixbtn);
		czk = (Button) ll.findViewById(R.id.consume_czk);
		czk.setBackgroundResource(R.drawable.consume2);
		czk.setTextColor(Color.WHITE);

		SixbtnClickListener mlistener = new SixbtnClickListener(this);
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

		userName = (TextView) findViewById(R.id.user_name);
		consumeSum = (TextView) findViewById(R.id.consume_sum);
		sumNo = (TextView) findViewById(R.id.sum_no);
		readCoin = (TextView) findViewById(R.id.read_coin);

		ensure = (Button) findViewById(R.id.ensure);//确认
		ensure.setOnClickListener(this);

		cardNo = (EditText) findViewById(R.id.card_no);
		cardPwd = (EditText) findViewById(R.id.card_pwd);

		cardConsumeBean = LocalStore.getCzk(this);
		userName.setText(cardConsumeBean.getUserName());
		consumeSum.setText(String.valueOf(cardConsumeBean.getCartMoney()));
		sumNo.setText(cardConsumeBean.getOrderId());
		//阅读币比值：1:90
		readCoin.setText(String.valueOf(cardConsumeBean.getCartMoney() * 90));

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
		if (v.getId() == R.id.title_btn_left2) {//点击回退按钮
			goBack();
		} else if (v.getId() == R.id.ensure) {//确认

			String cNo = cardNo.getText().toString();
			String cPwd = cardPwd.getText().toString();

			//提示对话框
			if (StringUtils.isBlank(cNo)) {
				ViewUtils.showDialog(this, "请输入充值卡卡号", R.drawable.infoicon, null);
				cardNo.requestFocus();//定位焦点
				return;
			} else if (StringUtils.isBlank(cPwd)) {
				ViewUtils.showDialog(this, "请输入充值卡密码", R.drawable.infoicon, null);
				cardPwd.requestFocus();//定位焦点
				return;
			}

			cardConsumeBean.setCardId(cNo);//卡号
			cardConsumeBean.setCardPwd(cPwd);//密码
			cardConsumeBean.setOrderId(CommonUtils.createCardPayNo());//订单号

			//测试1：90 电信
			//			cardConsumeBean.setCardId("0102001203270076274");
			//			cardConsumeBean.setCardPwd("105103234942946902");
			//联通
			//			cardConsumeBean.setCardId("111201291318659");
			//			cardConsumeBean.setCardPwd("1108990854840001806");
			//移动
			//			cardConsumeBean.setCardId("12045110186716042");
			//			cardConsumeBean.setCardPwd("111484158575810777");

			LocalStore.setCzk(this, cardConsumeBean);

			//付费异步任务
			new CZKTask(this).execute();

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//回退处理
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
		CardConsumeBean cardConsumeBean = LocalStore.getCzk(this);
		if (CardConsumeBean.CMCC == cardConsumeBean.getCardType()) {
			startActivity(new Intent(this, ConsumeCzkYd.class));
			finish();
		} else if (CardConsumeBean.UNICOM == cardConsumeBean.getCardType()) {
			startActivity(new Intent(this, ConsumeCzkLt.class));
			finish();
		} else if (CardConsumeBean.TELECOM == cardConsumeBean.getCardType()) {
			startActivity(new Intent(this, ConsumeCzkDx.class));
			finish();
		}
	}
}
