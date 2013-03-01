package com.bus3.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bus3.R;
import com.bus3.common.activity.BaseActivity;
import com.bus3.test.request.MoreSecondRequest;

public class MoreSecondActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_second_content);

		//		tabInvHandler().setTitle(R.layout.more_title);

		Button b = (Button) findViewById(R.id.otherButton);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if ("startThread".equals(view.getTag())) {
					System.out.println("开启当前进程1");

					((Button) view).setText("结束当前进程1");
					view.setTag("endThread");
				} else if ("endThread".equals(view.getTag())) {
					System.out.println("结束当前进程1");

					MoreSecondRequest.get.abort();// 结束线程中的http请求

					((Button) view).setText("开启当前进程1");
					view.setTag("startThread");
				}

			}

		});

		b = (Button) findViewById(R.id.otherButton2);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if ("startThread".equals(view.getTag())) {
					System.out.println("开启当前进程2");

					((Button) view).setText("结束当前进程2");
					view.setTag("endThread");
				} else if ("endThread".equals(view.getTag())) {
					System.out.println("结束当前进程2");

					((Button) view).setText("开启当前进程2");
					view.setTag("startThread");
				}
			}

		});

		b = (Button) findViewById(R.id.otherButton3);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MoreSecondActivity.this, NewActivity.class);
				startActivity(intent);
			}

		});

		setResult(RESULT_OK);
		finish();

	}

}
