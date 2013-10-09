package com.eastedge.readnovel.task;

import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.eastedge.readnovel.beans.Month;
import com.eastedge.readnovel.beans.User;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.http.HttpImpl;

public class CheckBaoYueTask extends EasyTask<Activity, Void, Void, String> {
	private ProgressDialog pd;

	public CheckBaoYueTask(Activity caller) {
		super(caller);
	}

	@Override
	public void onPreExecute() {
		//打加载对话框
		pd = ViewUtils.progressLoading(caller);
	}

	@Override
	public void onPostExecute(String result) {
		//关闭加载对话框
		pd.cancel();

		ViewUtils.showDialog(caller, "温馨提示", result, null);
	}

	@Override
	public String doInBackground(Void... params) {
		String msg = null;
		User user = BookApp.getUser();

		if (user != null) {
			Month month = HttpImpl.MonthForPhone(user.getUid(), user.getToken());
			String code = month.getCode();

			if ("1".equals(code)) {
				if ("1".equals(month.getMonth_status())) {

					SharedPreferences spf = caller.getSharedPreferences("buyTime", Context.MODE_WORLD_READABLE);
					Date date = month.getDate();

					String buyTime = spf.getString("time", (date.getYear() + 1900) + "年-" + (date.getMonth() + 1) + "月-" + date.getDay() + "日");
					msg = user.getUsername() + ":\n您好，您的手机号" + user.getTel() + "已经成功订购了包月服务，订购时间：" + buyTime;

				} else if ("2".equals(month.getMonth_status())) {

					msg = user.getUsername() + ":\n您好，您的手机号" + user.getTel() + "没有订购包月服务";

				} else if ("3".equals(month.getMonth_status())) {

					msg = user.getUsername() + ":\n您好，您的手机号" + user.getTel() + "正在申请包月服务";

				}
			}

		}

		return msg;
	}

}
