package com.eastedge.readnovel.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.eastedge.readnovel.utils.CommonUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.ConsumeCenterForBaoYue;
import com.xs.cn.activitys.ConsumeCenterForCzk;
import com.xs.cn.activitys.ConsumeCenterSMS;
import com.xs.cn.activitys.ConsumeQb1;
import com.xs.cn.activitys.ConsumeWy;

/** 
 * @author ninglv 
 * @version Time：2012-3-22 下午4:33:31 
 */
public class SixbtnClickListener implements OnClickListener {

	private Context context;

	public SixbtnClickListener(Context context) {
		super();
		this.context = context;
	}

	public void onClick(View v) {
		if (v.getId() == R.id.consume_sms) {
			context.startActivity(new Intent(context, ConsumeCenterSMS.class));
			((Activity) context).finish();
			return;
		}
		//包月
		if (v.getId() == R.id.consume_phone) {
			context.startActivity(new Intent(context, ConsumeCenterForBaoYue.class));
			((Activity) context).finish();
			return;
		}
		//充值卡
		if (v.getId() == R.id.consume_czk) {
			Intent i = new Intent(context, ConsumeCenterForCzk.class);
			i.putExtra("tag", "czk");
			context.startActivity(i);
			((Activity) context).finish();
			return;
		}
		//支付宝
		if (v.getId() == R.id.consume_zfb) {
			CommonUtils.goToConsume(context);
			((Activity) context).finish();
			return;
		}
		//Q币支付
		if (v.getId() == R.id.consume_qb) {
			Intent i = new Intent(context, ConsumeQb1.class);
			context.startActivity(i);
			((Activity) context).finish();
			return;
		}
		//网银支付
		if (v.getId() == R.id.consume_wy) {
			Intent i = new Intent(context, ConsumeWy.class);
			i.putExtra("tag", "wy");
			context.startActivity(i);
			((Activity) context).finish();
			return;
		}
	}

}
