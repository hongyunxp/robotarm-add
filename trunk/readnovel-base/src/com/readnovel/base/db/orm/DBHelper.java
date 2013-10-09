package com.readnovel.base.db.orm;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.readnovel.base.R;
import com.readnovel.base.common.CommonApp;
import com.readnovel.base.util.LogUtils;

/**
 * 静态Helper类，用于建立、更新和打开数据库
 * 
 * @author li.li
 *
 */
public abstract class DBHelper extends OrmLiteSqliteOpenHelper implements GetTableAble {

	/**
	 * ormlite框架使用
	 * @param context
	 */
	public DBHelper(Context context) {
		super(context, getDBName(context), null, getDBVersion(context));

	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {

			for (Class<?> clazz : tables()) {

				TableUtils.createTable(connectionSource, clazz);
			}

		} catch (SQLException e) {
			LogUtils.error("创建数据库失败|" + e.getMessage(), e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int arg2, int arg3) {
		try {

			for (Class<?> clazz : tables()) {

				TableUtils.dropTable(connectionSource, clazz, true);
			}

			onCreate(db, connectionSource);
		} catch (SQLException e) {
			LogUtils.error("更新数据库失败|" + e.getMessage(), e);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T getHelper(Class clazz) {

		return (T) OpenHelperManager.getHelper(CommonApp.getInstance(), clazz);
	}

	@Override
	public abstract List<Class<? extends TableAble>> tables();

	// 数据库名
	public static final String getDBName(Context context) {
		context.getString(R.string.db_name);

		return context.getString(R.string.db_name);
	}

	// 数据库版本号
	public static final int getDBVersion(Context context) {
		context.getString(R.string.db_version);

		return Integer.valueOf(context.getResources().getString(R.string.db_version));
	}

}
