package robot.arm.provider.view.touch;

import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * 根据图片的 matrix 获取图片相应的状态信息
 */
public class ImageStateBean extends RectF {
	/**
	 * matrix中的值
	 */
	public float[] values;
	/**
	 * matrix缩放的比例
	 */
	public float scale;

	public ImageStateBean(int srcWidth, int srcHeight, Matrix matrix) {
		values = new float[9];
		matrix.getValues(values);
		scale = values[0];
		left = values[2];
		top = values[5];
		right = left + srcWidth * scale;
		bottom = top + srcHeight * scale;
	}

}
