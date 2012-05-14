package robot.arm.core.view;

import robot.arm.R;
import robot.arm.core.view.TabScroll.OnScrollListener;
import robot.arm.provider.BGLoader;
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
		arrowRight.setVisibility(View.GONE);

	}

	@Override
	public void onLeft(ViewGroup parent) {
		arrowLeft.setVisibility(View.GONE);

	}

	@Override
	public void onScroll(final ViewGroup parent) {

		final View first = parent.getChildAt(0);
		final int[] location = new int[2];
		first.getLocationOnScreen(location);
		int left = location[0];
		if (left == 0) {//移动到了最左侧
			arrowLeft.setVisibility(View.GONE);
		} else {
			arrowLeft.setVisibility(View.VISIBLE);
		}

		final View last = parent.getChildAt(parent.getChildCount() - 1);
		final int[] lastLocation = new int[2];
		last.getLocationOnScreen(lastLocation);
		int right = lastLocation[0];
		Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
		int disWidth = display.getWidth();
		if (right == disWidth - last.getWidth()) {//移动到了最右侧
			arrowRight.setVisibility(View.GONE);
		} else {
			arrowRight.setVisibility(View.VISIBLE);
		}

	}

}
