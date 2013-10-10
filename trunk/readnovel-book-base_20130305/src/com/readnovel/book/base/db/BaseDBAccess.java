package com.readnovel.book.base.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDBAccess extends DBAccess {

	public BaseDBAccess(Context ctx) {
		super(ctx);
	}

	@Override
	protected SQLiteOpenHelper newSQLiteOpenHelper(Context ctx) {
		return new DBOpenHelper(ctx);
	}

}
