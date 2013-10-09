package com.eastedge.readnovel.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;

/**
 * 翻页动画接口
 * @author li.li
 *
 * Aug 7, 2013
 */
public abstract class PageWidget extends View {

	public PageWidget(Context context) {
		super(context);
	}

	/**
	 * 触摸事件处理
	 * 
	 * @param event
	 * @return
	 */
	public abstract boolean doTouchEvent(MotionEvent event);

	/**
	 * 设置位图图像
	 * 
	 * 设置翻页动画内容(当前页和下一页)
	 * 
	 * @param cur
	 * @param next
	 */
	public abstract void setBitmaps(Bitmap cur, Bitmap next);

	/**
	 * 计算拖拽点对应的拖拽脚
	 */
	public abstract void calcCornerXY(float x, float y);

	/**
	 * 退出动画
	 */
	public abstract void abortAnimation();

	/**
	 * 判断是否从左边翻向右边
	 */
	public abstract boolean isDragToRight();

	/**
	 * 设置背景图片所在数组位置
	 * @param p
	 */
	public abstract void cgbg(int p);
}
