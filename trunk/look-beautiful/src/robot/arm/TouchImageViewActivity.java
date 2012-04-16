/**
 * 
 */
package robot.arm;

import robot.arm.provider.view.touch.TouchImageView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * @author li.li
 * 
 *         Apr 16, 2012
 * 
 */
public class TouchImageViewActivity extends Activity {
	private TouchImageView touchImageView;

	/** 缩放手势识别 */
	private ScaleGestureDetector scaleGestureDetector;

	/** 移动手势 */
	private GestureDetector gestureDetector;

	/**
	 * 大图的触摸
	 */
	private OnTouchListener onTouch = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			if (scaleGestureDetector == null || gestureDetector == null)
				return false;
			scaleGestureDetector.onTouchEvent(event);
			gestureDetector.onTouchEvent(event);
			return true;
		}
	};

	/**
	 * 缩放监听器
	 */
	private SimpleOnScaleGestureListener simpleOnScaleGestureListener = new SimpleOnScaleGestureListener() {
		public boolean onScale(ScaleGestureDetector detector) {
			float sf = detector.getScaleFactor();
			if (sf > 0 && sf < 2)
				touchImageView.postScale(sf, detector.getFocusX(), detector.getFocusY());
			return true;
		}
	};

	/**
	 * 移动监听器
	 */
	private SimpleOnGestureListener simpleOnGestureListener = new SimpleOnGestureListener() {
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			if (scaleGestureDetector.isInProgress())
				return false;
			touchImageView.postTranslate(-distanceX, -distanceY);
			return true;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.touch_image_content);

		touchImageView = (TouchImageView) findViewById(R.id.touch_image_view);
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.m3);
		touchImageView.setImageBitmap(bm);
		touchImageView.reset();

		// 缩放手势
		scaleGestureDetector = new ScaleGestureDetector(this, simpleOnScaleGestureListener);
		// 移动手势
		gestureDetector = new GestureDetector(this, simpleOnGestureListener);

		touchImageView.setOnTouchListener(onTouch);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ((TabInvHandler) getParent()).titleVisible(false);
		// ((TabInvHandler) getParent()).tabVisible(false);
		// ((TabInvHandler) getParent()).needCloseSoftInput(true);
	}
}
