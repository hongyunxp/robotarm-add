package com.xs.cn.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.eastedge.readnovel.common.CloseActivity;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.common.LoadLayerProvider;
import com.readnovel.base.util.StringUtils;
import com.xs.cn.R;

/**
 * 公告
 * @author li.li
 *
 */
public class NoticeActivity extends Activity {
	//webview
	private WebView webView;

	//请求url
	private String url;

	private Button regButton;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CloseActivity.add(this);

		initParam();

		initView();

	}

	@Override
	protected void onStop() {
		super.onStop();
		LoadLayerProvider.getInstance().close();
		finish();
	}

	/**
	 * 初始化参数
	 */
	private void initParam() {
		url = getIntent().getStringExtra("url");
	}

	/**
	 * 初始化视图布局
	 */
	@SuppressLint({ "SetJavaScriptEnabled" })
	private void initView() {
		setContentView(R.layout.notice);

		setTopBar();

		regButton = (Button) findViewById(R.id.reg_button);
		webView = (WebView) findViewById(R.id.notice_webview);

		//登陆用户不显示注册按钮
		if (BookApp.getUser() != null)
			regButton.setVisibility(View.GONE);
		else
			regButton.setVisibility(View.VISIBLE);

		//加载公告内容
		webView.setWebViewClient(webViewClient);
		webView.setDownloadListener(new DownloadListener() {//支持下载
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}

		});

		WebSettings ws = webView.getSettings();
		ws.setSupportZoom(true); //可以缩放
		ws.setBuiltInZoomControls(true); //显示放大缩小 control
		ws.setUseWideViewPort(true); //让浏览器支持用户自定义view
		ws.setJavaScriptEnabled(true); //支持JavaScript
		webView.requestFocus(); //支持点击网页文本输入框时弹出软键盘等事件

		if (StringUtils.isNotBlank(url))
			webView.loadUrl(url); //设置加载URL

	}

	/**
	 * 设置标题顶部
	 */
	private void setTopBar() {
		Button left2 = (Button) findViewById(R.id.title_btn_left2);

		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText("大喜讯");

		left2.setVisibility(View.VISIBLE);
		left2.setText("返回");

		left2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 浏览器客户端
	 */
	private WebViewClient webViewClient = new WebViewClient() {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			LoadLayerProvider.getInstance().open();//打加载对话框
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			LoadLayerProvider.getInstance().close();//关闭加载对话框 
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);//点击连接从自身打开
			return true;
		}

	};

	/**
	 * 去注册页面
	 * @param view
	 */
	public void goToReg(View view) {
		//跳转到注册页面
		Intent intent = new Intent(this, RegistActivity.class);
		intent.putExtra("Tag", "readbook");
		startActivity(intent);
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		CloseActivity.remove(this);

		super.onDestroy();
	}

}
