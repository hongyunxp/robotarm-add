package com.eastedge.readnovel.task;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.eastedge.readnovel.beans.Shubenxinxiye;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.Constants.LoginType;
import com.eastedge.readnovel.common.Util;
import com.readnovel.base.openapi.QQAccessTokenKeeper;
import com.readnovel.base.openapi.QZoneAble;
import com.readnovel.base.openapi.TencentAPI;
import com.readnovel.base.openapi.TencentAPI.BaseApiListener;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.http.HttpImpl;

/**
 * QQ登陆异步任务
 * @author li.li
 *
 * Mar 13, 2013
 */
public class QQShareTask extends EasyTask<QZoneAble, Void, String, TencentAPI> {
	private String shareContent;
	private String imageUrl;
	private String bookId;
	private boolean isLogin;

	public QQShareTask(QZoneAble caller, String shareContent, String bookId) {
		super(caller);

		this.shareContent = shareContent;
		this.bookId = bookId;
	}

	public QQShareTask(QZoneAble caller, String shareContent) {
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
	public TencentAPI doInBackground(Void... params) {

		//得到书的明细
		if (bookId != null) {
			Shubenxinxiye bookDetail = HttpImpl.Shubenxinxiye(bookId);
			if (bookDetail != null)
				imageUrl = bookDetail.getBook_logo();
		}

		TencentAPI tencentAPI = TencentAPI.getInstance(caller, Constants.QQ_APP_ID);
		caller.setTencentAPI(tencentAPI);

		return tencentAPI;
	}

	@Override
	public void onPostExecute(TencentAPI tencentAPI) {

		if (StringUtils.isNotBlank(imageUrl)) {
			isLogin = tencentAPI.share(shareContent, Constants.OPEN_SHARE_URL_LOCATION, Constants.OPEN_SHARE_URL_NAME,
					Constants.OPEN_SHARE_URL_LOCATION, imageUrl, new BaseApiListener() {

						@Override
						public void doComplete(JSONObject response, Object state) {

							try {
								if (response != null && !response.isNull("msg") && response.getString("msg").equals("ok")) {
									publishProgress("QQ分享成功");
									if (!isLogin || BookApp.getUser() == null) //未登陆,执行登陆
										logIn();
								}

								else {
									publishProgress("QQ分享失败");
									LogUtils.info(response.toString());
								}

							} catch (JSONException e) {
								publishProgress("QQ分享失败");
								LogUtils.error(e.getMessage(), e);
							}
						}

					});

		}

	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	private void logIn() {

		QQAccessTokenKeeper accessToken = QQAccessTokenKeeper.readAccessToken(caller);
		String token = Util.md5(accessToken.getmAccessToken() + Constants.PRIVATE_KEY).substring(0, 10);
		String loginUrl = String.format(Constants.QQ_LOGIN_URL, accessToken.getmAccessToken(), token);
		//执行登陆
		new OpenLoginTask(caller, loginUrl, accessToken.getmAccessToken(), LoginType.qq).execute();
	}

}
