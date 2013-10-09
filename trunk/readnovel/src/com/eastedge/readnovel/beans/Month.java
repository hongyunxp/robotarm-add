package com.eastedge.readnovel.beans;

import java.sql.Date;

/** 
 * @author ninglv 
 * @version Time：2012-3-30 上午11:53:19 
 */
public class Month {
	private String code, month_status;
	private Date date;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMonth_status() {
		return month_status;
	}

	public void setMonth_status(String month_status) {
		this.month_status = month_status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
