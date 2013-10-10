package com.readnovel.book.base.utils;

import java.util.LinkedList;

import android.app.Activity;
/**
 * 
 * @author li.li
 *
 * Jul 11, 2012
 */
public class CloseAll {
	private static final CloseAll instance = new CloseAll();
	private static final LinkedList<Activity> list = new LinkedList<Activity>();

	public static CloseAll getInstance() {

		return instance;
	}

	public void add(Activity closeall) {
		list.add(closeall);
	}

	public void reMove(Activity closeall) {
		list.remove(closeall);
	}

	public void close() {
		CommonUtils.finishActs(list);
//		CommonUtils.killProcess();
	}

}
