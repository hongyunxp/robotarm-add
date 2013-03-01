/**
 * 
 */
package robot.arm.provider.http;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import robot.arm.R;
import robot.arm.provider.cache.Cache;
import robot.arm.provider.cache.card.ImgCacheProvider;
import robot.arm.utils.HttpUtils;
import robot.arm.utils.LogUtils;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * 图片异步加载工具
 * 
 * @author li.li
 * 
 */
public class LoadImgProvider {
	private static volatile LoadImgProvider instance;
	private static final int DELAY_TIME = 1000;//延迟加载时间
	private static final int THREAD_WORKER_COUNT = 3;// 工人数量
	private static final ExecutorService THREAD_WORKER = Executors.newFixedThreadPool(THREAD_WORKER_COUNT);// 线程池
	private static final Handler HANDER = new Handler();// UI处理

	private final Set<String> urlCache;// 图片url记录
	private final Cache<Bitmap> cache;// 磁盘缓存

	private LoadImgProvider(String picPath) {
		cache = ImgCacheProvider.getInstance(picPath);
		urlCache = new HashSet<String>();
	}

	/**
	 * 
	 * @param picPath 缓存相对路径。例：String READNOVEL_IMGCACHE = "/readnovel/imgCache/"
	 * @return
	 */
	public static LoadImgProvider getInstance(String picPath) {
		if (instance == null) {
			synchronized (LoadImgProvider.class) {
				if (instance == null) {
					instance = new LoadImgProvider(picPath);
				}

			}
		}

		return instance;
	}

	/**
	 * 图片加载带默认图
	 * 
	 * @param act
	 * @param imageUrl
	 *            图片url
	 * @param imageView
	 *            图片组件
	 */
	public void load(String imageUrl, ImageView imageView) {

		load(imageUrl, imageView, true, ScaleType.FIT_XY);

	}

	/**
	 * 图片加载
	 * 
	 * @param act
	 * @param imageUrl
	 *            图片url
	 * @param imageView
	 *            图片组件
	 * @param hasDefIcon
	 *            是否带默认图
	 * @param scaleType
	 *            绽放模式
	 */
	public void load(String imageUrl, ImageView imageView, boolean hasDefIcon, ScaleType scaleType) {
		if (hasDefIcon) {
			imageView.setImageResource(R.drawable.download);
		}

		if (View.VISIBLE != imageView.getVisibility())
			imageView.setVisibility(View.VISIBLE);

		Bitmap bm = cache.get(imageUrl, null);

		if (bm != null) {
			doLoadImageSync(bm, imageView, scaleType);// 从缓存取
			bm = null;
		} else {
			doLoadImageAsyn(imageUrl, imageView, scaleType);// 从网络加载
		}

	}

	/**
	 * 关闭线程池
	 */
	public static void shutdown() {
		THREAD_WORKER.shutdown();
	}

	/**
	 * *********************************************************************
	 * 以下私有方法不对外
	 * *********************************************************************
	 */

	/**
	 * 图片同步加载
	 * 
	 * @param pic 图片本地地址
	 * @param imageView 图片组件
	 */
	private void doLoadImageSync(Bitmap bm, ImageView imageView, ScaleType scaleType) {

		imageView.setScaleType(scaleType);
		imageView.setImageBitmap(bm);

	}

	/**
	 * 图片异步加载，成功后放入缓存
	 * 
	 * @param imageUrl 图片url
	 * @param imageView 图片组件
	 */
	private void doLoadImageAsyn(final String imageUrl, final ImageView imageView, final ScaleType scaleType) {
		// 防止重复加载同一url的图片
		synchronized (this) {
			if (urlCache.contains(imageUrl))
				return;
			urlCache.add(imageUrl);
		}
		// 异步加载
		THREAD_WORKER.execute(new Runnable() {
			@Override
			public void run() {

				final ImageView iv = imageView;
				final Bitmap bm = HttpUtils.loadImage(imageUrl);// 网络加载图片

				if (bm == null) {
					LogUtils.info("网络加载图片失败|" + imageUrl);
					return;
				}

				boolean result = cache.put(imageUrl, bm);// 放入缓存
				if (!result)
					LogUtils.info("放入存储卡失败|" + imageUrl);
				HANDER.postDelayed(new Runnable() {

					@Override
					public void run() {
						iv.setScaleType(scaleType);
						iv.setImageBitmap(bm);

						urlCache.remove(imageUrl);
					}
				}, DELAY_TIME);

			}
		});
	}

}
