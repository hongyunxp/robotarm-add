package com.eastedge.readnovel.beans;

import java.util.ArrayList;

public class FenleiList
{
	private ArrayList<BookType> childFl = null; // 子分类列表
	private ArrayList<NewBook> fllist = null; // 分类列表下的书
	private int bkcount; // 当前分类总数

	public ArrayList<BookType> getChildFl()
	{
		return childFl;
	}

	public void setChildFl(ArrayList<BookType> childFl)
	{
		this.childFl = childFl;
	}

	public ArrayList<NewBook> getFllist()
	{
		return fllist;
	}

	public void setFllist(ArrayList<NewBook> fllist)
	{
		this.fllist = fllist;
	}

	public int getBkcount()
	{
		return bkcount;
	}

	public void setBkcount(int bkcount)
	{
		this.bkcount = bkcount;
	}

}
