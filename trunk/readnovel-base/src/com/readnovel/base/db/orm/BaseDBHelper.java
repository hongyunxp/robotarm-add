package com.readnovel.base.db.orm;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.readnovel.base.common.CommonApp;

/**
 * 数据库DBHelper类的子类，供使用者更容易的使用ORM
 * 
 * @author li.li
 *
 * Mar 28, 2013
 */
public class BaseDBHelper extends DBHelper {
	private static volatile BaseDBHelper instance;
	protected final List<Class<? extends TableAble>> tables;

	public BaseDBHelper(Context context) {
		super(context);
	}

	{
		this.tables = new LinkedList<Class<? extends TableAble>>();
	}

	public static BaseDBHelper getInstance() {
		if (instance == null) {
			synchronized (BaseDBHelper.class) {
				if (instance == null) {
					instance = new BaseDBHelper(CommonApp.getInstance());
				}
			}
		}
		return instance;
	}

	@Override
	public List<Class<? extends TableAble>> tables() {
		return tables;
	}

}
