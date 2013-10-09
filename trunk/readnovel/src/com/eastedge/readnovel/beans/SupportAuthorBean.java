package com.eastedge.readnovel.beans;

public class SupportAuthorBean {
	public static final String SUCCESS = "1";
	public static final String FAILS = "2";

	private String code;
	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
