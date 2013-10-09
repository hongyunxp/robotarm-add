package com.eastedge.readnovel.beans;

import java.util.ArrayList;

public class OrderAllMsg {
	private String remain; //余额
	private String price; //单价
	private String total_price; //总价
	private String total_vip_word; //总字数
	private String total_chapter; //总章节数
	private ArrayList<String> chapterList;
	private long curtime;//服务器返回的当前时间
	private long overtime;//服务器返回的过期时间
	private String discountprice;// 折扣价
	private float discountCount;// 打折率
	private Boolean isDiscount;// 是否是折扣书

	public String getDiscountprice() {
		return discountprice;
	}

	public void setDiscountprice(String discountprice) {
		this.discountprice = discountprice;
	}

	public float getDiscountCount() {
		return discountCount;
	}

	public void setDiscountCount(float discountCount) {
		this.discountCount = discountCount;
	}

	public Boolean getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(Boolean isDiscount) {
		this.isDiscount = isDiscount;
	}

	public long getCurtime() {
		return curtime;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTotal_price() {
		return total_price;
	}

	public void setTotal_price(String totalPrice) {
		total_price = totalPrice;
	}

	public String getTotal_vip_word() {
		return total_vip_word;
	}

	public void setTotal_vip_word(String totalVipWord) {
		total_vip_word = totalVipWord;
	}

	public String getTotal_chapter() {
		return total_chapter;
	}

	public void setTotal_chapter(String totalChapter) {
		total_chapter = totalChapter;
	}

	public ArrayList<String> getChapterList() {
		return chapterList;
	}

	public void setChapterList(ArrayList<String> chapterList) {
		this.chapterList = chapterList;
	}

}
