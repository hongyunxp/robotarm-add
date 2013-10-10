package com.readnovel.book.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtils {
	/**
	 * 序列化
	 * 
	 * @param fileName
	 * @param obj
	 */
	public static void write(String fileName, Object obj) {
		ObjectOutputStream out = null;
		FileOutputStream fos = null;

		try {
			File file = new File(fileName);

			if (!file.exists())
				file.createNewFile();
			else
				file.delete();

			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);

			out.writeObject(obj);
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			try {
				if (out != null)
					out.close();
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				LogUtils.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 反序列化
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T read(String fileName) {

		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {
			File file = new File(fileName);

			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);

			return (T) ois.readObject();
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				LogUtils.error(e.getMessage(), e);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T read(InputStream is) {

		ObjectInputStream ois = null;
		InputStream fis = null;
		try {

			fis = is;
			ois = new ObjectInputStream(fis);

			return (T) ois.readObject();
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {  
			try {
				if (ois != null)
					ois.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				LogUtils.error(e.getMessage(), e);
			}
		}

		return null;
	}
}
