package com.eastedge.readnovel.beans;

public class Banbenxinxi
{
	private String version;
	private String updatetime;
	private String appurl;
	private String[] features;
	private int versionCode;
	private boolean isforce;
	private int[] wrongversion;
	public boolean getIsforce() {
		return isforce;
	}

	public void setIsforce(boolean isforce) {
		this.isforce = isforce;
	}

	public int[] getWrongversion() {
		return wrongversion;
	}

	public void setWrongversion(int[] wrongversion) {
		this.wrongversion = wrongversion;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getUpdatetime()
	{
		return updatetime;
	}

	public void setUpdatetime(String updatetime)
	{
		this.updatetime = updatetime;
	}

	public String getAppurl()
	{
		return appurl;
	}

	public void setAppurl(String appurl)
	{
		this.appurl = appurl;
	}

	public String[] getFeatures()
	{
		return features;
	}

	public void setFeatures(String[] features)
	{
		this.features = features;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

}
