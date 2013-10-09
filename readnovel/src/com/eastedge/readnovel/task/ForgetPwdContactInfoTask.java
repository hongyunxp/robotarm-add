package com.eastedge.readnovel.task;

import android.app.Activity;
import android.widget.TextView;

import com.eastedge.readnovel.beans.CallQQ;
import com.readnovel.base.sync.EasyTask;
import com.xs.cn.R;
import com.xs.cn.http.HttpImpl;

public class ForgetPwdContactInfoTask extends EasyTask<Activity, Void, Void, CallQQ> {
	private TextView contentTv;

	public ForgetPwdContactInfoTask(Activity caller, TextView contentTv) {
		super(caller);

		this.contentTv = contentTv;
	}

	@Override
	public CallQQ doInBackground(Void... params) {

		return HttpImpl.aboutMe();
	}

	@Override
	public void onPostExecute(CallQQ cq) {
		if (cq == null)
			return;

		String contact = "客服电话：" + cq.getCall() + "\n客服QQ：" + cq.getQq();
		String content = String.format(caller.getString(R.string.forget_password_content_bottom_tv), contact);

		contentTv.setText(content);

	}
}
