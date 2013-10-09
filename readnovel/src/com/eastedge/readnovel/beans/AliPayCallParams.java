package com.eastedge.readnovel.beans;

public class AliPayCallParams {
	private String alipayUserId;
	private String authCode;
	private String appId;
	private String version;
	private String alipayClientVersion;

	public AliPayCallParams(String alipayUserId, String authCode, String appId, String version, String alipayClientVersion) {
		this.alipayUserId = alipayUserId;
		this.authCode = authCode;
		this.appId = appId;
		this.version = version;
		this.alipayClientVersion = alipayClientVersion;
	}

	/**
	 * @return the alipayUserId
	 */
	public String getAlipayUserId() {
		return alipayUserId;
	}

	/**
	 * @param alipayUserId the alipayUserId to set
	 */
	public void setAlipayUserId(String alipayUserId) {
		this.alipayUserId = alipayUserId;
	}

	/**
	 * @return the authCode
	 */
	public String getAuthCode() {
		return authCode;
	}

	/**
	 * @param authCode the authCode to set
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the alipayClientVersion
	 */
	public String getAlipayClientVersion() {
		return alipayClientVersion;
	}

	/**
	 * @param alipayClientVersion the alipayClientVersion to set
	 */
	public void setAlipayClientVersion(String alipayClientVersion) {
		this.alipayClientVersion = alipayClientVersion;
	}

}
