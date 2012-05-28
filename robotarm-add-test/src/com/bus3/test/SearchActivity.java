package com.bus3.test;

import robot.arm.provider.LoaderPrivider;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.bus3.R;
import com.bus3.common.activity.BaseActivity;
import com.bus3.common.net.NetType;
import com.bus3.common.utils.BaseUtils;

//@Resume(resumable = false)
public class SearchActivity extends BaseActivity {
	private LoaderPrivider loader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		tabInvHandler().setTitle(R.layout.search_title);

		if (check()) {
			setContentView(R.layout.search_content);
		}

	}

	private boolean check() {
		boolean r = true;
		loader=LoaderPrivider.newInstance(this);
//		loader.show();

		if (NetType.NONE.equals(nt())) {
			BaseUtils.confirm(this, new OnClickListener() {

				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					check();// 重试

				}

			}, new OnClickListener() {

				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {

					loader.hide();

				}

			});

			r = false;
		}

		return r;
	}

}
