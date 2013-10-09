package com.eastedge.readnovel.task;

import java.net.URLDecoder;

import android.app.Activity;
import android.app.ProgressDialog;

import com.eastedge.readnovel.beans.AliPayBean;
import com.eastedge.readnovel.beans.AlipayRefreshToeknBean;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.Constants;
import com.readnovel.base.R;
import com.readnovel.base.alipay.Alipay;
import com.readnovel.base.http.HttpHelper;
import com.readnovel.base.openapi.AlipayAccessTokenKeeper;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.http.HttpImpl;

/**
 * 支付宝app异步任务
 * 
 * @author li.li
 *
 */
public class AlipayWalletPayTask extends EasyTask<Activity, Void, Void, Void> {
	//保证同一时间运行一个支付宝支付
	private volatile boolean isRunning;

	private ProgressDialog pd;
	private Alipay alipay;
	private double money;

	public AlipayWalletPayTask(Activity caller, Alipay alipay, double money) {
		super(caller);

		this.alipay = alipay;
		this.money = money;
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

		if (user != null) {//支付宝合作渠道没有充值金额限制

			AlipayAccessTokenKeeper tokenKeeper = AlipayAccessTokenKeeper.readAccessToken(caller);

			if (tokenKeeper.isSessionValid()) {//token有效

				pay(user, tokenKeeper.getAccessToken());

			} else {//失效
					//使用长token换取短token
				String url = String.format(Constants.ALIPAY_APP_WALLET_REFRESH_TOKEN_URL, tokenKeeper.getRefreshToken());
				AlipayRefreshToeknBean tokenBean = HttpHelper.get(url, null, AlipayRefreshToeknBean.class);

				if (tokenBean != null) {
					//保存更新后的token
					AlipayAccessTokenKeeper.clear(caller);//清除登陆token
					AlipayAccessTokenKeeper.keepAccessToken(caller, tokenBean.getAccess_token(), tokenBean.getAccess_token_expires_in(),
							tokenBean.getRefresh_token(), tokenBean.getRefresh_token_expires_in());

					//重新读取
					tokenKeeper = AlipayAccessTokenKeeper.readAccessToken(caller);

					pay(user, tokenKeeper.getAccessToken());
				}
			}

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

	private void pay(User user, String accessToken) {
		AliPayBean aliPayBean = HttpImpl.alipayWalletApp(user.getUid(), money, accessToken);

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
	}

}
