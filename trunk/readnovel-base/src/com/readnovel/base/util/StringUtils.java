package com.readnovel.base.util;

/**
 * 字符串工具类
 * 
 * @author li.li
 *
 */
public class StringUtils {

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return true;
		for (int i = 0; i < strLen; i++)
			if (!Character.isWhitespace(str.charAt(i)))
				return false;

		return true;
	}

	public static boolean isBlankOr(String... strs) {
		for (String str : strs) {
			return isBlank(str);
		}

		return true;
	}

	public static boolean isBlankAnd(String... strs) {
		for (String str : strs) {

			if (!isBlank(str))
				return false;

		}

		return true;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
}
