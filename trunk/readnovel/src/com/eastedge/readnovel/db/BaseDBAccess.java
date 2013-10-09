package com.eastedge.readnovel.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.readnovel.base.db.DBAccess;

/**
 * 数据库访问对象
 * 
 * @author li.li
 *
 */
public class BaseDBAccess extends DBAccess {

	public BaseDBAccess(Context ctx) {
		super(ctx);
	}

	@Override
	protected SQLiteOpenHelper newSQLiteOpenHelper(Context ctx) {
		return new DBOpenHelper(ctx);
	}

}
