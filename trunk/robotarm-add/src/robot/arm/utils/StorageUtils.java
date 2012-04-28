/**
 * 
 */
package robot.arm.utils;

import java.io.File;

import robot.arm.common.RobotArmApp;
import android.os.Environment;
import android.os.StatFs;

/**
 * @author li.li
 * 
 *         Apr 28, 2012
 * 
 */
public class StorageUtils {
	// 这个是手机内存的可用空间大小
	public static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	// 这个是手机内存的总空间大小
	public static long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}
	
	/**
	 * 获取手机存储根目录
	 */
	public static String internalMemoryRootPath() {
		return RobotArmApp.getApp().getFilesDir().getPath();
	}

	// 这个是手机sdcard的可用空间大小
	public static long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		} else {
			return 0;
		}
	}

	// 这个是手机sdcard的总空间大小
	public static long getTotalExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return totalBlocks * blockSize;
		} else {
			return 0;
		}
	}

	// 判断sd卡是否可用
	public static boolean externalMemoryAvailable() {
		String currentState = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(currentState);
	}

	/**
	 * 获取SDCard存储根目录
	 */
	public static String externalMemoryRootPath() {
		if (externalMemoryAvailable())
			return Environment.getExternalStorageDirectory().getPath();

		return null;
	}

}
