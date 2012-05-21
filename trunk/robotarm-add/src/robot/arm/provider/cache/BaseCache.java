/**
 * 
 */
package robot.arm.provider.cache;

import java.io.File;

import robot.arm.utils.BaseUtils;
import android.util.Log;

/**
 * @author li.li
 * 
 *         Apr 28, 2012
 * 
 */
public abstract class BaseCache implements Cache {
	private static final String TAG = Cache.class.getSimpleName();

	private static String PIC_PATH = "/look-beautiful/pic/";
	private static final long BYTE = 1;
	private static final long KB = 1024 * BYTE;
	private static final long MB = 1024 * KB;
	private static final long PIC_AVAILABLE_MIN_CACHE_SIZE = 20 * MB;

	protected boolean AVAILABLE = available();
	protected String ROOT_PATH = getRootPath();
	protected String PIC_ROOT_PATH = ROOT_PATH + PIC_PATH;
	private File picRootPath = new File(PIC_ROOT_PATH);

	{
		// 初始化图片保存路径
		if (AVAILABLE)
			checkPicPath(PIC_ROOT_PATH);

	}

	abstract public String getRootPath();

	abstract public boolean available();

	/**
	 * 保存图片信息到SD卡
	 */
	@Override
	public String put(String imageUrl) {

		if (!AVAILABLE)
			return null;// 不可用直接返回

		if (imageUrl == null)
			return null;

		// 判断可用空间是否够用，当不可用时清空
		if (PIC_AVAILABLE_MIN_CACHE_SIZE > getAvailableMemorySize())
			cleanFolder(picRootPath);

		return BaseUtils.loadImage(getRootPath(), imageUrl);// 加载图片到存储卡
	}

	/**
	 * 根据图片URL， 获取图片对象
	 */
	@Override
	public String get(String picUrl) {
		if (!AVAILABLE)// SD卡不可用
			return null;

		if ("".equals(picUrl))
			return null;

		return getPic(picUrl);
	}

	private String getPic(String picUrl) {
		try {
			// 根据图片url获取图片路径
			String picPath = BaseUtils.getPicPath(getRootPath(), picUrl);

			if (!checkPicExists(picPath))
				return null;

			return picPath;
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 检查图片目录是否存在 如不存在则创建目录
	 */
	private static boolean checkPicPath(String picPath) {
		File file = new File(picPath);
		if (!file.exists())
			file.mkdirs();
		return file.exists();
	}

	/**
	 * 检查图片是否存在
	 */
	private boolean checkPicExists(String picPath) {
		File file = new File(picPath);
		return file.exists();
	}

	/**
	 * 清空文件夹里的文件
	 */
	private void cleanFolder(File f) {
		if (f.exists() && f.isDirectory()) {
			File[] files = f.listFiles();
			for (File file : files)
				file.delete();
		}
	}
}
