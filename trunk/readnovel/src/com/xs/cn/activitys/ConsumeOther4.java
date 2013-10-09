package com.xs.cn.activitys;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.SixbtnClickListener;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

public class ConsumeOther4 extends Activity implements OnClickListener{
	
	private Button left1;
	private TextView topTv;
	
	private Button phone, sms, czk, zfb, qb, wy;
	private RelativeLayout rl;
	private LinearLayout ll;
	
	private WebView mWebView;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cz_webview);
		CloseActivity.add(this);
		
		setTopBar();
		
		title = (TextView)findViewById(R.id.yd_tv);
		
		ll = (LinearLayout)findViewById(R.id.consume_sixbtn);
		
		SixbtnClickListener mlistener = new SixbtnClickListener(ConsumeOther4.this);
		sms = (Button)ll.findViewById(R.id.consume_sms);
		sms.setOnClickListener(mlistener);
		phone = (Button)ll.findViewById(R.id.consume_phone);
		phone.setOnClickListener(mlistener);
		czk = (Button)ll.findViewById(R.id.consume_czk);
		czk.setOnClickListener(mlistener);
		zfb = (Button)ll.findViewById(R.id.consume_zfb);
		zfb.setOnClickListener(mlistener);
		qb = (Button)ll.findViewById(R.id.consume_qb);
		qb.setOnClickListener(mlistener);
		wy = (Button)ll.findViewById(R.id.consume_wy);
		wy.setOnClickListener(mlistener);
		
		String tag = getIntent().getStringExtra("tag");
		if("czk".equals(tag)){
			czk.setBackgroundResource(R.drawable.consume2);
			czk.setTextColor(Color.WHITE);
		}
		if("zfb".equals(tag)){
			zfb.setBackgroundResource(R.drawable.consume2);
			zfb.setTextColor(Color.WHITE);
		}
		if("qb".equals(tag)){
			qb.setBackgroundResource(R.drawable.consume2);
			qb.setTextColor(Color.WHITE);
		}
		if("wy".equals(tag)){
			wy.setBackgroundResource(R.drawable.consume2);
			wy.setTextColor(Color.WHITE);
		}
		
		mWebView = (WebView)findViewById(R.id.cz_webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
	    mWebView.setWebViewClient(new WebViewClient(){       
            public boolean shouldOverrideUrlLoading(WebView view, String url) {       
                view.loadUrl(url);       
                return true;       
            }       
	    });   
	    String url = "http://t.xs.cn/web/pay.php";
	    mWebView.loadUrl(url);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {       
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {       
            mWebView.goBack();       
                   return true;       
        }       
        return super.onKeyDown(keyCode, event);       
    }  
	
	private void setTopBar(){
		rl = (RelativeLayout)findViewById(R.id.consume_top);
		left1 = (Button)rl.findViewById(R.id.title_btn_left2);
		topTv = (TextView)rl.findViewById(R.id.title_tv);
		left1.setText("  返回");
		left1.setVisibility(View.VISIBLE);
		
		left1.setOnClickListener(this);
		
		topTv.setText("充值中心");
	}
	public void onClick(View v) {
		if(v.getId() == R.id.title_btn_left2){
			String tag = getIntent().getStringExtra("Tag");
			if("readbook".equals(tag)){
				finish();
			}else{
//				Intent i = new Intent(ConsumeOther4.this, PersonCenterActivity.class);
//				startActivity(i);
				finish();
			}
		}
	}
	
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		CloseActivity.curActivity=this;
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
}
