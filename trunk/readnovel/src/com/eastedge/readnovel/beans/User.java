package com.eastedge.readnovel.beans;

/** 
 * @author ninglv 
 * @version Time：2012-3-19 下午1:57:44 
 */
public class User {
	public static String LOGIN_SUCCESS = "1";

	private String code;
	private String uid;
	private String username;
	private String email;
	private int vipLevel = 1;//vip用户等级
	private boolean isBaoYue = true;//是否是包月用户 
	private String token;
	//同步用户数据时使用
	private String logo;
	private String remain;
	private String tel;//btel用户绑定手机号，

	private String btel;//tel为预定购绑定手机号

	//	private Drawable headLogo;

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getRemain() {
		return remain;
	}

	public void setRemain(String remain) {
		this.remain = remain;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	//	public Drawable getHeadLogo() {
	//		return headLogo;
	//	}
	//
	//	public void setHeadLogo(Drawable headLogo) {
	//		this.headLogo = headLogo;
	//	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBtel() {

		if ("0".equals(btel))
			return null;

		return btel;
	}

	public void setBtel(String btel) {
		this.btel = btel;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public boolean isBaoYue() {
		return isBaoYue;
	}

	public void setBaoYue(boolean isBaoYue) {
		this.isBaoYue = isBaoYue;
	}

}
