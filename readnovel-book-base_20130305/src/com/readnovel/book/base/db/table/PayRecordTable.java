package com.readnovel.book.base.db.table;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.readnovel.book.base.bean.PayRecord;
import com.readnovel.book.base.db.BaseDBAccess;
import com.readnovel.book.base.utils.DateUtils;

public class PayRecordTable extends BaseDBAccess {
	public PayRecordTable(Context ctx) {
		super(ctx);
	}

	private static final String NAME = "pay_record";
	private static final String ID = "id";
	private static final String CHAPTER_ID = "chapter_id";
	private static final String INTERVAL_RECORD_ID = "interval_record_id";
	private static final String FEE = "fee";
	private static final String ADD_TIME = "add_time";

	public static final String DB_CREATE = "CREATE TABLE " + NAME + " (" + ID + " integer primary key autoincrement, " + CHAPTER_ID + " integer , " + INTERVAL_RECORD_ID + " integer ," + FEE
			+ " real ," + ADD_TIME + " TEXT);";

	public List<PayRecord> getAll() {
		try {
			open();

			List<PayRecord> payRecords = new ArrayList<PayRecord>();
			Cursor returnCursor = findList(NAME, new String[] { ID, CHAPTER_ID, INTERVAL_RECORD_ID, FEE, ADD_TIME }, null, null, null, null, null);
			while (returnCursor.moveToNext()) {
				int id = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(ID));
				int chapterId = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(CHAPTER_ID));
				int intervalRecordId = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(INTERVAL_RECORD_ID));
				String addTime = returnCursor.getString(returnCursor.getColumnIndexOrThrow(ADD_TIME));
				float fee = returnCursor.getFloat(returnCursor.getColumnIndexOrThrow(FEE));
				
				PayRecord payRecord = new PayRecord();
				payRecord.setId(id);
				payRecord.setChapterId(chapterId);
				payRecord.setIntervalRecordId(intervalRecordId);
				payRecord.setAddTime(addTime);
				payRecord.setFee(fee);

