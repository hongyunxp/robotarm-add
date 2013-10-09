package com.readnovel.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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

	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat format_hm = new SimpleDateFormat("HH:mm");

	public static String format(Date date) {
		return format.format(date);
	}

	public static String formatHM(Date date) {
		return format_hm.format(date);
	}

	public static String format(long time) {
		if (String.valueOf(time).length() == 10)
			time = time * 1000;
		return format(new Date(time));
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

	/**
	 * 是否是同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(long data1, long data2) {
		try {
			Date date1 = new Date(data1);
			Date date2 = new Date(data2);

			return isSameDay(date1, date2);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * 是否是同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
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

	/**
	 * 是否是同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
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
	 * time以内
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

	public static boolean after(Date date) {
		long time = date.getTime();
		long now = System.currentTimeMillis();
		if (now > time)
			return true;

		return false;
	}

	/**
	 * 定时任务，精确到秒
	 * @param runnable 执行的任务
	 * @param hour 时（24小时制）
	 * @param minute 分
	 * @param second 秒
	 */
	public static void schedule(final Runnable runnable, final int hour, final int minute, final int second) {
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);

		schedule(runnable, cal.getTime());
	}

	/**
	 * 定时任务
	 * @param runnable 执行的任务
	 * @param date 执行的时间
	 */
	public static Timer schedule(final Runnable runnable, final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		Timer mTimer = new Timer(false);//非守候线程
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				Calendar now = Calendar.getInstance();
				now.add(Calendar.MINUTE, -1);//允许1分钟误差

				//首次执行时间过了首次不执行
				if (now.after(cal)) {
					LogUtils.info("时间过了首次不执行");
					return;
				}

				runnable.run();//执行操作
			}
		};

		mTimer.schedule(timerTask, cal.getTime());

		return mTimer;
	}
}
