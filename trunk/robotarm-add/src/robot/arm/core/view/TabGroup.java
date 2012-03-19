package robot.arm.core.view;

import robot.arm.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;
/**
 * 
 * @author li.li
 *
 * Mar 15, 2012
 *
 */
public class TabGroup extends RadioGroup {

	public TabGroup(Context context) {
		super(context);
	}

	public TabGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setTag(getResources().getString(R.string.tab_group_tag));

	}

}
