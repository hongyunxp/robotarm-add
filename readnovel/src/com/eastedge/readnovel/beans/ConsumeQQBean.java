package com.eastedge.readnovel.beans;

import java.io.Serializable;

public class ConsumeQQBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//用户ID
	private int uId;
	// 用户名
	private String userName;
	//订单号
	private String orderId;
	//支付金额
	private int payMoney;
	//卡号
	private String cardId;
	//密码
	private String cardPwd;

	public int getuId() {
		return uId;
	}

	public void setuId(int uId) {
		this.uId = uId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(int payMoney) {
		this.payMoney = payMoney;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getCardPwd() {
		return cardPwd;
	}

	public void setCardPwd(String cardPwd) {
		this.cardPwd = cardPwd;
	}

}
