package com.readnovel.base.cache.lru;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

/**
 * 硬盘图片缓存
 * @author li.li
 *
 * Apr 17, 2013
 */
public class BitmapDiskLruCache extends DiskLruCache<Bitmap> {
	private static final CompressFormat mCompressFormat = CompressFormat.JPEG;
	private static final int mCompressQuality = 70;
	private static final int IO_BUFFER_SIZE = 4 * 1024;

	public BitmapDiskLruCache(File cacheDir, long maxByteSize) {
		super(cacheDir, maxByteSize);
	}

	@Override
	protected boolean writeToFile(Bitmap bitmap, String file) throws IOException, FileNotFoundException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(file), IO_BUFFER_SIZE);
			return bitmap.compress(mCompressFormat, mCompressQuality, out);
		} finally {
			if (out != null) {
				out.close();
			}
		}

	}

	@Override
	protected Bitmap readFromFile(String file) throws OutOfMemoryError {
		return BitmapFactory.decodeFile(file);
	}

}
