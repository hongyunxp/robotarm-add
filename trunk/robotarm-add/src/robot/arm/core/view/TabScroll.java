package robot.arm.core.view;

import robot.arm.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
	private static final long DELAY_TIME = 500;

	private TabGroup tabGroup;
	private OnScrollListener onScrollListener;// 监听器

	public TabScroll(Context context) {
		super(context);

		init();
	}

	public TabScroll(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public TabScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	private void init() {
		Log.i(TAG, "init");

		setOnTouchListener(onTouchListener);// 设置手势监听器

	}

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
		final ViewGroup parent = (ViewGroup) getChildAt(0);

		if (parent != null && onScrollListener != null) {

			parent.post(new Runnable() {

				@Override
				public void run() {
					Log.i(TAG, "fireScrollEvent|" + parent.getMeasuredWidth() + "|" + getScrollX() + "|" + getWidth());

					if (parent.getMeasuredWidth() <= getScrollX() + getWidth()) {
						if (onScrollListener != null) {
							Log.i(TAG, "onRight");
							onScrollListener.onRight(parent);
						}

					} else if (getScrollX() == 0) {
						if (onScrollListener != null) {
							Log.i(TAG, "onLeft");
							onScrollListener.onLeft(parent);

						}
					} else {
						if (onScrollListener != null) {
							Log.i(TAG, "onScroll");

							// 适配tab选项
							fitTab(parent);

							parent.postDelayed(new Runnable() {

								@Override
								public void run() {
									onScrollListener.onScroll(parent);
								}
							}, DELAY_TIME);

						}
					}

				}
			});

		}
	}

	/**
	 * 自适应
	 * 
	 * @param childView
	 */
	private void fitTab(ViewGroup parent) {
		final View first = parent.getChildAt(0);
		final int[] location = new int[2];
		first.getLocationOnScreen(location);
		int left = location[0];
		int remaining = left % first.getWidth();

		if (remaining != 0)
			if (Math.abs(remaining) > first.getWidth() / 2)
				doScrollBy(first.getWidth() - Math.abs(remaining));
			else
				doScrollBy(remaining);

	}

	private void doScrollBy(final int x) {
		smoothScrollBy(x, 0);

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

	@Override
	public void fling(int velocityY) {
		return;// 取消fling滑动手势
	}

	public interface OnScrollListener {
		void onRight(ViewGroup parent);

		void onLeft(ViewGroup parent);

		void onScroll(ViewGroup parent);
	}

}
