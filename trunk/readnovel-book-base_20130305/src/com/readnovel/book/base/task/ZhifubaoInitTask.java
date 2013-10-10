package com.readnovel.book.base.task;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.readnovel.base.alipay.AlixId;
import com.readnovel.base.alipay.BaseHelper;
import com.readnovel.base.alipay.MobileSecurePayer;
import com.readnovel.book.base.PayMsgAndZhifubaoActivity;
import com.readnovel.book.base.R;
import com.readnovel.book.base.common.JsonToBean;
import com.readnovel.book.base.http.HttpComm;
import com.readnovel.book.base.sync.EasyTask;

public class ZhifubaoInitTask extends EasyTask<PayMsgAndZhifubaoActivity, Void, Void, Void> {
	public ProgressDialog mProgress = null;

	public ZhifubaoInitTask(PayMsgAndZhifubaoActivity caller) {
		super(caller);
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		mProgress = BaseHelper.showProgress(caller, null, "正在支付", false, true);
	}

	@Override
	public Void doInBackground(Void... params) {
		//		String ss = "http://www.hitesex.com/qhwx/alipay/alipay.php";www.smqhe.com/pay/alipay.php
		String ss = "http://www.smqhe.com/pay/alipay.php";
		JSONObject json = HttpComm.sendJSONToServer(ss);
		if (json == null)
			Toast.makeText(caller, "服务端没有数据", Toast.LENGTH_SHORT).show();
		caller.parter = JsonToBean.JsonToParter(json);

		HANDLER.post(new Runnable() {
			@Override
			public void run() {
				// 检测配置信息.
				if (!caller.checkInfo()) {
					BaseHelper.showDialog(caller, "提示", "缺少partner或者seller，请在src/com/alipay/android/appDemo4/PartnerConfig.java中增加。",
							R.drawable.infoicon);
					return;
				}
				// 根据订单信息开始进行支付.
				try {
					// 准备订单信息.
					String orderInfo = caller.getOrderInfo();
					// 这里根据签名方式对订单信息进行签名.
					String signType = caller.getSignType();
					String strsign = caller.sign(signType, orderInfo);
					// 对签名进行编码.
					strsign = URLEncoder.encode(strsign);
					// 组装好参数
					String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&" + caller.getSignType();
					// 调用pay方法进行支付.
					MobileSecurePayer msp = new MobileSecurePayer();
					boolean bRet = msp.pay(info, caller.mHandler, AlixId.RQF_PAY, caller);
					if (bRet) {
						// 显示“正在支付”进度条.
						closeProgress();
						mProgress = BaseHelper.showProgress(caller, null, "正在支付", false, true);
					}
				} catch (Exception ex) {
					Toast.makeText(caller, "启动远程服务失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return null;
	}

	@Override
	public void onPostExecute(Void result) {
		closeProgress();
	}

	public void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
