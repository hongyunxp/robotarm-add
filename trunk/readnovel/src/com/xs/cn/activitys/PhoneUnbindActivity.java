package com.xs.cn.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.common.LoadLayerProvider;
import com.xs.cn.R;

/**
 * 公告
 * @author li.li
 *
 */
public class PhoneUnbindActivity extends Activity {
	//webview
	private WebView webView;
	//请求url
	private String url;
	//textview
	private TextView textView;

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
	private void initView() {
		setContentView(R.layout.phone_unbind);

		setTopBar();

		textView = (TextView) findViewById(R.id.personcenter_unbindview_tv);
		textView.setText(Html.fromHtml(getString(R.string.unbind_phone_msg)));

	}

	/**
	 * 设置标题顶部
	 */
	private void setTopBar() {
		Button left2 = (Button) findViewById(R.id.title_btn_left2);

		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText("解除绑定");

		left2.setVisibility(View.VISIBLE);
		left2.setText("返回");

		left2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 解除绑定
	 * @param view
	 */
	public void unBindPhone(View view) {
		CommonUtils.unBindPhone(this);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}

}
