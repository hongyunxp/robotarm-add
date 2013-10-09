package com.eastedge.readnovel.threads;

import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.db.DBAdapter;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.http.HttpComm;
import com.xs.cn.http.HttpImpl;

public class CheckUpdateBookThread extends Thread {

	private Context context;
	public boolean hasup;
	public int count = 0;
	private DBAdapter db;

	public CheckUpdateBookThread(Context context) {
		this.context = context;
	}

	public CheckUpdateBookThread(DBAdapter db) {
		this.db = db;
	}

	public void run() {
		LogUtils.info("正在同步数据");

		try {
			if (db == null) {
				db = new DBAdapter(context);
			}
			db.open();
			HashMap<String, Long> map2 = db.queryBookToUp();
//			JSONObject json = HttpComm.sendJSONToServer(Constants.URL + "/web/book.php?a=up_info&booklist=" + cg(map2) + "&data=json&ct=android", 1);
			JSONObject json = HttpImpl.updateInfo(cg(map2));
			
			if (json == null) {
				return;
			}

			if (!json.isNull("root")) {

				JSONArray arr = json.getJSONArray("root");
				HashMap<String, Long> map = new HashMap<String, Long>();
				for (int i = 0; i < arr.length(); i++) {
					JSONObject obj = arr.getJSONObject(i);
					if (!obj.isNull("articleid") && !obj.isNull("last_text_time")) {
						map.put(obj.getString("articleid"), obj.getLong("last_text_time"));
					}
				}

				Set<String> aidSet = map.keySet();
				for (String string : aidSet) {
					if (map2.containsKey(string)) {
						long v1 = map.get(string);
						long v2 = map2.get(string);
						if (v1 > v2) {
							hasup = true;
							count++;
							db.isNeedUp(string, 1);
						}
					}
				}

			}
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			if (db != null)
				db.close();
		}
		LogUtils.info("同步完数据");

	}

	private String cg(HashMap<String, Long> mp) {
		Set<String> set = mp.keySet();
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
}
