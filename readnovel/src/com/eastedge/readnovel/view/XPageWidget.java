package com.eastedge.readnovel.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.widget.Scroller;

import com.eastedge.readnovel.common.Constants;
import com.readnovel.base.cache.lru.BMemCache;
import com.readnovel.base.util.PhoneUtils;

/**
 * 翻页动画-水平翻页
 * @author li.li
 *
 * Aug 7, 2013
 */
public class XPageWidget extends PageWidget {

	private int mWidth = 0;
	private int mHeight = 0;
	private int mCornerX = 0; // 拖拽点对应的页脚
	private int mCornerY = 0;
	private Path mPath0;
	private Path mPath1;
	private Bitmap mCurPageBitmap = null; // 当前页
	private Bitmap mNextPageBitmap = null;

	private PointF mTouch = new PointF(); // 拖拽点

	private boolean mIsRTandLB; // 是否属于右上左下

	private int[] mBackShadowColors;
	private int[] mFrontShadowColors;
	private GradientDrawable mBackShadowDrawableLR;
	private GradientDrawable mBackShadowDrawableRL;
	private GradientDrawable mFolderShadowDrawableLR;
	private GradientDrawable mFolderShadowDrawableRL;

	private GradientDrawable mFrontShadowDrawableHBT;
	private GradientDrawable mFrontShadowDrawableHTB;
	private GradientDrawable mFrontShadowDrawableVLR;
	private GradientDrawable mFrontShadowDrawableVRL;
	private Paint mPaint;
	private Scroller mScroller;
	private Bitmap bg;
	private int p;
	private int[] wh;

	public XPageWidget(Context context, int mWidth, int mHeight) {
		super(context);
		this.mWidth = mWidth;
		this.mHeight = mHeight;
		mPath0 = new Path();
		mPath1 = new Path();
		createDrawable();
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);

		mScroller = new Scroller(getContext());
		mTouch.x = -100.0f; // 不让x,y为0,否则在点计算时会有问题
		mTouch.y = 0.01f;

