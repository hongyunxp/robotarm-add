package com.eastedge.readnovel.beans;

public class RDBook {

	private long id;
	private String articleId;
	private String textId;
	//章节名
	private String textTitle;
	//本地书路径
	private String bookFile;
	//字数
	private int wordCount;
	private int order;
	private String text;
	private String code;
	//是否VIP
	private int isVip;
	private String preId;
	private int preVip;
	private String nextId;
	private int nextVip;
	private OrderMsg orderMsg;
	private int posi;
	private String bookName;
	private int isOL;
	private int finishflag;
	// 是否是折扣书
	private int is_discount_book;
	private double totalprice;
	// 折扣比例
	private int discount;

	public double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(double totalprice) {
		this.totalprice = totalprice;
	}

	public String getTextId() {
		return textId;
	}

	public int getIs_discount_book() {
		return is_discount_book;
	}

	public void setIs_discount_book(int is_discount_book) {
		this.is_discount_book = is_discount_book;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public void setTextId(String textId) {
		this.textId = textId;
	}

	public String getTextTitle() {
		return textTitle;
	}

	public void setTextTitle(String textTitle) {
		this.textTitle = textTitle;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getIsVip() {
		return isVip;
	}

	public void setIsVip(int isVip) {
		this.isVip = isVip;
	}

	public String getPreId() {
		return preId;
	}

	public void setPreId(String preId) {
		this.preId = preId;
	}

	public String getNextId() {
		return nextId;
	}

	public void setNextId(String nextId) {
		this.nextId = nextId;
	}

	public int getPreVip() {
		return preVip;
	}

	public void setPreVip(int preVip) {
		this.preVip = preVip;
	}

	public int getNextVip() {
		return nextVip;
	}

	public void setNextVip(int nextVip) {
		this.nextVip = nextVip;
	}

	public OrderMsg getOrderMsg() {
		return orderMsg;
	}

	public void setOrderMsg(OrderMsg orderMsg) {
		this.orderMsg = orderMsg;
	}

	public int getPosi() {
		return posi;
	}

	public void setPosi(int posi) {
		this.posi = posi;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIsOL() {
		return isOL;
	}

	public void setIsOL(int isOL) {
		this.isOL = isOL;
	}

	public String getBookFile() {
		return bookFile;
	}

	public void setBookFile(String bookFile) {
		this.bookFile = bookFile;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getFinishflag() {
		return finishflag;
	}

	public void setFinishflag(int finishflag) {
		this.finishflag = finishflag;
	}

}
