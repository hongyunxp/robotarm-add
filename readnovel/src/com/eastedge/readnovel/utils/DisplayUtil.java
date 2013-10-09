package com.eastedge.readnovel.utils;

import android.content.Context;
import android.content.res.Resources;

import com.readnovel.base.common.CommonApp;
import com.xs.cn.R;

/**
 * Android大小单位转换工具类
 * @author li.li
 * Aug 24, 2012
 */
public class DisplayUtil {

	/**
	* 将px值转换为dip或dp值，保证尺寸大小不变
	* @param pxValue
	* @param scale（DisplayMetrics类中属性density）
	* @return
	*/
	public static int px2dip(float pxValue, float scale) {
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	* 将dip或dp值转换为px值，保证尺寸大小不变
	* @param dipValue
	* @param scale（DisplayMetrics类中属性density）
	* @return
	* 
	*/
	public static int dip2px(float dipValue, float scale) {
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	* 将px值转换为sp值，保证文字大小不变
	* 
	* @param pxValue
	* @param fontScale（DisplayMetrics类中属性scaledDensity）
	* @return
	*/
	public static int px2sp(float pxValue, float fontScale) {
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	* 将sp值转换为px值，保证文字大小不变
	* 
	* @param spValue
	* @param fontScale（DisplayMetrics类中属性scaledDensity）
	* @return
	*/
	public static int sp2px(float spValue, float fontScale) {
		return (int) (spValue * fontScale + 0.5f);
	}

	public static int getFont1() {
		return (int) CommonApp.getInstance().getResources().getDimension(R.dimen.read_text_font_size_1);
	}

	public static int getFont2() {
		return (int) CommonApp.getInstance().getResources().getDimension(R.dimen.read_text_font_size_2);
	}

	public static int getFont3() {
		return (int) CommonApp.getInstance().getResources().getDimension(R.dimen.read_text_font_size_3);
	}

	public static int getFont4() {
		return (int) CommonApp.getInstance().getResources().getDimension(R.dimen.read_text_font_size_4);
	}

	public static int getFont5() {
		return (int) CommonApp.getInstance().getResources().getDimension(R.dimen.read_text_font_size_5);
	}

	public static int readTextTitleFondSize() {
		return (int) CommonApp.getInstance().getResources().getDimension(R.dimen.read_text_title_font_size);
	}

	public static int readTextTitleTopSpaceSize() {
		return (int) CommonApp.getInstance().getResources().getDimension(R.dimen.read_text_title_top_space_size);
	}

	public static int readTextTitleSpace() {
		return (int) CommonApp.getInstance().getResources().getDimension(R.dimen.read_text_left_right_size);
	}

	public static int readTextTopBottomSize() {
		return (int) CommonApp.getInstance().getResources().getDimension(R.dimen.read_text_top_bottom_size);
	}

	public static int readTextFontSpaceSize() {
		return (int) CommonApp.getInstance().getResources().getDimension(R.dimen.read_text_font_space_size);
	}

	public static int readTextLeftRightSize() {
		return (int) CommonApp.getInstance().getResources().getDimension(R.dimen.read_text_left_right_size);
	}

	public static int readTextTimeBottomSpaceSize() {
		return (int) CommonApp.getInstance().getResources().getDimension(R.dimen.read_text_time_bottom_space_size);
	}

	/**
	 * 通过字符串获得图片资源
	 * @return
	 */
	public static int getDrawableResource(Context ctx, String name) {
		Resources res = ctx.getResources();
		int resId = res.getIdentifier(name, "drawable", ctx.getPackageName());

		return resId;
	}
}
