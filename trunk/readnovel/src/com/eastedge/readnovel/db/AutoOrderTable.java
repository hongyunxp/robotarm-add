package com.eastedge.readnovel.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class AutoOrderTable extends BaseDBAccess {

	public static final String Table_auto = "auto"; // 数据表
	public static final String KEY_ID = "_id"; // 表ID
	public static final String KEY_articleid = "articleid"; // 书id

	public AutoOrderTable(Context context) {
		super(context);
	}

	public static final String DB_CREATE = "CREATE TABLE " + Table_auto + " (" + KEY_ID + " integer primary key autoincrement, " + KEY_articleid
			+ " text);";
	/**
	 * 添加自动订阅
	 */
	public long insertAutoOrder(String aid) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_articleid, aid);
		return db.insert(Table_auto, null, newValues);
	} 

	public boolean exist(String aid) {
		Cursor cur = db.query(Table_auto, new String[] { KEY_articleid }, KEY_articleid + "='" + aid + "'", null, null, null, null);
		if (cur != null && cur.moveToFirst() && cur.getCount() > 0) {
			return true;
		}
		return false;
	}

}
