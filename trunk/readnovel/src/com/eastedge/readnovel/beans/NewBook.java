package com.eastedge.readnovel.beans;

import java.io.Serializable;

public class NewBook implements Serializable {
	private static final long serialVersionUID = -8133791105886096010L;

	private String totalviews; //点击数
	private String articleid; //作品id  
	private String title; //作品名
	private String imgURL; //封面照片url
	//	private Drawable imgDrawable;		//图片
	//	private Uri fileUri;		//图片
	private String finishflag; //是否完结(1 完结 0  连载)
	private String sortname; //分类名
	private String author; //作者
	// 折扣区所需要的参数
	private String discount;

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getTotalviews() {
		return totalviews;
	}

	public void setTotalviews(String totalviews) {
		this.totalviews = totalviews;
	}

	public String getArticleid() {
		return articleid;
	}

	public void setArticleid(String articleid) {
		this.articleid = articleid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFinishflag() {
		return finishflag;
	}

	public void setFinishflag(String finishflag) {
		this.finishflag = finishflag;
	}

	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}
	//	public Drawable getImgDrawable() {
	//		return imgDrawable;
	//	}
	//	public void setImgDrawable(Drawable imgDrawable) {
	//		this.imgDrawable = imgDrawable;
	//	}
	// public Uri getFileUri() {
	// return fileUri;
	// }
	// public void setFileUri(Uri fileUri) {
	// this.fileUri = fileUri;
	// }

}
