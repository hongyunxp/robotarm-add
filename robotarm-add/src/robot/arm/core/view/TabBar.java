package robot.arm.core.view;

import robot.arm.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 带左右箭头的工具条
 * 
 * @author liuy
 * @since 1.0 2012.5.09
 */
public class TabBar extends RelativeLayout {

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
//		View arrowl = LayoutInflater.from(context).inflate(R.layout.tab_arrowl, this, false);
//		View arrowr = LayoutInflater.from(context).inflate(R.layout.tab_arrowr, this, false);
		View tabScroll = LayoutInflater.from(context).inflate(R.layout.tab_scroll, this, false);

		addChildView(tabScroll);
//		addChildView(arrowl);
//		addChildView(arrowr);
	}

	public void addChildView(View child) {
		addView(child);
//		if (getResources().getString(R.string.tab_arrowl_tag).equals(child.getTag()))
//			arrowl = (ImageView) child;
//		if (getResources().getString(R.string.tab_arrowr_tag).equals(child.getTag()))
//			arrowr = (ImageView) child;
		if (getResources().getString(R.string.tab_scroll_tag).equals(child.getTag()))
			tabScroll = (TabScroll) child;
	}

}
