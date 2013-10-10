package com.readnovel.book.base.bean;

public class Parter {
	private  String ALIPAY_PLUGIN_NAME ;
	private  String PARTNER ;
	private  String SELLER ;
	//支付宝公钥，根据商户公钥生成，在支付宝网页端完成
	private  String RSA_ALIPAY_PUBLIC ;	//商户私钥
	private  String RSA_PRIVATE ;
	public String getALIPAY_PLUGIN_NAME() {
		return ALIPAY_PLUGIN_NAME;
	}
	public void setALIPAY_PLUGIN_NAME(String aLIPAY_PLUGIN_NAME) {
		ALIPAY_PLUGIN_NAME = aLIPAY_PLUGIN_NAME;
	}
	public String getPARTNER() {
		return PARTNER;
	}
	public void setPARTNER(String pARTNER) {
		PARTNER = pARTNER;
	}
	public String getSELLER() {
		return SELLER;
	}
	public void setSELLER(String sELLER) {
		SELLER = sELLER;
	}
	public String getRSA_ALIPAY_PUBLIC() {
		return RSA_ALIPAY_PUBLIC;
	}
	public void setRSA_ALIPAY_PUBLIC(String rSA_ALIPAY_PUBLIC) {
		RSA_ALIPAY_PUBLIC = rSA_ALIPAY_PUBLIC;
	}
	public String getRSA_PRIVATE() {
		return RSA_PRIVATE;
	}
	public void setRSA_PRIVATE(String rSA_PRIVATE) {
		RSA_PRIVATE = rSA_PRIVATE;
	}
	
	
	
}
