package com.eastedge.readnovel.beans;

import java.util.ArrayList;

public class Shuping_huifu
{
	private int total_number;
	private int total_page_number;
	private int cur_page_number;
	private ArrayList<Shuping_huifuinfo> HFlist = null;

	public int getTotal_number()
	{
		return total_number;
	}

	public void setTotal_number(int total_number)
	{
		this.total_number = total_number;
	}

	public int getTotal_page_number()
	{
		return total_page_number;
	}

	public void setTotal_page_number(int total_page_number)
	{
		this.total_page_number = total_page_number;
	}

	public int getCur_page_number()
	{
		return cur_page_number;
	}

	public void setCur_page_number(int cur_page_number)
	{
		this.cur_page_number = cur_page_number;
	}

	public ArrayList<Shuping_huifuinfo> getHFlist()
	{
		return HFlist;
	}

	public void setHFlist(ArrayList<Shuping_huifuinfo> hFlist)
	{
		HFlist = hFlist;
	}

}
