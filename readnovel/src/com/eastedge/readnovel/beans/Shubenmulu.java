package com.eastedge.readnovel.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Shubenmulu implements Serializable {
	private String title;
	private String author;
	//是否完结
	private int finishflag;//0连载，1完结
	private int current_page_number;
	private int total_page_number;
	//最后更新时间
	private long lastuptime;
	//第一个vip章节
	private int fcvip;
	private HashMap<Integer, ArrayList<Chapterinfo>> muMap = new HashMap<Integer, ArrayList<Chapterinfo>>();
	private ArrayList<Chapterinfo> mulist = null;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public HashMap<Integer, ArrayList<Chapterinfo>> getMuMap() {
		return muMap;
	}

	public void setMuMap(HashMap<Integer, ArrayList<Chapterinfo>> muMap) {
		this.muMap = muMap;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getFinishflag() {
		return finishflag;
	}

	public void setFinishflag(int finishflag) {
		this.finishflag = finishflag;
	}

	public int getCurrent_page_number() {
		return current_page_number;
	}

	public void setCurrent_page_number(int current_page_number) {
		this.current_page_number = current_page_number;
	}

	public int getTotal_page_number() {
		return total_page_number;
	}

	public void setTotal_page_number(int total_page_number) {
		this.total_page_number = total_page_number;
	}

	//	public ArrayList<Chapterinfo> getMulist(int pageNb)
	//	{
	//		return muMap.get(pageNb);
	//	}

	//	public ArrayList<Chapterinfo> getMulist() {
	//		return mulist;
	//	}

	public void setMulist(ArrayList<Chapterinfo> mulist) {
		this.mulist = mulist;
	}

	public ArrayList<Chapterinfo> getMulist() {
		return mulist;
	}

	public int getFcvip() {
		return fcvip;
	}

	public void setFcvip(int fcvip) {
		this.fcvip = fcvip;
	}

	public long getLastuptime() {
		return lastuptime;
	}

	public void setLastuptime(long lastuptime) {
		this.lastuptime = lastuptime;
	}

}
