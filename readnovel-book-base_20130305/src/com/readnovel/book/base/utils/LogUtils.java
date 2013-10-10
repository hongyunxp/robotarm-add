package com.readnovel.book.base.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Build;
import android.util.Log;

import com.mobclick.android.MobclickAgent;

/**
 * log工具类
 */
public class LogUtils {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	//日志输出开关
	private static final boolean LOG_IS_ON = true;
	//日志输出级别
	private static final int LOG_LEVEL = 3;//DEBUG:3,INFO:4,ERROR:6

	/**
	 * 输出 DEBUG 级别日志
	 */
	public static void debug(String msg) {
		doLog(Log.DEBUG, msg, null);
	}

	/**
	 * 输出 INOF 级别日志
	 */
	public static void info(String msg) {
		doLog(Log.INFO, msg, null);
	}

	/**
	 * 输出 ERROR 级别日志
	 */
	public static void error(String msg, Throwable e) {
		doLog(Log.ERROR, msg, e);

		sendLog(msg, e);//向服务器发送日志
	}

	/**
	 * ***************************************************************************
	 * 以下私有方法不对外
	 * ***************************************************************************
	 */

	/**
	 * 控制台输出日志
	 */
	private static void doLog(int logLevel, String logMessage, Throwable throwable) {
		if (!LOG_IS_ON || logLevel < LOG_LEVEL)
			return;

		String tag = getLogTag();

		switch (logLevel) {
		case Log.DEBUG:
			Log.d(tag, logMessage, throwable);
			break;
		case Log.INFO:
			break;
		case Log.ERROR:
			Log.e(tag, logMessage, throwable);
			break;
		}
	}

	/**
	 * 得到tag
	 * @return
	 */
	private static String getLogTag() {
		StackTraceElement stackTraceElement = getStackTraceElement(6);
		String className = stackTraceElement.getClassName();
		String methodName = stackTraceElement.getMethodName();
		int line = stackTraceElement.getLineNumber();
		String tag = className + "|" + methodName + "|" + line + "|";

		return tag;
	}

	/**
	 * 获取堆栈信息
	 */
	private static StackTraceElement getStackTraceElement(int i) {
		return Thread.currentThread().getStackTrace()[i];
	}

	/**
	 * 手动向服务器发送日志
	 */
	private static void sendLog(String msg, Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);

		StringBuilder sb = new StringBuilder();
		sb.append(msg + "\n"); //标题
		sb.append("Version code is ");
		sb.append(Build.VERSION.SDK_INT + "\n"); //设备的Android版本号
		sb.append("Model is ");
		sb.append(Build.MODEL + "\n"); //设备型号
		sb.append("Time is ");
		sb.append(DATE_FORMAT.format(new Date()) + "\n"); //设备型号
		sb.append(sw.toString());

		//将异常输出至SD卡
		if (StorageUtils.externalMemoryAvailable()) {
			String fileFullName = StorageUtils.externalMemoryRootPath() + "/readnovel_error.log";
			StorageUtils.append(sb.toString(), fileFullName);
		}
	}
}
