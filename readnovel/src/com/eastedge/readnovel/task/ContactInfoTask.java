package com.eastedge.readnovel.task;

import android.app.Activity;
import android.widget.TextView;

import com.eastedge.readnovel.beans.CallQQ;
import com.readnovel.base.sync.EasyTask;
import com.xs.cn.http.HttpImpl;
/**
 * 客服联系信息
 * 
 * @author li.li
 *
 * Sep 6, 2012
 */
public class ContactInfoTask extends EasyTask<Activity, Void, Void, CallQQ> {
	private TextView callText;
	private TextView qqText;

	public ContactInfoTask(Activity caller, TextView callText, TextView qqText) {
		super(caller);

		this.callText = callText;
		this.qqText = qqText;
	}

	@Override
	public CallQQ doInBackground(Void... params) {

		return HttpImpl.aboutMe();
	}

	@Override
	public void onPostExecute(CallQQ cq) {
		if (cq == null)
			return;

		callText.setText("客服电话：" + cq.getCall());
		qqText.setText("客服QQ：" + cq.getQq());

	}

}
