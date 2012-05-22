/**
 * 
 */
package robot.arm.core.view;

import robot.arm.R;
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

	public View setTitle(int resouceId) {
		title = LayoutInflater.from(getContext()).inflate(resouceId, null);

		int count = getChildCount();

		for (int i = 0; i < count; i++) {
			View view = getChildAt(i);
			String tag = getResources().getString(R.string.tab_ad_tag);

			if (!tag.equals(view.getTag()))
				removeViewAt(i);
		}

		addView(title, 0);

		return title;

	}

	public View setAd(int resouceId) {
		ad = LayoutInflater.from(getContext()).inflate(resouceId, null);
		
		int count = getChildCount();

		for (int i = 0; i < count; i++) {
			View view = getChildAt(i);
			String tag = getResources().getString(R.string.tab_ad_tag);

			if (tag.equals(view.getTag()))
				removeViewAt(i);
		}
		
		addView(ad);

		return ad;
	}

}
