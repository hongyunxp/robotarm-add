package com.readnovel.book.base.utils;

import com.readnovel.book.base.BookApp;
import com.readnovel.book.base.common.Constants;

/**
 * 自定义统计事件
 * 
 * @author li.li
 *
 */
public class EventUtils {
	/**
	 * 根据渠道统计安装量
	 * 
	 * @param 渠道ID
	 * 
	 * @param 书ID
	 * 
	 */
	public static void installForChannel(String channelName, int bookId) {
		String eventId = String.format(Constants.INSTALL_FOR_CHANNEL, new Object[] { channelName });

		EventLogUtils.sendEventLog(BookApp.getInstance(), eventId, String.valueOf(bookId));

	}

}
