package com.readnovel.book.base.bean;

public class PayRecord {
	private int id;
	private int chapterId;
	private int intervalRecordId;
	private String addTime;
	private float fee;
	
	public float getFee() {
		return fee;
	}

	public void setFee(float fee) {
		this.fee = fee;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIntervalRecordId() {
		return intervalRecordId;
	}

	public void setIntervalRecordId(int intervalRecordId) {
		this.intervalRecordId = intervalRecordId;
	}

	public int getChapterId() {
		return chapterId;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	

}
