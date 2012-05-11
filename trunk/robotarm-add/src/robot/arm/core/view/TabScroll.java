package robot.arm.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * 
 * @author liuy
 * 
 *         May 8, 2012
 * 
 */
public class TabScroll extends HorizontalScrollView {

	public TabScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TabGroup getTabGroup() {

		for (int i = 0; i < getChildCount(); i++) {
			
			View v = getChildAt(i);
			
			if (v instanceof TabGroup)
				return (TabGroup) v;
		}

		return null;
	}

	@Override
	public void addView(View child) {

		if (child instanceof TabGroup)
			super.addView(child);
	}

}
