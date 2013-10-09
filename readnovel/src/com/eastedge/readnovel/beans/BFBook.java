package com.eastedge.readnovel.beans;

import android.graphics.drawable.Drawable;
import android.view.View;

public class BFBook {

	private long id=-1;
	private String articleid;			//作品id
	private String imgFile;				//本地封面路径
	private String bookFile;			//本地书
	private String bookURL;				//下载书路径
	private  String title;				//作品名
	private int finishFlag;				//是否完结 0连载 1为完结
	private Drawable bookDrawable;		//书封面
	private String wordtotal;			//作品总字数
	private String imagefname;			//封面网络路径
	private long lastuptime;                   
	private int isUp;					//是否需要更新
	private int isVip;					//以下为vip书架新增字段
	private String uid;
	private String last_text_time;
	private String totalviews;
	private String author;
	private String sortname;
	
	public String getLast_text_time() {
		return last_text_time;
	}
	public void setLast_text_time(String last_text_time) {
		this.last_text_time = last_text_time;
	}
	public String getTotalviews() {
		return totalviews;
	}
	public void setTotalviews(String totalviews) {
		this.totalviews = totalviews;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
//	public String getFinishflag() {
//		return finishflag;
//	}
//	public void setFinishflag(String finishflag) {
//		this.finishflag = finishflag;
//	}
	public String getSortname() {
		return sortname;
	}
	public void setSortname(String sortname) {
		this.sortname = sortname;
	}
	private int isdelete=View.GONE;
	private int isonDown=View.GONE;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getArticleid() {
		return articleid;
	}
	public void setArticleid(String articleid) {
		this.articleid = articleid;
	}
	public String getImgFile() {
		return imgFile;
	}
	public void setImgFile(String imgFile) {
		this.imgFile = imgFile;
	}
	
	public String getBookURL() {
		return bookURL;
	}
	public void setBookURL(String bookURL) {
		this.bookURL = bookURL;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBookFile() {
		return bookFile;
	}
	public void setBookFile(String bookFile) {
		this.bookFile = bookFile;
	}
	public int getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}
	public int getIsonDown() {
		return isonDown;
	}
	public void setIsonDown(int isonDown) {
		this.isonDown = isonDown;
	}
	public Drawable getBookDrawable() {
		return bookDrawable;
	}
	public void setBookDrawable(Drawable bookDrawable) {
		this.bookDrawable = bookDrawable;
	}
	public String getWordtotal() {
		return wordtotal;
	}
	public void setWordtotal(String wordtotal) {
		this.wordtotal = wordtotal;
	}
	public String getImagefname() {
		return imagefname;
	}
	public void setImagefname(String imagefname) {
		this.imagefname = imagefname;
	}
	public int getIsVip() {
		return isVip;
	}
	public void setIsVip(int isVip) {
		this.isVip = isVip;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public long getLastuptime() {
		return lastuptime;
	}
	public void setLastuptime(long lastuptime) {
		this.lastuptime = lastuptime;
	}
	public int getIsUp() {
		return isUp;
	}
	public void setIsUp(int isUp) {
		this.isUp = isUp;
	}
	public int getFinishFlag() {
		return finishFlag;
	}
	public void setFinishFlag(int finishFlag) {
		this.finishFlag = finishFlag;
	}
	
	
}
