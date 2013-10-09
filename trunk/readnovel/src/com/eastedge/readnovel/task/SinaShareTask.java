package com.eastedge.readnovel.task;

import java.io.IOException;

import android.widget.Toast;

import com.eastedge.readnovel.beans.Shubenxinxiye;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.Constants.LoginType;
import com.eastedge.readnovel.common.Util;
import com.readnovel.base.openapi.QZoneAble;
import com.readnovel.base.openapi.SinaAPI;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.ViewUtils;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.http.HttpImpl;

/**
 * 新浪登陆异步任务
 * @author li.li
 *
 * Mar 13, 2013
 */
public class SinaShareTask extends EasyTask<QZoneAble, Void, String, SinaAPI> {
	private String shareContent;
	private String imageUrl;
	private String bookId;
	private boolean isLogin;

	public SinaShareTask(QZoneAble caller, String shareContent, String bookId) {
		super(caller);

		this.shareContent = shareContent;
		this.bookId = bookId;
	}

	public SinaShareTask(QZoneAble caller, String shareContent) {
		super(caller);

		this.shareContent = shareContent;
	}

	@Override
	public void onPreExecute() {

	}

	@Override
	public void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);

		if (!caller.isFinishing())
			for (String msg : values) {
				//提示对话框
				ViewUtils.toastDialog(caller, msg, Toast.LENGTH_SHORT);
			}

	}

	@Override
	public SinaAPI doInBackground(Void... params) {

		//得到书的明细
		if (bookId != null) {
			Shubenxinxiye bookDetail = HttpImpl.Shubenxinxiye(bookId);
			if (bookDetail != null) {
				imageUrl = bookDetail.getBook_logo();
			}
		}

		SinaAPI sinaAPI = SinaAPI.getInstance(caller, Constants.CONSUMER_KEY, Constants.REDIRECT_URL);

		return sinaAPI;
	}

	@Override
	public void onPostExecute(SinaAPI sinaAPI) {

		isLogin = sinaAPI.share(shareContent, imageUrl, new RequestListener() {

			@Override
			public void onComplete(String response) {
				publishProgress("新浪微博分享成功");

				if (!isLogin || BookApp.getUser() == null) //未登陆,执行登陆
					logIn();
			}

			@Override
			public void onIOException(IOException e) {
				publishProgress("新浪微博分享失败");
				LogUtils.error(e.getMessage(), e);
			}

			@Override
			public void onError(WeiboException e) {
				String errorMsg = "";

				if (e.getMessage().contains("repeat content"))
					errorMsg += "内容已分享过";

				publishProgress(errorMsg);

				LogUtils.error(e.getMessage(), e);
			}

		});
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	private void logIn() {

		Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(caller);
		String token = Util.md5(accessToken.getToken() + Constants.PRIVATE_KEY).substring(0, 10);
		String loginUrl = String.format(Constants.SINA_LOGIN_URL, accessToken.getToken(), token);

		//执行登陆
		new OpenLoginTask(caller, loginUrl, accessToken.getToken(), LoginType.sina).execute();
	}

}
