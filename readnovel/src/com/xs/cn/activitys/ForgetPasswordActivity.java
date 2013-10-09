package com.xs.cn.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.task.ForgetPwdContactInfoTask;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.common.LoadLayerProvider;
import com.xs.cn.R;

/**
 * 忘记密码内容
 * @author li.li
 *
 */
public class ForgetPasswordActivity extends Activity {
	//顶部TextView
	private TextView topTextView;
	private TextView aboutweTextView;

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
	}

	/**
	 * 初始化视图布局
	 */
	private void initView() {
		setContentView(R.layout.forget_passord);

		topTextView = (TextView) findViewById(R.id.forget_password_content_top_tv);
		topTextView.setText(Html.fromHtml(getString(R.string.forget_password_msg)));
		aboutweTextView = (TextView) findViewById(R.id.aboutwe_tv);

		setTopBar();

		//创建和启动异步任务，取客服数据
		ForgetPwdContactInfoTask aboutWeTask = new ForgetPwdContactInfoTask(this, aboutweTextView);
		aboutWeTask.execute();

	}

	/**
	 * 设置标题顶部
	 */
	private void setTopBar() {
		Button left2 = (Button) findViewById(R.id.title_btn_left2);

		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText("找回密码");

		left2.setVisibility(View.VISIBLE);
		left2.setText("返回");

		left2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 点击找回密码按钮
	 * @param view
	 */
	public void forgetPassword(View view) {
		CommonUtils.forgotPassword(this);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}

}
