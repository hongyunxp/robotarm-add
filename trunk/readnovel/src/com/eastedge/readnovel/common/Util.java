package com.eastedge.readnovel.common;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.view.WindowManager;

import com.eastedge.readnovel.beans.Shubenmulu;
import com.readnovel.base.util.LogUtils;

public class Util {

	/**
	 * 通过URL 获取图片
	 * @param ctx
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static Drawable getDrawableFromCache(Context ctx, String url) throws IOException {
		if (url == null || url.equals(""))
			return null;

		String urlPath = "";
		Uri uri = null;

		urlPath = url;

		File cacheFile = new File(Constants.READNOVEL_IMGCACHE);
		cacheFile.mkdirs();
		File file = new File(cacheFile, md5(urlPath));
		try {
			if (file.exists()) {
				uri = Uri.fromFile(file);
			} else {
				FileOutputStream outStream = new FileOutputStream(file);
				HttpURLConnection conn = (HttpURLConnection) new URL(urlPath).openConnection();
				conn.setConnectTimeout(10 * 1000);
				//				conn.setRequestMethod("GET");
				if (conn.getResponseCode() == 200) {
					InputStream inStream = conn.getInputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					outStream.close();
					inStream.close();
					uri = Uri.fromFile(file);
				} else
					return null;
			}

		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			if (file.length() < 10) {
				file.delete();
				return null;
			}
		}
		return Drawable.createFromStream(ctx.getContentResolver().openInputStream(uri), null);
	}

	public static Drawable UritoDrawable(Context ctx, Uri uri) {
		try {
			return Drawable.createFromStream(ctx.getContentResolver().openInputStream(uri), null);
		} catch (FileNotFoundException e) {
			LogUtils.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 下载图片
	 * @param ctx
	 * @param url
	 * @return
	 */
	public static String saveImgFile(Context ctx, String url) {
		if (url == null || url.equals(""))
			return null;
		String urlPath = url;

		File cacheFile = new File(Constants.READNOVEL_IMGCACHE);
		cacheFile.mkdirs();
		File file = new File(cacheFile, md5(urlPath));
		try {
			if (file.exists()) {
				return file.getPath();
			} else {
				FileOutputStream outStream = new FileOutputStream(file);
				HttpURLConnection conn = (HttpURLConnection) new URL(urlPath).openConnection();
				conn.setConnectTimeout(10 * 1000);
				//				conn.setRequestMethod("GET");
				if (conn.getResponseCode() == 200) {
					InputStream inStream = conn.getInputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					outStream.close();
					inStream.close();
					return file.getPath();
				} else
					return null;
			}

		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			if (file.length() < 10) {
				file.delete();
				return null;
			}
		}
		return file.getPath();
	}

	public static String slStr(String str) {
		if (str == null)
			return "";
		if (str.length() > 5) {
			return str.substring(0, 5);
		}
		return str;
	}

	/**
	 * md5加密
	 * @param s
	 * @return
	 */
	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			LogUtils.error(e.getMessage(), e);
		}

		return "";
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * gbk转成Unicode
	 * @author beiyongji2
	 * @date 2012-3-22 下午5:40:55 
	 * @param str
	 * @return
	 */
	public static String GBK2Unicode(String str) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char chr1 = (char) str.charAt(i);
			if (!isNeedConvert(chr1)) {
				result.append(chr1);
				continue;
			}
			result.append("\\u" + Integer.toHexString((int) chr1));
		}
		return result.toString();
	}

	public static boolean isNeedConvert(char para) {
		return ((para & (0x00FF)) != para);
	}

	/** * 获取屏幕的亮度 */

	public static int getScreenBrightness(Activity activity) {
		int nowBrightnessValue = 0;
		ContentResolver resolver = activity.getContentResolver();
		try {
			nowBrightnessValue = android.provider.Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return nowBrightnessValue;
	}

	/** * 获取屏幕的亮度 */

	public static int getScreenBrightness(Context context) {
		int nowBrightnessValue = 0;
		ContentResolver resolver = context.getContentResolver();
		try {
			nowBrightnessValue = android.provider.Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return nowBrightnessValue;
	}

	/** * 设置亮度 */

	public static void setBrightness(Activity activity, int brightness) {

		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

		lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);

		activity.getWindow().setAttributes(lp);
	}

	public static void copyphoneToSd(Context context, String from) {
		AssetManager am = context.getAssets();
		InputStream ins = null;

		String sp = Environment.getExternalStorageDirectory() + "/read/";
		File outFile = new File(sp, "test.txt");
		String parentDir = outFile.getParent();
		File parentFile = new File(parentDir);

		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}

		if (!outFile.exists()) {
			try {
				outFile.createNewFile();
				ins = am.open(from);
				DataInputStream dis = new DataInputStream(ins);
				FileOutputStream fOut = new FileOutputStream(outFile);
				int n = 4 * 1024;
				byte[] buffer = new byte[n];
				int num;
				while ((dis.read(buffer)) != -1) {
					fOut.write(buffer);
				}
				fOut.flush();
				fOut.close();
				dis.close();
			} catch (FileNotFoundException e) {
				LogUtils.error(e.getMessage(), e);
			} catch (IOException e) {
				LogUtils.error(e.getMessage(), e);
			}
		}

	}

	/**
	 * 存目录
	 * @param aid
	 * @param mul
	 */
	public static void write(String aid, Shubenmulu mul) {
		try {
			File f = new File(Constants.READNOVEL_DownML, "dlml" + aid);
			f.getParentFile().mkdirs();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
			out.writeObject(mul);
			out.close();
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
	}

	/**
	 * 取目录
	 * @param aid
	 * @return
	 */
	public static Shubenmulu read(String aid) {
		ObjectInputStream in = null;
		try {
			File f = new File(Constants.READNOVEL_DownML, "dlml" + aid);
			if (f.exists()) {
				in = new ObjectInputStream(new FileInputStream(f));
				Shubenmulu mul = (Shubenmulu) in.readObject();
				return mul;
			}

		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					LogUtils.error(e.getMessage(), e);
				}
			}

		}

		return null;
	}

	/**
	 * 取书
	 * @param aid
	 * @return
	 */
	public static File readBook(String aid) {
		//书所在位置
		String bookFileName = Constants.READNOVEL_BOOK + "/book_text_" + aid + ".txt";
		File bookFile = new File(bookFileName);

		return bookFile;
	}

	// 判断系统版本
	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {

		}
		return version;
	}

	public static String setToString(Set<String> set) {
		if (set != null && set.size() > 0) {
			String st = "";
			StringBuffer buff = new StringBuffer();
			for (String string : set) {
				buff.append(st + string);
				if ("".equals(st)) {
					st = ",";
				}
			}
			return buff.toString();
		}

		return "";
	}
}
