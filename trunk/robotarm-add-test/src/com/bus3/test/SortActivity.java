package com.bus3.test;

import android.os.Bundle;

import com.bus3.R;
import com.bus3.common.activity.BaseActivity;

public class SortActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sort_content);
	}

	@Override
	protected void onResume() {
		super.onResume();
		tabInvHandler().setTitle(R.layout.sort_title);
		// parent().toolsVisible(false);
		// LoaderPrivider loader = LoaderPrivider.newInstance(this);
		// loader.show();
	}
}
