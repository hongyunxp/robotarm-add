package com.readnovel.base.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.readnovel.base.util.LogUtils;

/**
 * 数据库基本操作通用对象
 * @author li.li
 *
 * Jul 31, 2012
 */
public abstract class DBAccess {
	protected static volatile SQLiteDatabase db;//数据库访问
	private final Context ctx;

	public DBAccess(Context ctx) {
		this.ctx = ctx;
	}

	/**
	 * 打开数据库
	 * 
	 * @throws SQLiteException
	 */
	public void open() throws SQLiteException {
		if (db != null && db.isOpen())
			return;

		synchronized (DBAccess.class) {

			if (db != null && db.isOpen())
				return;

			SQLiteOpenHelper dbOpenHelper = newSQLiteOpenHelper(ctx);//数据库创建，数据库打开，数据库版本管理

			try {
				db = dbOpenHelper.getWritableDatabase();
			} catch (SQLiteException e) {
				db = dbOpenHelper.getReadableDatabase();//空间不够存储的时候设为只读
			}
		}
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		//		if (db != null && db.isOpen()) {
		//			db.close();
		//		}
	}

	public SQLiteDatabase getDB() {
		return db;
	}

	/**
	 * 创建SQLiteOpenHelper
	 * @return
	 */
	protected abstract SQLiteOpenHelper newSQLiteOpenHelper(Context ctx);

	/**
	 *********************************************************** 
	 * 数据库基本操作
	 ***********************************************************
	 */

	/**
	 * 插入数据
	 * 参数：tableName 表名
	 * initialValues 要插入的列对应值
	 *   */
	public long insert(String tableName, ContentValues initialValues) {

		return db.insert(tableName, null, initialValues);
	}

	/**
	 * 删除数据
	 * 参数：tableName 表名
	 * deleteCondition 删除的条件
	 * deleteArgs 如果deleteCondition中有“？”号，将用此数组中的值替换
	 *   */
	public boolean delete(String tableName, String deleteCondition, String[] deleteArgs) {

		return db.delete(tableName, deleteCondition, deleteArgs) > 0;
	}

	/**
	 * 更新数据
	 * 参数：tableName 表名
	 * initialValues 要更新的列
	 * selection 更新的条件
	 * selectArgs 如果selection中有“？”号，将用此数组中的值替换
	 *   */
	public boolean update(String tableName, ContentValues initialValues, String selection, String[] selectArgs) {

		int returnValue = db.update(tableName, initialValues, selection, selectArgs);

		return returnValue > 0;
	}

	/**
	 * 取得一个列表
	 * 参数：tableName 表名
	 * columns 返回的列
	 * selection 查询条件
	 * selectArgs 如果selection中有“？”号，将用此数组中的值替换
	 *   */
	public Cursor findList(String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {

		return db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy);
	}

	/**
	 * 取得单行记录
	 * 参数：tableName 表名
	 * columns 返回的列
	 * selection 查询条件
	 * selectArgs 如果selection中有“？”号，将用此数组中的值替换
	 *   */
	public Cursor findInfo(String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit, boolean distinct) {

		Cursor mCursor = db.query(distinct, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

		return mCursor;
	}

	/**
	 * 执行sql
	 * 参数：sql 要执行的sql
	 *   
	 *   */
	public void execSQL(String sql) {

		db.execSQL(sql);

	}

	/**
	    * 判断某张表是否存在
	    * @param tabName 表名
	    * @return
	    */
	public boolean isTableExist(String tableName) {
		boolean result = false;
		if (tableName == null)
			return false;

		try {
			Cursor cursor = null;
			String sql = "select count(1) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

			cursor.close();
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	* 判断某张表中是否存在某字段(注，该方法无法判断表是否存在，因此应与isTableExist一起使用)
	* 
	* @param tabName 表名
	* @return
	*/
	public boolean isColumnExist(String tableName, String columnName) {
		boolean result = false;

		if (tableName == null)
			return false;

		try {
			Cursor cursor = null;
			String sql = "select count(1) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "' and sql like '%"
					+ columnName.trim() + "%'";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

			cursor.close();
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return result;
	}

}
