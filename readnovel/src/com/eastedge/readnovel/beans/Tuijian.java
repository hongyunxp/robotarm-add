package com.eastedge.readnovel.beans;

import com.eastedge.readnovel.common.Constants;

public class Tuijian {
	private String title;
	private String intro;
	private String displayorder;
	private String addtime;
	private String updatetime;
	private String logo;
	private String logotime;
	private String imageURL;
	private String id;
	private String auther;

	public String getAuther() {
		return auther;
	}

	public void setAuther(String auther) {
		this.auther = auther;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(String displayorder) {
		this.displayorder = displayorder;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		imageURL = Constants.imgURL + logo.trim();
		this.logo = logo;
	}

	public String getLogotime() {
		return logotime;
	}

	public void setLogotime(String logotime) {
		this.logotime = logotime;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
