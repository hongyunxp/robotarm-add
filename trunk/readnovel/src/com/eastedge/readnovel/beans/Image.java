package com.eastedge.readnovel.beans;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class Image {
	private String imageURL;   //图片的原地址
//	private Drawable imgDrawable;   //图片
	private int order;
	private String aid;			//列表编号
	private String type;		//排行上面图片的 类型
	private String title;
	private String article ;
//	private Uri fileUri;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
//	public Drawable getImgDrawable() {
//		return imgDrawable;
//	}
//	public void setImgDrawable(Drawable imgDrawable) {
//		this.imgDrawable = imgDrawable;
//	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
//	public Uri getFileUri() {
//		return fileUri;
//	}
//	public void setFileUri(Uri fileUri) {
//		this.fileUri = fileUri;
//	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	
}
