package com.readnovel.book.base.bean;

public class VipPayInterval {
	public static final int UN_ORDER = 0;
	public static final int ORDER_INPROGRESS = 1;
	public static final int UN_CHECK = 2;
	public static final int PAY_SUCCESS = 3;
	public static final int PAY_FAIL = 4;

	private int id;
	private String bookIds;
	private int state;//0未订购,1订购中,2未验证，3订购成功，4订购失败
	private String upTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBookIds() {
		return bookIds;
	}

	public void setBookIds(String bookIds) {
		this.bookIds = bookIds;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

}
