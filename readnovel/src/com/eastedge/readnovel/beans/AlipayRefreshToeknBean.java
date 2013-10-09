package com.eastedge.readnovel.beans;

public class AlipayRefreshToeknBean {
	private String access_token;
	private String refresh_token;
	private String access_token_expires_in;
	private String refresh_token_expires_in;
	private String code;

	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}

	/**
	 * @param access_token the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	/**
	 * @return the refresh_token
	 */
	public String getRefresh_token() {
		return refresh_token;
	}

	/**
	 * @param refresh_token the refresh_token to set
	 */
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	/**
	 * @return the access_token_expires_in
	 */
	public String getAccess_token_expires_in() {
		return access_token_expires_in;
	}

	/**
	 * @param access_token_expires_in the access_token_expires_in to set
	 */
	public void setAccess_token_expires_in(String access_token_expires_in) {
		this.access_token_expires_in = access_token_expires_in;
	}

	/**
	 * @return the refresh_token_expires_in
	 */
	public String getRefresh_token_expires_in() {
		return refresh_token_expires_in;
	}

	/**
	 * @param refresh_token_expires_in the refresh_token_expires_in to set
	 */
	public void setRefresh_token_expires_in(String refresh_token_expires_in) {
		this.refresh_token_expires_in = refresh_token_expires_in;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

}
