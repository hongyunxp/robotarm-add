package robot.arm.core.view;

import robot.arm.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
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
	private static final String TAG = TabScroll.class.getName();
	private static final int DELAY_EVENT_TIME = 200;// 判断Scroll状态延时

	private TabGroup tabGroup;
	private OnScrollListener onScrollListener;// 监听器

	private OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				break;

			case MotionEvent.ACTION_UP:

				fireScrollEvent();

				break;

			default:
				break;
			}

			return false;
		}

	};
	
	private void fireScrollEvent() {
		final View childView = getChildAt(0);

		if (childView != null && onScrollListener != null) {

			childView.postDelayed(new Runnable() {

				@Override
				public void run() {
					Log.i(TAG, "fireScrollEvent|" + childView.getMeasuredWidth() + "|" + getScrollX() + "|" + getWidth());

					if (childView.getMeasuredWidth() <= getScrollX() + getWidth()) {
						if (onScrollListener != null) {
							Log.i(TAG, "onRight");
							onScrollListener.onRight();
						}

					} else if (getScrollX() == 0) {
						if (onScrollListener != null) {
							Log.i(TAG, "onLeft");
							onScrollListener.onLeft();
						}
					} else {
						if (onScrollListener != null) {
							Log.i(TAG, "onScroll");
							onScrollListener.onScroll();
						}
					}
					
				}
			},DELAY_EVENT_TIME);

		}
	}

	public TabScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init();
	}
	
	private void init() {
		Log.i(TAG, "init");

		setOnTouchListener(onTouchListener);//设置手势监听器

	}

	public TabGroup getTabGroup() {
		return tabGroup;
	}

	public void addChildView(View child) {

		addView(child);

		if (getResources().getString(R.string.tab_group_tag).equals(child.getTag()))
			tabGroup = (TabGroup) child;

	}
	
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;

	}

	public interface OnScrollListener {
		void onRight();

		void onLeft();

		void onScroll();
	}

}
