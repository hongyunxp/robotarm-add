package com.readnovel.base.util;

import android.content.Context;
import android.content.res.Resources;

/**
 * 资源工具类
 * 
 * @author li.li
 *
 */
public class ResourceUtils {
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
}
