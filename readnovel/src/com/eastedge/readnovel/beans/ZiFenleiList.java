package com.eastedge.readnovel.beans;

import java.util.ArrayList;

public class ZiFenleiList {
	
	private int bkcount;		//总数量
	private int curpage;		//当前页
	private ArrayList<NewBook> bklist=null;  //书集合
	public int getBkcount() {
		return bkcount;
	}
	public void setBkcount(int bkcount) {
		this.bkcount = bkcount;
	}
	public ArrayList<NewBook> getBklist() {
		return bklist;
	}
	public void setBklist(ArrayList<NewBook> bklist) {
		this.bklist = bklist;
	}
	public int getCurpage() {
		return curpage;
	}
	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}
	
	
	
}
