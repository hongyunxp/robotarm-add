package com.eastedge.readnovel.beans;

import java.io.Serializable;

public class Chapterinfo implements Serializable {
	private String id;
	//章节名
	private String subhead;
	//是否VIP
	private int is_vip;
	//字数
	private int word_count;
	private String create_time;
	private String update_time;
	private int status;
	//章节编号
	private int displayorder;
	//章节起始位置
	private int posi;
	//章节长度
	private int len;
	//下一章节ID
	private String nextid;
	//下一章节是否是vip
	private int nextvip;
	//上一章节ID
	private String preid;
	private String textjj;
	//上一章节是否是vip
	private int previp;
	private int curF;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubhead() {
		return subhead;
	}

	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}

	public int getIs_vip() {
		return is_vip;
	}

	public void setIs_vip(int is_vip) {
		this.is_vip = is_vip;
	}

	public int getWord_count() {
		return word_count;
	}

	public void setWord_count(int word_count) {
		this.word_count = word_count;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(int displayorder) {
		this.displayorder = displayorder;
	}

	public int getPosi() {
		return posi;
	}

	public void setPosi(int posi) {
		this.posi = posi;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public int getCurF() {
		return curF;
	}

	public void setCurF(int curF) {
		this.curF = curF;
	}

	public String getNextid() {
		return nextid;
	}

	public void setNextid(String nextid) {
		this.nextid = nextid;
	}

	public String getPreid() {
		return preid;
	}

	public void setPreid(String preid) {
		this.preid = preid;
	}

	public int getNextvip() {
		return nextvip;
	}

	public void setNextvip(int nextvip) {
		this.nextvip = nextvip;
	}

	public int getPrevip() {
		return previp;
	}

	public void setPrevip(int previp) {
		this.previp = previp;
	}

	public String getTextjj() {
		return textjj;
	}

	public void setTextjj(String textjj) {
		this.textjj = textjj;
	}

}
