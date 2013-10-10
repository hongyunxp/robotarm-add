package com.readnovel.book.base.utils;

import android.content.Context;
import android.content.res.Resources;

import com.readnovel.book.base.R;

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
	// 电量宽度
		public static int getLightWidthBetween(Context ctx) {
			return (int) ctx.getResources().getDimension(R.dimen.read_light_wight);
		}

		// 电量高度
		public static int getLightHeightBetween(Context ctx) {
			return (int) ctx.getResources().getDimension(R.dimen.read_light_height);
		}
		
		public static int getLightBetween(Context ctx) {
			return (int) ctx.getResources().getDimension(R.dimen.read_lightleft_height);
		}
		public static int getTimeBetween(Context ctx) {
			return (int) ctx.getResources().getDimension(R.dimen.read_timeleft_height);
		}
		public static int getLightTopBetween(Context ctx) {
			return (int) ctx.getResources().getDimension(R.dimen.read_light_top);
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

	public static int getFontSmall(Context ctx) {
		return (int) ctx.getResources().getDimension(R.dimen.read_text_font_size_small);
	}

	public static int getFontMiddle(Context ctx) {
		return (int) ctx.getResources().getDimension(R.dimen.read_text_font_size_middle);
	}

	public static int getFontBig(Context ctx) {
		return (int) ctx.getResources().getDimension(R.dimen.read_text_font_size_big);
	}

	public static int getZhangJieFontSize(Context ctx) {
		return (int) ctx.getResources().getDimension(R.dimen.read_zhangjie_font_size);
	}

	public static int getBottomfontsize(Context ctx) {
		return (int) ctx.getResources().getDimension(R.dimen.read_bottom_font_size);
	}

	public static int getHangHeight(Context ctx) {
		return (int) ctx.getResources().getDimension(R.dimen.read_hang_height);
	}

	public static int getDuanBetween(Context ctx) {
		return (int) ctx.getResources().getDimension(R.dimen.read_duanbetween_height);
	}
	public static int getone(Context ctx) {
		return (int) ctx.getResources().getDimension(R.dimen.read_one);
	}
	public static int gettwo(Context ctx) {
		return (int) ctx.getResources().getDimension(R.dimen.read_two);
	}
	public static int getLeftRightBianJu(Context ctx) {
		return (int) ctx.getResources().getDimension(R.dimen.read_leftright_bianju);
	}

	public static int getTopBottomBianJu(Context ctx) {
		return (int) ctx.getResources().getDimension(R.dimen.read_topbottom_bianju);
	}

	public static int getZhangjieBetweenTop(Context ctx) {
		return (int) ctx.getResources().getDimension(R.dimen.read_zhangjieming_top_bianju);
	}

	public static int getTopBetweenContent(Context ctx) {
		return (int) ctx.getResources().getDimension(R.dimen.read_topbetweencontent_height);
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
