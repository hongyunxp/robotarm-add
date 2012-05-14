package robot.arm.core.view;

import robot.arm.R;
import robot.arm.core.view.TabScroll.OnScrollListener;
import android.content.Context;
import android.util.AttributeSet;
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

	ImageView arrowl;
	ImageView arrowr;
	TabScroll tabScroll;

	public TabScroll getTabScroll() {
		return tabScroll;
	}

	public ImageView getArrowl() {
		return arrowl;
	}

	public ImageView getArrowr() {
		return arrowr;
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
			arrowl = (ImageView) child;
		if (getResources().getString(R.string.tab_arrowr_tag).equals(child.getTag()))
			arrowr = (ImageView) child;
		if (getResources().getString(R.string.tab_scroll_tag).equals(child.getTag()))
			tabScroll = (TabScroll) child;
	}

	@Override
	public void onRight(ViewGroup parent) {
		arrowr.setVisibility(View.GONE);

	}

	@Override
	public void onLeft(ViewGroup parent) {
		arrowl.setVisibility(View.GONE);

	}

	@Override
	public void onScroll(ViewGroup parent) {
		arrowl.setVisibility(View.VISIBLE);
		arrowr.setVisibility(View.VISIBLE);

	}

}
