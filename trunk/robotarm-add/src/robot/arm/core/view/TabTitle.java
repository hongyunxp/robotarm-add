/**
 * 
 */
package robot.arm.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author li.li
 * 
 *         May 22, 2012
 * 
 */
public class TabTitle extends LinearLayout {
	private View title;
	private View ad;

	public TabTitle(Context context) {
		super(context, null);

		init();
	}

	public TabTitle(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	private void init() {

	}

	public void setTitle(int resouceId) {
		title = LayoutInflater.from(getContext()).inflate(resouceId, null);
		int index = indexOfChild(title);

		if (index != -1)
			removeViewAt(index);

		int childCount = getChildCount();
		addView(title, childCount == 0 ? 0 : childCount - 1);

	}

	public void setAd(int resouceId) {
		ad = LayoutInflater.from(getContext()).inflate(resouceId, null);
		int index = indexOfChild(ad);

		if (index != -1)
			removeViewAt(index);

		addView(ad);
	}

}
