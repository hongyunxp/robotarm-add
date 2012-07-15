package robot.arm.provider.view;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;

/**
 * 
 * @author li.li
 *
 * Mar 15, 2012
 *
 *  图片组展示
 */
public class ImageGroupView extends AdapterView<Adapter> implements GestureDetector.OnGestureListener {
	private final String TAG = getClass().getSimpleName();

	private static final Executor singleThread = Executors.newSingleThreadExecutor();// 一个静态待命线程

	private static final int TIME = 10;// 次数
	private static final int DURATION = 10;// 时间间隔
	private static final int FLING_SPEED = 300;// fling速度
	private static final int FIT_DELAY = 200;// 位置校正延迟

	private Adapter mAdapter;// 数据视图适配器
	private GestureDetector detector;// 手势识别
	private VelocityTracker vt;// 滑动速度检测器

	private volatile int curDistance;// 子元素需移动的步长
	private volatile SlideType slideType = SlideType.none;// 滑动类型
	private volatile boolean lock;// 滑动锁
	private volatile int direction;// 滑动方向
	private volatile boolean canScroll;// 是否可以滑动

	public ImageGroupView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);

		initProductImagesView();
	}

	// 初始化
	private void initProductImagesView() {
		Log.i(TAG, "ProductImagesView.initProductImagesView()");
		this.detector = new GestureDetector(getContext(), this);
		this.vt = VelocityTracker.obtain();
	}

	/**
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 
	 * adapter相关
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 */

	@Override
	public void setAdapter(Adapter adapter) {
		this.mAdapter = adapter;

		final int count = mAdapter.getCount();
		View localView;
		for (int i = 0; i < count; i++) {
			localView = mAdapter.getView(i, null, this);

			addViewInLayout(localView, i, new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
	}

	@Override
	public Adapter getAdapter() {
		return mAdapter;
	}

	@Override
	public View getSelectedView() {
		// 空实现
		return null;
	}

	@Override
	public void setSelection(int position) {
		// 空实现
	}

	/**
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 
	 * 布局相关
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 */

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if (changed) {
			layoutChild();
		}
	}

	/**
	 * 布局子元素
	 */
	private void layoutChild() {
		scrollChild(0);
	}

	/**
	 * 子元素水平滑动
	 */
	private void scrollChild(int distance) {
		curDistance += distance;

		post(new Runnable() {

			@Override
			public void run() {

				final int count = getChildCount();
				for (int i = 0; i < count; i++) {
					View child = getChildAt(i);
					child.measure(child.getMeasuredWidth(), child.getMeasuredHeight());

					int left = child.getMeasuredWidth() * i - curDistance;
					int top = 0;
					int right = child.getMeasuredWidth() * (i + 1) - curDistance;
					int bottom = child.getMeasuredHeight();

					if ((right <= 0 || left >= getWidth())) // 不可见不显示
						child.setVisibility(View.GONE);
					else
						child.setVisibility(View.VISIBLE);

					child.layout(left, top, right, bottom);

				}
			}

		});

	}

	/**
	 * 子元素水平滑动(带动画效果)
	 */
	private void scrollChildAnimated(final int distance) {

		singleThread.execute(new Runnable() {

			@Override
			public void run() {
				final int step = distance / TIME;// 步长
				final int remaining = distance % TIME;// 余数

				for (int i = 0; i < TIME; i++) {// 动画滑动

					final int j = i;
					scrollChild(step);
					if (j == TIME - 1)
						scrollChild(remaining);// 弥补没整除的像素

					try {
						Thread.sleep(DURATION);
					} catch (InterruptedException e) {
						Log.e(TAG, e.getMessage(), e);
					}

				}
			}

		});
	}

	/**
	 * 水平滑动
	 */
	private void doScrollChild(final int distance) {
		if (distance != 0)
			direction = distance / Math.abs(distance);// 指定滑动方向

		if (SlideType.scroll.equals(slideType)) {// scroll普通滑动

			scrollChild(distance);
		}

	}

	/**
	 * 校正图片位置
	 */
	private void correctLocation() {

		postDelayed(new Runnable() {

			@Override
			public void run() {
				singleThread.execute(new Runnable() {

					@Override
					public void run() {
						View first = getChildAt(0);
						View last = getChildAt(getChildCount() - 1);

						if (first.getLeft() > 0) {// 左侧越界,右移校正
							Log.d(TAG, "ProductImagesView.correctLocation()|左侧越界,右移" + first.getLeft());

							scrollChildAnimated(first.getLeft());

							return;
						} else if (last.getRight() < getWidth()) {// 右侧越界,左移校正
							Log.d(TAG, "ProductImagesView.correctLocation()|右侧越界,左移" + (last.getRight() - getWidth()));

							scrollChildAnimated(last.getRight() - getWidth());

							return;
						}

						final int count = getChildCount();
						for (int i = 0; i < count; i++) {// 校正位置
							final View child = getChildAt(i);
							final int left = child.getMeasuredWidth() * i - curDistance;

							if (Math.abs(left) <= child.getWidth() * 0.5) {// 移动
								Log.d(TAG, "ProductImagesView.correctLocation()|移动+" + left + "|" + i);

								scrollChildAnimated(left);

							}
						}
					}

				});

			}
		}, FIT_DELAY);

	}

	/*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 
	 * 手势识别相关
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 */

	@Override
	public boolean onTouchEvent(MotionEvent e) {

		if (MotionEvent.ACTION_UP == e.getAction() || MotionEvent.ACTION_CANCEL == e.getAction()) {

			if (SlideType.fling.equals(slideType) && canScroll) {// fling翻页滑动（手指离开时执行）
				lock = true;// 锁住

				scrollChildAnimated(getWidth() * direction);
			}

			correctLocation();

			slideType = SlideType.scroll;// 恢复
			lock = false;// 解锁
		}

		return detector.onTouchEvent(e);

	}

	@Override
	public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, float velocityY) {// (用力地)摔
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// 返回true时才会响应触屏手势事件

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {// 长按

		int touchPosition = pointToPosition((int) e.getX(), (int) e.getY());
		View touchView = getChildAt(touchPosition);

		if (getOnItemLongClickListener() != null) {

			getOnItemLongClickListener().onItemLongClick(this, touchView, touchPosition, touchView.getId());
		}
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {// 滚动
		slideType = Math.abs(vt.getXVelocity()) > FLING_SPEED ? SlideType.fling : SlideType.scroll;
		canScroll = canScroll(distanceX);

		vt.addMovement(e2);
		vt.computeCurrentVelocity(1000);

		if (!lock && canScroll) {

			doScrollChild((int) distanceX);// scrolling
		}

		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {// 按
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {// 轻敲

		int touchPosition = pointToPosition((int) e.getX(), (int) e.getY());
		View touchView = getChildAt(touchPosition);

		if (getOnItemClickListener() != null) {
			getOnItemClickListener().onItemClick(this, touchView, touchPosition, touchView.getId());

			return true;
		}

		return false;
	}

	/**
	 * 被点击的子元素
	 */
	private int pointToPosition(int x, int y) {

		Rect touchFrame = new Rect();
		final int count = getChildCount();

		for (int i = count - 1; i >= 0; i--) {
			View child = getChildAt(i);
			if (child.getVisibility() == View.VISIBLE) {
				child.getHitRect(touchFrame);
				if (touchFrame.contains(x, y)) {
					return i;
				}
			}
		}

		return INVALID_POSITION;
	}

	/**
	 * 是否可以滑动
	 */
	private boolean canScroll(float distance) {

		return !(Border.left.equals(getBorder()) && distance <= 0 || Border.right.equals(getBorder()) && distance >= 0);
	}

	/**
	 * 得到边界类型
	 */
	private Border getBorder() {

		View first = getChildAt(0);
		View last = getChildAt(getChildCount() - 1);

		if (first.getVisibility() == View.VISIBLE && first.getLeft() >= 0) {// 左侧边界

			return Border.left;
		} else if (last.getVisibility() == View.VISIBLE && last.getRight() <= getWidth()) {// 右侧边界

			return Border.right;
		} else {

			return Border.none;// 不是边界
		}
	}

	/**
	 * 滑动类型
	 */
	private enum SlideType {
		scroll, //
		fling, //
		none, //
		;
	}

	/**
	 * 边界类型
	 */
	private enum Border {
		left, // 左侧边界
		right, // 右侧边界
		none, // 不是边界
	}

}
