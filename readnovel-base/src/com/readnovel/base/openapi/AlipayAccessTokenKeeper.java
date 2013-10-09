package com.readnovel.base.openapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.readnovel.base.util.StringUtils;

/**
 * 支付宝登陆token维护器
 * @author li.li
 *
 * Jul 18, 2013
 */
public class AlipayAccessTokenKeeper {
	private static final String PREFERENCES_NAME = "com.alipay.open";

	private String accessToken;//短token
	private long accessTokenExpiresTime;
	private String refreshToken;
	private long refreshTokenExpiresTime;

	public AlipayAccessTokenKeeper(String accessToken, long accessTokenExpiresTime, String refreshToken, long refreshTokenExpiresTime) {

		this.accessToken = accessToken;
		this.accessTokenExpiresTime = System.currentTimeMillis() + accessTokenExpiresTime * 1000;

		this.refreshToken = refreshToken;
		this.refreshTokenExpiresTime = System.currentTimeMillis() + refreshTokenExpiresTime * 1000;
	}

	/**
	 * 当前token是否有效
	 * @return
	 */
	public boolean isSessionValid() {
		boolean v1 = (!StringUtils.isBlank(accessToken) && (accessTokenExpiresTime == 0 || (System.currentTimeMillis() < accessTokenExpiresTime)));
		boolean v2 = (!StringUtils.isBlank(refreshToken) && (refreshTokenExpiresTime == 0 || (System.currentTimeMillis() < refreshTokenExpiresTime)));

		return v1 && v2;
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
	 * 保存accesstoken到SharedPreferences
	 * @param context Activity 上下文环境
	 * @param token Oauth2AccessToken
	 */
	public static void keepAccessToken(Context context, String accessToken, String accessTokenExpiresTime, String refreshToken,
			String refreshTokenExpiresTime) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("accessToken", accessToken);

		if (accessTokenExpiresTime != null)
			editor.putLong("accessTokenExpiresTime", Long.parseLong(accessTokenExpiresTime));
		editor.putString("refreshToken", refreshToken);

		if (refreshTokenExpiresTime != null)
			editor.putLong("refreshTokenExpiresTime", Long.parseLong(refreshTokenExpiresTime));
		editor.commit();
	}

	/**
	 * 从SharedPreferences读取accessstoken
	 * @param context
	 * @return Oauth2AccessToken
	 */
	public static AlipayAccessTokenKeeper readAccessToken(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);

		String accessToken = pref.getString("accessToken", "");
		long accessTokenExpiresTime = pref.getLong("accessTokenExpiresTime", 0);
		String refreshToken = pref.getString("refreshToken", "");
		long refreshTokenExpiresTime = pref.getLong("refreshTokenExpiresTime", 0);

		AlipayAccessTokenKeeper token = new AlipayAccessTokenKeeper(accessToken, accessTokenExpiresTime, refreshToken, refreshTokenExpiresTime);

		return token;
	}

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @return the accessTokenExpiresTime
	 */
	public long getAccessTokenExpiresTime() {
		return accessTokenExpiresTime;
	}

	/**
	 * @param accessTokenExpiresTime the accessTokenExpiresTime to set
	 */
	public void setAccessTokenExpiresTime(long accessTokenExpiresTime) {
		this.accessTokenExpiresTime = accessTokenExpiresTime;
	}

	/**
	 * @return the refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * @param refreshToken the refreshToken to set
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * @return the refreshTokenExpiresTime
	 */
	public long getRefreshTokenExpiresTime() {
		return refreshTokenExpiresTime;
	}

	/**
	 * @param refreshTokenExpiresTime the refreshTokenExpiresTime to set
	 */
	public void setRefreshTokenExpiresTime(long refreshTokenExpiresTime) {
		this.refreshTokenExpiresTime = refreshTokenExpiresTime;
	}

}
