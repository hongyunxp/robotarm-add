package com.eastedge.readnovel.beans;

public class OrderMsg {
	private String remain; //余额
	private String calPrice; //总价
	private String wordCount; //总字数
	private String price; //单价
	private String text;
	private long curtime;//服务器返回的当前时间
	private long overtime;//服务器返回的过期时间
    private float discount;
	public long getCurtime() {
		return curtime;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public void setCurtime(long curtime) {
		this.curtime = curtime;
	}

	public long getOvertime() {
		return overtime;
	}

	public void setOvertime(long overtime) {
		this.overtime = overtime;
	}

	public String getRemain() {
		return remain;
	}

	public void setRemain(String remain) {
		this.remain = remain;
	}

	public String getCalPrice() {
		return calPrice;
	}

	public void setCalPrice(String calPrice) {
		this.calPrice = calPrice;
	}

	public String getWordCount() {
		return wordCount;
	}

	public void setWordCount(String wordCount) {
		this.wordCount = wordCount;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
