package com.eastedge.readnovel.beans;

import java.util.ArrayList;

public class SearchResult {
	
	private boolean noResult;	//是否有返回值 true为没有
	private int bkcount;		//总数量
	private int curpage;		//当前页
	private ArrayList<NewBook> bookList;

	
	public boolean isNoResult() {
		return noResult;
	}
	public void setNoResult(boolean noResult) {
		this.noResult = noResult;
	}
	public ArrayList<NewBook> getBookList() {
		return bookList;
	}
	public void setBookList(ArrayList<NewBook> bookList) {
		this.bookList = bookList;
	}
	public int getBkcount() {
		return bkcount;
	}
	public void setBkcount(int bkcount) {
		this.bkcount = bkcount;
	}
	public int getCurpage() {
		return curpage;
	}
	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}
	
	
	
}
