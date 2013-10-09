package com.eastedge.readnovel.beans;

/**
 * 公告确认Bean
 * 
 * @author li.li
 *
 */
public class NoticeCheck {
	public static final String IS_OPEN = "1";
	public static final String IS_CLOSE = "0";

	//公告是否开户
	private String sign; //sign 为改渠道公告是否开启，1为开启，0为关闭
	//公告标题
	private String title;
	//公告内容url
	private String url;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
