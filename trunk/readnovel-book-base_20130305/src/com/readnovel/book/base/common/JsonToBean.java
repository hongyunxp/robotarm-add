package com.readnovel.book.base.common;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.readnovel.book.base.bean.MoreDanBen;
import com.readnovel.book.base.bean.MoreHeji;
import com.readnovel.book.base.bean.Parter;
import com.readnovel.book.base.entity.More;

public class JsonToBean {
	public static MoreHeji JsonToMoreBook(JSONObject json) {
		MoreHeji moreheji = new MoreHeji();
		ArrayList<More> asmore = new ArrayList<More>();
		try {
			if (!json.isNull("totalpage")) {
				moreheji.setTotalpage(json.getInt("totalpage"));
			}
			if (!json.isNull("total_heji_num")) {
				moreheji.setTotal_heji_num(json.getInt("total_heji_num"));
			}
			if (!json.isNull("currentpage")) {
				moreheji.setCurrentpage(json.getInt("currentpage"));
			}
			if (!json.isNull("heji_info")) {
				if (!"".equals(json.get("heji_info").toString())) {
					JSONArray jsonArray = json.getJSONArray("heji_info");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jobj = (JSONObject) jsonArray.opt(i);
						More more = new More();
						if (!jobj.isNull("id")) {
							more.setId(jobj.getString("id"));
						}
						if (!jobj.isNull("hejiid")) {
							more.setHejiid(jobj.getString("hejiid"));
						}
						if (!jobj.isNull("imagefname")) {
							more.setImagefname(jobj.getString("imagefname"));
						}
						if (!jobj.isNull("order")) {
							more.setOrder(jobj.getString("order"));
						}
						if (!jobj.isNull("title")) {
							more.setTitle(jobj.getString("title"));

						}
						if (!jobj.isNull("totaldownload")) {
							more.setTatoldownload(jobj.getString("totaldownload"));

						}
						if (!jobj.isNull("description")) {
							more.setDescription(jobj.getString("description"));
						}
						if (!jobj.isNull("down_url")) {
							more.setDownurl(jobj.getString("down_url"));
						}
						asmore.add(more);
					}
				}
			}
			moreheji.setMorehejihash(asmore);
		} catch (Exception e) {
		}
		return moreheji;
	}

	public static ArrayList<MoreDanBen> JsonToDanben(JSONArray json) throws JSONException {

		if (json == null)
			return null;

		ArrayList<MoreDanBen> danbens = new ArrayList<MoreDanBen>();
		for (int i = 0; i < json.length(); i++) {
			MoreDanBen mdb = new MoreDanBen();
			JSONObject jobj = (JSONObject) json.opt(i);
			if (!jobj.isNull("title")) {
				mdb.setBookTitle(jobj.getString("title"));
				Log.i("msgdanben", jobj.getString("title"));
			}
			if (!jobj.isNull("author")) {
				mdb.setAuthor(jobj.getString("author"));
				Log.i("msgdanben", jobj.getString("author"));
			}
			if (!jobj.isNull("sortname")) {
				mdb.setType(jobj.getString("sortname"));
				Log.i("msgdanben", jobj.getString("sortname"));
			}
			if (!jobj.isNull("book_logo")) {
				mdb.setBookLogo(jobj.getString("book_logo"));
				Log.i("msgdanben", jobj.getString("book_logo"));
			}
			if (!jobj.isNull("appurl")) {
				mdb.setAppurl(jobj.getString("appurl"));
				Log.i("msgdanben", jobj.getString("appurl"));
			}
			if (!jobj.isNull("totalviews")) {
				mdb.setDownLoadNum(jobj.getString("totalviews"));
				Log.i("msgdanben", jobj.getString("totalviews"));
			}
			danbens.add(mdb);
		}
		return danbens;

	}
	
	/*
	 * 动态获取服务端的商家账号，支付宝的公钥和私钥
	 * 
	 */
	public static Parter JsonToParter(JSONObject json) {
		Parter parter = new Parter();
		try {
			if (!json.isNull("PARTNER")) {
				parter.setPARTNER(json.getString("PARTNER"));
				Log.i("msg", json.getString("PARTNER"));
			}
			if (!json.isNull("SELLER")) {
				parter.setSELLER(json.getString("SELLER"));
				Log.i("msg", json.getString("SELLER"));
			}
			if (!json.isNull("RSA_ALIPAY_PUBLIC")) {
				parter.setRSA_ALIPAY_PUBLIC(json.getString("RSA_ALIPAY_PUBLIC"));
				Log.i("msg", json.getString("RSA_ALIPAY_PUBLIC"));
			}
			if (!json.isNull("RSA_PRIVATE")) {
				parter.setRSA_PRIVATE(json.getString("RSA_PRIVATE"));
				Log.i("msg", json.getString("RSA_PRIVATE"));
			}

		} catch (Exception e) {

		}

		return parter;

	}
	
}
