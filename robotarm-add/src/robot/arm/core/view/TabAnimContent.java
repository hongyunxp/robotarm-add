/**
 * 
 */
package robot.arm.core.view;

import robot.arm.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

/**
 * @author li.li
 * 
 *         May 18, 2012
 * 
 */
public class TabAnimContent extends FrameLayout {

	private Animation inRightToLeft;
	private Animation outRightToLeft;

	public TabAnimContent(Context context) {
		super(context, null);

		init();
	}

	public TabAnimContent(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public TabAnimContent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	private void init() {
		inRightToLeft = AnimationUtils.loadAnimation((getContext()), R.anim.in_right_to_left);

		inRightToLeft.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// 移除掉多余的view
				View pre = getChildAt(getChildCount() - 2);
				if (pre != null)
					removeView(pre);
			}
		});
	}

	public void animShow(View child) {
		if (getChildCount() == 0) {
			addView(child);

			return;
		} else if (indexOfChild(child) == -1)

			addView(child);

		else {
			removeView(child);
			addView(child);
		}

		show(getChildCount() - 1);
	}

	private void show(int childIndex) {

		final int count = getChildCount();

		for (int i = 0; i < count; i++) {

			final View child = getChildAt(i);
			if (i == childIndex) {
				if (inRightToLeft != null)
					child.startAnimation(inRightToLeft);
			} else if (outRightToLeft != null)
				child.startAnimation(outRightToLeft);

		}
	}
}
