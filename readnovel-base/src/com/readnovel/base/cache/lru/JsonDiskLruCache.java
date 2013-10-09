package com.readnovel.base.cache.lru;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.Writer;

import org.json.JSONObject;

import com.readnovel.base.util.FileUtils;

/**
 * LRU
 * @author li.li
 *
 * Apr 22, 2013
 */
public class JsonDiskLruCache extends DiskLruCache<JSONObject> {

	public JsonDiskLruCache(File cacheDir) {
		super(cacheDir);
	}

	public JsonDiskLruCache(File cacheDir, long maxByteSize) {
		super(cacheDir, maxByteSize);
	}

	public JsonDiskLruCache(File cacheDir, int maxItemSize) {
		super(cacheDir, maxItemSize);
	}

	public JsonDiskLruCache(File cacheDir, long maxByteSize, int maxItemSize) {
		super(cacheDir, maxItemSize);
	}

	@Override
	protected boolean writeToFile(JSONObject obj, String file) throws Throwable {

		Writer out = null;
		try {
			FileUtils.write(obj.toString(), out);
			return true;
		} finally {
			if (out != null)
				out.close();
		}

	}

	@Override
	protected JSONObject readFromFile(String file) throws Throwable {
		Reader reader = null;

		try {
			reader = new FileReader(new File(file));
			String json = FileUtils.readString(reader);

			return new JSONObject(json);
		} finally {
			if (reader != null)
				reader.close();

		}

	}

}
