package com.bus3.test;

import robot.arm.common.BGLoader;
import android.os.Bundle;

import com.bus3.R;
import com.bus3.common.activity.BaseActivity;

public class NewActivity extends BaseActivity {
	private BGLoader loader;// 背景加载数据层提供者

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_content);

		loader = BGLoader.newInstance(this);

		loader.start(getClass());

		loader.getLoading().postDelayed(new Runnable() {// 三秒后关闭

					@Override
					public void run() {
						loader.stop(NewActivity.this.getClass());

					}
				}, 3000);

	}

	@Override
	public void finish() {
		super.finish();

	}

}
