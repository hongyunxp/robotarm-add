package com.eastedge.readnovel.beans;

import java.io.Serializable;

/**
 * 充值卡支付Bean
 * @author li.li
 *
 * Dec 13, 2012
 */
public class CardConsumeBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int CMCC = 0;//移动 
	public static final int UNICOM = 1;//联通
	public static final int TELECOM = 2;//电信

	//用户ID
	private int uId;
	// 用户名
	private String userName;
	//订单号
	private String orderId;
	//充值卡类型
	private int cardType;//1移动，2联通，3电信
	//充值 卡金额
	private int cartMoney;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	public int getCartMoney() {
		return cartMoney;
	}

	public void setCartMoney(int cartMoney) {
		this.cartMoney = cartMoney;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
