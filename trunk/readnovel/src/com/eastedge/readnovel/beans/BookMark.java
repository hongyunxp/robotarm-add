package com.eastedge.readnovel.beans;

public class BookMark
{
	private long id = -1;
	private String articleid; // 作品id
	private String textid; // 章节id
	private String texttitle; // 章节标题
	private String textjj; // 作品简介
	private long time; // 时间
	private int location; // 位置
	private int length; // 长度
	private int isd; // 是否是本地书
	private int isV; // 是否为VIP

	public int getIsV()
	{
		return isV;
	}

	public void setIsV(int isV)
	{
		this.isV = isV;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getArticleid()
	{
		return articleid;
	}

	public void setArticleid(String articleid)
	{
		this.articleid = articleid;
	}

	public String getTextid()
	{
		return textid;
	}

	public void setTextid(String textid)
	{
		this.textid = textid;
	}

	public String getTexttitle()
	{
		return texttitle;
	}

	public void setTexttitle(String texttitle)
	{
		this.texttitle = texttitle;
	}

	public String getTextjj()
	{
		return textjj;
	}

	public void setTextjj(String textjj)
	{
		this.textjj = textjj;
	}

	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	public int getLocation()
	{
		return location;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public void setLocation(int location)
	{
		this.location = location;
	}

	public int getIsd()
	{
		return isd;
	}

	public void setIsd(int isd)
	{
		this.isd = isd;
	}

}
