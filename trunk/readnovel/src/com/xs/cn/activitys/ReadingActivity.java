package com.xs.cn.activitys;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.JavaScript;
import com.eastedge.readnovel.fragment.NetworkNotAvailableFragment;
import com.readnovel.base.common.NetType;
import com.readnovel.base.util.NetUtils;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;

public class ReadingActivity extends FragmentActivity {
	private ProgressDialog pd;
	private WebView mWebView;
	private String url;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.reading);
		CloseActivity.add(this);

		url = getIntent().getStringExtra("url");

		mWebView = (WebView) findViewById(R.id.reading_webview);
		//配置WebView
		WebSettings ws = mWebView.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setBuiltInZoomControls(false);
		ws.setUseWideViewPort(true); //让浏览器支持用户自定义view

		if (NetType.TYPE_NONE.equals(NetUtils.checkNet())) {
			ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		} else {
			ws.setCacheMode(WebSettings.LOAD_DEFAULT);
		}

		//映射Java对象到一个名为”xs“的Javascript对象上
		//JavaScript中可以通过"window.xs"来调用Java对象的方法
		mWebView.addJavascriptInterface(JavaScript.newInstance(this, mWebView), JavaScript.NAME);

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				//打加载对话框
				if (pd != null)
					pd.cancel();
				pd = ViewUtils.progressLoading(ReadingActivity.this);
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
				view.loadUrl(url);//点击连接从自身打开
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {//网络错误
				mWebView.setVisibility(View.GONE);
				getSupportFragmentManager()//
						.beginTransaction()//
						.replace(R.id.reading, new NetworkNotAvailableFragment())//
						.commit();

			}

		});

		loadData();
	}

	public void goBack(View view) {
		finish();
	}

	public void retry(View view) {
		loadData();
	}

	private void loadData() {

		if (StringUtils.isNotBlank(url))
			mWebView.loadUrl(url);
	}
}
