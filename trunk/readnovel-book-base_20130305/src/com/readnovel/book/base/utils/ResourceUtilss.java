package com.readnovel.book.base.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * 资源工具类
 * 
 * @author li.li
 *
 */
public class ResourceUtilss {
	/**
	 * 通过字符串获得图片资源
	 * @return
	 */
	public static int getDrawableResource(Context ctx, String name) {
		Resources res = ctx.getResources();
		int resId = res.getIdentifier(name, "drawable", ctx.getPackageName());

		return resId;
	}

	/**
	 * 通过字符串获得integer资源
	 * @return
	 */
	public static int getIntegrResource(Context ctx, String name) {
		Resources res = ctx.getResources();
		int resId = res.getIdentifier(name, "integer", ctx.getPackageName());

		return resId;
	}

	/**
	 * 通过字符串获得String资源
	 * @return
	 */
	public static int getStringResource(Context ctx, String name) {
		Resources res = ctx.getResources();
		int resId = res.getIdentifier(name, "string", ctx.getPackageName());

		return resId;
	}
}

