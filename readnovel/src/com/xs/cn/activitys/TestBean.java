package com.xs.cn.activitys;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.readnovel.base.db.orm.TableAble;

@DatabaseTable
public class TestBean implements TableAble {
	@DatabaseField
	private String test;

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

}
