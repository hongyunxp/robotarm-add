/**
 * 
 */
package robot.arm.provider.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * @author li.li
 * 
 *         Apr 25, 2012
 * 
 */
public class MyScrollView extends ScrollView {
	private static final String TAG = MyScrollView.class.getName();
	private static final int DELAY_EVENT_TIME = 200;// 判断Scroll状态延时

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
	
	private void fireScrollEvent(){
		final View childView = getChildAt(0);
		
		if (childView != null && onScrollListener != null) {

			childView.postDelayed(new Runnable() {

				@Override
				public void run() {

					if (childView.getMeasuredHeight() <= getScrollY() + getHeight()) {
						if (onScrollListener != null) {
							onScrollListener.onBottom();
							Log.i(TAG, "onBottom");
						}

					} else if (getScrollY() == 0) {
						if (onScrollListener != null)
							onScrollListener.onTop();
						Log.i(TAG, "onTop");
					} else {
						if (onScrollListener != null)
							Log.i(TAG, "onScroll");
						onScrollListener.onScroll();
					}

				}
			}, DELAY_EVENT_TIME);

		}
	}

	public MyScrollView(Context context) {
		super(context);
		init();
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		Log.i(TAG, "init");

		this.setOnTouchListener(onTouchListener);

	}

	public interface OnScrollListener {
		void onBottom();

		void onTop();

		void onScroll();
	}

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;

	}
}
