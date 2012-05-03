package com.mokoclient.core.bean;

public class PostBean {
	
	private String title;
	private String coverUrl;
	private String detailUrl;
	
	public PostBean(String title, String coverUrl, String detailUrl){
		this.title = title;
		this.coverUrl = coverUrl;
		this.detailUrl = detailUrl;
	}
	
	public String getCoverUrl() {
		return coverUrl;
	}
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
