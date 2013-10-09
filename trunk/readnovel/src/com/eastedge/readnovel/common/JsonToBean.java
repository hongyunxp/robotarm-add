package com.eastedge.readnovel.common;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.beans.Banbenxinxi;
import com.eastedge.readnovel.beans.BookType;
import com.eastedge.readnovel.beans.CallQQ;
import com.eastedge.readnovel.beans.Chapterinfo;
import com.eastedge.readnovel.beans.FenleiList;
import com.eastedge.readnovel.beans.Image;
import com.eastedge.readnovel.beans.Month;
import com.eastedge.readnovel.beans.NewBook;
import com.eastedge.readnovel.beans.NewBookList;
import com.eastedge.readnovel.beans.OrderAllMsg;
import com.eastedge.readnovel.beans.OrderMsg;
import com.eastedge.readnovel.beans.Paihang;
import com.eastedge.readnovel.beans.PaihangMain;
import com.eastedge.readnovel.beans.RDBook;
import com.eastedge.readnovel.beans.SearchResult;
import com.eastedge.readnovel.beans.Shubenmulu;
import com.eastedge.readnovel.beans.Shubenxinxiye;
import com.eastedge.readnovel.beans.Shuping_huifu;
import com.eastedge.readnovel.beans.Shuping_huifuinfo;
import com.eastedge.readnovel.beans.Shuping_maininfo;
import com.eastedge.readnovel.beans.Shupinginfo;
import com.eastedge.readnovel.beans.Subed_chapters_info;
import com.eastedge.readnovel.beans.Theme;
import com.eastedge.readnovel.beans.Tuijian;
import com.eastedge.readnovel.beans.TuijianMain;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.beans.Version;
import com.eastedge.readnovel.beans.ZiFenleiList;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.StringUtils;

