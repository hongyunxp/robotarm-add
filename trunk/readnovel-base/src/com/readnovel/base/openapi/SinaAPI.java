package com.readnovel.base.openapi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.readnovel.base.R;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;

/**
 * 新浪API
 * @author li.li
 *
 * Mar 12, 2013
 */
public class SinaAPI {
	private Weibo mWeibo;
	private Context ctx;

	private SinaAPI(Context ctx, String appKey, String redirectUrl) {
		this.ctx = ctx;
		mWeibo = Weibo.getInstance(appKey, redirectUrl);
	}

	public static SinaAPI getInstance(Activity act, String appKey, String redirectUrl) {

		return new SinaAPI(act, appKey, redirectUrl);
	}

	/**
	 * 登陆
	 * @param weiboAuthListener
	 */
	public void login(final LoginListener loginListener) {
		mWeibo.authorize(ctx, new WeiboAuthListener() {

			@Override
			public void onComplete(Bundle values) {
				String accessToken = values.getString("access_token");
				String expiresIn = values.getString("expires_in");

				Oauth2AccessToken token = new Oauth2AccessToken(accessToken, expiresIn);
				//持久化token
				AccessTokenKeeper.keepAccessToken(ctx, token);

				//回调
				loginListener.onComplete(accessToken);
			}

			@Override
			public void onWeiboException(WeiboException e) {
				ViewUtils.showDialog(ctx, ctx.getString(R.string.common_dialog_title), "新浪微博登陆失败" + e.getMessage(), null);
				LogUtils.error(e.getMessage(), e);
			}

			@Override
			public void onError(WeiboDialogError e) {
				ViewUtils.showDialog(ctx, ctx.getString(R.string.common_dialog_title), "新浪微博登陆失败" + e.getMessage(), null);
				LogUtils.error(e.getMessage(), e);
			}

			@Override
			public void onCancel() {
				ViewUtils.showDialog(ctx, ctx.getString(R.string.common_dialog_title), "新浪微博登陆取消", null);
			}
		});
	}

	/**
	 * 分享
	 * @param content
	 * @param requestListener
	 */
	public boolean share(final String content, final String imageUrl, final RequestListener requestListener) {
		Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(ctx);

		if (token.isSessionValid()) {
			StatusesAPI statusesApi = new StatusesAPI(token);//分享

			if (StringUtils.isNotBlank(imageUrl))
				statusesApi.uploadUrlText(content, imageUrl, "0.0", "0.0", requestListener);
			else {
				statusesApi.update(content, "0.0", "0.0", requestListener);
			}

			return true;

		} else {
			login(new LoginListener() {

				@Override
				public void onComplete(String accessToken) {
					share(content, imageUrl, requestListener);
				}

			});

			return false;
		}

	}

	/**
	 * 加好友
	 * @param uid
	 * @param name
	 * @param listener
	 */
	public void addFriend(final long uid, final String name, final RequestListener listener) {
		Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(ctx);

		if (token.isSessionValid()) {
			FriendshipsAPI friendshipsAPI = new FriendshipsAPI(token);
			friendshipsAPI.create(uid, name, listener);

		} else {
			login(new LoginListener() {

				@Override
				public void onComplete(String accessToken) {
					addFriend(uid, name, listener);
				}

			});
		}
	}

	/**
	 * 登陆状态是否有效
	 * @param act
	 * @return
	 */
	public static boolean isSessionValid(Activity act) {
		Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(act);

		return token.isSessionValid();
	}
}
