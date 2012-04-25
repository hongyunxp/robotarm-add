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
	private static final int DELAY_TIME = 200;
	
	private OnScrollListener onScrollListener;//监听器

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

		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				final View childView = getChildAt(0);

				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_UP:
					if (childView != null && onScrollListener != null) {

						childView.postDelayed(new Runnable() {

							@Override
							public void run() {

								if (childView.getMeasuredHeight() <= getScrollY() + getHeight()) {
									if (onScrollListener != null){
										onScrollListener.onBottom();
//										fullScroll(View.FOCUS_DOWN);  
									}

								} else if (getScrollY() == 0) {
									if (onScrollListener != null)
										onScrollListener.onTop();

								} else {
									if (onScrollListener != null)
										onScrollListener.onScroll();
								}

							}
						}, DELAY_TIME);

					}
					break;

				default:
					break;
				}

				return false;
			}

		});

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