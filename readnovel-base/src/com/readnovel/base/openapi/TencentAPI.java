package com.readnovel.base.openapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.readnovel.base.util.LogUtils;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 腾讯API
 * @author li.li
 *
 * Mar 26, 2013
 */
public class TencentAPI {
	private static volatile TencentAPI instance;
	private static final String SCOPE = "all";

	private Tencent mTencent;
	private Activity act;

	private TencentAPI(Activity act, String appId) {
		this.mTencent = Tencent.createInstance(appId, act);
		this.act = act;
	}

	public static TencentAPI getInstance(Activity act, String appId) {

		return new TencentAPI(act, appId);
	}

	public static TencentAPI getInstance() {
		return instance;
	}

	/**
	 * 登陆
	 * @param listener
	 */
	public void login(final LoginListener loginListener) {
		mTencent.login(act, SCOPE, new BaseUiListener() {

			@Override
			public void doComplete(JSONObject values) {
				//{"openid":"396CABC9ED22C6761A97C41D00E37DF8","expires_in":"7776000","access_token":"E986827BBAB31114D7E4A39D575DCC25"}
				try {
					String accessToken = values.getString("access_token");
					String expiresIn = values.getString("expires_in");
					String openId = values.getString("openid");

					QQAccessTokenKeeper token = new QQAccessTokenKeeper(accessToken, expiresIn, openId);
					QQAccessTokenKeeper.keepAccessToken(act, token);//持久化token

					loginListener.onComplete(accessToken);
				} catch (JSONException e) {
					LogUtils.error(e.getMessage(), e);
				}

			}

		});

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 分享
	 * @param title 标题
	 * @param titleUrl 标题连接
	 * @param site 网站
	 * @param siteUrl 网站连接
	 * @param listener 
	 * @return true 已登陆，false未登陆
	 */
	public boolean share(final String title, final String titleUrl, final String site, final String siteUrl, final String imageUrl,
			final BaseApiListener sharelistener) {

		QQAccessTokenKeeper token = QQAccessTokenKeeper.readAccessToken(act);

		if (token.isSessionValid()) {
			//初始化登陆信息
			mTencent.setOpenId(token.getOpenId());//设置openId
			mTencent.setAccessToken(token.getmAccessToken(), String.valueOf(token.getmExpiresTime()));//设置token，expiresTime

			Bundle parmas = new Bundle();
			parmas.putString(Constants.PARAM_TITLE, title);// 必须。feeds的标题，最长36个中文字，超出部分会被截断。
			parmas.putString(Constants.PARAM_URL, titleUrl);// 必须。分享所在网页资源的链接，点击后跳转至第三方网页，请以http://开头。
			parmas.putString("site", site);
			parmas.putString("fromurl", siteUrl);
			parmas.putString("images", imageUrl);

			mTencent.requestAsync(Constants.GRAPH_ADD_SHARE, parmas, Constants.HTTP_POST, sharelistener, null);

			return true;
		} else {
			login(new LoginListener() {

				@Override
				public void onComplete(String accessToken) {
					share(title, titleUrl, site, siteUrl, imageUrl, sharelistener);
				}

			});

			return false;
		}

	}

	/**
	 * 登陆状态是否有效
	 * @param act
	 * @return
	 */
	public static boolean isSessionValid(Activity act) {
		QQAccessTokenKeeper token = QQAccessTokenKeeper.readAccessToken(act);

		return token.isSessionValid();
	}

	/**
	 * UI回调
	 */
	public static abstract class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(JSONObject response) {
			LogUtils.info("BaseUiListener.onComplete:" + response.toString());
			doComplete(response);
		}

		protected abstract void doComplete(JSONObject values);

		@Override
		public void onError(UiError e) {
			LogUtils.error(e.errorMessage, new Exception(e.errorDetail));
		}

		@Override
		public void onCancel() {
			LogUtils.info("onCancel");
		}
	}

	/**
	 * 请求回调
	 */
	public static abstract class BaseApiListener implements IRequestListener {

		@Override
		public void onComplete(final JSONObject response, Object state) {
			LogUtils.info("IRequestListener.onComplete:" + response.toString());
			doComplete(response, state);
		}

		public abstract void doComplete(JSONObject response, Object state);

		@Override
		public void onIOException(final IOException e, Object state) {
			LogUtils.info("IRequestListener.onIOException:" + e.getMessage());
		}

		@Override
		public void onMalformedURLException(final MalformedURLException e, Object state) {
			LogUtils.info("IRequestListener.onMalformedURLException" + e.toString());
		}

		@Override
		public void onJSONException(final JSONException e, Object state) {
			LogUtils.info("IRequestListener.onJSONException:" + e.getMessage());
		}

		@Override
		public void onConnectTimeoutException(ConnectTimeoutException arg0, Object arg1) {
			LogUtils.info("IRequestListener.onConnectTimeoutException:" + arg0.getMessage());

		}

		@Override
		public void onSocketTimeoutException(SocketTimeoutException arg0, Object arg1) {
			LogUtils.info("IRequestListener.SocketTimeoutException:" + arg0.getMessage());
		}

		@Override
		public void onUnknowException(Exception arg0, Object arg1) {
			LogUtils.info("IRequestListener.onUnknowException:" + arg0.getMessage());
		}

		@Override
		public void onHttpStatusException(HttpStatusException arg0, Object arg1) {
			LogUtils.info("IRequestListener.HttpStatusException:" + arg0.getMessage());
		}

		@Override
		public void onNetworkUnavailableException(NetworkUnavailableException arg0, Object arg1) {
			LogUtils.info("IRequestListener.onNetworkUnavailableException:" + arg0.getMessage());
		}
	}

	public Tencent getmTencent() {
		return mTencent;
	}

}
