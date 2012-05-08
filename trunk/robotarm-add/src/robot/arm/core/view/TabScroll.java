package robot.arm.core.view;

import robot.arm.R;
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

	private TabGroup tabGroup;

	public TabScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TabGroup getTabGroup() {
		return tabGroup;
	}

	public void addChildView(View child) {

		addView(child);

		if (getResources().getString(R.string.tab_group_tag).equals(child.getTag()))
			tabGroup = (TabGroup) child;
		
	}

}
