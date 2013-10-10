package com.readnovel.book.base.entity;

public class BookTag {
	String fileName;
	int lastRead;
	String time;
	String foreText;
	String percent;
	int fontSize;
	String pagenum;
	public String getPagenum() {
		return pagenum;
	}

	public void setPagenum(String pagenum) {
		this.pagenum = pagenum;
	}

	public String getChapterFileName() {
		return chapterFileName;
	}

	public void setChapterFileName(String chapterFileName) {
		this.chapterFileName = chapterFileName;
	}

	String chapterFileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getLastRead() {
		return lastRead;
	}

	public void setLastRead(int lastRead) {
		this.lastRead = lastRead;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getForeText() {
		return foreText;
	}

	public void setForeText(String foreText) {
		this.foreText = foreText;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
}
