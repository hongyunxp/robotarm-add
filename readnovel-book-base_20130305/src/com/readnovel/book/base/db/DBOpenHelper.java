package com.readnovel.book.base.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.readnovel.book.base.db.table.PayRecordTable;
import com.readnovel.book.base.db.table.VipPayIntervalRecord;
import com.readnovel.book.base.utils.LogUtils;

/**
 * 静态Helper类，用于建立、更新和打开数据库
 */
public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "readnl.db";// 数据库名
	private static final int DB_VERSION = 1; // 数据库版本号

	public DBOpenHelper(Context context) {
		this(context, DB_NAME, null, DB_VERSION);
	}

	private DBOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * 创建数据库
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		LogUtils.info("创建数据库");
		db.execSQL(PayRecordTable.DB_CREATE);
		db.execSQL(VipPayIntervalRecord.DB_CREATE);
	}

	/**
	 * 函数在数据库需要升级时被调用，一般用来删除旧的数据库表，并将数据转移到新版本的数据库表中
	 */
	@Override
	public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
		LogUtils.info("升级数据库");
	}

}
