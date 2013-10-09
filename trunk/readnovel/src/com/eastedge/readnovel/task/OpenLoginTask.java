package com.eastedge.readnovel.task;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.beans.orm.UserLoginTrace;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.Constants.LoginType;
import com.eastedge.readnovel.common.JsonToBean;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.db.orm.OrmDBHelper;
import com.eastedge.readnovel.utils.CommonUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.readnovel.base.db.orm.DBHelper;
import com.readnovel.base.http.HttpHelper;
import com.readnovel.base.openapi.QZoneAble;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;

/**
 * QQ登陆异步任务
 * 
 * @author li.li
 *
 * Mar 20, 2013
 */
public class OpenLoginTask extends EasyTask<QZoneAble, Void, Void, User> {
	private ProgressDialog pd;
	private String loginUrl;
	private String accessToken;
	private LoginType loginType;
	private OrmDBHelper dbHelper;

	public OpenLoginTask(QZoneAble caller, String loginUrl, String accessToken, LoginType loginType) {
		super(caller);

		this.loginUrl = loginUrl;
		this.accessToken = accessToken;
		this.loginType = loginType;

		dbHelper = DBHelper.getHelper(OrmDBHelper.class);
	}

	@Override
	public void onPreExecute() {
		//打加载对话框
		pd = ViewUtils.progressLoading(caller, "正在登陆中，请稍后...");
	}

	@Override
	public void onPostExecute(User user) {
		//关闭加载对话框
		pd.cancel();

		if (user == null)
			return;

		CommonUtils.openLoginCallBack(caller, user, loginType);

	}

	@Override
	public User doInBackground(Void... params) {

		String token = Util.md5(accessToken + Constants.PRIVATE_KEY).substring(0, 10);
		String url = String.format(loginUrl, accessToken, token);

		//在主站执行登陆并得到登陆结果
		String jsonStr = HttpHelper.get(url, null);

		User user = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			user = JsonToBean.JsonToUser(json);

			//登陆成功发分享
			if (user != null)
				share(user);

			try {
				Dao<UserLoginTrace, Integer> payIntevalDao = dbHelper.getDao(UserLoginTrace.class);
				if (user != null) {//加入更新用户登陆记录
					UserLoginTrace userLoginTrace = new UserLoginTrace();
					userLoginTrace.setUserId(Integer.parseInt(user.getUid()));
					userLoginTrace.setLoginType(loginType.getValue());
					userLoginTrace.setLastLoginTime(System.currentTimeMillis());

					payIntevalDao.createOrUpdate(userLoginTrace);
				}

			} catch (SQLException e) {
				LogUtils.error(e.getMessage(), e);
			}

		} catch (JSONException e) {
			LogUtils.error(e.getMessage(), e);
		}

		return user;
	}

	/**
	 * 分享
	 */
	private void share(User user) {
		//分享限制，一个用户半年一次(180天)
		try {
			if (user != null) {
				Dao<UserLoginTrace, Integer> payIntevalDao = dbHelper.getDao(UserLoginTrace.class);
				//				UserLoginTrace userLoginTrace = payIntevalDao.queryForId(Integer.parseInt(user.getUid()));
				//复合查询简写
				QueryBuilder<UserLoginTrace, Integer> queryBuilder = payIntevalDao.queryBuilder();
				List<UserLoginTrace> userLoginTraces = payIntevalDao.query(queryBuilder.where().eq(UserLoginTrace.USER_ID, user.getUid()).and()
						.eq(UserLoginTrace.LOGIN_TYPE, loginType.getValue()).prepare());
				//查询用户登陆记录
				if (userLoginTraces != null && !userLoginTraces.isEmpty())
					for (UserLoginTrace userLoginTrace : userLoginTraces) {
						if (userLoginTrace != null) {
							long lastLoginTime = userLoginTrace.getLastLoginTime();
							long nowTime = System.currentTimeMillis();
							if (nowTime - lastLoginTime < Constants.SHARE_AFTER_LOGIN_TIME) {
								LogUtils.info("不满足条件，不发分享");
								return;//同一用户登陆小于180天不分享
							} else
								LogUtils.info("满足条件，发分享");
						}
					}

			}

			if (LoginType.qq.equals(loginType)) {

				CommonUtils.shareForQQLogin(caller, caller.getString(R.string.login_share_content), Constants.OPEN_LOGIN_SHARE_IMG);

			} else if (LoginType.sina.equals(loginType)) {
				//登陆分享
				CommonUtils.shareForSinaLogin(caller, caller.getString(R.string.login_share_content), Constants.OPEN_LOGIN_SHARE_IMG);
				//登陆加好友
				CommonUtils.addFriendForSina(caller, Constants.OPEN_SINA_ADD_FRIEND_ID, Constants.OPEN_SINA_ADD_FRIEND_NAME);

			}
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		}

	}

	//	/**
	//	 * 支付宝登陆
	//	 */
	//	private User aliPayLogin() {
	//		String token = Util.md5(accessToken + Constants.PRIVATE_KEY).substring(0, 10);
	//		String url = String.format(Constants.ALIPAY_LOGIN_URL, accessToken, token);
	//
	//		//在主站执行登陆并得到登陆结果
	//		String jsonStr = HttpHelper.get(url, null);
	//
	//		User user = null;
	//		try {
	//			JSONObject json = new JSONObject(jsonStr);
	//			user = JsonToBean.JsonToUser(json);
	//
	//			try {
	//				Dao<UserLoginTrace, Integer> payIntevalDao = dbHelper.getDao(UserLoginTrace.class);
	//				if (user != null) {//加入更新用户登陆记录
	//					UserLoginTrace userLoginTrace = new UserLoginTrace();
	//					userLoginTrace.setUserId(Integer.parseInt(user.getUid()));
	//					userLoginTrace.setLoginType(loginType.getValue());
	//					userLoginTrace.setLastLoginTime(System.currentTimeMillis());
	//
	//					payIntevalDao.createOrUpdate(userLoginTrace);
	//				}
	//
	//			} catch (SQLException e) {
	//				LogUtils.error(e.getMessage(), e);
	//			}
	//
	//		} catch (JSONException e) {
	//			LogUtils.error(e.getMessage(), e);
	//		}
	//
	//		return user;
	//	}
}
