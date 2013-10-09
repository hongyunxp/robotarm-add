package com.eastedge.readnovel.beans;

import java.util.HashSet;
import java.util.Set;

public class Subed_chapters_info
{
	private String info;
	private HashSet<String> subed_textid_list;

	public HashSet<String> getSubed_textid_list()
	{
		return subed_textid_list;
	}

	public void setSubed_textid_list(HashSet<String> subed_textid_list)
	{
		this.subed_textid_list = subed_textid_list;
	}

	public String getInfo()
	{
		return info;
	}

	public void setInfo(String info)
	{
		this.info = info;
	}
}
