package com.eastedge.readnovel.task;

import org.json.JSONObject;

import android.app.Activity;

import com.eastedge.readnovel.beans.AliPayLoginBean;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.beans.orm.UserLoginTrace;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.Constants.LoginType;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.db.orm.OrmDBHelper;
import com.eastedge.readnovel.utils.CommonUtils;
import com.j256.ormlite.dao.Dao;
import com.readnovel.base.db.orm.DBHelper;
import com.readnovel.base.http.HttpHelper;
import com.readnovel.base.openapi.AlipayAccessTokenKeeper;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.JsonUtils;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.activitys.BookApp;

public class AliPayCallTask extends EasyTask<Activity, Void, Void, User> {
	private LoginType loginType;
	private OrmDBHelper dbHelper;
	private String alipayUserId;
	private String authCode;

	public AliPayCallTask(Activity caller, String alipayUserId, String authCode, LoginType loginType) {
		super(caller);

		this.loginType = loginType;
		this.alipayUserId = alipayUserId;
		this.authCode = authCode;

		dbHelper = DBHelper.getHelper(OrmDBHelper.class);
	}

	@Override
	public void onPreExecute() {
	}

	@Override
	public void onPostExecute(User user) {

		if (user == null)
			return;

		CommonUtils.openLoginCallBack(caller, user, loginType);

	}

	@Override
	public User doInBackground(Void... params) {

		User user = aliPayLogin();

		return user;
	}

	/**
	 * 支付宝登陆
	 */
	private User aliPayLogin() {
		User user = BookApp.getUser();

		String token = Util.md5(alipayUserId + Constants.PRIVATE_KEY).substring(0, 10);

		String userId = null;//当前用户id

		if (user != null)
			userId = user.getUid();//得到当前登陆用户uid

		String url = String.format(Constants.ALIPAY_LOGIN_URL, alipayUserId, userId, token, authCode);
		//在主站执行登陆并得到登陆结果
		String jsonStr = HttpHelper.get(url, null);
		LogUtils.info(url + "|" + jsonStr);

		try {
			JSONObject json = new JSONObject(jsonStr);
			AliPayLoginBean aliPayLoginBean = JsonUtils.fromJson(json.toString(), AliPayLoginBean.class);

			AlipayAccessTokenKeeper.keepAccessToken(caller, aliPayLoginBean.getAccess_token(), aliPayLoginBean.getAccess_token_expires_in(),
					aliPayLoginBean.getRefresh_token(), aliPayLoginBean.getRefresh_token_expires_in());

			user = aliPayLoginBean;

			Dao<UserLoginTrace, Integer> payIntevalDao = dbHelper.getDao(UserLoginTrace.class);
			if (user != null) {//加入更新用户登陆记录
				UserLoginTrace userLoginTrace = new UserLoginTrace();
				userLoginTrace.setUserId(Integer.parseInt(user.getUid()));
				userLoginTrace.setLoginType(loginType.getValue());
				userLoginTrace.setLastLoginTime(System.currentTimeMillis());

				payIntevalDao.createOrUpdate(userLoginTrace);
			}

		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		}

		return user;
	}
}
