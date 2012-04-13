package com.mokoclient.util;

public class Util {
	/**职业大类 电影电视ID 1*/
	public static final int VOCATION_MOVIES_ID = 1;
	
	/**职业大类 演员主持ID 143*/
	public static final int VOCATION_ACTOR_ID = 143;
	
	/**职业大类 音乐ID 13*/
	public static final int VOCATION_MUSIC_ID = 13;
	
	/**职业大类 摄影造型ID 28*/
	public static final int VOCATION_PHOTOGRAPHY_ID = 28;
	
	/**职业大类 模特儿ID 23*/
	public static final int VOCATION_MODEL_ID = 23;
	
	/**职业大类 艺术ID 71*/
	public static final int VOCATION_ARTS_ID = 71;
	
	/**职业大类 设计ID 41*/
	public static final int VOCATION_DESIGN_ID = 41;
	
	/**职业大类 广告传媒ID 53*/
	public static final int VOCATION_ADS_ID = 53;
	
	/**职业大类 更多行业ID 94*/
	public static final int VOCATION_MORE_ID = 94;
	
	/**
	 * moko 域名
	 */
	public static final String MOKO_DOMAIN = "http://www.moko.cc";
	public static final String MOKO_LOGIN_ACTION = MOKO_DOMAIN +"/login.action";
	public static final String USERNAMEKEY = "usermingzi";
	public static final String PASSWORDKEY = "userkey";
	public static final String USERNAME = "h231847@rppkn.com";
	public static final String PASSWORD = "h231847@rppkn.com";
	
	/**
	 * 获取指定行业指定页url
	 * @param vocationId 行业ID
	 * @param curPage 当前页
	 * @return
	 */
	public static String getVocationUrl(int vocationId, int curPage){
		return String.format(MOKO_DOMAIN + "/channels/post/%s/%s.html", vocationId, curPage);
	}
}
