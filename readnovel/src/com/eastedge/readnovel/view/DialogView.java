package com.eastedge.readnovel.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.beans.OrderAllMsg;
import com.eastedge.readnovel.beans.OrderMsg;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.util.DateUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.activitys.ConsumeCenterForBaoYue;
import com.xs.cn.activitys.ConsumeCenterForCzk;
import com.xs.cn.activitys.ConsumeOther4;
import com.xs.cn.activitys.ConsumeWy;
import com.xs.cn.activitys.LoginActivity;

public class DialogView {
	private Context context;

	public DialogView(Context context) {
		this.context = context;
	}

	public View getDialogVip(OrderMsg msg) {
		View view = LayoutInflater.from(context).inflate(R.layout.readbook_vip, null);
		TextView tv_zsydb = (TextView) view.findViewById(R.id.tv_zsydb);
		TextView tv_dyjg = (TextView) view.findViewById(R.id.tv_dyjg);
		TextView tv_syydb = (TextView) view.findViewById(R.id.tv_syydb);
		//		CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
		//		TextView mscz = (TextView) view.findViewById(R.id.textView2);

		tv_zsydb.setText("字数：" + msg.getWordCount() + "（单价：" + msg.getPrice() + "阅读币/1000字）");
		tv_dyjg.setText(msg.getCalPrice() + "阅读币");
		tv_syydb.setText("剩余阅读币：" + msg.getRemain() + "个");
		//订阅按钮
		//		Button bt_dy = (Button) view.findViewById(R.id.bt_dy);//订阅按钮
		Button bt_dyqbzj = (Button) view.findViewById(R.id.bt_dyqbzj);//订阅全部按钮
		//		Button bt_zqdyqbzj = (Button) view.findViewById(R.id.bt_zqdyqbzj);//折扣订阅全部按钮
		Button bt_dy_5_chapters = (Button) view.findViewById(R.id.bt_dy_5_chapters);//订阅5章按钮

		TextView sub_free_time_tv = (TextView) view.findViewById(R.id.sub_free_time_tv);//免费时间提示textview
		View sub_free_time = view.findViewById(R.id.sub_free_time);//免费时间提示
		TextView tv_dyjg_free = (TextView) view.findViewById(R.id.tv_dyjg_free);//免费阅读币提示

		//				msg.setCurtime(1349835759l);
		//				msg.setOvertime(1349835756l);

		if (msg.getCurtime() > msg.getOvertime()) {//过了免费期
			bt_dyqbzj.setVisibility(View.VISIBLE);
			bt_dy_5_chapters.setVisibility(View.VISIBLE);
			sub_free_time.setVisibility(View.GONE);
			tv_dyjg_free.setVisibility(View.GONE);
			tv_dyjg.getPaint().setFlags(Paint.DEV_KERN_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//去掉原价格加横线
		} else {
			bt_dyqbzj.setVisibility(View.GONE);
			bt_dy_5_chapters.setVisibility(View.GONE);
			sub_free_time.setVisibility(View.VISIBLE);
			tv_dyjg_free.setVisibility(View.VISIBLE);
			sub_free_time_tv.setText(DateUtils.format(msg.getOvertime()));//过期时间
			tv_dyjg.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//原价格加横线
		}

		return view;
	}

	public View getDialogAll(OrderAllMsg allMsg) {
		View view = LayoutInflater.from(context).inflate(R.layout.readbook_qbdy, null);
		TextView tv_zszj = (TextView) view.findViewById(R.id.tv_zszj);
		TextView tv_sxydb = (TextView) view.findViewById(R.id.tv_sxydb);
		TextView tv_syydb = (TextView) view.findViewById(R.id.tv_syydb);

		tv_zszj.setText("字数：" + allMsg.getTotal_vip_word() + "（共" + allMsg.getTotal_chapter() + "章节）");
		tv_sxydb.setText("所需阅读币：" + allMsg.getTotal_price() + "个（" + allMsg.getPrice() + "阅读币/1000字）");
		tv_syydb.setText("剩余阅读币：" + allMsg.getRemain() + "个");

		Button bt_dyqb = (Button) view.findViewById(R.id.bt_dyqb);
		return view;
	}

	public View getDiscountDialogAll(OrderAllMsg allMsg) {

		View view = LayoutInflater.from(context).inflate(R.layout.discount_sub_cfm, null);
		TextView tv_zszj = (TextView) view.findViewById(R.id.tv_zszj);// 原价
		TextView tv_sxydb = (TextView) view.findViewById(R.id.tv_sxydb); //折扣 
		TextView tv_syydb = (TextView) view.findViewById(R.id.tv_syydb); //折扣价
		TextView remain = (TextView) view.findViewById(R.id.remain);// 余额
		tv_zszj.setText("原价：" + allMsg.getPrice() + " 阅读币");
		tv_sxydb.setText("折扣：" + allMsg.getDiscountCount() + " 折 ");
		tv_syydb.setText("" + getDiscount(Float.parseFloat(allMsg.getPrice()) * allMsg.getDiscountCount() / 10));
		remain.setText("剩余阅读币：" + allMsg.getRemain());
		Button bt_dyqb = (Button) view.findViewById(R.id.bt_dyqb);
		return view;
	}

	public float getDiscount(float d) {
		if (d < 100)
			return d;
		int m = (int) d / 100;
		return m * 100;
	}

	/**
	 * 余额不足显示充值页面,
	 * @param uid
	 * @return
	 */
	public View getDialogView3(String uid) {
		View view = LayoutInflater.from(context).inflate(R.layout.readbook_yebz3, null);
		// 准备数据，进行沟通
		// 包月帐户
		RelativeLayout baoyue = (RelativeLayout) view.findViewById(R.id.baoyue_fufei);
		baoyue.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (BookApp.getUser() != null && BookApp.getUser().getUid() != null) {

					Intent intent = new Intent(context, ConsumeCenterForBaoYue.class);
					context.startActivity(intent);

					return;
				} else {
					Toast.makeText(context, "您尚未登录，请先登录！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(context, LoginActivity.class);
					intent.putExtra("Tag", "readbook");
					context.startActivity(intent);
				}
			}
		});
		// 支付宝账户
		RelativeLayout zhifubao = (RelativeLayout) view.findViewById(R.id.zhifubao_fufei);
		zhifubao.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtils.goToConsume(context);
			}
		});
		// 银行卡
		RelativeLayout yinhangka = (RelativeLayout) view.findViewById(R.id.yinhangka);
		yinhangka.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, ConsumeWy.class);
				context.startActivity(i);
			}
		});
		// 短信20元
		RelativeLayout Sms20 = (RelativeLayout) view.findViewById(R.id.duanxin20);
		Sms20.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("smsto:1065800883147");
				Intent i = new Intent(Intent.ACTION_SENDTO, uri);
				i.putExtra("sms_body", "200#" + BookApp.getUser().getUid());
				context.startActivity(i);
			}
		});
		// 短信10
		RelativeLayout Sms10 = (RelativeLayout) view.findViewById(R.id.duanxin10);
		Sms10.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("smsto:1065800883147");
				Intent i = new Intent(Intent.ACTION_SENDTO, uri);
				i.putExtra("sms_body", "100#" + BookApp.getUser().getUid());
				context.startActivity(i);
			}
		});
		// 充值卡
		RelativeLayout chongzhika = (RelativeLayout) view.findViewById(R.id.chongzhika);
		chongzhika.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, ConsumeCenterForCzk.class);
				context.startActivity(i);
			}
		});
		// 其他充值方式
		RelativeLayout other = (RelativeLayout) view.findViewById(R.id.other_fufei);
		other.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (BookApp.getUser() != null && BookApp.getUser().getUid() != null) {
					Intent i = new Intent(context, ConsumeOther4.class);
					i.putExtra("Tag", "readbook");
					context.startActivity(i);
					return;
				} else {
					Toast.makeText(context, "您尚未登录，请先登录！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(context, LoginActivity.class);
					intent.putExtra("Tag", "readbook");
					context.startActivity(intent);
				}
			}
		});
		return view;
	}
}
