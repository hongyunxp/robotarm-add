package com.xs.cn.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.JavaScript;
import com.eastedge.readnovel.common.SixbtnClickListener;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;

public class ConsumeCenterForBaoYue extends Activity implements OnClickListener {
	private RelativeLayout rl;
	private Button left1;
	private TextView topTv;
	private Button phone, sms, czk, zfb, qb, wy;
	private LinearLayout ll;

	private ProgressDialog pd;
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.consume_baoyue);
		CloseActivity.add(this);

		setTopBar();

		initView();

	}

	/**
	 * 初始化视图布局
	 */
	private void initView() {
		ll = (LinearLayout) findViewById(R.id.consume_sixbtn);
		phone = (Button) ll.findViewById(R.id.consume_phone);
		phone.setBackgroundResource(R.drawable.consume2);
		phone.setTextColor(Color.WHITE);

		SixbtnClickListener mlistener = new SixbtnClickListener(this);
		sms = (Button) ll.findViewById(R.id.consume_sms);
		sms.setOnClickListener(mlistener);
		czk = (Button) ll.findViewById(R.id.consume_czk);
		czk.setOnClickListener(mlistener);
		zfb = (Button) ll.findViewById(R.id.consume_zfb);
		zfb.setOnClickListener(mlistener);
		qb = (Button) ll.findViewById(R.id.consume_qb);
		qb.setOnClickListener(mlistener);
		wy = (Button) ll.findViewById(R.id.consume_wy);
		wy.setOnClickListener(mlistener);

		//init webview
		mWebView = (WebView) findViewById(R.id.cz_webview);
		mWebView.requestFocus(); //支持点击网页文本输入框时弹出软键盘等事件

		//配置WebView
		WebSettings ws = mWebView.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setBuiltInZoomControls(true);
		ws.setUseWideViewPort(true); //让浏览器支持用户自定义view
		ws.setAppCacheEnabled(true);//使用缓存

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

				pd = ViewUtils.progressLoading(ConsumeCenterForBaoYue.this);
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
				//不处理点击电话号码
				if (url == null || url.contains("tel"))
					return true;

				view.loadUrl(url);//点击连接从自身打开
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);

				//				mWebView.clearHistory();//清除历史记录

				//去包月预定购页面
				goPreOrder();

				ViewUtils.confirm(ConsumeCenterForBaoYue.this, "网络延时，请点击按钮重试！", "重试", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						User user = BookApp.getUser();

						if (user != null) {//包月预订购

							mWebView.clearHistory();//清除历史记录

							// 预定购不使用缓存
							// 设置缓存模式-不使用缓存
							mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

							String url = String.format(Constants.PAY_MONTH_ORDER, user.getUid(), user.getToken(), user.getTel());
							mWebView.loadUrl(url);
						}
					}

				});
			}

		});

		//支持js alert
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

				ViewUtils.showDialog(ConsumeCenterForBaoYue.this, "温馨提示", message, new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						result.confirm();
					}
				});

				return true;
			}
		});

		//去包月预定购页面
		goPreOrder();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		CloseActivity.curActivity = this;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.title_btn_left2)
			goBack();
	}

	/**
	 * 去包月主页面
	 */
	private void goPreOrder() {
		User user = BookApp.getUser();

		if (user != null) {//包月主页面
			String url = String.format(Constants.PAY_MONTH_PRE_ORDER, user.getUid(), user.getToken());
			// 设置缓存模式-缓存优先
			mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			mWebView.loadUrl(url);
		}
	}

	private void setTopBar() {
		rl = (RelativeLayout) findViewById(R.id.consume_top);
		left1 = (Button) rl.findViewById(R.id.title_btn_left2);
		topTv = (TextView) rl.findViewById(R.id.title_tv);
		left1.setText("  返回");
		left1.setVisibility(View.VISIBLE);

		left1.setOnClickListener(this);

		topTv.setText("充值中心");
	}

	private void goBack() {
		if (mWebView != null && mWebView.canGoBack())
			mWebView.goBack();
		else
			finish();
	}

}
