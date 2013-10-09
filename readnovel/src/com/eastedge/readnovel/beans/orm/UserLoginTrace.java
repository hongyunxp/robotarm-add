package com.eastedge.readnovel.beans.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.readnovel.base.db.orm.TableAble;

/**
 * 用户登陆记录表
 * @author li.li
 *
 * Mar 28, 2013
 */
@DatabaseTable(tableName = "user_login_trace")
public class UserLoginTrace implements TableAble {
	public static final String ID = "id";
	public static final String USER_ID = "user_id";
	public static final String LOGIN_TYPE = "login_type";
	public static final String LAST_LOGIN_TIME = "last_login_time";

	@DatabaseField(generatedId = true, useGetSet = true, columnName = ID)
	private int id;//主键
	@DatabaseField(useGetSet = true, columnName = USER_ID)
	private int userId;//用户id
	@DatabaseField(useGetSet = true, columnName = LOGIN_TYPE)
	private int loginType;
	@DatabaseField(useGetSet = true, columnName = LAST_LOGIN_TIME)
	private long lastLoginTime;//最后登陆时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

}
