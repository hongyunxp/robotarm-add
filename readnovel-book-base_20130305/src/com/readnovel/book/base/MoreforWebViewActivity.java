package com.readnovel.book.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.readnovel.book.base.utils.JavaScript;
import com.readnovel.book.base.utils.ViewUtils;

public class MoreforWebViewActivity extends Activity {
	private WebView mWebView;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.moretwo);
		mWebView = (WebView) this.findViewById(R.id.webView1);
		WebSettings ws = mWebView.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setBuiltInZoomControls(true);

		mWebView.addJavascriptInterface(JavaScript.newInstance(this, mWebView), JavaScript.NAME);
		//支持点击下载
		mWebView.setDownloadListener(new DownloadListener() {
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
				pd = ViewUtils.progressLoading(MoreforWebViewActivity.this);
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
		//		mWebView.loadUrl("http://www.hitesex.com/qhwx/more/book.php?a=more_single_book_page&showTab=1&cat=yanqing");
		mWebView.loadUrl("http://www.smqhe.com/more/more.html");
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}
