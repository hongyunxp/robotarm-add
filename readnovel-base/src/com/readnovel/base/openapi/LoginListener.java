package com.readnovel.base.openapi;

/**
 * 登陆监听器
 * @author li.li
 *
 * Mar 19, 2013
 */
public interface LoginListener {
	/**
	 * 登陆状态token
	 * @param accessToken
	 */
	public abstract void onComplete(String accessToken);
}
