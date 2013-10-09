package com.eastedge.readnovel.task;

import android.app.Activity;
import android.app.ProgressDialog;

import com.eastedge.readnovel.beans.Shubenxinxiye;
import com.eastedge.readnovel.common.ReadBookShareListener;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.http.HttpImpl;

/**
 * 得到书本明细异步任务
 * @author li.li
 *
 * Mar 13, 2013
 */
public class BookDetailTask extends EasyTask<Activity, Void, Void, String> {
	private ProgressDialog pd;
	private ReadBookShareListener readBookShareListener;

	public BookDetailTask(Activity caller, ReadBookShareListener readBookShareListener) {
		super(caller);

		this.readBookShareListener = readBookShareListener;
	}

	@Override
	public void onPreExecute() {
		//打加载对话框
		pd = ViewUtils.progressLoading(caller);

	}

	@Override
	public void onPostExecute(String bookName) {
		if (StringUtils.isNotBlank(bookName))
			readBookShareListener.setShareContent(bookName);

		//关闭加载对话框
		pd.cancel();
	}

	@Override
	public String doInBackground(Void... params) {
		//得到书的明细
		Shubenxinxiye bookDetail = HttpImpl.Shubenxinxiye(readBookShareListener.getaId());
		if (bookDetail != null) {
			String bookName = bookDetail.getTitle();
			return bookName;

		}

		return null;
	}
}
