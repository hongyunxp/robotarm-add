package com.readnovel.book.base.utils;

import android.app.Activity;
import android.util.DisplayMetrics;


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

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	public static int getVersionCode() {
		int version = 0;
		try {
			version = android.os.Build.VERSION.SDK_INT;
		} catch (NumberFormatException e) {
		}
		return version;
	}
	public static int[] getScreenPix(Activity act) {
		DisplayMetrics dm = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int[] pixs = { dm.widthPixels, dm.heightPixels };
		return pixs;
	}
}
