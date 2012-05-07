package com.mokoclient.core.bean;

import java.util.List;

public class PostDetailBean {
	
	private int pageCount;
	private List<String> postDetailList;
	
	public PostDetailBean(int pageCount, List<String> postDetailList){
		this.pageCount = pageCount;
		this.postDetailList = postDetailList;
	}
	
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public List<String> getPostDetailList() {
		return postDetailList;
	}
	public void setPostDetailList(List<String> postDetailList) {
		this.postDetailList = postDetailList;
	}

}
