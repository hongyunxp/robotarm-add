package com.eastedge.readnovel.db.orm;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.readnovel.base.db.orm.DBHelper;
import com.readnovel.base.db.orm.TableAble;
import com.xs.cn.activitys.TestBean;

/**
 * 静态Helper类，用于建立、更新和打开数据库
 */
public class TestDBHelper extends DBHelper {

	public TestDBHelper(Context context) {
		super(context);
	}

	/**
	 * 需要持久化的数据表
	 */
	@Override
	public List<Class<? extends TableAble>> tables() {
		List<Class<? extends TableAble>> tables = new LinkedList<Class<? extends TableAble>>();

		//加入数据库表TestBean
		tables.add(TestBean.class);

		return tables;
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		try {
			//得到数据库访问对象DAO
			TestDBHelper testDBHelper = DBHelper.getHelper(TestDBHelper.class);
			Dao<TestBean, Integer> aliPayDao = testDBHelper.getDao(TestBean.class);

			//执行数据库操作
			List<TestBean> aliPayBeanList = aliPayDao.queryForAll();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
