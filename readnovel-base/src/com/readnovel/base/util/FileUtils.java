package com.readnovel.base.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作工具类
 * 
 * 
 * @author li.li
 *
 */
public class FileUtils {

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * @param dir 将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful.
	 *                 If a deletion fails, the method stops attempting to
	 *                 delete and returns "false".
	 */
	public static boolean deleteDir(File file) {
		return deleteDir(file, null);
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * @param dir 将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful.
	 *                 If a deletion fails, the method stops attempting to
	 *                 delete and returns "false".
	 */
	public static boolean deleteDir(File file, FilenameFilter filter) {
		if (!file.exists())
			return false;

		if (file.isDirectory()) {
			String[] children = file.list(filter);
			//递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(file, children[i]), filter);
				if (!success)
					return false;
			}
		}
		// 目录此时为空，可以删除
		return file.delete();
	}

	/**
	 * 递归删除目录下的所有文件
	 * @param dir 将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful.
	 *                 If a deletion fails, the method stops attempting to
	 *                 delete and returns "false".
	 */
	public static boolean deleteChildDir(File file, FilenameFilter filter) {
		if (!file.exists())
			return false;

		if (file.isDirectory()) {
			String[] children = file.list(filter);
			//递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(file, children[i]), filter);
				if (!success)
					return false;
			}
		}

		return false;
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

	/**
	 * 读取文件内容
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String readString(Reader input) throws IOException {
		BufferedReader reader = toBufferedReader(input);
		StringBuilder sb = new StringBuilder();
		for (String line = reader.readLine(); line != null; line = reader.readLine())
			sb.append(line + "\n");

		return sb.toString();
	}

	/**
	 * 读取文件内容
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLines(Reader input) throws IOException {
		BufferedReader reader = toBufferedReader(input);
		List<String> list = new ArrayList<String>();
		for (String line = reader.readLine(); line != null; line = reader.readLine())
			list.add(line);

		return list;
	}

	private static BufferedReader toBufferedReader(Reader reader) {
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

	public static void write(String data, Writer output) throws IOException {
		if (data != null)
			output.write(data);
	}

	public static void write(String data, OutputStream output) throws IOException {
		if (data != null)
			output.write(data.getBytes());
	}

}
