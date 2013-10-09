package com.xs.cn.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.ConstantEvents;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.JavaScript;
import com.readnovel.base.common.NetType;
import com.readnovel.base.util.EventLogUtils;
import com.readnovel.base.util.NetUtils;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;

/**
 * 应用推荐
 * @author li.li
 *
 * Apr 9, 2013
 */
public class SettingAppActivity extends Activity {
	private WebView mWebView;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_app);
		CloseActivity.add(this);

		if (NetUtils.checkNet().equals(NetType.TYPE_NONE)) {
			ViewUtils.showDialog(this, "温馨提示", "网络不给力！", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}

			});
		}

		initView();
	}

	/**
	 * 初始化视图布局
	 */
	private void initView() {
		//init webview
		mWebView = (WebView) findViewById(R.id.app_webview);
		//配置WebView
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setBuiltInZoomControls(false);

		//映射Java对象到一个名为”xs“的Javascript对象上
		//JavaScript中可以通过"window.xs"来调用Java对象的方法
		mWebView.addJavascriptInterface(JavaScript.newInstance(this, mWebView), JavaScript.NAME);
		mWebView.setDownloadListener(new DownloadListener() {//支持下载
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}

		});

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				//打加载对话框
				if (pd != null)
					pd.cancel();
				pd = ViewUtils.progressLoading(SettingAppActivity.this);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				//关闭加载对话框 
				if (pd != null)
					pd.cancel();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (StringUtils.isBlank(url))
					return true;

				EventLogUtils.sendEventLog(SettingAppActivity.this, ConstantEvents.SETTING_APP_CLICK_EVENT, url);

				view.loadUrl(url);//点击连接从自身打开
				return true;
			}

		});

		mWebView.loadUrl(Constants.SETTING_APP_URL);
	}

	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}

}
