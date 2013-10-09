package com.eastedge.readnovel.task;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;

import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.common.CommonApp;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.DateUtils;
import com.readnovel.base.util.FileUtils;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.ViewUtils;

public class PhoneCleanTask extends EasyTask<Activity, Void, Void, Void> {

	private ProgressDialog pd;

	public PhoneCleanTask(Activity caller) {
		super(caller);
	}

	@Override
	public void onPreExecute() {
		//打加载对话框
		pd = ViewUtils.progressLoading(caller, "缓存清理中...\n\n如果数据过多花费时间较长，请耐心等待。");
	}

	@Override
	public void onPostExecute(Void result) {
		//关闭加载对话框
		pd.cancel();

		ViewUtils.toastLong(caller, "缓存清理完成");
	}

	@Override
	public Void doInBackground(Void... params) {
		//清理应用缓存
		File cacheFile = new File("/data/data/" + CommonApp.getInstance().getPackageName());
		FileUtils.deleteChildDir(cacheFile, null);
		CommonUtils.logout(caller);

		try {
			Thread.sleep(DateUtils.SECOND * 1);
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}
}
