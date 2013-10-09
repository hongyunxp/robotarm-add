package com.xs.cn.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.JavaScript;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;

public class ConsumeOnlyZfb extends Activity implements OnClickListener {
	private ProgressDialog pd;
	private WebView mWebView;
	private String uid;
	private double fee;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.consume_only_zfb);
		CloseActivity.add(this);

		setTopBar();

		initParam();

		initView();

	}

	private void setTopBar() {
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.consume_top);
		Button left1 = (Button) rl.findViewById(R.id.title_btn_left2);
		TextView topTv = (TextView) rl.findViewById(R.id.title_tv);
		left1.setText("  返回");
		left1.setVisibility(View.VISIBLE);
		left1.setOnClickListener(this);

		topTv.setText("支付宝充值");
	}

	/**
	 * 初始化参数
	 */
	private void initParam() {
		uid = getIntent().getStringExtra("uid");
		fee = getIntent().getDoubleExtra("fee", 0);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.title_btn_left2) {
			finish();
		}
	}

	/**
	 * 初始化视图布局
	 */
	private void initView() {

		//init webview
		mWebView = (WebView) findViewById(R.id.cz_webview);
		//配置WebView
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

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
				pd = ViewUtils.progressLoading(ConsumeOnlyZfb.this);
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

		});

		mWebView.loadUrl(String.format(Constants.ALIPAY_APP_WEP_URL, uid, fee));
	}
	
	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}
}
