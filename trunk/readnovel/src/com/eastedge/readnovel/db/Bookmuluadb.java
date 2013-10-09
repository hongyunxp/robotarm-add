package com.eastedge.readnovel.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.eastedge.readnovel.beans.Muludb;

public class Bookmuluadb extends BaseDBAccess {
	public static final String Table_Bookmulu = "Bookmulu"; // 数据表
	public static final String KEY_ID = "_id"; // 表ID
	public static final String KEY_articleid = "articleid"; // 书id
	public static final String KEY_textid = "tid"; // 章节ID
	public static final String KEY_bookp = "bookp"; // 当前看书本目录的位置
	public static final String KEY_bookbeg = "KEY_bookbeg"; // 点钱目录的位置

	/**
	 * 书本目录表架表
	 */
	public static final String DB_CREATE = "CREATE TABLE " + Table_Bookmulu + " (" + KEY_ID + " integer primary key autoincrement, " + KEY_articleid
			+ " text , " + KEY_textid + " text , " + KEY_bookp + " integer ," + KEY_bookbeg + " integer);";

	public Bookmuluadb(Context context) {
		super(context);
	}

	// X如目录表
	public String insertBook(String aid, String tid, int bookp, int bookpbeg) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_articleid, aid);
		newValues.put(KEY_textid, tid);
		newValues.put(KEY_bookp, bookp);
		newValues.put(KEY_bookbeg, bookpbeg);
		long a = db.insert(Table_Bookmulu, null, newValues);
		if (a != -1) {
			return "添加成功";
		}
		return "添加失败";
	}

	/**
	 * 查询数据库
	 * 
	 * @return
	 */
	public Muludb queryDatas(String aid) {
		Muludb mulu = new Muludb();
		String sql = "select * from Bookmulu where articleid =? ";
		Cursor cursor = this.db.rawQuery(sql, new String[] { aid });
		while (cursor.moveToNext()) {
			mulu.setAid(aid);
			mulu.setTid(cursor.getString(cursor.getColumnIndex(KEY_textid)));
			mulu.setBookp(cursor.getInt(cursor.getColumnIndex(KEY_bookp)));
			mulu.setBookpbeg(cursor.getInt(cursor.getColumnIndex(KEY_bookbeg)));
		}
		cursor.close();
		this.db.close();
		return mulu;
	}
}
