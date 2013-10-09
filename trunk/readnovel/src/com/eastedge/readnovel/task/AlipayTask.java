package com.eastedge.readnovel.task;

import java.net.URLDecoder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.webkit.WebView;
import android.widget.EditText;

import com.eastedge.readnovel.beans.AliPayBean;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.Constants;
import com.readnovel.base.alipay.Alipay;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.http.HttpImpl;

/**
 * 支付宝app异步任务
 * 
 * @author li.li
 *
 */
public class AlipayTask extends EasyTask<Activity, Void, Void, Void> {
	//保证同一时间运行一个支付宝支付
	private volatile boolean isRunning;
	private WebView mWebView;

	private ProgressDialog pd;
	private Alipay alipay;

	public AlipayTask(Activity caller, Alipay alipay) {
		super(caller);

		this.alipay = alipay;
	}

	@Override
	public void onPreExecute() {
		//打加载对话框
		pd = ViewUtils.progressLoading(caller);
	}

	@Override
	public void onPostExecute(Void result) {
		isRunning = false;

		//关闭加载对话框
		pd.cancel();
	}

	@Override
	public Void doInBackground(Void... params) {

		//返回
		if (isRunning)
			return null;

		User user = BookApp.getUser();

		EditText input = (EditText) caller.findViewById(R.id.consume_zfb_input);

		double money = 0;
		if (StringUtils.isNotBlank(input.getText().toString()) && user != null) {
			money = Double.parseDouble(input.getText().toString());
			if (money < Constants.CONSUME_PAY_MIN) {
				return null;
			}

			AliPayBean aliPayBean = HttpImpl.alipayApp(user.getUid(), money);

			final String orderInfo = URLDecoder.decode(aliPayBean.getContent());
			final String encodeStrSign = aliPayBean.getSign();
			final String signType = aliPayBean.getSign_type();

			aliPayBean = new AliPayBean();

			HANDLER.post(new Runnable() {

				@Override
				public void run() {
					//执行支付
					alipay.pay(orderInfo, encodeStrSign, signType);
				}
			});
		} else {
			HANDLER.post(new Runnable() {
				@Override
				public void run() {
					//						Toast.makeText(caller, "最低充值限额" + Constants.CONSUME_PAY_MIN + "元", Toast.LENGTH_LONG).show();
					ViewUtils.showDialog(caller, "请输入20以上的整数", R.drawable.infoicon, null);
				}
			});
		}

		return null;
	}

}
