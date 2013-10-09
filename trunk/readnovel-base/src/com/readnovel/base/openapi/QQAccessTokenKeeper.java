package com.readnovel.base.openapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class QQAccessTokenKeeper {
	private static final String PREFERENCES_NAME = "com.tencent.open";

	private String mAccessToken;
	private long mExpiresTime;
	private String openId;

	public QQAccessTokenKeeper(String mAccessToken, String mExpiresTime, String openId) {
		this.mAccessToken = mAccessToken;
		this.mExpiresTime = System.currentTimeMillis() + Long.parseLong(mExpiresTime) * 1000;
		this.openId = openId;
	}

	/**
	 * 保存accesstoken到SharedPreferences
	 * @param context Activity 上下文环境
	 * @param token Oauth2AccessToken
	 */
	public static void keepAccessToken(Context context, QQAccessTokenKeeper token) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("token", token.getmAccessToken());
		editor.putLong("expiresTime", token.getmExpiresTime());
		editor.putString("openId", token.getOpenId());
		editor.commit();
	}

	public boolean isSessionValid() {
		return (openId != null && !TextUtils.isEmpty(mAccessToken) && (mExpiresTime == 0 || (System.currentTimeMillis() < mExpiresTime)));
	}

	/**
	 * 清空sharepreference
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 从SharedPreferences读取accessstoken
	 * @param context
	 * @return Oauth2AccessToken
	 */
	public static QQAccessTokenKeeper readAccessToken(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);

		String accessToken = pref.getString("token", "");
		long expiresTime = pref.getLong("expiresTime", 0);
		String openId = pref.getString("openId", "");

		QQAccessTokenKeeper token = new QQAccessTokenKeeper(accessToken, String.valueOf(expiresTime), openId);

		return token;
	}

	public String getmAccessToken() {
		return mAccessToken;
	}

	public long getmExpiresTime() {
		return mExpiresTime;
	}

	public String getOpenId() {
		return openId;
	}

	public void setmAccessToken(String mAccessToken) {
		this.mAccessToken = mAccessToken;
	}

	public void setmExpiresTime(long mExpiresTime) {
		this.mExpiresTime = mExpiresTime;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

}
