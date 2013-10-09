package com.eastedge.readnovel.task;

import java.io.File;
import java.io.FilenameFilter;

import android.app.Activity;
import android.app.ProgressDialog;

import com.eastedge.readnovel.common.Constants;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.FileUtils;
import com.readnovel.base.util.ViewUtils;

/**
 * SD卡清理异步任务
 * 
 * @author li.li
 *
 * Mar 21, 2013
 */
public class SDCleanTask extends EasyTask<Activity, Void, Void, Void> {
	private ProgressDialog pd;

	public SDCleanTask(Activity caller) {
		super(caller);
	}

	@Override
	public void onPreExecute() {
		//打加载对话框
		pd = ViewUtils.progressLoading(caller, "数据清理中...\n\n如果数据过多花费时间较长，请耐心等待。");
	}

	@Override
	public void onPostExecute(Void result) {
		//关闭加载对话框
		pd.cancel();

		ViewUtils.toastLong(caller, "数据清理完成");
	}

	@Override
	public Void doInBackground(Void... params) {

		//清理数据
		File dataFile = new File(Constants.READNOVEL_DATA_ROOT);
		FileUtils.deleteDir(dataFile, new FilenameFilter() {

			@Override
			public boolean accept(File file, String filename) {
				//imageCache目录不清理
				if (file.isDirectory() && Constants.READNOVEL_IMGCACHE.equals(file.getAbsolutePath() + File.separator + filename))
					return false;

				return true;
			}
		});

		//清理error log
		File logFile = new File(Constants.READNOVEL_LOG);
		FileUtils.deleteDir(logFile, null);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}
}