				payRecords.add(payRecord);
			}

			return payRecords;

		} finally {
			close();
		}

	}

	public List<PayRecord> getAllDesc() {
		try {
			open();

			List<PayRecord> payRecords = new ArrayList<PayRecord>();
			Cursor returnCursor = findList(NAME, new String[] { ID, CHAPTER_ID, INTERVAL_RECORD_ID, FEE, ADD_TIME }, null, null, null, null, "id desc");
			while (returnCursor.moveToNext()) {
				int id = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(ID));
				int chapterId = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(CHAPTER_ID));
				int intervalRecordId = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(INTERVAL_RECORD_ID));
				String addTime = returnCursor.getString(returnCursor.getColumnIndexOrThrow(ADD_TIME));
				float fee = returnCursor.getFloat(returnCursor.getColumnIndexOrThrow(FEE));
				
				PayRecord payRecord = new PayRecord();
				payRecord.setId(id);
				payRecord.setChapterId(chapterId);
				payRecord.setIntervalRecordId(intervalRecordId);
				payRecord.setAddTime(addTime);
				payRecord.setFee(fee);

				payRecords.add(payRecord);
			}

			return payRecords;

		} finally {
			close();
		}

	}

	public long add(int chapterId, int intervalRecordId, String addTime) {
		try {
			open();

			ContentValues cv = new ContentValues();
			cv.put(CHAPTER_ID, chapterId);
			cv.put(INTERVAL_RECORD_ID, intervalRecordId);
			cv.put(ADD_TIME, addTime);

			return insert(NAME, cv);

		} finally {
			close();
		}
	}

	public PayRecord getByIntervaId(int intervaId) {
		try {
			open();

			Cursor returnCursor = findInfo(NAME, new String[] { ID, CHAPTER_ID, INTERVAL_RECORD_ID, FEE, ADD_TIME }, INTERVAL_RECORD_ID + "=" + intervaId, null, null, null, null, null, false);
			while (returnCursor.moveToNext()) {
				int id = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(ID));
				int chapterId = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(CHAPTER_ID));
				int intervalRecordId = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(INTERVAL_RECORD_ID));
				String addTime = returnCursor.getString(returnCursor.getColumnIndexOrThrow(ADD_TIME));
				float fee = returnCursor.getFloat(returnCursor.getColumnIndexOrThrow(FEE));
				
				PayRecord payRecord = new PayRecord();
				payRecord.setId(id);
				payRecord.setChapterId(chapterId);
				payRecord.setIntervalRecordId(intervalRecordId);
				payRecord.setAddTime(addTime);
				payRecord.setFee(fee);

				return payRecord;
			}

		} finally {
			close();
		}

		return null;
	}

	public PayRecord getById(int cId) {
		try {
			open();

			Cursor returnCursor = findInfo(NAME, new String[] { ID, CHAPTER_ID, INTERVAL_RECORD_ID, FEE, ADD_TIME }, ID + "=" + cId, null, null, null, null, null, false);
			while (returnCursor.moveToNext()) {
				int id = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(ID));
				int chapterId = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(CHAPTER_ID));
				int intervalRecordId = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(INTERVAL_RECORD_ID));
				String addTime = returnCursor.getString(returnCursor.getColumnIndexOrThrow(ADD_TIME));
				float fee = returnCursor.getFloat(returnCursor.getColumnIndexOrThrow(FEE));
				
				PayRecord payRecord = new PayRecord();
				payRecord.setId(id);
				payRecord.setChapterId(chapterId);
				payRecord.setIntervalRecordId(intervalRecordId);
				payRecord.setAddTime(addTime);
				payRecord.setFee(fee);

				return payRecord;
			}

		} finally {
			close();
		}

		return null;
	}
	
	public boolean upFee(int intervalRecordId,float fee){
		try {
			open();

			ContentValues cv = new ContentValues();
			cv.put(FEE, fee);

			return update(NAME, cv, INTERVAL_RECORD_ID + " = "+intervalRecordId, null);

		} finally {
			close();
		}
	}

	public PayRecord getByChapterId(int chapterId) {
		try {
			open();

			Cursor returnCursor = findInfo(NAME, new String[] { ID, CHAPTER_ID, INTERVAL_RECORD_ID, FEE, ADD_TIME }, CHAPTER_ID + "=" + chapterId, null, null, null, "add_time desc", null, false);
			while (returnCursor.moveToNext()) {
				int id = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(ID));
				int intervalRecordId = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(INTERVAL_RECORD_ID));
				String addTime = returnCursor.getString(returnCursor.getColumnIndexOrThrow(ADD_TIME));
				float fee = returnCursor.getFloat(returnCursor.getColumnIndexOrThrow(FEE));
				
				PayRecord payRecord = new PayRecord();
				payRecord.setId(id);
				payRecord.setChapterId(chapterId);
				payRecord.setIntervalRecordId(intervalRecordId);
				payRecord.setAddTime(addTime);
				payRecord.setFee(fee);

				return payRecord;
			}

		} finally {
			close();
		}

		return null;
	}

	public List<PayRecord> getTodayRecord() {
		try {
			open();

			Cursor returnCursor = findList(NAME, new String[] { ID, CHAPTER_ID, INTERVAL_RECORD_ID, FEE, ADD_TIME }, null, null, null, null, null);

			List<PayRecord> vpis = new ArrayList<PayRecord>();
			while (returnCursor.moveToNext()) {
				int id = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(ID));
				int chapterId = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(CHAPTER_ID));
				int intervalRecordId = returnCursor.getInt(returnCursor.getColumnIndexOrThrow(INTERVAL_RECORD_ID));
				String addTime = returnCursor.getString(returnCursor.getColumnIndexOrThrow(ADD_TIME));
				float fee = returnCursor.getFloat(returnCursor.getColumnIndexOrThrow(FEE));
				
				PayRecord payRecord = new PayRecord();
				payRecord.setId(id);
				payRecord.setChapterId(chapterId);
				payRecord.setIntervalRecordId(intervalRecordId);
				payRecord.setAddTime(addTime);
				payRecord.setFee(fee);

				vpis.add(payRecord);

			}

			List<PayRecord> todayRecords = new ArrayList<PayRecord>();
			for (PayRecord payRecord : vpis) {
				String time = payRecord.getAddTime();
				if (DateUtils.isSameDay(time, DateUtils.now()))
					todayRecords.add(payRecord);
			}

			return todayRecords;

		} finally {
			close();
		}
	}

}
