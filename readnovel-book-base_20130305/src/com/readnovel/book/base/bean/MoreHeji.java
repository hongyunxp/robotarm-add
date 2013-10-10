package com.readnovel.book.base.bean;

import java.util.ArrayList;
import java.util.HashMap;

import com.readnovel.book.base.entity.More;

public class MoreHeji {
    private int totalpage ;
    private int total_heji_num ;
    private int currentpage;
    private ArrayList<More> morehejihash = new ArrayList<More>();
	public int getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}
	public int getTotal_heji_num() {
		return total_heji_num;
	}
	public void setTotal_heji_num(int total_heji_num) {
		this.total_heji_num = total_heji_num;
	}
	public int getCurrentpage() {
		return currentpage;
	}
	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}
	public ArrayList<More> getMorehejihash() {
		return morehejihash;
	}
	public void setMorehejihash(ArrayList<More> morehejihash) {
		this.morehejihash = morehejihash;
	}


}