		wh = PhoneUtils.getScreenPix((Activity) context);
	}

	/**
	 * 触摸事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Override
	public boolean doTouchEvent(MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_MOVE) {
			mTouch.x = e.getX();
			mTouch.y = e.getY();
			if (mTouch.y > mHeight - 1)
				mTouch.y = mHeight - 1;

			postInvalidate();
		}
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			mTouch.x = e.getX();
			mTouch.y = e.getY();
			if (mTouch.y > mHeight - 1)
				mTouch.y = mHeight - 1;
		}
		if (e.getAction() == MotionEvent.ACTION_UP) {

			startAnimation(1200);
			postInvalidate();
		}

		return true;
	}

	/**
	 * 设置位图图像
	 * 
	 * @param cur
	 * @param next
	 */
	@Override
	public void setBitmaps(Bitmap cur, Bitmap next) {
		mCurPageBitmap = cur;
		mNextPageBitmap = next;
	}

	/**
	 * Author : hmg25 Version: 1.0 Description : 计算拖拽点对应的拖拽脚
	 */
	@Override
	public void calcCornerXY(float x, float y) {

		if (x <= mWidth / 2)
			mCornerX = 0;
		else
			mCornerX = mWidth;
		if (y <= mHeight / 2)
			mCornerY = 0;
		else
			mCornerY = mHeight;
		if ((mCornerX == 0 && mCornerY == mHeight) || (mCornerX == mWidth && mCornerY == 0))
			mIsRTandLB = true;
		else
			mIsRTandLB = false;

	}

	/**
	 * 退出动画
	 */
	@Override
	public void abortAnimation() {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
	}

	/**
	 * 判断是否从左边翻向右边
	 */
	@Override
	public boolean isDragToRight() {
		if (mCornerX > 0)
			return false;
		return true;
	}

	/*******************************************************************************
	 * 
	 * 以下是实现或重写，无需显示调用
	 * 
	 *******************************************************************************/

	/**
	 * 实现绘制画布
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		if (canvas == null)
			return;

		// 创建屏幕大小的背景
		String key = String.format(Constants.IMG_CACHE_KEY_PAGEWIDGET_BG, p);
		Bitmap cacheBM = BMemCache.getInstance().get(key);
		if (cacheBM == null || cacheBM.isRecycled()) {//缓存为空
			Bitmap bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), Constants.READ_BG_LIST.get(p)), mWidth,
					wh[1], true);

			canvas.drawBitmap(bg, 0, 0, null);

			BMemCache.getInstance().put(key, bg);//放入缓存

		} else {//缓存不为空
			canvas.drawBitmap(cacheBM, 0, 0, null);
		}

		if (mCornerX > 0) {//下一页
			drawCurrentPageArea(canvas, mCurPageBitmap, mPath0);
			drawNextPageAreaAndShadow(canvas, mNextPageBitmap);
		} else {//上一页
			drawCurrentPageArea(canvas, mNextPageBitmap, mPath0);
			drawNextPageAreaAndShadow(canvas, mCurPageBitmap);
		}
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			float x = mScroller.getCurrX();
			float y = mScroller.getCurrY();
			mTouch.x = x;
			mTouch.y = y;
			postInvalidate();
		}
	}

	/**
	 * 设置背景图片所在数组位置
	 * @param p
	 */
	public void cgbg(int p) {
		this.p = p;
		setBg();
	}

	/*******************************************************************************
	 * 
	 * 以下是私有方法不对外
	 * 
	 *******************************************************************************/

	/**
	 * 更新背景图片
	 * @param canvas
	 */
	private void setBg() {
		//		canvas.drawColor(R.drawable.readbg23);
		Bitmap tempBg = BitmapFactory.decodeResource(getContext().getResources(), Constants.READ_BG_LIST.get(p));

		// 释放之前的背景资源
		if (bg != null && !bg.isRecycled())
			bg.recycle();

		// 创建屏幕大小的背景
		bg = Bitmap.createScaledBitmap(tempBg, mWidth, mHeight, true);

		// 释放资源
		tempBg.recycle();
	}

	/**
	 * 绘制当前页
	 */
	private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap, Path path) {
		mPath0.reset();
		mPath0.moveTo(mTouch.x, 0);
		mPath0.lineTo(mTouch.x, mHeight);
		mPath0.lineTo(mWidth, mHeight);
		mPath0.lineTo(mWidth, 0);
		mPath0.close();
		canvas.save();
		canvas.clipPath(path, Region.Op.XOR);
		canvas.drawBitmap(bitmap, -mWidth + mTouch.x, 0, null);
		canvas.restore();
	}

	/**
	 * 绘制下一页
	 */
	private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {

		if (mTouch.x == -100.0f) {
			mPath1.reset();
			mPath1.moveTo(mTouch.x, 0);
			mPath1.lineTo(mTouch.x, mHeight);
			mPath1.lineTo(mWidth, mHeight);
			mPath1.lineTo(mWidth, 0);
			mPath1.close();
			canvas.save();
			canvas.clipPath(mPath0);
			canvas.clipPath(mPath1, Region.Op.INTERSECT);
			canvas.drawBitmap(bitmap, 0, 0, null);
			canvas.restore();
		} else {
			mPath1.reset();
			mPath1.moveTo(mTouch.x, 0);
			mPath1.lineTo(mTouch.x, mHeight);
			mPath1.lineTo(mWidth, mHeight);
			mPath1.lineTo(mWidth, 0);
			mPath1.close();
			int leftx;
			int rightx;
			GradientDrawable mBackShadowDrawable;
			leftx = (int) (mTouch.x);
			rightx = (int) (mTouch.x + 20);
			mBackShadowDrawable = mBackShadowDrawableLR;

			canvas.save();
			canvas.clipPath(mPath0);
			canvas.clipPath(mPath1, Region.Op.INTERSECT);
			canvas.drawBitmap(bitmap, 0, 0, null);
			mBackShadowDrawable.setBounds(leftx, (int) 0, rightx, (int) (mHeight));
			mBackShadowDrawable.draw(canvas);
			canvas.restore();
		}
	}

	/**
	 * 创建阴影的GradientDrawable
	 */
	private void createDrawable() {
		int[] color = { 0x333333, 0xb0333333 };
		mFolderShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, color);
		mFolderShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFolderShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, color);
		mFolderShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowColors = new int[] { 0xff111111, 0x111111 };
		mBackShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
		mBackShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
		mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowColors = new int[] { 0x80111111, 0x111111 };
		mFrontShadowDrawableVLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
		mFrontShadowDrawableVLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		mFrontShadowDrawableVRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
		mFrontShadowDrawableVRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHTB = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
		mFrontShadowDrawableHTB.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHBT = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
		mFrontShadowDrawableHBT.setGradientType(GradientDrawable.LINEAR_GRADIENT);
	}

	private void startAnimation(int delayMillis) {
		int dx;
		if (mCornerX > 0) //下一页
			dx = -(int) mTouch.x;
		else
			//上一页
			dx = (int) (mWidth - mTouch.x);

		mScroller.startScroll((int) mTouch.x, 0, dx, 0, delayMillis);
	}

}