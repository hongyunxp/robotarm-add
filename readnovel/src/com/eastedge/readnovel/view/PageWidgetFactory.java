package com.eastedge.readnovel.view;

import android.content.Context;

/**
 * 翻页实例工厂 
 * @author li.li
 *
 * Aug 7, 2013
 */
public class PageWidgetFactory {
	public enum PageWidgetType {
		real, //仿真翻页
		horizontal, //水平翻页（默认）
		vertical;//垂直翻页
	}

	public static PageWidget createPageWidget(Context ctx, int w, int h) {

		return createPageWidget(ctx, w, h, PageWidgetType.real);
	}

	private static PageWidget createPageWidget(Context ctx, int w, int h, PageWidgetType type) {

		if (PageWidgetType.real.equals(type))
			return new RealPageWidget(ctx, w, h);
		else if (PageWidgetType.horizontal.equals(type))
			return new XPageWidget(ctx, w, h);

		return new XPageWidget(ctx, w, h);
	}
}