public class JsonToBean {
	// 折扣区的代码
	public static Theme JsonToDiscountTheme(JSONObject json) {
		// {"total_info_number":"408",
		// "current_page":"2",
		// "max_page":"21",
		// "alubm_info":
		// {"discount":"5",
		// "totalviews":,
		// "articleid":,
		// "title":,
		// "imagefname":,
		// "finishflag":"1",
		// "sortname":
		// },
		if (json == null)
			return null;
		// 这一块要进行修改，使用折扣区的代码 ,添加折扣的标志
		Theme theme = new Theme();
		// 折扣区的标志
		theme.setIsDiscount(true);
		ArrayList<NewBook> list = new ArrayList<NewBook>();
		try {
			if (!json.isNull("total_info_number")) {
				theme.setNub(json.getInt("total_info_number"));
			}
			if (!json.isNull("alubm_title")) {
				theme.setAlubm_title(json.getString("alubm_title"));
			}
			if (!json.isNull("alubm_info")) {
				JSONArray jsonArray = json.getJSONArray("alubm_info");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					NewBook book = new NewBook();
					if (!jobj.isNull("totalviews")) {
						book.setTotalviews(jobj.getString("totalviews"));
					}
					if (!jobj.isNull("discount")) {
						book.setDiscount(jobj.getString("discount"));
					}
					if (!jobj.isNull("articleid")) {
						book.setArticleid(jobj.getString("articleid"));
					}
					if (!jobj.isNull("title")) {
						book.setTitle(jobj.getString("title"));
					}
					if (!jobj.isNull("author")) {
						book.setAuthor(jobj.getString("author"));
					}
					if (!jobj.isNull("finishflag")) {
						book.setFinishflag(jobj.getString("finishflag"));
					}
					if (!jobj.isNull("sortname")) {
						book.setSortname(jobj.getString("sortname"));
					}
					if (!jobj.isNull("imagefname")) {
						book.setImgURL(jobj.getString("imagefname"));
					}
					list.add(book);
				}
			}
			theme.setBookList(list);
			return theme;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	public static Theme JsonToTheme(JSONObject json) {

		if (json == null)
			return null;
		Theme theme = new Theme();
		ArrayList<NewBook> list = new ArrayList<NewBook>();
		try {
			if (!json.isNull("total_info_number")) {
				theme.setNub(json.getInt("total_info_number"));
			}
			if (!json.isNull("alubm_title")) {
				theme.setAlubm_title(json.getString("alubm_title"));
			}
			if (!json.isNull("alubm_info")) {
				JSONArray jsonArray = json.getJSONArray("alubm_info");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					NewBook book = new NewBook();
					if (!jobj.isNull("totalviews")) {
						book.setTotalviews(jobj.getString("totalviews"));
					}
					if (!jobj.isNull("articleid")) {
						book.setArticleid(jobj.getString("articleid"));
					}
					if (!jobj.isNull("title")) {
						book.setTitle(jobj.getString("title"));
					}
					if (!jobj.isNull("author")) {
						book.setAuthor(jobj.getString("author"));
					}
					if (!jobj.isNull("finishflag")) {
						book.setFinishflag(jobj.getString("finishflag"));
					}
					if (!jobj.isNull("sortname")) {
						book.setSortname(jobj.getString("sortname"));
					}
					if (!jobj.isNull("imagefname")) {
						book.setImgURL(jobj.getString("imagefname"));
					}
					list.add(book);
				}
			}
			theme.setBookList(list);
			return theme;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	public static User JsonToUser(JSONObject json) {
		if (json == null)
			return null;

		User user = new User();

		try {
			if (!json.isNull("code")) {
				user.setCode(json.getString("code"));
			}
			if (!json.isNull("uid")) {
				user.setUid(json.getString("uid"));
			}
			if (!json.isNull("username")) {
				user.setUsername(json.getString("username"));
			}
			if (!json.isNull("email")) {
				user.setEmail(json.getString("email"));
			}
			if (!json.isNull("logo")) {
				user.setLogo(json.getString("logo"));
			}
			if (!json.isNull("remain")) {
				user.setRemain(json.getString("remain"));
			}
			if (!json.isNull("tel")) {
				user.setTel(json.getString("tel"));
			}
			if (!json.isNull("vip_level")) {
				user.setVipLevel(Integer.parseInt(json.getString("vip_level")));
			}
			if (!json.isNull("is_baoyue")) {
				boolean isBaoYue = "0".equals(json.getString("is_baoyue")) ? false
						: true;
				user.setBaoYue(isBaoYue);
			}
			if (!json.isNull("token")) {
				user.setToken(json.getString("token"));
			}

			if (!json.isNull("mobile")) {
				user.setBtel(json.getString("mobile"));
			}

		} catch (JSONException e) {
			LogUtils.error(e.getMessage(), e);
		}
		return user;
	}

	public static Version JsonToVersion(JSONObject json) {
		if (json == null)
			return null;

		Version v = new Version();
		try {
			if (!json.isNull("version")) {
				v.setVersionCode(json.getString("version"));
			}
			if (!json.isNull("subver")) {
				v.setSubver(json.getString("subver"));
			}
			if (!json.isNull("appname")) {
				v.setAppname(json.getString("appname"));
			}
			if (!json.isNull("appurl")) {
				v.setAppurl(json.getString("appurl"));
			}

		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return v;
	}

	public static ArrayList<BookType> JsonToFenleiList(JSONObject json) {
		if (json == null)
			return null;

		ArrayList<BookType> list = new ArrayList<BookType>();
		try {
			if (!json.isNull("root")) {
				JSONArray jsonArray = json.getJSONArray("root");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					BookType btype = new BookType();
					if (!jobj.isNull("sort_id")) {
						btype.setSortId(jobj.getString("sort_id"));
					}
					if (!jobj.isNull("title")) {
						btype.setTitle(jobj.getString("title"));
					}
					list.add(btype);
				}

				return list;
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return null;
	}

	public static PaihangMain JsonToPaihangMainList(JSONObject json) {
		if (json == null)
			return null;

		PaihangMain paihangmain = new PaihangMain();
		ArrayList<Image> imgList = new ArrayList<Image>();
		ArrayList<Paihang> paihangList = new ArrayList<Paihang>();
		try {
			if (!json.isNull("top_four_top_list")) {
				JSONArray jsonArray = json.getJSONArray("top_four_top_list");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					Image img = new Image();
					if (!jobj.isNull("img")) {
						img.setImageURL(Constants.imgURL
								+ jobj.getString("img"));
					}
					if (!jobj.isNull("type")) {
						img.setType(jobj.getString("type"));
					}
					if (!jobj.isNull("title")) {
						img.setTitle(jobj.getString("title"));
					}
					imgList.add(img);
				}
			}
			if (!json.isNull("top_list_info")) {
				JSONArray jsonArray = json.getJSONArray("top_list_info");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					Paihang paihang = new Paihang();
					if (!jobj.isNull("title")) {
						paihang.setTitle(jobj.getString("title"));
					}
					if (!jobj.isNull("sort_id")) {
						paihang.setSortId(jobj.getString("sort_id"));
					}
					if (!jobj.isNull("logo")) {
						paihang.setLogoUrl(Constants.imgURL
								+ jobj.getString("logo"));
					}
					paihangList.add(paihang);
				}
			}
			paihangmain.setImgList(imgList);
			paihangmain.setPaihangList(paihangList);
			return paihangmain;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return null;
	}

	public static ArrayList<NewBook> JsonToNewBookList(JSONObject json) {
		if (json == null)
			return null;

		ArrayList<NewBook> list = new ArrayList<NewBook>();
		try {
			if (!json.isNull("root")) {
				JSONArray jsonArray = json.getJSONArray("root");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					NewBook book = new NewBook();
					if (!jobj.isNull("totalviews")) {
						book.setTotalviews(jobj.getString("totalviews"));
					}
					if (!jobj.isNull("articleid")) {
						book.setArticleid(jobj.getString("articleid"));
					}
					if (!jobj.isNull("title")) {
						book.setTitle(jobj.getString("title"));
					}
					if (!jobj.isNull("author")) {
						book.setAuthor(jobj.getString("author"));
					}
					if (!jobj.isNull("finishflag")) {
						book.setFinishflag(jobj.getString("finishflag"));
					}
					if (!jobj.isNull("sortname")) {
						book.setSortname(jobj.getString("sortname"));
					}
					if (!jobj.isNull("imagefname")) {
						book.setImgURL(jobj.getString("imagefname"));
					}
					list.add(book);
				}
				return list;
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	public static FenleiList JsonToFlList(JSONObject json) {
		if (json == null)
			return null;

		FenleiList fllist = new FenleiList();
		ArrayList<NewBook> list = new ArrayList<NewBook>();
		ArrayList<BookType> btlist = new ArrayList<BookType>();
		try {
			if (!json.isNull("this_sort_info")) {
				JSONArray jsonArray = json.getJSONArray("this_sort_info");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					NewBook book = new NewBook();
					if (!jobj.isNull("totalviews")) {
						book.setTotalviews(jobj.getString("totalviews"));
					}
					if (!jobj.isNull("articleid")) {
						book.setArticleid(jobj.getString("articleid"));
					}
					if (!jobj.isNull("title")) {
						book.setTitle(jobj.getString("title"));
					}
					if (!jobj.isNull("author")) {
						book.setAuthor(jobj.getString("author"));
					}
					if (!jobj.isNull("finishflag")) {
						book.setFinishflag(jobj.getString("finishflag"));
					}
					if (!jobj.isNull("sortname")) {
						book.setSortname(jobj.getString("sortname"));
					}
					if (!jobj.isNull("imagefname")) {
						book.setImgURL(jobj.getString("imagefname"));
					}
					list.add(book);
				}
			}
			if (!json.isNull("child_sort_info")) {
				JSONArray jsonArray = json.getJSONArray("child_sort_info");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					BookType btype = new BookType();
					if (!jobj.isNull("sortid")) {
						btype.setSortId(jobj.getString("sortid"));
					}
					if (!jobj.isNull("title")) {
						btype.setTitle(jobj.getString("title"));
					}
					btlist.add(btype);
				}
			}
			if (!json.isNull("total_number")) {
				fllist.setBkcount(json.getInt("total_number"));
			}
			fllist.setChildFl(btlist);
			fllist.setFllist(list);
			return fllist;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	public static NewBookList JsonToBKList(JSONObject json) {
		if (json == null)
			return null;

		NewBookList bk = new NewBookList();
		ArrayList<NewBook> list = new ArrayList<NewBook>();
		try {
			if (!json.isNull("info")) {
				JSONArray jsonArray = json.getJSONArray("info");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					NewBook book = new NewBook();
					if (!jobj.isNull("totalviews")) {
						book.setTotalviews(jobj.getString("totalviews"));
					}
					if (!jobj.isNull("articleid")) {
						book.setArticleid(jobj.getString("articleid"));
					}
					if (!jobj.isNull("title")) {
						book.setTitle(jobj.getString("title"));
					}
					if (!jobj.isNull("author")) {
						book.setAuthor(jobj.getString("author"));
					}
					if (!jobj.isNull("finishflag")) {
						book.setFinishflag(jobj.getString("finishflag"));
					}
					if (!jobj.isNull("sortname")) {
						book.setSortname(jobj.getString("sortname"));
					}
					if (!jobj.isNull("imagefname")) {
						book.setImgURL(jobj.getString("imagefname"));
					}
					list.add(book);
				}
			}
			bk.setBklist(list);
			if (!json.isNull("total_number")) {
				bk.setBkcount(json.getInt("total_number"));
			}
			if (!json.isNull("cur_page_num")) {
				bk.setCurpage(json.getInt("cur_page_num"));
			}
			return bk;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	public static ArrayList<Image> JsonToImageList(JSONObject json) {
		if (json == null)
			return null;

		ArrayList<Image> imgList = new ArrayList<Image>();

		try {
			if (!json.isNull("img")) {
				JSONArray jsonArray = json.getJSONArray("img");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					Image img = new Image();
					if (!jobj.isNull("img")) {
						img.setImageURL(jobj.getString("img"));
					}
					if (!jobj.isNull("order")) {
						img.setOrder(jobj.getInt("order"));
					}
					imgList.add(img);
				}
			}
			return imgList;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return null;
	}

	public static TuijianMain JsonToTuijianMainList(JSONObject json) {
		if (json == null)
			return null;

		TuijianMain tuijianMain = new TuijianMain();
		ArrayList<Image> imgList = new ArrayList<Image>();
		ArrayList<Tuijian> tuijianList = new ArrayList<Tuijian>();
		try {
			if (!json.isNull("sort")) {
				JSONArray jsonArray = json.getJSONArray("sort");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					Tuijian tuijian = new Tuijian();
					Image img = new Image();
					if (!jobj.isNull("book_id")) {
						tuijian.setId(jobj.getString("book_id"));
						img.setImageURL(jobj.getString("thumb_url"));
						img.setAid(jobj.getString("book_id"));
					}
					if (!jobj.isNull("book_name")) {
						tuijian.setTitle(jobj.getString("book_name"));
					}
					if (!jobj.isNull("author")) {
						tuijian.setAuther(jobj.getString("author"));
					}
					if (!jobj.isNull("thumb_url")) {
						tuijian.setImageURL(jobj.getString("thumb_url"));
					}
					imgList.add(img);
					tuijianList.add(tuijian);
				}
			}

			tuijianMain.setImgList(imgList);
			tuijianMain.setTuijianList(tuijianList);
			Log.i("smqhe", imgList.size() + ":" + tuijianList.size());

			return tuijianMain;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return null;
	}

	public static ArrayList<String> JsonToTagList(JSONObject json) {
		if (json == null)
			return null;

		ArrayList<String> list = new ArrayList<String>();
		try {
			if (!json.isNull("root")) {
				JSONArray jsonArray = json.getJSONArray("root");
				for (int i = 0; i < jsonArray.length(); i++) {
					Object jobj = jsonArray.opt(i);
					list.add(jobj.toString());
				}
				return list;
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	public static OrderAllMsg JsonToOrderAll(JSONObject json) {
		if (json == null)
			return null;

		OrderAllMsg all = new OrderAllMsg();
		try {
			if (!json.isNull("remain")) {
				all.setRemain(json.getString("remain"));
			}
			if (!json.isNull("price")) {
				all.setPrice(json.getString("price"));
			}
			if (!json.isNull("chapter_info")) {
				JSONObject j = json.getJSONObject("chapter_info");

				if (!j.isNull("total_price")) {
					all.setTotal_price(j.getString("total_price"));
				}
				if (!j.isNull("total_vip_word")) {
					all.setTotal_vip_word(j.getString("total_vip_word"));
				}
				if (!j.isNull("total_chapter")) {
					all.setTotal_chapter(j.getString("total_chapter"));
				}
				if (!j.isNull("need_sub_chapter_id_list")) {
					JSONArray arr = j.getJSONArray("need_sub_chapter_id_list");
					ArrayList<String> al = new ArrayList<String>();
					for (int i = 0; i < arr.length(); i++) {
						al.add(arr.optString(i));
					}
					all.setChapterList(al);
				}

			}
			if (!json.isNull("time")) {// 服务器返回的当前时间
				all.setCurtime(json.getLong("time"));
			}
			if (!json.isNull("expireTime")) {// 服务器返回的过期
				all.setOvertime(json.getLong("expireTime"));
			}

			return all;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	public static OrderAllMsg JsonToOrderDiscountAll(JSONObject json) {
		if (json == null)
			return null;

		OrderAllMsg all = new OrderAllMsg();
		try {
			if (!json.isNull("remain")) {
				all.setRemain(json.getString("remain"));
			}
			if (!json.isNull("totalprice")) {
				all.setPrice(json.getString("totalprice"));
			}
			if (!json.isNull("discount")) {
				all.setDiscountCount((float) json.getInt("discount"));
			}
			if (!json.isNull("expireTime")) {// 服务器返回的过期
				all.setOvertime(json.getLong("expireTime"));
			}
			if (!json.isNull("expireTime")) {// 服务器返回的过期
				all.setOvertime(json.getLong("expireTime"));
			}
			if (!json.isNull("chapter_info")) {
				JSONObject j = json.getJSONObject("chapter_info");

				if (!j.isNull("total_price")) {
					all.setTotal_price(j.getString("total_price"));
				}
				if (!j.isNull("total_vip_word")) {
					all.setTotal_vip_word(j.getString("total_vip_word"));
				}
				if (!j.isNull("total_chapter")) {
					all.setTotal_chapter(j.getString("total_chapter"));
				}
				if (!j.isNull("need_sub_chapter_id_list")) {
					JSONArray arr = j.getJSONArray("need_sub_chapter_id_list");
					ArrayList<String> al = new ArrayList<String>();
					for (int i = 0; i < arr.length(); i++) {
						al.add(arr.optString(i));
					}
					all.setChapterList(al);
				}

			}
			if (!json.isNull("time")) {// 服务器返回的当前时间
				all.setCurtime(json.getLong("time"));
			}
			if (!json.isNull("expireTime")) {// 服务器返回的过期
				all.setOvertime(json.getLong("expireTime"));
			}

			return all;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	public static SearchResult JsonToSearch(JSONObject json) {
		if (json == null)
			return null;

		SearchResult sr = new SearchResult();
		ArrayList<NewBook> list = new ArrayList<NewBook>();
		try {
			if (!json.isNull("rec_result") || !json.isNull("info")) {
				JSONArray jsonArray = null;
				if (!json.isNull("rec_result")) {
					jsonArray = json.getJSONArray("rec_result");
				} else {
					jsonArray = json.getJSONArray("info");
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					NewBook book = new NewBook();
					if (!jobj.isNull("totalviews")) {
						book.setTotalviews(jobj.getString("totalviews"));
					}
					if (!jobj.isNull("articleid")) {
						book.setArticleid(jobj.getString("articleid"));
					}
					if (!jobj.isNull("title")) {
						book.setTitle(jobj.getString("title"));
					}
					if (!jobj.isNull("author")) {
						book.setAuthor(jobj.getString("author"));
					}
					if (!jobj.isNull("finishflag")) {
						book.setFinishflag(jobj.getString("finishflag"));
					}
					if (!jobj.isNull("sortname")) {
						book.setSortname(jobj.getString("sortname"));
					}
					if (!jobj.isNull("imagefname")) {
						book.setImgURL(jobj.getString("imagefname"));
					}
					list.add(book);
				}
				Collections.sort(list, new Comparator<NewBook>() {

					public int compare(NewBook object1, NewBook object2) {
						try {

							long v1 = Long.parseLong(object1.getTotalviews());
							long v2 = Long.parseLong(object2.getTotalviews());

							if (v2 > v1)
								return 1;

						} catch (Exception e) {
						}

						return -1;
					}
				});
				sr.setBookList(list);
			}
			if (!json.isNull("no_result")) {
				sr.setNoResult(true);
			}
			if (!json.isNull("total_number")) {
				sr.setBkcount(json.getInt("total_number"));
			}
			if (!json.isNull("cur_page_num")) {
				sr.setCurpage(json.getInt("cur_page_num"));
			}
			return sr;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	// 分类 子分类
	public static ZiFenleiList JsonToZiFenlei(JSONObject json) {
		if (json == null)
			return null;

		ZiFenleiList bk = new ZiFenleiList();
		ArrayList<NewBook> list = new ArrayList<NewBook>();
		try {
			if (!json.isNull("this_sort_info")) {
				JSONArray jsonArray = json.getJSONArray("this_sort_info");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					NewBook book = new NewBook();
					if (!jobj.isNull("totalviews")) {
						book.setTotalviews(jobj.getString("totalviews"));
					}
					if (!jobj.isNull("articleid")) {
						book.setArticleid(jobj.getString("articleid"));
					}
					if (!jobj.isNull("title")) {
						book.setTitle(jobj.getString("title"));
					}
					if (!jobj.isNull("author")) {
						book.setAuthor(jobj.getString("author"));
					}
					if (!jobj.isNull("finishflag")) {
						book.setFinishflag(jobj.getString("finishflag"));
					}
					if (!jobj.isNull("sortname")) {
						book.setSortname(jobj.getString("sortname"));
					}
					if (!jobj.isNull("imagefname")) {
						book.setImgURL(jobj.getString("imagefname"));
					}
					list.add(book);
				}
			}
			bk.setBklist(list);
			if (!json.isNull("total_number")) {
				bk.setBkcount(json.getInt("total_number"));
			}
			return bk;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return null;
	}

	public static CallQQ JsonToAboutme(JSONObject json) {
		if (json == null)
			return null;

		CallQQ cq = new CallQQ();
		try {
			if (!json.isNull("tel")) {
				JSONArray jsonArray = json.getJSONArray("tel");
				StringBuffer buffer = new StringBuffer();
				String d = "、";
				for (int i = 0; i < jsonArray.length(); i++) {
					if (i == jsonArray.length() - 1)
						d = " ";
					Object obj = jsonArray.get(i);
					buffer.append(obj.toString() + d);
				}
				cq.setCall(buffer.toString());
			}
			if (!json.isNull("qq")) {
				JSONArray jsonArray = json.getJSONArray("qq");
				StringBuffer buffer = new StringBuffer();
				String d = "、";
				for (int i = 0; i < jsonArray.length(); i++) {
					if (i == jsonArray.length() - 1)
						d = " ";
					Object obj = jsonArray.get(i);
					buffer.append(obj.toString() + d);
				}
				cq.setQq(buffer.toString());
			}

		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return cq;
	}

	public static RDBook JsonToRDBook(JSONObject json) {
		if (json == null)
			return null;

		RDBook book = new RDBook();
		try {
			if (!json.isNull("code")) {
				book.setCode(json.getString("code"));
			} else {
				OrderMsg order = new OrderMsg();
				if (!json.isNull("remain")) {
					order.setRemain(json.getString("remain"));
				}
				if (!json.isNull("cal_price")) {
					order.setCalPrice(json.getString("cal_price"));
				}
				if (!json.isNull("word_count")) {
					order.setWordCount(json.getString("word_count"));
				}
				if (!json.isNull("price")) {
					order.setPrice(json.getString("price"));
				}
				if (!json.isNull("vip_text_for_sub")) {
					order.setText(json.getString("vip_text_for_sub"));
				}
				if (!json.isNull("time")) {// 服务器返回的当前时间
					order.setCurtime(json.getLong("time"));
				}
				if (!json.isNull("expireTime")) {// 服务器返回的过期
					order.setOvertime(json.getLong("expireTime"));
				}
				if (!json.isNull("discount")) {
					order.setDiscount(json.getInt("discount"));
					book.setDiscount(json.getInt("discount"));
				}
				book.setOrderMsg(order);
			}
			// 添加折扣信息
			if (!json.isNull("is_discount_book")) {
				book.setIs_discount_book(json.getInt("is_discount_book"));
			}
			if (!json.isNull("totalprice")) {
				// String s = json.getString("totalprice");
				// double s1 = Double.parseDouble(s);
				book.setTotalprice(json.getDouble("totalprice"));
			}
			if (!json.isNull("is_vip")) {
				book.setIsVip(json.getInt("is_vip"));
			}
			if (!json.isNull("displayorder")) {
				book.setOrder(json.getInt("displayorder"));
			}
			if (!json.isNull("text_id")) {
				book.setTextId(json.getString("text_id"));
			}
			if (!json.isNull("text_title")) {
				book.setTextTitle(json.getString("text_title"));
			}
			if (!json.isNull("word_count")) {
				book.setWordCount(json.getInt("word_count"));
			}
			if (!json.isNull("article_id")) {
				book.setArticleId(json.getString("article_id"));
			}
			if (!json.isNull("finishflag")) {
				book.setFinishflag(json.getInt("finishflag"));
			}
			if (!json.isNull("text")) {
				book.setText(json.getString("text"));
			}
			if (!json.isNull("pre")) {
				Object obj = json.get("pre");
				if (obj != null && !"".equals(obj.toString())) {
					JSONObject j = json.getJSONObject("pre");
					if (!j.isNull("id")) {
						book.setPreId(j.getString("id"));
					}
					if (!j.isNull("is_vip")) {
						book.setPreVip(j.getInt("is_vip"));
					}
				}

			}
			if (!json.isNull("next")) {
				Object obj = json.get("next");
				if (obj != null && !"".equals(obj.toString())) {
					JSONObject j = json.getJSONObject("next");
					if (!j.isNull("id")) {
						book.setNextId(j.getString("id"));
					}
					if (!j.isNull("is_vip")) {
						book.setNextVip(j.getInt("is_vip"));
					}
				}
			}

		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return book;
	}

	public static Banbenxinxi JsonToBanbenxinxi(JSONObject json) {
		if (json == null)
			return null;

		Banbenxinxi bbxx = new Banbenxinxi();
		try {
			if (!json.isNull("version")) {
				bbxx.setVersion(json.getString("version"));
			}
			if (!json.isNull("versionCode")) {
				bbxx.setVersionCode(json.getInt("versionCode"));
			}
			if (!json.isNull("update_time")) {
				bbxx.setUpdatetime(json.getString("update_time"));
			}
			if (!json.isNull("appurl")) {
				bbxx.setAppurl(json.getString("appurl"));
			}
			if (!json.isNull("new_features")) {
				JSONArray jsonArray = json.getJSONArray("new_features");
				String[] a = new String[jsonArray.length()];
				for (int i = 0; i < jsonArray.length(); i++) {
					String obj = jsonArray.get(i).toString();
					a[i] = obj;
				}
				bbxx.setFeatures(a);
			}

			if (!json.isNull("isforce"))// 强制更新
				bbxx.setIsforce(json.getBoolean("isforce"));

			if (!json.isNull("wrongversion")) {// 错误版本

				JSONArray jsonArray = json.getJSONArray("wrongversion");

				int[] wrongarray = new int[jsonArray.length()];

				for (int i = 0; i < jsonArray.length(); i++) {
					int obj = jsonArray.getInt(i);
					wrongarray[i] = obj;
				}
				bbxx.setWrongversion(wrongarray);
			}

		} catch (JSONException e1) {

		}
		return bbxx;
	}

	public static Shubenxinxiye JsonToShu(JSONObject json) {
		if (json == null)
			return null;
		Shubenxinxiye sbxxy = new Shubenxinxiye();
		if (!json.isNull("detail")) {
			JSONArray jsonArray;
			try {
				jsonArray = json.getJSONArray("detail");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					if (!jobj.isNull("summary")) {
						sbxxy.setDescription(jobj.getString("summary"));
					}
					if (!jobj.isNull("book_id")) {
						sbxxy.setTextid(jobj.getString("book_id"));
					}
					if (!jobj.isNull("book_name")) {
						sbxxy.setTitle(jobj.getString("book_name"));
					}
					if (!jobj.isNull("author")) {
						sbxxy.setAuthor(jobj.getString("author"));
					}
					if (!jobj.isNull("type_one")) {
						sbxxy.setSortname(jobj.getString("type_one"));
					}
					if (!jobj.isNull("words")) {
						sbxxy.setWordtotal(Integer.parseInt(jobj
								.getString("words")));
					}
					if (!jobj.isNull("content_url")) {
						sbxxy.setContent_url(jobj.getString("content_url"));
					}
					if (!jobj.isNull("cover_url")) {
						sbxxy.setBook_logo(jobj.getString("cover_url"));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return sbxxy;
	}

	public static Shubenxinxiye JsonToShubenxinxiye(JSONObject json) {

		if (json == null)
			return null;
		Shubenxinxiye sbxxy = new Shubenxinxiye();
		try {
			if (!json.isNull("author")) {
				sbxxy.setAuthor(json.getString("author"));
			}
			if (!json.isNull("sortname")) {
				sbxxy.setSortname(json.getString("sortname"));
			}
			if (!json.isNull("wordtotal")) {
				sbxxy.setWordtotal(json.getInt("wordtotal"));
			}
			if (!json.isNull("totalviews")) {
				sbxxy.setTotalviews(json.getInt("totalviews"));
			}
			if (!json.isNull("finishflag")) {
				sbxxy.setFinishflag(json.getInt("finishflag"));
			}
			if (!json.isNull("book_logo")) {
				sbxxy.setBook_logo(json.getString("book_logo"));
			}

			if (!json.isNull("title")) {
				sbxxy.setTitle(json.getString("title"));
			}
			if (!json.isNull("voters")) {
				sbxxy.setVoters(json.getInt("voters"));
			}
			if (!json.isNull("description")) {
				sbxxy.setDescription(json.getString("description"));
			}
			if (!json.isNull("textcount")) {
				sbxxy.setTextcount(json.getInt("textcount"));
			}
			if (!json.isNull("free_textcount")) {
				sbxxy.setFree_textcount(json.getInt("free_textcount"));
			}
			if (!json.isNull("first_vip_chapter_displayorder")) {
				sbxxy.setFirst_vip_chapter_displayorder(json
						.getInt("first_vip_chapter_displayorder"));
			}
			if (!json.isNull("first_chapter_is_vip")) {
				sbxxy.setFirst_chapter_is_vip(json
						.getInt("first_chapter_is_vip"));
			}
			if (!json.isNull("first_chapter_id")) {
				sbxxy.setFirst_chapter_id(json.getString("first_chapter_id"));
			}
			if (!json.isNull("lastUpdateInfo")) {
				JSONObject jobj = new JSONObject(
						json.getString("lastUpdateInfo"));
				if (!jobj.isNull("textid")) {
					sbxxy.setTextid(jobj.getString("textid"));
				}
				if (!jobj.isNull("is_vip")) {
					sbxxy.setIs_vip(jobj.getInt("is_vip"));
				}
				if (!jobj.isNull("order")) {
					sbxxy.setOrder(jobj.getString("order"));
				}
				if (!jobj.isNull("chapter_title")) {
					sbxxy.setChapter_title(jobj.getString("chapter_title"));
				}
				if (!jobj.isNull("update_time")) {
					sbxxy.setUpdate_time(jobj.getLong("update_time") * 1000
							+ "");
				}
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return sbxxy;
	}

	public static Shubenmulu JsonToShubenmulu(JSONObject json, int page) {
		if (json == null)
			return null;

		Shubenmulu sbml = new Shubenmulu();
		ArrayList<Chapterinfo> list = new ArrayList<Chapterinfo>();
		// HashMap<Integer, ArrayList<Chapterinfo>> muMap = new HashMap<Integer,
		// ArrayList<Chapterinfo>>();
		try {
			if (!json.isNull("article_info")) {
				JSONObject jobj = new JSONObject(json.getString("article_info"));
				if (!jobj.isNull("title")) {
					sbml.setTitle(jobj.getString("title"));
				}
				if (!jobj.isNull("author")) {
					sbml.setAuthor(jobj.getString("author"));
				}
				if (!jobj.isNull("finishflag")) {
					sbml.setFinishflag(jobj.getInt("finishflag"));
				}
				if (!jobj.isNull("last_text_time")) {
					sbml.setLastuptime(jobj.getLong("last_text_time"));
				}
				if (!jobj.isNull("first_vip_chapter_displayorder")) {
					sbml.setFcvip(jobj.getInt("first_vip_chapter_displayorder"));
				}
			}
			if (!json.isNull("current_page_number")) {
				sbml.setCurrent_page_number(json.getInt("current_page_number"));
			}

			if (!json.isNull("total_page_number")) {
				sbml.setTotal_page_number(json.getInt("total_page_number"));
			}
			if (!json.isNull("chapter_info")) {
				if (!"".equals(json.get("chapter_info").toString())) {
					JSONArray jsonArray = json.getJSONArray("chapter_info");
					int p = 0;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jobj = (JSONObject) jsonArray.opt(i);
						Chapterinfo cti = new Chapterinfo();
						if (!jobj.isNull("id")) {
							cti.setId(jobj.getString("id"));
						}
						if (!jobj.isNull("subhead")) {
							cti.setSubhead(jobj.getString("subhead"));
						}
						if (!jobj.isNull("is_vip")) {
							cti.setIs_vip(jobj.getInt("is_vip"));
						}
						if (!jobj.isNull("word_count")) {
							cti.setWord_count(jobj.getInt("word_count"));
							cti.setPosi(p);
							p += jobj.getInt("word_count");
						}
						if (!jobj.isNull("create_time")) {
							cti.setCreate_time(jobj.getString("create_time"));
						}
						if (!jobj.isNull("update_time")) {
							cti.setUpdate_time(jobj.getString("update_time"));
						}
						if (!jobj.isNull("status")) {
							cti.setStatus(jobj.getInt("status"));
						}
						if (!jobj.isNull("displayorder")) {
							cti.setDisplayorder(jobj.getInt("displayorder"));
						}

						list.add(cti);
					}
				}
			}
			// if(page==-1){
			// sbml.setMulist(list);
			// }else{
			// muMap.put(page, list);
			// }
			//
			sbml.setMulist(list);
			return sbml;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

		return null;
	}

	public static Month JsonToMonth(JSONObject json) {
		if (json == null)
			return null;

		Month month;
		try {
			month = new Month();
			if (!json.isNull("code")) {
				month.setCode(json.getString("code"));
			}
			if (!json.isNull("month_status")) {
				month.setMonth_status(json.getString("month_status"));
			}
			if (!json.isNull("date")) {
				month.setDate(new Date((json.getLong("date") * 1000)));
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
			return null;
		}
		return month;
	}

	public static Shupinginfo JsonToShupinginfo(JSONObject json) {
		if (json == null)
			return null;

		Shupinginfo spif = new Shupinginfo();
		ArrayList<Shuping_maininfo> list = new ArrayList<Shuping_maininfo>();
		try {
			if (!json.isNull("meta_info")) {
				JSONObject jobj = new JSONObject(json.getString("meta_info"));
				if (!jobj.isNull("total_number")) {
					spif.setTotal_number(jobj.getInt("total_number"));
				}
				if (!jobj.isNull("total_page_number")) {
					spif.setTotal_page_number(jobj.getInt("total_page_number"));
				}
				if (!jobj.isNull("cur_page_number")) {
					spif.setCur_page_number(jobj.getInt("cur_page_number"));
				}
			}
			if (!json.isNull("main_info")) {
				JSONArray jsonArray = json.getJSONArray("main_info");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					Shuping_maininfo spmif = new Shuping_maininfo();
					if (!jobj.isNull("tid")) {
						spmif.setTid(jobj.getInt("tid"));
					}
					if (!jobj.isNull("fid")) {
						spmif.setFid(jobj.getInt("fid"));
					}
					if (!jobj.isNull("iconid")) {
						spmif.setIconid(jobj.getInt("iconid"));
					}
					if (!jobj.isNull("typeid")) {
						spmif.setTypeid(jobj.getInt("typeid"));
					}
					if (!jobj.isNull("readperm")) {
						spmif.setReadperm(jobj.getInt("readperm"));
					}
					if (!jobj.isNull("price")) {
						spmif.setPrice(jobj.getInt("price"));
					}
					if (!jobj.isNull("author")) {
						spmif.setAuthor(jobj.getString("author"));
					}
					if (!jobj.isNull("authorid")) {
						spmif.setAuthorid(jobj.getInt("authorid"));
					}
					if (!jobj.isNull("subject")) {
						spmif.setSubject(jobj.getString("subject"));
					}
					if (!jobj.isNull("dateline")) {
						spmif.setDateline(jobj.getLong("dateline") * 1000 + "");
					}
					if (!jobj.isNull("lastpost")) {
						spmif.setLastpost(jobj.getLong("lastpost") * 1000 + "");
					}
					if (!jobj.isNull("lastposter")) {
						spmif.setLastposter(jobj.getString("lastposter"));
					}
					if (!jobj.isNull("views")) {
						spmif.setViews(jobj.getInt("views"));
					}
					if (!jobj.isNull("replies")) {
						spmif.setReplies(jobj.getInt("replies"));
					}
					if (!jobj.isNull("displayorder")) {
						spmif.setDisplayorder(jobj.getInt("displayorder"));
					}
					if (!jobj.isNull("highlight")) {
						spmif.setHighlight(jobj.getInt("highlight"));
					}
					if (!jobj.isNull("digest")) {
						spmif.setDigest(jobj.getInt("digest"));
					}
					if (!jobj.isNull("rate")) {
						spmif.setRate(jobj.getInt("rate"));
					}
					if (!jobj.isNull("blog")) {
						spmif.setBlog(jobj.getInt("blog"));
					}
					if (!jobj.isNull("special")) {
						spmif.setSpecial(jobj.getInt("special"));
					}
					if (!jobj.isNull("attachment")) {
						spmif.setAttachment(jobj.getInt("attachment"));
					}
					if (!jobj.isNull("subscribed")) {
						spmif.setSubscribed(jobj.getInt("subscribed"));
					}
					if (!jobj.isNull("moderated")) {
						spmif.setModerated(jobj.getInt("moderated"));
					}
					if (!jobj.isNull("closed")) {
						spmif.setClosed(jobj.getInt("closed"));
					}
					if (!jobj.isNull("itemid")) {
						spmif.setItemid(jobj.getInt("itemid"));
					}
					if (!jobj.isNull("supe_pushstatus")) {
						spmif.setSupe_pushstatus(jobj.getInt("supe_pushstatus"));
					}
					if (!jobj.isNull("author_replied")) {
						spmif.setAuthor_replied(jobj.getInt("author_replied"));
					}
					if (!jobj.isNull("pid")) {
						spmif.setPid(jobj.getInt("pid"));
					}
					if (!jobj.isNull("message")) {
						spmif.setMessage(jobj.getString("message"));
					}
					if (!jobj.isNull("vip_level")) {
						spmif.setVip_level(jobj.getString("vip_level"));
					}
					if (!jobj.isNull("lastposterid")) {
						spmif.setLastposterid(jobj.getString("lastposterid"));
					}
					list.add(spmif);
				}
			}
			spif.setSPlist(list);
			return spif;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return null;
	}

	public static Shuping_huifu JsonToShupinghuifu(JSONObject json) {
		if (json == null)
			return null;

		Shuping_huifu sphf = new Shuping_huifu();
		ArrayList<Shuping_huifuinfo> list = new ArrayList<Shuping_huifuinfo>();
		try {
			if (!json.isNull("meta_info")) {
				JSONObject jobj = new JSONObject(json.getString("meta_info"));
				if (!jobj.isNull("total_number")) {
					sphf.setTotal_number(jobj.getInt("total_number"));
				}
				if (!jobj.isNull("total_page_number")) {
					sphf.setTotal_page_number(jobj.getInt("total_page_number"));
				}
				if (!jobj.isNull("cur_page_number")) {
					sphf.setCur_page_number(jobj.getInt("cur_page_number"));
				}
			}
			if (!json.isNull("main_info")) {
				JSONArray jsonArray = json.getJSONArray("main_info");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jobj = (JSONObject) jsonArray.opt(i);
					Shuping_huifuinfo sphfif = new Shuping_huifuinfo();
					if (!jobj.isNull("pid")) {
						sphfif.setPid(jobj.getInt("pid"));
					}
					if (!jobj.isNull("fid")) {
						sphfif.setFid(jobj.getInt("fid"));
					}
					if (!jobj.isNull("tid")) {
						sphfif.setTid(jobj.getInt("tid"));
					}
					if (!jobj.isNull("first")) {
						sphfif.setFirst(jobj.getInt("first"));
					}
					if (!jobj.isNull("author")) {
						sphfif.setAuthor(jobj.getString("author"));
					}
					if (!jobj.isNull("authorid")) {
						sphfif.setAuthorid(jobj.getInt("authorid"));
					}
					if (!jobj.isNull("subject")) {
						sphfif.setSubject(jobj.getString("subject"));
					}
					if (!jobj.isNull("dateline")) {
						sphfif.setDateline(jobj.getLong("dateline") * 1000 + "");
					}
					if (!jobj.isNull("message")) {
						sphfif.setMessage(jobj.getString("message"));
					}
					if (!jobj.isNull("useip")) {
						sphfif.setUseip(jobj.getString("useip"));
					}
					if (!jobj.isNull("invisible")) {
						sphfif.setInvisible(jobj.getInt("invisible"));
					}
					if (!jobj.isNull("anonymous")) {
						sphfif.setAnonymous(jobj.getInt("anonymous"));
					}
					if (!jobj.isNull("usesig")) {
						sphfif.setUsesig(jobj.getInt("usesig"));
					}
					if (!jobj.isNull("htmlon")) {
						sphfif.setHtmlon(jobj.getInt("htmlon"));
					}
					if (!jobj.isNull("bbcodeoff")) {
						sphfif.setBbcodeoff(jobj.getInt("bbcodeoff"));
					}
					if (!jobj.isNull("smileyoff")) {
						sphfif.setSmileyoff(jobj.getInt("smileyoff"));
					}
					if (!jobj.isNull("parseurloff")) {
						sphfif.setParseurloff(jobj.getInt("parseurloff"));
					}
					if (!jobj.isNull("attachment")) {
						sphfif.setAttachment(jobj.getInt("attachment"));
					}
					if (!jobj.isNull("rate")) {
						sphfif.setRate(jobj.getInt("rate"));
					}
					if (!jobj.isNull("ratetimes")) {
						sphfif.setRatetimes(jobj.getInt("ratetimes"));
					}
					if (!jobj.isNull("status")) {
						sphfif.setStatus(jobj.getInt("status"));
					}
					list.add(sphfif);
				}
			}
			sphf.setHFlist(list);
			return sphf;
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return null;
	}

	public static Subed_chapters_info JsonToSubedchaptersinfo(JSONObject json) {
		if (json == null)
			return null;

		Subed_chapters_info subed_chapters_info = new Subed_chapters_info();

		try {
			if (!json.isNull("info")) {
				subed_chapters_info.setInfo(json.getString("info"));
			}
			if (!json.isNull("subed_textid_list")) {
				JSONArray jsonArray = json.getJSONArray("subed_textid_list");
				HashSet<String> hashSet = new HashSet<String>();
				for (int i = 0; i < jsonArray.length(); i++) {
					String str = jsonArray.optString(i);
					hashSet.add(str);
				}
				subed_chapters_info.setSubed_textid_list(hashSet);
			}
		} catch (JSONException e) {
			LogUtils.error(e.getMessage(), e);
		}
		return subed_chapters_info;
	}

	public static Vector<BFBook> jsonToBFBook(JSONObject json) {
		if (json == null)
			return null;

		Vector<BFBook> list = new Vector<BFBook>();

		try {
			if (!json.isNull("root")) {

				JSONArray array = json.getJSONArray("root");

				for (int i = 0; i < array.length(); i++) {

					JSONObject obj = (JSONObject) array.opt(i);
					BFBook bfbook = new BFBook();

					if (!obj.isNull("articleid")) {
						bfbook.setArticleid(obj.getString("articleid"));
					}
					if (!obj.isNull("wordtotal")) {
						bfbook.setWordtotal(obj.getString("wordtotal"));
					}
					if (!obj.isNull("title")) {
						bfbook.setTitle(obj.getString("title"));
					}
					if (!obj.isNull("finishflag")) {
						bfbook.setFinishFlag(obj.getInt("finishflag"));
					}
					if (!obj.isNull("imagefname")) {
						bfbook.setImagefname(obj.getString("imagefname"));
					}
					list.add(bfbook);
				}
				return list;
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return null;
	}

	public static Vector<BFBook> jsonToVip(JSONObject json) {
		if (json == null)
			return null;

		Vector<BFBook> al = new Vector<BFBook>();
		try {
			if (!json.isNull("root")) {
				if (StringUtils.isBlank(json.getString("root")))
					return null;

				JSONArray array = json.getJSONArray("root");
				for (int i = 0; i < array.length(); i++) {
					BFBook book = new BFBook();
					JSONObject jobj = (JSONObject) array.opt(i);
					if (!jobj.isNull("wordtotal")) {
						book.setWordtotal(jobj.getString("wordtotal"));
					}
					if (!jobj.isNull("last_text_time")) {
						book.setLast_text_time(jobj.getString("last_text_time"));
					}
					if (!jobj.isNull("totalviews")) {
						book.setTotalviews(jobj.getString("totalviews"));
					}
					if (!jobj.isNull("articleid")) {
						book.setArticleid(jobj.getString("articleid"));
					}
					if (!jobj.isNull("title")) {
						book.setTitle(jobj.getString("title"));
					}
					if (!jobj.isNull("author")) {
						book.setAuthor(jobj.getString("author"));
					}
					if (!jobj.isNull("imagefname")) {
						book.setImagefname(jobj.getString("imagefname"));
					}
					if (!jobj.isNull("finishflag")) {
						book.setFinishFlag(jobj.getInt("finishflag"));
					}
					if (!jobj.isNull("sortname")) {
						book.setSortname(jobj.getString("sortname"));
					}
					al.add(book);
				}
				return al;
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return null;
	}
}
