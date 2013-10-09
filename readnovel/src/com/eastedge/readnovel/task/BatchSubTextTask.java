package com.eastedge.readnovel.task;

import java.sql.SQLException;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;

import com.eastedge.readnovel.beans.SubResultBean;
import com.eastedge.readnovel.beans.orm.OrderRecord;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.db.orm.OrmDBHelper;
import com.j256.ormlite.dao.Dao;
import com.readnovel.base.db.orm.DBHelper;
import com.readnovel.base.http.HttpHelper;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.activitys.BookApp;

/**
 * 批量订阅
 * @author li.li
 *
 * Aug 14, 2013
 */
public class BatchSubTextTask extends EasyTask<Activity, Void, Void, Void> {
	private String aId;
	private String textId;
	private long t;
	private int count;
	private OrmDBHelper dbHelper;
	private Handler handler;
	private ProgressDialog progress;

	public BatchSubTextTask(Activity caller, String aId, String textId, long t, int count, Handler handler, ProgressDialog progress) {
		super(caller);

		this.aId = aId;
		this.textId = textId;
		this.t = t;
		this.count = count;
		this.handler = handler;
		this.progress = progress;

		dbHelper = DBHelper.getHelper(OrmDBHelper.class);
	}

	@Override
	public Void doInBackground(Void... params) {

		if (BookApp.getUser() == null)
			return null;

		String uid = BookApp.getUser().getUid();
		String auth = Util.md5(Util.md5(uid + t + Constants.PRIVATE_KEY) + Constants.PRIVATE_KEY).substring(0, 10);

		String url = String.format(Constants.SUB_BATCH_TEXT_URL, new Object[] { textId, uid, BookApp.getUser().getToken(), t, auth, count });

		try {
			SubResultBean subResultBean = HttpHelper.get(url, null, SubResultBean.class);

			if (subResultBean == null) {
				handler.sendEmptyMessage(44);
				return null;// balk
			}

			if (SubResultBean.SUCCESS.equals(subResultBean.getCode())) {// 订阅成功

				try {//订阅成功后添加记录

					Dao<OrderRecord, Integer> orderRecordDAO = dbHelper.getDao(OrderRecord.class);

					for (int i = 0; i < count; i++) {//count条记录
						OrderRecord or = new OrderRecord();
						or.setBookId(aId);
						or.setUserId(BookApp.getUser().getUid());
						or.setAddTime(new Date(System.currentTimeMillis()));
						orderRecordDAO.createIfNotExists(or);
					}

				} catch (SQLException e) {
					LogUtils.error(e.getMessage(), e);
				}

				handler.sendEmptyMessage(23);

			} else if (SubResultBean.FAILS.equals(subResultBean.getCode()) && "remain is not enough for pay".equals(subResultBean.getInfo())) {// 订阅失败,阅读币不足
				handler.sendEmptyMessage(42);

			} else if (SubResultBean.FAILS.equals(subResultBean.getCode()) && "this textid is paid before!".equals(subResultBean.getInfo())) {// 订阅失败,已订阅
				handler.sendEmptyMessage(43);
			} else
				handler.sendEmptyMessage(45);

		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public void onPostExecute(Void result) {
		if (progress != null)
			progress.dismiss();
	}

}
