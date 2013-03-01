package robot.arm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.os.StatFs;

/**
 * 
 * 存储工具类
 * 
 * @author li.li
 * 
 */
public class StorageUtils {
	//存储大小
	public static final long BYTE = 1;//byte
	public static final long KB = 1024 * BYTE;//bm
	public static final long MB = 1024 * KB;//mb
	//时间
	public static long SECOND = 1000;//秒
	public static long MINUTE = SECOND * 60;//分
	public static long HOUR = MINUTE * 60;//时
	public static long DAY = HOUR * 24;//天

	public static final long CARD_MIN_CACHE_SIZE = 10 * MB;//SD卡最小缓存10m
	public static long VIEW_DATA_TIME_OUT = DAY * 5;//超时时间为5天

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

	/**
	 * 获取手机内部存储空间
	 */
	public static String internalMemoryRootPath() {
		return Environment.getDataDirectory().getPath();
	}

	//	/**
	//	 * 根据图片URL生成图片存储路径
	//	 */
	//	public static String getPicPath(String picUrl) throws Throwable {
	//		return convertUrlToFileName(picUrl);
	//	}

	/**
	 * 对图片URL进行MD5加密 作为图片名
	 */
	public static String convertUrlToFileName(String url) {
		return getMD5(url);
	}

	/**
	 * 获得对字符串进行MD5加密后的结果字符串
	 */
	private static String getMD5(String value) {
		if (value == null || "".equals(value))
			return null;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(value.getBytes("UTF-8"));
			return toHexString(md.digest());
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 获得指定byte[]对象中的所有byte值的16进制形式的结果字符串
	 */
	private static String toHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Character.forDigit((bytes[i] & 0XF0) >> 4, 16));
			sb.append(Character.forDigit(bytes[i] & 0X0F, 16));
		}
		return sb.toString();
	}

	/**  
	 * 得到文件的最后修改时间 
	 */
	public static long laftFileTime(String filePath) {
		File file = new File(filePath);

		//毫秒数
		long modifiedTime = file.lastModified();

		return modifiedTime;
	}

	public static void delete(String filePath) {
		File file = new File(filePath);

		file.deleteOnExit();
	}

	public static boolean exists(String filePath) {
		File file = new File(filePath);

		return file.exists();
	}

	public static boolean write(String data, String filePath) {
		OutputStream output = null;
		try {
			output = new FileOutputStream(new File(filePath));
			write(data, output, "utf-8");
			return true;
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					LogUtils.error(e.getMessage(), e);
				}
			}
		}

		return false;
	}

	public static String read(String filePath) {
		InputStream input = null;
		try {
			StringBuilder sb = new StringBuilder();
			input = new FileInputStream(new File(filePath));
			List<String> lines = readLines(input, "utf-8");
			for (String line : lines) {
				sb.append(line);
			}

			return sb.toString();
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LogUtils.error(e.getMessage(), e);
				}
			}
		}

		return null;
	}

	private static void write(String data, OutputStream output, String encoding) throws IOException {
		if (data != null)
			output.write(data.getBytes(encoding));
	}

	private static List<String> readLines(InputStream input, String encoding) throws IOException {

		InputStreamReader reader = new InputStreamReader(input, encoding);
		return readLines(((Reader) (reader)));

	}

	private static List<String> readLines(Reader input) throws IOException {
		BufferedReader reader = toBufferedReader(input);
		List<String> list = new ArrayList<String>();
		for (String line = reader.readLine(); line != null; line = reader.readLine())
			list.add(line);

		return list;
	}

	private static BufferedReader toBufferedReader(Reader reader) {
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

	public static boolean append(String data, String filePath) {
		Writer writer = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new FileWriter(file, true);
			writer.write(data);
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					LogUtils.error(e.getMessage(), e);
				}
			}
		}

		return false;
	}

}
