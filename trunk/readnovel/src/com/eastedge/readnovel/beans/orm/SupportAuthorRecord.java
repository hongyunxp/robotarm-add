package com.eastedge.readnovel.beans.orm;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.readnovel.base.db.orm.TableAble;

/**
 * 支持作者（送红包）
 * @author li.li
 *
 * Aug 12, 2013
 */
@DatabaseTable(tableName = "support_author_record")
public class SupportAuthorRecord implements TableAble {
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String ID = "id";
	public static final String BOOK_ID = "book_id";
	public static final String USER_ID = "user_id";
	public static final String ADD_TIME = "add_time";

	@DatabaseField(generatedId = true, useGetSet = true, columnName = ID)
	private int id;//主键
	@DatabaseField(useGetSet = true, columnName = USER_ID)
	private String userId;//用户id
	@DatabaseField(useGetSet = true, columnName = BOOK_ID)
	private String bookId;//书id
	@DatabaseField(useGetSet = true, columnName = ADD_TIME, dataType = DataType.DATE_STRING, format = DATE_FORMAT)
	private Date addTime;//加入时间

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the bookId
	 */
	public String getBookId() {
		return bookId;
	}

	/**
	 * @param bookId the bookId to set
	 */
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	/**
	 * @return the addTime
	 */
	public Date getAddTime() {
		return addTime;
	}

	/**
	 * @param addTime the addTime to set
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}
