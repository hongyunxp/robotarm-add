package com.eastedge.readnovel.task;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.JavaScript;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;

/**
 * 弹出红包对话框
 * @author li.li
 *
 * Aug 9, 2013
 */
public class SupportAuthorPageTask extends EasyTask<Activity, Void, Void, Void> {
	private WebView mWebView;
	private ProgressDialog pd;
	private Dialog dialog;
	private String aId;
	private int titleType;

	public SupportAuthorPageTask(Activity caller, String aId, int titleType) {
		super(caller);

		this.aId = aId;
		this.titleType = titleType;

		//init webview
		mWebView = new WebView(caller, null, R.style.style_support_author_webview);
	}

	@Override
	public void onPreExecute() {
	}

	@Override
	public void onPostExecute(Void result) {
		dialog = new Dialog(caller, R.style.Theme_FullHeightDialog);
		dialog.setContentView(mWebView);
		dialog.show();
	}

	@Override
	public Void doInBackground(Void... params) {
		//配置WebView
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

		//		//适应全屏  39适应竖屏    57适应横屏  
		//		mWebView.setInitialScale(39);

		//映射Java对象到一个名为”xs“的Javascript对象上
		//JavaScript中可以通过"window.xs"来调用Java对象的方法
		mWebView.addJavascriptInterface(JavaScript.newInstance(caller, mWebView, this), JavaScript.NAME);

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				//打加载对话框
				if (pd != null)
					pd.cancel();
				pd = ViewUtils.progressLoading(caller);
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

		mWebView.loadUrl(String.format(Constants.SUPPORT_AUTHOR_PAGE_URL, titleType));

		return null;
	}

	/**
	 * @return the dialog
	 */
	public Dialog getDialog() {
		return dialog;
	}

	public String getAId() {
		return aId;
	}

}
