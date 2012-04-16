package robot.arm.provider.view.touch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 实现了 图片 缩放、移动、有效移动范围
 */
public class TouchImageView extends ImageView {

	public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public TouchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TouchImageView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
	}

	private Bitmap bitmap;
	private Matrix matrix = new Matrix();
	private Paint paint = new Paint();
	private ImageStateBean imageState;

	// 放大比例
	private float maxScale = 5.0f;
	// 缩小比例
	private float minScale = 1;

	@Override
	public void setImageBitmap(Bitmap bm) {
		bitmap = bm;
		setVisibility(INVISIBLE);

		//图片屏幕居中
		postDelayed(new Runnable() {
			public void run() {
				int mw = getWidth() - bitmap.getWidth();
				int mh = getHeight() - bitmap.getHeight();
				postTranslate(mw / 2, mh / 2);
				setVisibility(VISIBLE);
			}
		}, 100);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (bitmap == null)
			return;
		adjustPosition();
		canvas.drawBitmap(bitmap, matrix, paint);
	}

	// 校正图片位置（位置限制）
	private void adjustPosition() {
		imageState = new ImageStateBean(bitmap.getWidth(), bitmap.getHeight(), matrix);
		float offsetX = getWidth() - imageState.right;
		float offsetY = getHeight() - imageState.bottom;

		// left、right的范围处理
		if (imageState.width() > getWidth()) {
			// 图片宽 > 容器宽，则限制图片的左右移动范围 >= 容器的left、right
			if (imageState.left > 0) {
				postTranslate(-imageState.left, 0);
			} else if (offsetX > 0) {
				postTranslate(offsetX, 0);
			}
		} else {
			// 图片宽 < 容器宽，则限制图片的左右移动范围 <= 容器的left、right
			if (imageState.left < 0) {
				postTranslate(-imageState.left, 0);
			} else if (offsetX < 0) {
				postTranslate(offsetX, 0);
			}
		}
		// top、bottom的范围处理
		if (imageState.height() > getHeight()) {
			// 图片高 > 容器高，则限制图片的上下移动范围 >= 容器的top、bottom
			if (imageState.top > 0) {
				postTranslate(0, -imageState.top);
			} else if (offsetY > 0) {
				postTranslate(0, offsetY);
			}
		} else {
			// 图片高 < 容器高，则限制图片的上下移动范围 <= 容器的top、bottom
			if (imageState.top < 0) {
				postTranslate(0, -imageState.top);
			} else if (offsetY < 0) {
				postTranslate(0, offsetY);
			}
		}
	}

	// 还原图片 放大、移动
	public void reset() {
		matrix = new Matrix();
		invalidate();
	}

	// 图片移动
	public void postTranslate(float distanceX, float distanceY) {
		if (bitmap == null)
			return;
		matrix.postTranslate(distanceX, distanceY);
		invalidate();
	}

	// 图片缩放
	public void postScale(float scale, float focusX, float focusY) {
		if (bitmap == null)
			return;
		imageState = new ImageStateBean(bitmap.getWidth(), bitmap.getHeight(), matrix);
		// 放大
		if (scale > 1) {
			float m = maxScale / imageState.scale;
			scale = Math.min(scale, m);
		}
		// 缩小
		else {
			float m = minScale / imageState.scale;
			scale = Math.max(scale, m);
		}
		matrix.postScale(scale, scale, focusX, focusY);
		invalidate();
	}

}
