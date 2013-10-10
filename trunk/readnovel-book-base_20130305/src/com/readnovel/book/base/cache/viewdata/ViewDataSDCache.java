package com.readnovel.book.base.cache.viewdata;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.readnovel.book.base.cache.Filter;
import com.readnovel.book.base.cache.KeyCreater;
import com.readnovel.book.base.utils.StorageUtils;
import com.readnovel.book.base.utils.StringUtils;


/**
 * SD卡视图数据缓存
 * 
 */
public class ViewDataSDCache extends AbsViewDataSDCache<JSONObject> {
	private static volatile ViewDataSDCache instance;
	private final String rootPath;

	private ViewDataSDCache(String path) {
		//初始化根路径
		rootPath = getRootPath() + path;

		// 初始化创建图片根路径
		if (available())
			checkPicPath(rootPath);

	}

	/**
	 * 得到实例
	 * @param path 存储相对路径
	 * @return
	 */
	public static ViewDataSDCache getInstance(String path) {
		if (instance == null) {
			synchronized (ViewDataSDCache.class) {
				if (instance == null) {
					instance = new ViewDataSDCache(path);
				}
			}
		}
		return instance;
	}

	@Override
	public JSONObject get(String url) {
		return get(url, null);
	}

	// 从缓存中取
	@Override
	public JSONObject get(String url, KeyCreater keyCreater) {

		return get(url, keyCreater, StorageUtils.VIEW_DATA_TIME_OUT);
	}

	@Override
	public JSONObject get(String url, KeyCreater keyCreater, long timeOut) {
		if (!available())
			return null;// 不可用直接返回

		if (StringUtils.isBlank(url))
			return null;

		String fileName = null;

		if (keyCreater != null)
			fileName = keyCreater.create();
		else
			fileName = StorageUtils.convertUrlToFileName(url);

		String fileFullName = rootPath + fileName;

		JSONObject jo = null;

		boolean exists = StorageUtils.exists(fileFullName);

		if (exists) {//当文件存在时
			long time = StorageUtils.laftFileTime(fileFullName);
			long curTime = System.currentTimeMillis();

			if (time != 0 && curTime - time > timeOut) {//超过缓存时间删除
				StorageUtils.delete(fileFullName);
			} else {
				String viewData = StorageUtils.read(fileFullName);//取缓存数据
				try {
					if (StringUtils.isNotBlank(viewData))
						jo = new JSONObject(viewData);//将缓存数据组织成JSONObject
				} catch (JSONException e) {
					}
			}
		}

			
		return jo;
	}

	// 放入缓存
	@Override
	public boolean put(String url, JSONObject viewData) {

		return put(url, viewData, null, null);
	}

	// 放入缓存,根据一定条件
	@Override
	public boolean put(String url, JSONObject viewData, Filter<JSONObject> filter) {

		return put(url, viewData, filter, null);
	}

	@Override
	public boolean put(String url, JSONObject viewData, Filter<JSONObject> filter, KeyCreater keyCreater) {
		if (!available())
			return false;// 不可用直接返回

		if (StringUtils.isBlank(url) || viewData == null)
			return false;

		if (filter != null && !filter.accept(viewData))
			return false;

		// 判断可用空间是否够用，当不可用时清空
		if (StorageUtils.CARD_MIN_CACHE_SIZE > getAvailableMemorySize())
			cleanFolder(rootPath);

		String key = null;

		if (keyCreater != null)
			key = keyCreater.create();
		else
			key = StorageUtils.convertUrlToFileName(url);

		String fileFullName = rootPath + key;
		boolean result = StorageUtils.write(viewData.toString(), fileFullName);//写缓存数据

		
		return result;
	}

	public void rename(String fromFileName, String toFileName) {
		File fromFile = new File(rootPath + fromFileName);
		if (fromFile.exists()) {
			File toFile = new File(rootPath + toFileName);
			fromFile.renameTo(toFile);
		}
	}

	public void delCache(String url) {
		String key = StorageUtils.convertUrlToFileName(url);
		String fileFullName = rootPath + key;
		File cacheFile = new File(fileFullName);

		if (cacheFile.exists())
			cacheFile.delete();

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

	/**
	 * 检查图片目录是否存在 如不存在则创建目录
	 */
	private boolean checkPicPath(String picPath) {
		File file = new File(picPath);
		if (!file.exists())
			file.mkdirs();
		return file.exists();
	}

}
