package com.bus3.test;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bus3.R;
import com.bus3.common.activity.BaseActivity;

public class MoreActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_content);

		tabInvHandler().setTitle(R.layout.more_title);

		Button b = (Button) findViewById(R.id.more_button);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				tabInvHandler().startSubActivity(R.id.main_tools_more, MoreSecondActivity.class);
			}

		});

	}

}
