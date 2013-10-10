package com.readnovel.book.base.utils;

import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * 处理JavaScript回调
 * 
 * @author li.li
 *
 * Jan 15, 2013
 */
public class JavaScript {
	public static final String NAME = "xs";

	private Activity act;
	private WebView mWebView;

	private JavaScript(Activity act, WebView mWebView) {
		this.act = act;
		this.mWebView = mWebView;
	}

	public static JavaScript newInstance(Activity act, WebView mWebView) {
		return new JavaScript(act, mWebView);
	}

	public void back() {
		mWebView.clearView();
		act.finish();
	}

	/**                                                                                                                                                                                                                                                                                                                                                                           
	 * 去用户中心，js回调
	 */
	public void goPersonCenter() {
	}

}
