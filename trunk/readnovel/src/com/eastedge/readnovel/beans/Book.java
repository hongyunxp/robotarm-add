package com.eastedge.readnovel.beans;

import android.view.View;

public class Book {

	private String bookId;	 //编号
	private String bookName;	 //书名
	private String bookType;  //类型
	private String bookEditer;  //作者
	private long   bookPoint;	//点击量
	private int isdelete=View.GONE;
	public Book(){
		
	}

	public Book(String bookName){
		this.bookName=bookName;
	}
	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookType() {
		return bookType;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
	}

	public String getBookEditer() {
		return bookEditer;
	}

	public void setBookEditer(String bookEditer) {
		this.bookEditer = bookEditer;
	}

	public long getBookPoint() {
		return bookPoint;
	}

	public void setBookPoint(long bookPoint) {
		this.bookPoint = bookPoint;
	}

	public int getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}


	
	
}
