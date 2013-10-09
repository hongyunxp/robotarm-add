package com.eastedge.readnovel.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eastedge.readnovel.beans.AliPayUserBean;
import com.eastedge.readnovel.beans.AlipayRefreshToeknBean;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.Util;
import com.readnovel.base.http.HttpHelper;
import com.readnovel.base.openapi.AlipayAccessTokenKeeper;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.BookApp;

public class AliPayUserSyncTask extends EasyTask<Activity, Void, Void, AliPayUserBean> {
	private ProgressDialog pd;

	public AliPayUserSyncTask(Activity caller) {
		super(caller);

	}

	@Override
	public void onPreExecute() {
		//打加载对话框
		pd = ViewUtils.progressLoading(caller, "用户资料同步中...");
	}

	@Override
	public void onPostExecute(AliPayUserBean aliPayUser) {
		//关闭加载对话框
		pd.cancel();

		if (aliPayUser == null || !"1".equals(aliPayUser.getCode())) {
			ViewUtils.toastShart(caller, "获得用户信息失败");
			return;
		}

		//设置支付宝信息
		TextView alipayEmailTV = (TextView) caller.findViewById(R.id.alipay_email);
		caller.findViewById(R.id.alipay_email_layout).setVisibility(View.VISIBLE);
		String alipayEmail = aliPayUser.getAlipay_user_info().getEmail();
		alipayEmailTV.setText(alipayEmail);
		//设置用户基本信息
		TextView username = (TextView) caller.findViewById(R.id.center_name);
		TextView email = (TextView) caller.findViewById(R.id.center_email);
		TextView readbi = (TextView) caller.findViewById(R.id.center_readbi);
		ImageView icon = (ImageView) caller.findViewById(R.id.center_icon);
		TextView uIdTextView = (TextView) caller.findViewById(R.id.center_uid);
		LinearLayout bindViewLayout = (LinearLayout) caller.findViewById(R.id.personcenter_bindview);
		LinearLayout bindTelLayout = (LinearLayout) caller.findViewById(R.id.personcenter_bindphone_layout);
		TextView phonetv = (TextView) caller.findViewById(R.id.center_phone);

		TextView unBindTextView = (TextView) caller.findViewById(R.id.personcenter_unbind_tv);
		LinearLayout unbindTelLayout = (LinearLayout) caller.findViewById(R.id.personcenter_unbindview);

		try {
			icon.setImageDrawable(Util.getDrawableFromCache(caller, aliPayUser.getOur_user_info().getLogo()));
			readbi.setText(aliPayUser.getOur_user_info().getRemain());
			phonetv.setText(aliPayUser.getOur_user_info().getTel());
			username.setText(aliPayUser.getOur_user_info().getUsername());
			email.setText(aliPayUser.getOur_user_info().getEmail());
			uIdTextView.setText(aliPayUser.getOur_user_info().getUid());
			readbi.setText(aliPayUser.getOur_user_info().getRemain());
			email.setText(aliPayUser.getOur_user_info().getEmail());
			//绑定手机号
			if (StringUtils.isNotBlank(aliPayUser.getOur_user_info().getMobile())) {
				phonetv.setText(aliPayUser.getOur_user_info().getMobile());
				bindTelLayout.setVisibility(View.VISIBLE);
				bindViewLayout.setVisibility(View.GONE);

				String unbindContent = String.format(caller.getString(R.string.person_center_bottom_unbind_tv), new Object[] { aliPayUser
						.getOur_user_info().getMobile() });
				unBindTextView.setText(Html.fromHtml(unbindContent));
				unbindTelLayout.setVisibility(View.VISIBLE);
			} else {
				bindTelLayout.setVisibility(View.GONE);
				bindViewLayout.setVisibility(View.VISIBLE);
				unbindTelLayout.setVisibility(View.GONE);
			}
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		}

	}

	@Override
	public AliPayUserBean doInBackground(Void... params) {

		AlipayAccessTokenKeeper tokenKeeper = AlipayAccessTokenKeeper.readAccessToken(caller);

		if (tokenKeeper.isSessionValid()) {

			return doGetPayUserInfo(tokenKeeper);
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

				return doGetPayUserInfo(tokenKeeper);
			}

		}

		return null;
	}

	private AliPayUserBean doGetPayUserInfo(AlipayAccessTokenKeeper tokenKeeper) {
		User user = BookApp.getUser();
		String token = Util.md5(tokenKeeper.getAccessToken() + Constants.PRIVATE_KEY).substring(0, 10);
		String url = String.format(Constants.INFO_ALIPAY_URL, new Object[] { token, tokenKeeper.getAccessToken(), user.getUid() });

		AliPayUserBean aliPayUser = HttpHelper.get(url, null, AliPayUserBean.class);
		LogUtils.info("AliPayUserSyncTask.doInBackground|" + url + "|" + aliPayUser.getOur_user_info().getMobile());

		return aliPayUser;
	}
}
