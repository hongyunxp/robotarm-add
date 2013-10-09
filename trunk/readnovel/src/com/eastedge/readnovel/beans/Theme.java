package com.eastedge.readnovel.beans;

import java.util.ArrayList;

public class Theme {
	private Boolean isDiscount = false;
	private String imgUrl;
	private String alubm_title;
	private int nub;
	private ArrayList<NewBook> bookList = new ArrayList<NewBook>();

	public Theme() {

	}

	public Boolean getIsDiscount() {

		return isDiscount;
	}

	public void setIsDiscount(Boolean isDiscount) {
		this.isDiscount = isDiscount;
	}

	public String getAlubm_title() {
		return alubm_title;
	}

	public void setAlubm_title(String alubm_title) {
		this.alubm_title = alubm_title;
	}

	public int getNub() {
		return nub;
	}

	public void setNub(int nub) {
		this.nub = nub;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public ArrayList<NewBook> getBookList() {
		return bookList;
	}

	public void setBookList(ArrayList<NewBook> bookList) {
		this.bookList = bookList;
	}
}
