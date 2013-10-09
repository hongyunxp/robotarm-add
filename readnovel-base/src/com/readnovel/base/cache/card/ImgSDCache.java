package com.readnovel.base.cache.card;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.readnovel.base.cache.Filter;
import com.readnovel.base.cache.KeyCreater;
import com.readnovel.base.util.HttpUtils;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.StorageUtils;
import com.readnovel.base.util.StringUtils;

/**
 * 缓存实现父类
 * 
 * @author li.li
 */
public class ImgSDCache extends AbsImgSDCache<Bitmap> {
	private static final long BYTE = 1;
	private static final long KB = 1024 * BYTE;
	private static final long MB = 1024 * KB;
	private static final long PIC_AVAILABLE_MIN_CACHE_SIZE = 10 * MB;

	private String picPath;
	protected String picRootPath = getRootPath() + picPath;

	public ImgSDCache(String picPath) {

		this.picPath = picPath;
		this.picRootPath = getRootPath() + picPath;

		// 初始化创建图片根路径
		if (available())
			checkPicPath(picRootPath);
	}

	/**
	 * 保存图片信息到SD卡
	 */
	@Override
	public boolean put(String url, Bitmap bm) {

		return put(url, bm, null, null);
	}

	@Override
	public boolean put(String url, Bitmap bm, Filter<Bitmap> filter) {

		return put(url, bm, filter, null);
	}

	@Override
	public boolean put(String url, Bitmap bm, Filter<Bitmap> filter, KeyCreater keyCreater) {
		if (!available())
			return false;// 不可用直接返回

		if (url == null || bm == null)
			return false;

		if (filter != null && !filter.accept(bm))
			return false;

		// 判断可用空间是否够用，当不可用时清空
		if (PIC_AVAILABLE_MIN_CACHE_SIZE > getAvailableMemorySize())
			cleanFolder(picRootPath);

		String key = null;

		if (keyCreater != null)
			key = keyCreater.create();
		else
			key = StorageUtils.convertUrlToFileName(url);

		try {
			return HttpUtils.saveImage(picRootPath + key, bm);// 保存图片到存储卡

		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		}

		return false;
	}

	@Override
	public Bitmap get(String url) {
		return get(url, null);
	}

	/**
	 * 根据图片URL， 获取图片对象
	 */
	@Override
	public Bitmap get(String url, KeyCreater keyCreater) {
		if (!available())// SD卡不可用
			return null;

		if (StringUtils.isBlank(url))
			return null;

		String key = null;
		if (keyCreater != null)
			key = keyCreater.create();
		else
			key = url;

		return getPicBitmap(key);
	}

	@Override
	public Bitmap get(String url, KeyCreater keyCreater, long timeOut) {
		if (!available())// SD卡不可用
			return null;

		if (StringUtils.isBlank(url))
			return null;

		String key = null;
		if (keyCreater != null)
			key = keyCreater.create();
		else
			key = url;

		return getPicBitmap(key);
	}

	/**
	 * **************************************************************** 
	 * 私有方法不对外
	 * ****************************************************************
	 */

	private String getPic(String picUrl) {
		try {
			// 根据图片url获取图片路径
			String picPath = StorageUtils.convertUrlToFileName(picUrl);
			String fullPicPath = picRootPath + picPath;

			checkPicPath(picRootPath);// 实时检测根路径是否存在，不存在创建

			if (!checkPicExists(fullPicPath))// 不存在图片返回空
				return null;

			return fullPicPath;
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	private Bitmap getPicBitmap(String picUrl) {
		try {
			if (StringUtils.isNotBlank(picUrl)) {
				String filePath = getPic(picUrl);
				return BitmapFactory.decodeFile(filePath);
			}
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
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
	private void cleanFolder(String filePath) {
		File f = new File(filePath);
		if (f.exists() && f.isDirectory()) {
			File[] files = f.listFiles();
			for (File file : files)
				file.delete();
		}
	}

}
