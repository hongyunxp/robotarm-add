package com.readnovel.book.base.http;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.readnovel.book.base.bean.MoreDanBen;
import com.readnovel.book.base.bean.MoreHeji;
import com.readnovel.book.base.common.JsonToBean;
import com.readnovel.book.base.utils.StorageUtils;

public class HttpImpl {
	public static MoreHeji getmoreunion(String moreheji) {
		JSONObject jsonmoreheji = HttpComm.sendJSONToServerWithCache(moreheji, StorageUtils.DAY * 30);
		//		JSONObject jsonmoreheji = HttpComm.sendJSONToServer(moreheji, 0);
		return JsonToBean.JsonToMoreBook(jsonmoreheji);
	}

	/*
	 *  得到更多单本的接口
	 */
	public static ArrayList<MoreDanBen> getmoredanben(String url) throws JSONException {
		JSONArray jsondanben = HttpComm.sendJSONArrayToServerWithCache(url, StorageUtils.DAY * 30);
		//		JSONArray jsondanben = HttpComm.sendJSONArrayToServer(url, 0);
		return JsonToBean.JsonToDanben(jsondanben);
	}
}
