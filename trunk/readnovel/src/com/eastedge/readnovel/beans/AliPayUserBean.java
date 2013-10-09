package com.eastedge.readnovel.beans;

/**
 * 支付宝用户信息
 * @author li.li
 *
 * Jul 18, 2013
 */
public class AliPayUserBean {
	private AliPayInnerUserBean alipay_user_info;
	private AliPayOuterUserBean our_user_info;
	private String code;

	/**
	 * @return the alipay_user_info
	 */
	public AliPayInnerUserBean getAlipay_user_info() {
		return alipay_user_info;
	}

	/**
	 * @param alipay_user_info the alipay_user_info to set
	 */
	public void setAlipay_user_info(AliPayInnerUserBean alipay_user_info) {
		this.alipay_user_info = alipay_user_info;
	}

	/**
	 * @return the our_user_info
	 */
	public AliPayOuterUserBean getOur_user_info() {
		return our_user_info;
	}

	/**
	 * @param our_user_info the our_user_info to set
	 */
	public void setOur_user_info(AliPayOuterUserBean our_user_info) {
		this.our_user_info = our_user_info;
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

	public static final class AliPayInnerUserBean {
		private String is_bank_auth;
		private String is_mobile_auth;
		private String is_licence_auth;
		private String email;
		private String user_id;
		private String user_status;
		private String gender;
		private String user_type_value;
		private String is_certified;
		private String real_name;
		private String is_id_auth;

		/**
		 * @return the is_bank_auth
		 */
		public String getIs_bank_auth() {
			return is_bank_auth;
		}

		/**
		 * @param is_bank_auth the is_bank_auth to set
		 */
		public void setIs_bank_auth(String is_bank_auth) {
			this.is_bank_auth = is_bank_auth;
		}

		/**
		 * @return the is_mobile_auth
		 */
		public String getIs_mobile_auth() {
			return is_mobile_auth;
		}

		/**
		 * @param is_mobile_auth the is_mobile_auth to set
		 */
		public void setIs_mobile_auth(String is_mobile_auth) {
			this.is_mobile_auth = is_mobile_auth;
		}

		/**
		 * @return the is_licence_auth
		 */
		public String getIs_licence_auth() {
			return is_licence_auth;
		}

		/**
		 * @param is_licence_auth the is_licence_auth to set
		 */
		public void setIs_licence_auth(String is_licence_auth) {
			this.is_licence_auth = is_licence_auth;
		}

		/**
		 * @return the email
		 */
		public String getEmail() {
			return email;
		}

		/**
		 * @param email the email to set
		 */
		public void setEmail(String email) {
			this.email = email;
		}

		/**
		 * @return the user_id
		 */
		public String getUser_id() {
			return user_id;
		}

		/**
		 * @param user_id the user_id to set
		 */
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		/**
		 * @return the user_status
		 */
		public String getUser_status() {
			return user_status;
		}

		/**
		 * @param user_status the user_status to set
		 */
		public void setUser_status(String user_status) {
			this.user_status = user_status;
		}

		/**
		 * @return the gender
		 */
		public String getGender() {
			return gender;
		}

		/**
		 * @param gender the gender to set
		 */
		public void setGender(String gender) {
			this.gender = gender;
		}

		/**
		 * @return the user_type_value
		 */
		public String getUser_type_value() {
			return user_type_value;
		}

		/**
		 * @param user_type_value the user_type_value to set
		 */
		public void setUser_type_value(String user_type_value) {
			this.user_type_value = user_type_value;
		}

		/**
		 * @return the is_certified
		 */
		public String getIs_certified() {
			return is_certified;
		}

		/**
		 * @param is_certified the is_certified to set
		 */
		public void setIs_certified(String is_certified) {
			this.is_certified = is_certified;
		}

		/**
		 * @return the real_name
		 */
		public String getReal_name() {
			return real_name;
		}

		/**
		 * @param real_name the real_name to set
		 */
		public void setReal_name(String real_name) {
			this.real_name = real_name;
		}

		/**
		 * @return the is_id_auth
		 */
		public String getIs_id_auth() {
			return is_id_auth;
		}

		/**
		 * @param is_id_auth the is_id_auth to set
		 */
		public void setIs_id_auth(String is_id_auth) {
			this.is_id_auth = is_id_auth;
		}

	}

	public static class AliPayOuterUserBean {
		private String userid;
		private String username;
		private String email;
		private String tel;
		private String is_baoyue;
		private String remain;
		private String vip_level;
		private String logo;
		private String uid;
		private String code;

		private String mobile;//tel为预定购绑定手机号

		/**
		 * @return the userid
		 */
		public String getUserid() {
			return userid;
		}

		/**
		 * @param userid the userid to set
		 */
		public void setUserid(String userid) {
			this.userid = userid;
		}

		/**
		 * @return the username
		 */
		public String getUsername() {
			return username;
		}

		/**
		 * @param username the username to set
		 */
		public void setUsername(String username) {
			this.username = username;
		}

		/**
		 * @return the email
		 */
		public String getEmail() {
			return email;
		}

		/**
		 * @param email the email to set
		 */
		public void setEmail(String email) {
			this.email = email;
		}

		/**
		 * @return the tel
		 */
		public String getTel() {
			return tel;
		}

		/**
		 * @param tel the tel to set
		 */
		public void setTel(String tel) {
			this.tel = tel;
		}

		/**
		 * @return the is_baoyue
		 */
		public String getIs_baoyue() {
			return is_baoyue;
		}

		/**
		 * @param is_baoyue the is_baoyue to set
		 */
		public void setIs_baoyue(String is_baoyue) {
			this.is_baoyue = is_baoyue;
		}

		/**
		 * @return the remain
		 */
		public String getRemain() {
			return remain;
		}

		/**
		 * @param remain the remain to set
		 */
		public void setRemain(String remain) {
			this.remain = remain;
		}

		/**
		 * @return the vip_level
		 */
		public String getVip_level() {
			return vip_level;
		}

		/**
		 * @param vip_level the vip_level to set
		 */
		public void setVip_level(String vip_level) {
			this.vip_level = vip_level;
		}

		/**
		 * @return the logo
		 */
		public String getLogo() {
			return logo;
		}

		/**
		 * @param logo the logo to set
		 */
		public void setLogo(String logo) {
			this.logo = logo;
		}

		/**
		 * @return the uid
		 */
		public String getUid() {
			return uid;
		}

		/**
		 * @param uid the uid to set
		 */
		public void setUid(String uid) {
			this.uid = uid;
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

		/**
		 * @return the mobile
		 */
		public String getMobile() {
			if ("0".equals(mobile))
				return null;

			return mobile;
		}

		/**
		 * @param mobile the mobile to set
		 */
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

	}

}
