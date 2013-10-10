package com.readnovel.book.base.db.table;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.readnovel.book.base.bean.VipPayInterval;
import com.readnovel.book.base.db.BaseDBAccess;
import com.readnovel.book.base.utils.DateUtils;

public class VipPayIntervalRecord extends BaseDBAccess {

	private static final String NAME = "vip_pay_interval";
	private static final String ID = "id";
	private static final String CHAPTER_IDS = "chapter_ids";
	private static final String STATE = "state";
	private static final String UP_TIME = "up_time";

	public VipPayIntervalRecord(Context ctx) {
		super(ctx);
	}

	public static final String DB_CREATE = "CREATE TABLE " + NAME + " (" + ID + " integer primary key autoincrement, " + CHAPTER_IDS + " TEXT , " + STATE + " text , " + UP_TIME + " text);";

	public List<VipPayInterval> getAll() {
		try {
			open();

			Cursor returnCursor = findList(NAME, new String[] { ID, CHAPTER_IDS, STATE, UP_TIME }, null, null, null, null, null);

			List<VipPayInterval> vpis = new ArrayList<VipPayInterval>();
			while (returnCursor.moveToNext()) {
				int id = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(ID));
				String bookIds = returnCursor.getString(returnCursor.getColumnIndexOrThrow(CHAPTER_IDS));
				int state = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(STATE));
				String upTime = returnCursor.getString(returnCursor.getColumnIndexOrThrow(UP_TIME));

				VipPayInterval vp = new VipPayInterval();
				vp.setId(id);
				vp.setBookIds(bookIds);
				vp.setState(state);
				vp.setUpTime(upTime);

				vpis.add(vp);

			}
			return vpis;

		} finally {
			close();
		}

	}

	public List<VipPayInterval> getByStatus(int status) {
		try {
			open();

			Cursor returnCursor = findList(NAME, new String[] { ID, CHAPTER_IDS, STATE, UP_TIME }, STATE + " = " + status, null, null, null, null);

			List<VipPayInterval> vpis = new ArrayList<VipPayInterval>();
			while (returnCursor.moveToNext()) {
				int id = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(ID));
				String bookIds = returnCursor.getString(returnCursor.getColumnIndexOrThrow(CHAPTER_IDS));
				int state = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(STATE));
				String upTime = returnCursor.getString(returnCursor.getColumnIndexOrThrow(UP_TIME));

				VipPayInterval vp = new VipPayInterval();
				vp.setId(id);
				vp.setBookIds(bookIds);
				vp.setState(state);
				vp.setUpTime(upTime);

				vpis.add(vp);

			}
			return vpis;

		} finally {
			close();
		}
	}

	public VipPayInterval getByChapterId(int chapterId) {
		try {
			open();

			Cursor returnCursor = findInfo(NAME, new String[] { ID, CHAPTER_IDS, STATE, UP_TIME }, CHAPTER_IDS + " like '%" + chapterId + "%'", null, null, null, null, null, false);
			while (returnCursor.moveToNext()) {
				int id = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(ID));
				String bookIds = returnCursor.getString(returnCursor.getColumnIndexOrThrow(CHAPTER_IDS));
				int state = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(STATE));
				String upTime = returnCursor.getString(returnCursor.getColumnIndexOrThrow(UP_TIME));

				VipPayInterval vp = new VipPayInterval();
				vp.setId(id);
				vp.setBookIds(bookIds);
				vp.setState(state);
				vp.setUpTime(upTime);

				return vp;

			}

			return null;

		} finally {
			close();
		}

	}

	public long add(String bookIds, int state) {
		try {
			open();

			ContentValues cv = new ContentValues();
			cv.put(CHAPTER_IDS, bookIds);
			cv.put(STATE, state);
			cv.put(UP_TIME, DateUtils.format(System.currentTimeMillis()));

			return insert(NAME, cv);

		} finally {
			close();
		}
	}

	public void add(List<VipPayInterval> vpis, int state) {
		for (VipPayInterval vpi : vpis) {
			add(vpi.getBookIds(), state);
		}
	}

	public boolean upStateById(int id, int state) {

		try {
			open();

			ContentValues cv = new ContentValues();
			cv.put(STATE, state);
			cv.put(UP_TIME, DateUtils.format(System.currentTimeMillis()));

			return update(NAME, cv, ID + " = " + id, null);

		} finally {
			close();
		}
	}
}
