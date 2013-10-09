package com.eastedge.readnovel.db.orm;

import java.util.List;

import android.content.Context;

import com.eastedge.readnovel.beans.orm.OrderRecord;
import com.eastedge.readnovel.beans.orm.SupportAuthorRecord;
import com.eastedge.readnovel.beans.orm.UserLoginTrace;
import com.readnovel.base.db.orm.BaseDBHelper;
import com.readnovel.base.db.orm.TableAble;

/**
 * ORM数据库帮助类
 * 
 * @author li.li
 *
 * Mar 28, 2013
 */
public class OrmDBHelper extends BaseDBHelper {

	public OrmDBHelper(Context context) {
		super(context);
	}

	@Override
	public List<Class<? extends TableAble>> tables() {
		tables.add(UserLoginTrace.class);
		tables.add(SupportAuthorRecord.class);
		tables.add(OrderRecord.class);

		return super.tables();
	}

}
