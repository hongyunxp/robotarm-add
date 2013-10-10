package com.readnovel.book.base.reader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import com.readnovel.book.base.sp.StyleSaveUtil;
import com.readnovel.book.lru.BMemCache;

public class PageWidget1 extends View {
	StyleSaveUtil sst;
	BMemCache bm = BMemCache.getInstance();
	private int mWidth = 0;
	private int mHeight = 0;
	private int mCornerX = 0; // 拖拽点对应的页脚
	private int mCornerY = 0;
	private Path mPath0;
	private Path mPath1;
	private Bitmap mCurPageBitmap = null; // 当前页
	private Bitmap mNextPageBitmap = null;
	private PointF mTouch = new PointF(); // 拖拽点
	private int[] mBackShadowColors;
	private int[] mBackShadowColors_night;
	private GradientDrawable mBackShadowDrawableLR;
	private GradientDrawable mBackShadowDrawableLR_Night;
	private Paint mPaint;
	private Scroller mScroller;
	private Bitmap bg;
	private int p;

	public PageWidget1(Context context, int mWidth, int mHeight) {
		super(context);
		this.mWidth = mWidth;
		this.mHeight = mHeight;
		mPath0 = new Path();
		mPath1 = new Path();
		createDrawable();
		mPaint = new Paint();
		sst = new StyleSaveUtil(context);
		mPaint.setStyle(Paint.Style.FILL);
		mScroller = new Scroller(getContext());
		mTouch.x = 0.0f; // 不让x,y为0,否则在点计算时会有问题
		mTouch.y = 0.01f;
	}

	/*
	 * 自动阅读，根据传入的纵坐标的值y进行绘制当前界面、
	 */
	public void doAotoRead(int y) {

		this.postInvalidate();
	}

	public void reflash() {
		this.postInvalidate();
	}

	/**
	 * 触摸事件处理
	 * 
	 * @param event
	 * @return
	 */
	public boolean doTouchEvent(MotionEvent e1, MotionEvent e2) {
		mTouch.x = e2.getX() - e1.getX();
		this.postInvalidate();
		return true;
	}

	public void doTouchUp() {
		startAnimation(800);
		this.postInvalidate();
	}

	public void doTouchUp(MotionEvent e2) {
		if (e2.getAction() == MotionEvent.ACTION_UP) {
			startAnimation(800);
			this.postInvalidate();
		}
	}

	/**
	 * 模拟按音量键时进行调节翻页，因为没有触摸时间，因此传过来我们定义的触摸点点的值，并且进行相应的模拟翻页操作
	 * 
	 * @param event
	 * @return
	 */
	public boolean doVoiceEvent(Boolean isleft) {
		if (isleft) {
			mScroller.startScroll((int) 0, 0, -31 * mWidth / 30, 0, 800);
		} else {
			mScroller.startScroll((int) 0, 0, mWidth, 0, 800);
		}
		this.postInvalidate();
		return true;
	}

	/**
	 * 设置位图图像
	 * 
	 * @param cur
	 * @param next
	 */
	public void setBitmaps(Bitmap cur, Bitmap next) {
		mCurPageBitmap = cur;
		mNextPageBitmap = next;
	}

	/**
	 *  主要目的是用来在ondraw方法中进行画图做准备，
	 */
	public void calc_CornerXY(float x, float y) {
		if (x < 0)
			mCornerX = mWidth;
		else
			mCornerX = 0;
	}

	/**
	 * 退出动画
	 */
	public void abortAnimation() {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
	}

	/**
	 * 判断是否从左边翻向右边
	 */
	public boolean isDragToRight() {
		if (mCornerX > 0)
			return false;
		return true;
	}


	/**
	 * 实现绘制画布
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		if (bg == null)//当无背景时
			setBg(sst.getreadbg());
		canvas.drawBitmap(bg, 0, 0, null);
		drawCurrentPageArea(canvas, mCurPageBitmap, mPath0);
		drawNextPageAreaAndShadow(canvas, mNextPageBitmap);
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
		setBg(p);
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
	private void setBg(int p) {
		bg = bm.get("bg" + p);
	}

	/**
	 * 绘制当前页
	 */
	private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap, Path path) {

		if ((new Float(mTouch.x)).intValue() >= 0) {
			// 左移
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
		} else {
			mPath0.reset();
			mPath0.moveTo(mWidth + mTouch.x, 0);
			mPath0.lineTo(mWidth + mTouch.x, mHeight);
			mPath0.lineTo(mWidth, mHeight);
			mPath0.lineTo(mWidth, 0);
			mPath0.close();
			canvas.save();
			canvas.clipPath(path, Region.Op.XOR);
			canvas.drawBitmap(bitmap, mTouch.x, 0, null);
			canvas.restore();
		}
	}

	/**
	 * 绘制下一页
	 */
	private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {
		mPath1.reset();
		mPath1.moveTo(mTouch.x, 0);
		mPath1.lineTo(mTouch.x, mHeight);
		mPath1.lineTo(mWidth, mHeight);
		mPath1.lineTo(mWidth, 0);
		mPath1.close();
		int leftx = 0;
		int rightx = 0;
		GradientDrawable mBackShadowDrawable;
		if (mTouch.x > 0) {
			leftx = (int) (mTouch.x);
			rightx = (int) (mTouch.x + 3*mWidth / 60);
		} else if (mTouch.x < 0) {
			leftx = (int) (mWidth + mTouch.x);
			rightx = (int) (mTouch.x + 63 * mWidth / 60);
		}
		if (sst.getreadbg() == 0) {
			mBackShadowDrawable = mBackShadowDrawableLR;
		} else {
			mBackShadowDrawable = mBackShadowDrawableLR_Night;
		}
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		canvas.drawBitmap(bitmap, 0, 0, null);
		if (mTouch.x != 0) {
			mBackShadowDrawable.setBounds(leftx, (int) 0, rightx, (int) (mHeight));
			mBackShadowDrawable.draw(canvas);
		}
		canvas.restore();
	}

	/**
	 * 创建阴影的GradientDrawable
	 */
	private void createDrawable() {
		int[] color = { 0x22666666, 0x00CCCCCC };
		
		mBackShadowColors = new int[] { 0x66333333, 0x00CCCCCC };
		mBackShadowColors_night = new int[] { 0x99111111, 0x99333333 };
		
		mBackShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
		mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowDrawableLR_Night = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors_night);
		mBackShadowDrawableLR_Night.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		mBackShadowColors_night = new int[] { 0x66333333, 0x00CCCCCC };

		
	}

	// 这个动画处理滑动
	private void startAnimation(int delayMillis) {
		int dx = 0;

		if (mTouch.x > 0) {
			//            if(mTouch.x < mWidth/10 ){
			//            	dx = -(int) (mTouch.x+0.1);

			//            }else{
			dx = (int) (mWidth - mTouch.x);
			//            }
		} else if (mTouch.x < 0) {
			//			if(mTouch.x > -mWidth/10){
			//				dx = -(int)(mTouch.x);
			//			}else {
			dx = -(int) (mWidth + mTouch.x + mWidth / 30);
		}
		//            }
		mScroller.startScroll((int) mTouch.x, 0, dx, 0, delayMillis);
	}

	// 这个动画处理：音量键翻页，轻击翻页 
	public void startAnimation2(Boolean isleft) {
		if (isleft) {
			mScroller.startScroll((int) 0, 0, -31 * mWidth / 30, 0, 800);
		} else {
			mScroller.startScroll((int) 0, 0, mWidth, 0, 800);
		}
	}
}