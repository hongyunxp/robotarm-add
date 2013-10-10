package com.readnovel.book.base.entity;

import java.io.Serializable;

/**
 * 章节
 * @author li.li
 *
 * Aug 2, 2012
 */
public class Chapter implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String title;
	private String fileName;
	private String num;
	private boolean isVip;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public boolean isVip() {
		return isVip;
	}

	public void setVip(boolean isVip) {
		this.isVip = isVip;
	}

}
