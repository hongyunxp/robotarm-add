package com.xs.cn.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.JavaScript;
import com.eastedge.readnovel.common.SixbtnClickListener;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;

public class ConsumeWyWap extends Activity implements OnClickListener {
	private ProgressDialog pd;
	private WebView mWebView;
	private String uid;
	private double fee;
	private String tag;
	private Boolean isXXk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cz_wy_webview);
		CloseActivity.add(this);
		setTopBar();

		initParam();

		initView();
	}

	/**
	 * 初始化视图布局
	 */
	private void initView() {
		//init tab
		LinearLayout ll = (LinearLayout) findViewById(R.id.consume_sixbtn);
		SixbtnClickListener mlistener = new SixbtnClickListener(this);
		Button sms = (Button) ll.findViewById(R.id.consume_sms);
		sms.setOnClickListener(mlistener);
		Button phone = (Button) ll.findViewById(R.id.consume_phone);
		phone.setOnClickListener(mlistener);
		Button czk = (Button) ll.findViewById(R.id.consume_czk);
		czk.setOnClickListener(mlistener);
		Button zfb = (Button) ll.findViewById(R.id.consume_zfb);
		zfb.setOnClickListener(mlistener);
		Button qb = (Button) ll.findViewById(R.id.consume_qb);
		qb.setOnClickListener(mlistener);
		Button wy = (Button) ll.findViewById(R.id.consume_wy);
		wy.setOnClickListener(mlistener);

		if ("wy".equals(tag)) {//网银
			wy = (Button) ll.findViewById(R.id.consume_wy);
			wy.setBackgroundResource(R.drawable.consume2);
			wy.setTextColor(Color.WHITE);
		} else {//支付宝
			zfb = (Button) ll.findViewById(R.id.consume_zfb);
			zfb.setBackgroundResource(R.drawable.consume2);
			zfb.setTextColor(Color.WHITE);
		}

		//init webview
		mWebView = (WebView) findViewById(R.id.cz_wy_webview);
		//配置WebView
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setBuiltInZoomControls(true);

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
				pd = ViewUtils.progressLoading(ConsumeWyWap.this);
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

		if (fee < Constants.CONSUME_PAY_MIN) {
			Toast.makeText(this, "最低充值限额" + Constants.CONSUME_PAY_MIN + "元", Toast.LENGTH_LONG).show();
			finish();
			return;//非法
		}

		if (StringUtils.isBlank(uid)) {
			Toast.makeText(this, "请登陆后充值" + Constants.CONSUME_PAY_MIN + "元", Toast.LENGTH_LONG).show();
			finish();
			return;//非法
		}
		if (isXXk) {
			mWebView.loadUrl(String.format(Constants.WY_XXK_WAP_URL, uid, fee));
		} else {
			mWebView.loadUrl(String.format(Constants.WY_CCK_WAP_URL, uid, fee));
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setTopBar() {
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.consume_top);
		Button left1 = (Button) rl.findViewById(R.id.title_btn_left2);
		TextView topTv = (TextView) rl.findViewById(R.id.title_tv);
		left1.setText("  返回");
		left1.setVisibility(View.VISIBLE);

		left1.setOnClickListener(this);

		topTv.setText("充值中心");
	}

	/**
	 * 初始化参数
	 */
	private void initParam() {
		uid = getIntent().getStringExtra("uid");
		fee = getIntent().getDoubleExtra("fee", 0);
		tag = getIntent().getStringExtra("tag");
		isXXk = getIntent().getBooleanExtra("isXXk", false);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		CloseActivity.curActivity = this;
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPause(this);
		super.onPause();
	}

	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (pd != null)
			pd.cancel();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.title_btn_left2) {
			finish();
		}
	}
}
