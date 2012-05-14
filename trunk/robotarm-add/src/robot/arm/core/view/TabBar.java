package robot.arm.core.view;

import robot.arm.R;
import robot.arm.core.view.TabScroll.OnScrollListener;
import robot.arm.utils.BaseUtils;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 带左右箭头的工具条
 * 
 * @author liuy
 * @since 1.0 2012.5.09
 */
public class TabBar extends RelativeLayout implements OnScrollListener {

	private ImageView arrowLeft;
	private ImageView arrowRight;
	private TabScroll tabScroll;

	public TabScroll getTabScroll() {
		return tabScroll;
	}

	public TabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		View arrowl = LayoutInflater.from(context).inflate(R.layout.tab_arrow_left, this, false);
		View arrowr = LayoutInflater.from(context).inflate(R.layout.tab_arrow_right, this, false);
		TabScroll tabScroll = (TabScroll) LayoutInflater.from(context).inflate(R.layout.tab_scroll, this, false);
		tabScroll.setOnScrollListener(this);// 事件监听

		addChildView(tabScroll);
		addChildView(arrowl);
		addChildView(arrowr);

	}

	public void addChildView(View child) {
		addView(child);

		if (getResources().getString(R.string.tab_arrowl_tag).equals(child.getTag()))
			arrowLeft = (ImageView) child;
		if (getResources().getString(R.string.tab_arrowr_tag).equals(child.getTag()))
			arrowRight = (ImageView) child;
		if (getResources().getString(R.string.tab_scroll_tag).equals(child.getTag()))
			tabScroll = (TabScroll) child;
	}

	@Override
	public void onRight(ViewGroup parent) {

		scrollEnd(parent);
	}

	@Override
	public void onLeft(ViewGroup parent) {

		scrollEnd(parent);
	}

	@Override
	public void onScroll(ViewGroup parent) {

		scrollEnd(parent);
	}

	private void scrollEnd(ViewGroup parent) {
		View first = parent.getChildAt(0);
		controlArrow(first, arrowLeft, 0);// 移动到了最左侧

		View last = parent.getChildAt(parent.getChildCount() - 1);
		Display display = BaseUtils.getScreenDisplay((Activity) getContext());
		controlArrow(last, arrowRight, display.getWidth() - last.getWidth());// 移动到了最右侧
	}

	private void controlArrow(View view, View arrow, int l) {
		final int[] location = new int[2];
		view.getLocationOnScreen(location);
		int point = location[0];

		if (point == l || Math.abs(point - l) < view.getWidth() / 2)// 允许有误差
			arrow.setVisibility(View.GONE);
		else
			arrow.setVisibility(View.VISIBLE);

	}
}
