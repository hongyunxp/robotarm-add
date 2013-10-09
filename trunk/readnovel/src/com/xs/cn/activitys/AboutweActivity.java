package com.xs.cn.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.task.ContactInfoTask;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

/**
 *   关于页面
 */
public class AboutweActivity extends Activity implements OnClickListener {
	private Button left1;
	private TextView textView3;
	private TextView textView2;
	private ContactInfoTask aboutWeTask;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutwe);
		CloseActivity.add(this);

		setTopBar();
		textView3 = (TextView) findViewById(R.id.textView3);
		textView2 = (TextView) findViewById(R.id.textView2);

		//创建和启动异步任务，取客服信息
		aboutWeTask = new ContactInfoTask(this, textView3, textView2);
		aboutWeTask.execute();

	}

	private void setTopBar() {
		left1 = (Button) findViewById(R.id.title_btn_left2);
		left1.setText("返回");
		left1.setGravity(Gravity.CENTER);

		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText("关于我们");
		left1.setVisibility(View.VISIBLE);
		left1.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == left1.getId()) {
			finish();
		}
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

}
