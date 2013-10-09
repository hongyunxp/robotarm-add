package com.eastedge.readnovel.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 静态Helper类，用于建立、更新和打开数据库
 */
public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "smqhe.db";// 数据库名
	private static final int DB_VERSION = 10; // 数据库版本号

	public DBOpenHelper(Context context) {
		this(context, DB_NAME, null, DB_VERSION);
	}

	private DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/**
	 * 创建数据库
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// LogUtils.info("创建数据库"); 这些表到时候都要改
		db.execSQL(DBAdapter.DB_CREATE);
		db.execSQL(DBAdapter.DB_TABLE_SQ);
		db.execSQL(DBAdapter.DB_CREATE_GX);
		db.execSQL(LastReadTable.DB_CREATE);
		db.execSQL(AutoOrderTable.DB_CREATE);
		db.execSQL(UserBookTable.DB_CREATE);
		db.execSQL(Bookmuluadb.DB_CREATE);
	}

	/**
	 * 函数在数据库需要升级时被调用，一般用来删除旧的数据库表， 并将数据转移到新版本的数据库表中
	 */
	@Override
	public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
		// LogUtils.info("升级数据库");
		_db.execSQL("DROP TABLE IF EXISTS " + DBAdapter.DB_BF);
		_db.execSQL("DROP TABLE IF EXISTS " + DBAdapter.DB_SQ);
		_db.execSQL("DROP TABLE IF EXISTS " + DBAdapter.DB_GX);
		_db.execSQL("DROP TABLE IF EXISTS " + Bookmuluadb.Table_Bookmulu);
		_db.execSQL("DROP TABLE IF EXISTS " + LastReadTable.Table_lastread);
		_db.execSQL("DROP TABLE IF EXISTS " + AutoOrderTable.Table_auto);
		_db.execSQL("DROP TABLE IF EXISTS " + UserBookTable.Table_tbName);
		onCreate(_db);
	}

}
