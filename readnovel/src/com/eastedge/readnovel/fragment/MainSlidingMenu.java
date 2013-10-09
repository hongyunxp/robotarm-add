package com.eastedge.readnovel.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.JavaScript;
import com.eastedge.readnovel.common.LocalStore;
import com.readnovel.base.common.NetType;
import com.readnovel.base.util.NetUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.BookApp;

/**
 * 抽屉菜单
 * @author li.li
 *
 */
public class MainSlidingMenu extends Fragment {
	private ProgressDialog pd;
	private WebView mWebView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_sliding_menu_content, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mWebView = (WebView) getActivity().findViewById(R.id.main_sliding_menu_webview);
		//配置WebView
		WebSettings ws = mWebView.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setBuiltInZoomControls(false);
		ws.setUseWideViewPort(true); //让浏览器支持用户自定义view

		User user = BookApp.getUser();

		if (user == null) {
			mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		} else {
			if (NetType.TYPE_NONE.equals(NetUtils.checkNet())) {
				mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			} else {
				mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
			}
		}

		//映射Java对象到一个名为”xs“的Javascript对象上
		//JavaScript中可以通过"window.xs"来调用Java对象的方法
		mWebView.addJavascriptInterface(JavaScript.newInstance(getActivity(), mWebView), JavaScript.NAME);

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				//打加载对话框
				if (pd != null)
					pd.cancel();
				pd = ViewUtils.progressLoading(getActivity());
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				//关闭加载对话框 
				if (pd != null)
					pd.cancel();

				LocalStore.setSlidingMenuTime(getActivity(), System.currentTimeMillis());

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);//点击连接从自身打开
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {//网络错误

				getActivity().getSupportFragmentManager()//
						.beginTransaction()//
						.replace(R.id.main_sliding_menu_content, new NetworkNotAvailableFragment())//
						.commit();
			}

		});

		loadData();
	}

	private void loadData() {
		User user = BookApp.getUser();
		String url = null;

		if (user != null)
			url = String.format(Constants.MSG_CENTER_URL, new Object[] { user.getUid(), user.getToken() });
		else
			url = Constants.MSG_CENTER_URL;

		mWebView.loadUrl(url);
	}

}
