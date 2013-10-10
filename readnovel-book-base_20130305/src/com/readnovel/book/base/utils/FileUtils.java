package com.readnovel.book.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件操作工具类
 * 
 * 
 * @author li.li
 *
 */
public class FileUtils {

	public static void copy(InputStream in, OutputStream out) {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = in;
			output = out;
			copyLarge(input, output);
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					LogUtils.error(e.getMessage(), e);
				}
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					LogUtils.error(e.getMessage(), e);
				}
		}
	}

	public static void copy(String from, String to) {
		File fromFile = new File(from);
		File toFile = new File(to);

		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(fromFile);
			output = new FileOutputStream(toFile);
			copyLarge(input, output);
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					LogUtils.error(e.getMessage(), e);
				}
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					LogUtils.error(e.getMessage(), e);
				}
		}
	}

	private static long copyLarge(InputStream input, OutputStream output) throws IOException {
		return copyLarge(input, output, new byte[4096]);
	}

	private static long copyLarge(InputStream input, OutputStream output, byte buffer[]) throws IOException {
		long count = 0L;
		for (int n = 0; -1 != (n = input.read(buffer));) {
			output.write(buffer, 0, n);
			count += n;
		}

		return count;
	}
}
