package com.eastedge.readnovel.beans;

/**
 * 订购结果bean
 * 
 * @author li.li
 */
public class SubResultBean {
	public static final String SUCCESS = "1";
	public static final String FAILS = "2";

	private String code;
	private String info;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
