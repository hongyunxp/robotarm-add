package com.readnovel.book.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author li.li
 *
 * Sep 19, 2012
 */
public class DateUtils {
	//时间
	public static long SECOND = 1000;//秒
	public static long MINUTE = SECOND * 60;//分
	public static long HOUR = MINUTE * 60;//时
	public static long DAY = HOUR * 24;//天

	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd ");

	public static String format(Date date) {
		return format.format(date);
	}
	public static String format2(Date date) {
		return format2.format(date);
	}
	public static String format(long time) {
		if (String.valueOf(time).length() == 10)
			time = time * 1000;
		return format(new Date(time));
	}
	public static String format2(long time) {
		if (String.valueOf(time).length() == 10)
			time = time * 1000;
		return format2(new Date(time));
	}
	public static long getTime(String dateStr) {

		try {
			Date date = format.parse(dateStr);

			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static String now() {
		return format.format(new Date());
	}

	public static boolean isSameDay(String dateStr1, String dateStr2) {
		try {
			Date date1 = format.parse(dateStr1);
			Date date2 = format.parse(dateStr2);

			return isSameDay(date1, date2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;

	}

	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		} else {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
			return isSameDay(cal1, cal2);
		}
	}

	/**
	 * 一小时以内
	 * @param dateStr
	 * @return
	 */
	public static boolean isInside(String dateStr, long time) {

		long last = DateUtils.getTime(dateStr);
		long now = System.currentTimeMillis();
		if ((now - last) < time)
			return true;

		return false;
	}

	/**
	 * 一小时以外
	 * @param dateStr
	 * @return
	 */
	public static boolean isOutHour(String dateStr, long time) {
		long last = DateUtils.getTime(dateStr);
		long now = System.currentTimeMillis();
		if ((now - last) >= time)
			return true;

		return false;
	}

	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null)
			throw new IllegalArgumentException("The date must not be null");
		else
			return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
	}

}
