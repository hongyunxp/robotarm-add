/**
 * 
 */
package robot.arm.utils;

import robot.arm.provider.asyc.AsycTask;
import robot.arm.provider.cache.Cache;
import robot.arm.provider.cache.CacheProvider;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * @author li.li
 * 
 *         Apr 19, 2012
 * 
 */
public class LoadImageUtils {
	private static final Cache cache = CacheProvider.getInstance();// 图片缓存

	public static void loadImageSync(Activity act, final String imageUrl, final ImageView imageView) {

		// 创建并执行异步任务
		new AsycTask<Activity>(act) {
			String picPath;

			@Override
			public void doCall() {

				picPath = cache.get(imageUrl);
				if (picPath == null)
					picPath = cache.put(imageUrl);

			}

			@Override
			public void doResult() {
				if (picPath == null)
					return;

				Bitmap bm = BitmapFactory.decodeFile(picPath);
				if (bm != null) {
					imageView.setImageBitmap(bm);
					imageView.setScaleType(ScaleType.CENTER_CROP);

				}

			}

		}.execute();

	}

}
