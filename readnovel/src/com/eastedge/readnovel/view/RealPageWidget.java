package com.eastedge.readnovel.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
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
 * 翻页动画-仿真翻页
 * @author li.li
 *
 * Dec 19, 2012
 */
public class RealPageWidget extends PageWidget {

	private int mWidth;
	private int mHeight;
	private int mCornerX; // 拖拽点对应的页脚
	private int mCornerY;
	private Path mPath0;
	private Path mPath1;
	private Bitmap mCurPageBitmap; // 当前页
	private Bitmap mNextPageBitmap;
	private PointF mTouch = new PointF(); // 拖拽点
	private PointF mBezierStart1 = new PointF(); // 贝塞尔曲线起始点
	private PointF mBezierControl1 = new PointF(); // 贝塞尔曲线控制点
	private PointF mBeziervertex1 = new PointF(); // 贝塞尔曲线顶点
	private PointF mBezierEnd1 = new PointF(); // 贝塞尔曲线结束点
	private PointF mBezierStart2 = new PointF(); // 另一条贝塞尔曲线
	private PointF mBezierControl2 = new PointF();
	private PointF mBeziervertex2 = new PointF();
	private PointF mBezierEnd2 = new PointF();
	private float mMiddleX;
	private float mMiddleY;
	private float mDegrees;
	private float mTouchToCornerDis;
	private ColorMatrixColorFilter mColorMatrixFilter;
	private Matrix mMatrix;
	private float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };

	private boolean mIsRTandLB; // 是否属于右上左下
	private float mMaxLength;
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
	private int p;
	private int[] wh;

	public RealPageWidget(Context context, int mWidth, int mHeight) {
		super(context);
		this.mWidth = mWidth;
		this.mHeight = mHeight;
		mMaxLength = (float) Math.hypot(mWidth, mHeight);
		mPath0 = new Path();
		mPath1 = new Path();
		createDrawable();

		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);

		ColorMatrix cm = new ColorMatrix();
		float array[] = { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0, 0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0 };
		cm.set(array);
		mColorMatrixFilter = new ColorMatrixColorFilter(cm);
		mMatrix = new Matrix();
		mScroller = new Scroller(getContext());

		mTouch.x = 0.01f; // 不让x,y为0,否则在点计算时会有问题
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
	public boolean doTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			mTouch.x = event.getX();
			mTouch.y = event.getY();
			this.postInvalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mTouch.x = event.getX();
			mTouch.y = event.getY();

			//计算拖拽点对应的拖拽脚
			calcCornerXY(event.getX(), event.getY());
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			startAnimation(1200);

			this.postInvalidate();
		}

		return true;
	}

	/**
	 * 设置位图图像
	 * 
	 * 设置翻页动画内容(当前页和下一页)
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

		//		canvas.drawBitmap(bg, 0, 0, null);

		calcPoints();
		drawCurrentPageArea(canvas, mCurPageBitmap, mPath0);
		drawNextPageAreaAndShadow(canvas, mNextPageBitmap);
		//		drawCurrentPageShadow(canvas);
		drawCurrentBackArea(canvas, mCurPageBitmap);

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

		//		setBg();
	}

	/*******************************************************************************
	 * 
	 * 以下是私有方法不对外
	 * 
	 *******************************************************************************/

	/**
	 * Author : hmg25 Version: 1.0 Description : 求解直线P1P2和直线P3P4的交点坐标
	 */
	private PointF getCross(PointF P1, PointF P2, PointF P3, PointF P4) {
		PointF CrossP = new PointF();
		// 二元函数通式： y=ax+b
		float a1 = (P2.y - P1.y) / (P2.x - P1.x);
		float b1 = ((P1.x * P2.y) - (P2.x * P1.y)) / (P1.x - P2.x);

		float a2 = (P4.y - P3.y) / (P4.x - P3.x);
		float b2 = ((P3.x * P4.y) - (P4.x * P3.y)) / (P3.x - P4.x);
		CrossP.x = (b2 - b1) / (a1 - a2);
		CrossP.y = a1 * CrossP.x + b1;
		return CrossP;
	}

	private void calcPoints() {
		mMiddleX = (mTouch.x + mCornerX) / 2;
		mMiddleY = (mTouch.y + mCornerY) / 2;
		mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY) * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
		mBezierControl1.y = mCornerY;
		mBezierControl2.x = mCornerX;
		mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);

		mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x) / 2;
		mBezierStart1.y = mCornerY;

		// 当mBezierStart1.x < 0或者mBezierStart1.x > 480时
		// 如果继续翻页，会出现BUG故在此限制
		if (mTouch.x > 0 && mTouch.x < mWidth) {
			if (mBezierStart1.x < 0 || mBezierStart1.x > mWidth) {
				if (mBezierStart1.x < 0)
					mBezierStart1.x = mWidth - mBezierStart1.x;

				float f1 = Math.abs(mCornerX - mTouch.x);
				float f2 = mWidth * f1 / mBezierStart1.x;
				mTouch.x = Math.abs(mCornerX - f2);

				float f3 = Math.abs(mCornerX - mTouch.x) * Math.abs(mCornerY - mTouch.y) / f1;
				mTouch.y = Math.abs(mCornerY - f3);

				mMiddleX = (mTouch.x + mCornerX) / 2;
				mMiddleY = (mTouch.y + mCornerY) / 2;

				mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY) * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
				mBezierControl1.y = mCornerY;

				mBezierControl2.x = mCornerX;
				mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);
				mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x) / 2;
			}
		}
		mBezierStart2.x = mCornerX;
		mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y) / 2;

		mTouchToCornerDis = (float) Math.hypot((mTouch.x - mCornerX), (mTouch.y - mCornerY));

		mBezierEnd1 = getCross(mTouch, mBezierControl1, mBezierStart1, mBezierStart2);
		mBezierEnd2 = getCross(mTouch, mBezierControl2, mBezierStart1, mBezierStart2);

		mBeziervertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4;
		mBeziervertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4;
		mBeziervertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4;
		mBeziervertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4;
	}

	/**
	 * 绘制当前页
	 */
	private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap, Path path) {
		mPath0.reset();
		mPath0.moveTo(mBezierStart1.x, mBezierStart1.y);
		mPath0.quadTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x, mBezierEnd1.y);
		mPath0.lineTo(mTouch.x, mTouch.y);
		mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y);
		mPath0.quadTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x, mBezierStart2.y);
		mPath0.lineTo(mCornerX, mCornerY);
		mPath0.close();

		canvas.save();
		//		canvas.clipPath(path, Region.Op.DIFFERENCE);
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.restore();

	}

	/**
	 * 绘制下一页
	 */
	private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {
		mPath1.reset();
		mPath1.moveTo(mBezierStart1.x, mBezierStart1.y);
		mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
		mPath1.lineTo(mBeziervertex2.x, mBeziervertex2.y);
		mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
		mPath1.lineTo(mCornerX, mCornerY);
		mPath1.close();

		mDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl1.x - mCornerX, mBezierControl2.y - mCornerY));
		int leftx;
		int rightx;
		GradientDrawable mBackShadowDrawable;
		if (mIsRTandLB) {
			leftx = (int) (mBezierStart1.x);
			rightx = (int) (mBezierStart1.x + mTouchToCornerDis / 4);
			mBackShadowDrawable = mBackShadowDrawableLR;
		} else {
			leftx = (int) (mBezierStart1.x - mTouchToCornerDis / 4);
			rightx = (int) mBezierStart1.x;
			mBackShadowDrawable = mBackShadowDrawableRL;
		}
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
		mBackShadowDrawable.setBounds(leftx, (int) mBezierStart1.y, rightx, (int) (mMaxLength + mBezierStart1.y));
		mBackShadowDrawable.draw(canvas);
		canvas.restore();
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

	/**
	 * 绘制翻起页的阴影
	 */
	private void drawCurrentPageShadow(Canvas canvas) {
		double degree;
		if (mIsRTandLB) {
			degree = Math.PI / 4 - Math.atan2(mBezierControl1.y - mTouch.y, mTouch.x - mBezierControl1.x);
		} else {
			degree = Math.PI / 4 - Math.atan2(mTouch.y - mBezierControl1.y, mTouch.x - mBezierControl1.x);
		}
		// 翻起页阴影顶点与touch点的距离
		double d1 = (float) 25 * 1.414 * Math.cos(degree);
		double d2 = (float) 25 * 1.414 * Math.sin(degree);
		float x = (float) (mTouch.x + d1);
		float y;
		if (mIsRTandLB) {
			y = (float) (mTouch.y + d2);
		} else {
			y = (float) (mTouch.y - d2);
		}
		mPath1.reset();
		mPath1.moveTo(x, y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mBezierControl1.x, mBezierControl1.y);
		mPath1.lineTo(mBezierStart1.x, mBezierStart1.y);
		mPath1.close();
		float rotateDegrees;
		canvas.save();

		canvas.clipPath(mPath0, Region.Op.XOR);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		int leftx;
		int rightx;
		GradientDrawable mCurrentPageShadow;
		if (mIsRTandLB) {
			leftx = (int) (mBezierControl1.x);
			rightx = (int) mBezierControl1.x + 25;
			mCurrentPageShadow = mFrontShadowDrawableVLR;
		} else {
			leftx = (int) (mBezierControl1.x - 25);
			rightx = (int) mBezierControl1.x + 1;
			mCurrentPageShadow = mFrontShadowDrawableVRL;
		}

		rotateDegrees = (float) Math.toDegrees(Math.atan2(mTouch.x - mBezierControl1.x, mBezierControl1.y - mTouch.y));
		canvas.rotate(rotateDegrees, mBezierControl1.x, mBezierControl1.y);
		mCurrentPageShadow.setBounds(leftx, (int) (mBezierControl1.y - mMaxLength), rightx, (int) (mBezierControl1.y));
		mCurrentPageShadow.draw(canvas);
		canvas.restore();

		mPath1.reset();
		mPath1.moveTo(x, y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mBezierControl2.x, mBezierControl2.y);
		mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
		mPath1.close();
		canvas.save();
		canvas.clipPath(mPath0, Region.Op.XOR);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		if (mIsRTandLB) {
			leftx = (int) (mBezierControl2.y);
			rightx = (int) (mBezierControl2.y + 25);
			mCurrentPageShadow = mFrontShadowDrawableHTB;
		} else {
			leftx = (int) (mBezierControl2.y - 25);
			rightx = (int) (mBezierControl2.y + 1);
			mCurrentPageShadow = mFrontShadowDrawableHBT;
		}
		rotateDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl2.y - mTouch.y, mBezierControl2.x - mTouch.x));
		canvas.rotate(rotateDegrees, mBezierControl2.x, mBezierControl2.y);
		float temp;
		if (mBezierControl2.y < 0)
			temp = mBezierControl2.y - mHeight;
		else
			temp = mBezierControl2.y;

		int hmg = (int) Math.hypot(mBezierControl2.x, temp);
		if (hmg > mMaxLength)
			mCurrentPageShadow.setBounds((int) (mBezierControl2.x - 25) - hmg, leftx, (int) (mBezierControl2.x + mMaxLength) - hmg, rightx);
		else
			mCurrentPageShadow.setBounds((int) (mBezierControl2.x - mMaxLength), leftx, (int) (mBezierControl2.x), rightx);

		mCurrentPageShadow.draw(canvas);
		canvas.restore();
	}

	/**
	 * 绘制翻起页背面
	 */
	private void drawCurrentBackArea(Canvas canvas, Bitmap bitmap) {
		int i = (int) (mBezierStart1.x + mBezierControl1.x) / 2;
		float f1 = Math.abs(i - mBezierControl1.x);
		int i1 = (int) (mBezierStart2.y + mBezierControl2.y) / 2;
		float f2 = Math.abs(i1 - mBezierControl2.y);
		float f3 = Math.min(f1, f2);
		mPath1.reset();
		mPath1.moveTo(mBeziervertex2.x, mBeziervertex2.y);
		mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
		mPath1.lineTo(mBezierEnd1.x, mBezierEnd1.y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mBezierEnd2.x, mBezierEnd2.y);
		mPath1.close();
		GradientDrawable mFolderShadowDrawable;
		int left;
		int right;
		if (mIsRTandLB) {
			left = (int) (mBezierStart1.x - 1);
			right = (int) (mBezierStart1.x + f3 + 1);
			mFolderShadowDrawable = mFolderShadowDrawableLR;
		} else {
			left = (int) (mBezierStart1.x - f3 - 1);
			right = (int) (mBezierStart1.x + 1);
			mFolderShadowDrawable = mFolderShadowDrawableRL;
		}
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);

		mPaint.setColorFilter(mColorMatrixFilter);

		float dis = (float) Math.hypot(mCornerX - mBezierControl1.x, mBezierControl2.y - mCornerY);
		float f8 = (mCornerX - mBezierControl1.x) / dis;
		float f9 = (mBezierControl2.y - mCornerY) / dis;
		mMatrixArray[0] = 1 - 2 * f9 * f9;
		mMatrixArray[1] = 2 * f8 * f9;
		mMatrixArray[3] = mMatrixArray[1];
		mMatrixArray[4] = 1 - 2 * f8 * f8;
		mMatrix.reset();
		mMatrix.setValues(mMatrixArray);
		mMatrix.preTranslate(-mBezierControl1.x, -mBezierControl1.y);
		mMatrix.postTranslate(mBezierControl1.x, mBezierControl1.y);
		//解决翻页白屏问题
		//		canvas.drawBitmap(bitmap, mMatrix, mPaint);
		canvas.drawBitmap(bitmap, mMatrix, null);
		mPaint.setColorFilter(null);
		canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
		mFolderShadowDrawable.setBounds(left, (int) mBezierStart1.y, right, (int) (mBezierStart1.y + mMaxLength));
		mFolderShadowDrawable.draw(canvas);
		canvas.restore();
	}

	public void startAnimation(int delayMillis) {
		int dx, dy;
		// dx 水平方向滑动的距离，负值会使滚动向左滚动
		// dy 垂直方向滑动的距离，负值会使滚动向上滚动
		if (mCornerX > 0) {
			dx = -(int) (mWidth + mTouch.x);
		} else {
			dx = (int) (mWidth - mTouch.x + mWidth);
		}
		if (mCornerY > 0) {
			dy = (int) (mHeight - 1 - mTouch.y);
		} else {
			dy = (int) (1 - mTouch.y); // 防止mTouch.y最终变为0
		}
		mScroller.startScroll((int) mTouch.x, (int) mTouch.y, dx, dy, delayMillis);
	}

}
