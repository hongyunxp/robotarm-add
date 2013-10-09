package com.xs.cn.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import com.eastedge.readnovel.beans.AliPayBean;
import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.beans.Banbenxinxi;
import com.eastedge.readnovel.beans.BookType;
import com.eastedge.readnovel.beans.CallQQ;
import com.eastedge.readnovel.beans.Chapterinfo;
import com.eastedge.readnovel.beans.FenleiList;
import com.eastedge.readnovel.beans.Month;
import com.eastedge.readnovel.beans.NewBook;
import com.eastedge.readnovel.beans.NewBookList;
import com.eastedge.readnovel.beans.NoticeCheck;
import com.eastedge.readnovel.beans.OrderAllMsg;
import com.eastedge.readnovel.beans.PaihangMain;
import com.eastedge.readnovel.beans.RDBook;
import com.eastedge.readnovel.beans.SearchResult;
import com.eastedge.readnovel.beans.Shubenmulu;
import com.eastedge.readnovel.beans.Shubenxinxiye;
import com.eastedge.readnovel.beans.Shuping_huifu;
import com.eastedge.readnovel.beans.Shupinginfo;
import com.eastedge.readnovel.beans.Subed_chapters_info;
import com.eastedge.readnovel.beans.Theme;
import com.eastedge.readnovel.beans.TuijianMain;
import com.eastedge.readnovel.beans.Version;
import com.eastedge.readnovel.beans.ZiFenleiList;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.JsonToBean;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.db.DBAdapter;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.cache.Filter;
import com.readnovel.base.util.JsonUtils;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.PhoneUtils;
import com.readnovel.base.util.StorageUtils;
import com.readnovel.base.util.StringUtils;
import com.xs.cn.activitys.BookApp;

public class HttpImpl {

	/**
	 * *************************************************************************
	 * ************ 页面展示相关接口
	 * ****************************************************
	 * *********************************
	 */
	/**
	 * 特别推荐(推荐一级页面)
	 */
	public static TuijianMain tuijian() {

		// JSONObject json =
		// HttpComm.sendJSONToServerWithCache(Constants.TUIJIAN_URL);
		JSONObject json = HttpComm
				.sendJSONToServerWithCache(Constants.TUIJIAN_URL);
		if (json == null)
			return null;
		return JsonToBean.JsonToTuijianMainList(json);
	}

	// 需要传递过来类型进行处理数据
	public static TuijianMain tuijian(int sortid, int page) {
		String url = String
				.format("http://opendata.smqhe.com/client.php?sm=sort&sortid=%s&page=%s&data=json",
						new Object[] { sortid, page });

		JSONObject json = HttpComm.sendJSONToServerWithCache(url);
		if (json == null)
			return null;
		return JsonToBean.JsonToTuijianMainList(json);
	}

	/**
	 * 专题列表显示(推荐二级页面)
	 */
	public static Theme theme(String aid, int page, String sorted) {
		// 在这里需要开辟新的接口，原有接口和以前有冲突
		if ("99999".equals(aid)) {
			String url = String.format(Constants.DISCOUNT_URL, new Object[] {
					sorted, page });
			JSONObject json = HttpComm.sendJSONToServerWithCache(url);
			if (json == null)
				return null;
			return JsonToBean.JsonToDiscountTheme(json);
		} else {
			String url = String.format(Constants.THEME_URL, new Object[] { aid,
					page });
			JSONObject json = HttpComm.sendJSONToServerWithCache(url);
			if (json == null)
				return null;
			return JsonToBean.JsonToTheme(json);
		}
	}

	/**
	 * 排行榜(排行一级页面)
	 */
	public static PaihangMain paihang() {
		JSONObject json = HttpComm.sendJSONToServerWithCache(
				Constants.PAIHANG_URL, StorageUtils.DAY * 30);
		if (json == null)
			return null;
		return JsonToBean.JsonToPaihangMainList(json);
	}

	/**
	 * 各类排行榜接口(排行二级页面)
	 */
	public static ArrayList<NewBook> paihangItemList(String type) {
		String url = String.format(Constants.PAIHANG_ITEM_URL,
				new Object[] { type });

		JSONObject json = HttpComm.sendJSONToServerWithCache(url, 1);
		if (json == null)
			return null;
		return JsonToBean.JsonToNewBookList(json);
	}

	/**
	 * 新书列表
	 */
	public static NewBookList newbook(int page, int sortid) {
		String url = String.format(Constants.NEW_URL, new Object[] { sortid,
				page });
		JSONObject json = HttpComm.sendJSONToServerWithCache(url);
		if (json == null)
			return null;
		return JsonToBean.JsonToBKList(json);
	}

	/**
	 * 分类列表(分类一级页面)
	 */
	public static ArrayList<BookType> fenleiList() {
		JSONObject json = HttpComm.sendJSONToServerWithCache(
				Constants.SORT_LIST_URL, 1, StorageUtils.DAY * 30);
		if (json == null)
			return null;
		return JsonToBean.JsonToFenleiList(json);
	}

	/**
	 * 分类单项列表(分类二级页面)
	 */
	public static FenleiList fenleiItemList(String id, int page) {
		String url = String.format(Constants.SORT_URL,
				new Object[] { id, page });
		JSONObject json = HttpComm.sendJSONToServerWithCache(url, 1);
		if (json == null)
			return null;
		return JsonToBean.JsonToFlList(json);
	}

	/**
	 * 分类单项子分类列表(分类三级页面)
	 */
	public static ZiFenleiList ZifenleiItemList(String id, int page) {
		String url = String.format(Constants.SORT_URL,
				new Object[] { id, page });

		JSONObject json = HttpComm.sendJSONToServerWithCache(url, 1);
		if (json == null)
			return null;
		return JsonToBean.JsonToZiFenlei(json);
	}

	/**
	 * 搜索热词
	 */
	public static ArrayList<String> taglist() {
		JSONObject json = HttpComm.sendJSONToServerWithCache(
				Constants.TAG_WORD_URL, 1);
		if (json == null)
			return null;
		return JsonToBean.JsonToTagList(json);
	}

	/**
	 * 搜索关键字
	 */
	public static SearchResult seachList(String word, int page) {
		String url = String.format(Constants.SEARCH_URL, new Object[] { word,
				page });
		JSONObject json = HttpComm.sendJSONToServerWithCache(url);
		if (json == null)
			return null;
		return JsonToBean.JsonToSearch(json);
	}

	/**
	 * 书本信息页(书本明细)
	 */
	public static Shubenxinxiye Shubenxinxiye(String id) {
		String url = String
				.format("http://opendata.smqhe.com/client.php?sm=detail&book_id=%s&data=json",
						new Object[] { id });

		JSONObject json = HttpComm.sendJSONToServerWithCache(url);
		if (json == null)
			return null;
		return JsonToBean.JsonToShu(json);
	}

	/**
	 * *************************************************************************
	 * ************ 在线阅读接口
	 * ******************************************************
	 * *******************************
	 */

	/**
	 * 在线阅读书
	 */
	public static RDBook downText(String textid) {
		LogUtils.debug("执行普通预读|" + textid);
		String url = String.format(Constants.TEXT_URL, new Object[] { textid });

		JSONObject json = HttpComm.sendJSONToServerWithCache(url);
		if (json == null)
			return null;

		try {
			LogUtils.debug("执行普通预读结果|" + json.getString("code"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return JsonToBean.JsonToRDBook(json);
	}

	/**
	 * 在线阅读Vip书 NIMEIDE
	 */
	public static RDBook downVipText(final String textid) {
		LogUtils.debug("执行普通读章节|" + textid);
		if (BookApp.getUser() == null) {
			return null;
		}

		String uid = BookApp.getUser().getUid();
		String channel = CommonUtils.getCustomChannel(BookApp.getInstance());
		String model = CommonUtils.getChannel(BookApp.getInstance());
		String imei = PhoneUtils.getPhoneImei(BookApp.getInstance());

		String url = String.format(Constants.VIP_TEXT_URL,
				new Object[] { textid, uid, BookApp.getUser().getToken(),
						channel, model.toUpperCase(), imei });

		JSONObject json = HttpComm.sendJSONToServerWithCache(url,
				new Filter<JSONObject>() {// 缓存结果过滤器
					@Override
					public boolean accept(JSONObject json) {
						if (json != null) {
							RDBook book = JsonToBean.JsonToRDBook(json);
							if (book != null
									&& StringUtils.isNotBlank(book.getNextId()))
								return true;
						}
						return false;// 最后章节不从缓存中取数据
					}
				}, new Filter<JSONObject>() {// 缓存过滤器，只缓存成功取到VIP章节全部内容的数据（code为1时）
					@Override
					public boolean accept(JSONObject json) {
						if (json != null && !json.isNull("code")) {// 当code为1时，并且不是最后一章节时缓存
							try {
								String code = json.getString("code");
								if ("1".equals(code))
									return true;
							} catch (Throwable e) {
								LogUtils.error(e.getMessage(), e);
							}
						}
						return false;
					}
				});
		if (json == null)
			return null;
		LogUtils.debug("执行普通读章节结果|" + json);
		return JsonToBean.JsonToRDBook(json);
	}

	/**
	 * 强制预读VIP章节
	 */
	public static RDBook prepareVipText(final String textid) {
		if (BookApp.getUser() == null) {
			return null;
		}
		LogUtils.debug("执行强制读章节|" + textid);
		String token = Util.md5(textid + Constants.PRIVATE_KEY)
				.substring(0, 10);
		String url = String.format(Constants.PREPARE_VIP_TEXT, new Object[] {
				textid, token });

		JSONObject json = HttpComm.sendJSONToServerWithCache(url,
				new Filter<JSONObject>() {// 缓存过滤器，只缓存成功取到VIP章节全部内容的数据
					@Override
					public boolean accept(JSONObject json) {
						if (json != null && !json.isNull("code")) {
							try {
								String code = json.getString("code");
								if ("1".equals(code))
									return true;
							} catch (Throwable e) {
								LogUtils.error(e.getMessage(), e);
							}
						}
						return false;
					}
				});

		LogUtils.debug("执行强制读章节结果|" + json);
		return JsonToBean.JsonToRDBook(json);

	}

	/**
	 * *************************************************************************
	 * ************ 其它接口
	 * ********************************************************
	 * *****************************
	 */

	/**
	 * 软件更新提醒
	 */
	public static Version update() {
		JSONObject json = HttpComm.sendJSONToServer(Constants.UPDATE_INFO_URL);
		if (json == null)
			return null;
		return JsonToBean.JsonToVersion(json);
	}

	/**
	 * 注册
	 */
	public static JSONObject regist(String username, String passwdhash) {
		passwdhash = Util.md5(passwdhash);
		String md5 = Util.md5(username + Constants.PRIVATE_KEY);
		md5 = md5.substring(0, 10);
		String url = String.format(Constants.REG_URL, new Object[] { username,
				passwdhash, md5 });

		JSONObject json = HttpComm.sendJSONToServer(url);
		return json;
	}

	/**
	 * 登陆
	 */
	public static JSONObject login(String username, String passwdhash,
			boolean isHash) {
		try {
			username = URLEncoder.encode(username, "gb2312");
		} catch (UnsupportedEncodingException e) {
			LogUtils.error(e.getMessage(), e);
			return null;
		}

		if (!isHash)// 如果没有md5需hash后使用
			passwdhash = Util.md5(passwdhash);

		String url = String.format(Constants.LOGIN_URL, new Object[] {
				username, passwdhash });

		JSONObject json = HttpComm.sendJSONToServer(url);
		LogUtils.info("登陆信息|" + json);

		return json;
	}

	/**
	 * 修改密码
	 */

	public static JSONObject updatePass(String uid, String oldPass,
			String newPass, String token) {
		oldPass = Util.md5(oldPass);
		newPass = Util.md5(newPass);
		long t = System.currentTimeMillis();
		String auth = Util.md5(
				Util.md5(uid + t + Constants.PRIVATE_KEY)
						+ Constants.PRIVATE_KEY).substring(0, 10);

		String url = String.format(Constants.CH_PASSWD_URL, new Object[] { uid,
				oldPass, newPass, auth, t, token });

		JSONObject json = HttpComm.sendJSONToServer(url);

		return json;
	}

	/**
	 * 更新提醒
	 */
	public static JSONObject updateInfo(String bookList) {
		String url = String.format(Constants.BOOK_UP_INFO_URL,
				new Object[] { bookList });

		JSONObject json = HttpComm.sendJSONToServer(url, 1);

		return json;
	}

	/**
	 * 得到手机绑定号码
	 */
	public static JSONObject getBindMobile(String uid, String token) {
		String url = String.format(Constants.GET_BIND_MOBILE_URL2,
				new Object[] { uid, token });

		JSONObject jobj = HttpComm.sendJSONToServer(url);

		return jobj;
	}

	/**
	 * 与主站收藏同步
	 */
	public static JSONObject synMyfavor(String uid, String token, String add,
			String del) {
		String url = String.format(Constants.SYNCHRO_MY_FAVOR_URL,
				new Object[] { uid, token, add, del });

		JSONObject json = HttpComm.sendJSONToServer(url);

		return json;
	}

	/**
	 * 关于我们QQ电话接口
	 */
	public static CallQQ aboutMe() {
		JSONObject json = HttpComm
				.sendJSONToServerWithCache(Constants.HELP_INFO_URL);

		if (json == null)
			return null;

		return JsonToBean.JsonToAboutme(json);
	}

	/**
	 * 设置版本更新
	 */
	public static Banbenxinxi banbenxinxi() {
		JSONObject json = HttpComm
				.sendJSONToServer(Constants.GET_NEWEST_VERSION);
		if (json == null)
			return null;
		LogUtils.info("执行版本更新|" + json);
		return JsonToBean.JsonToBanbenxinxi(json);
	}

	// /**
	// * 目录
	// */
	// public static Shubenmulu Shubenmulu(String id, int page) {
	// String url = String.format(Constants.ARTICLE_ID_URL, new Object[] { id,
	// page });
	//
	// JSONObject json = HttpComm.sendJSONToServer(url);
	// if (json == null)
	// return null;
	// return JsonToBean.JsonToShubenmulu(json, page);
	// }

	/**
	 * 所有目录
	 */
	public static Shubenmulu ShubenmuluAll(String id) {
		String url = String.format(Constants.INDEX_INFO_ALL_URL,
				new Object[] { id });

		JSONObject json = HttpComm.sendJSONToServer(url);
		if (json == null)
			return null;
		return JsonToBean.JsonToShubenmulu(json, -1);
	}

	// /**
	// * textid后面的目录
	// */
	// public static Shubenmulu ShubenmuluText(String aid, String textid) {
	// String url = String.format(Constants.UP_INDEX_INFO_ALL, new Object[] {
	// aid, textid });
	//
	// JSONObject json = HttpComm.sendJSONToServer(url);
	// if (json == null)
	// return null;
	// return JsonToBean.JsonToShubenmulu(json, -1);
	// }

	/**
	 * 文件下载
	 * 
	 * @param <HttpServletResponse>
	 * @param <HttpServletResponse>
	 * 
	 * @功能信息 :
	 * @参数信息 :
	 * @返回值信息 :
	 * @异常信息 :
	 */

	/*
	 * @param downFile
	 * 
	 * @param mul
	 * 
	 * @throws Throwable
	 */
	public static void downBook(DownFile downFile, Shubenmulu mul)
			throws Throwable {
		String u = String.format(Constants.BOOK_DOWNLOAD,
				new Object[] { downFile.aid });
		String filename = "book_text_" + downFile.aid + ".txt";

		URL url = new URL(u);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setRequestProperty("Content-Type", "text/plain");
		httpCon.setRequestProperty("charset", "utf-8");
		downFile.fileLen = httpCon.getContentLength();
		File f = new File(Constants.READNOVEL_BOOK, "temp" + filename);
		f.getParentFile().mkdirs();
		if (f.exists()) {
			f.delete();
		}
		f.createNewFile();
		InputStream ins = httpCon.getInputStream();
		if (ins == null) {
			LogUtils.info("获取不到服务器信息  ----------");
			downFile.isOK = -1;
			return;
		}
		FileOutputStream fout = new FileOutputStream(f);
		byte[] buf = new byte[1024];
		int nnumber = 0;
		nnumber = ins.read(buf, 0, buf.length);
		while (nnumber != -1) {
			downFile.downLen += nnumber;
			fout.write(buf, 0, nnumber);
			nnumber = ins.read(buf, 0, buf.length);
		}
		ins.close();
		fout.close();

		if (mul != null) {
			ArrayList<Integer> plist = new ArrayList<Integer>();
			ArrayList<String> jjlist = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(f));
			File finish = new File(Constants.READNOVEL_BOOK, filename);
			downFile.filelocation = finish.getPath();
			PrintWriter out = new PrintWriter(finish);
			String s = br.readLine();
			int p = 0;
			StringBuffer buff = null;
			while (s != null) {
				int w = s.indexOf("#nan#wu#ou#ni#tou#fo###");
				if (w != -1) {
					s = s.replace("#nan#wu#ou#ni#tou#fo###", "");
					plist.add(p + w);
					if (buff == null) {
						buff = new StringBuffer();
					} else {
						int len = 30;
						if (buff.length() < 30)
							len = buff.length();
						jjlist.add(buff.toString().substring(0, len) + "...");
						buff = new StringBuffer();
					}
				} else {
					if (buff.length() < 30)
						buff.append(s);
				}
				s = s + "\n";
				out.print(s);
				p += s.getBytes("utf-8").length;
				s = br.readLine();
			}
			out.close();
			br.close();
			if (buff != null) {
				int len = 30;
				if (buff.length() < 30)
					len = buff.length();
				jjlist.add(buff.toString().substring(0, len) + "...");
				jjlist.add(buff.toString());
			}
			ArrayList<Chapterinfo> clist = mul.getMulist();
			for (int i = 0; i < clist.size() && i < plist.size(); i++) {
				Chapterinfo cinf = clist.get(i);
				int x = plist.get(i);
				int y = p;
				if (i < clist.size() - 1 && i < plist.size() - 1) {
					y = plist.get(i + 1);
					Chapterinfo cinfNext = clist.get(i + 1);
					cinf.setNextid(cinfNext.getId());
					cinfNext.setPreid(cinf.getId());
				}
				cinf.setPosi(x);
				cinf.setLen(y - x);
				cinf.setTextjj(jjlist.get(i));
			}
			if (clist.size() > plist.size() && plist.size() > 0) {
				Chapterinfo cinfNext = clist.get(plist.size());
				Chapterinfo cinf = clist.get(plist.size() - 1);
				cinf.setNextvip(cinfNext.getIs_vip());
				cinf.setNextid(cinfNext.getId());
			}
			// 存目录目录
			Util.write(downFile.aid, mul);

			// 更新数据库(存书本内容文件到数据库)
			if (CloseActivity.curActivity != null) {
				LogUtils.info("下载信息 " + "aid：" + downFile.aid + "   时间     "
						+ mul.getLastuptime());
				DBAdapter dbAdapter = new DBAdapter(CloseActivity.curActivity);
				dbAdapter.open();
				dbAdapter.updateSetBookFile(downFile.filelocation,
						downFile.aid, mul.getLastuptime());
				dbAdapter.close();
			}
		}

	}

	/**
	 * 同步资料
	 */
	public static JSONObject syncUserInfo(String uid, String token) {
		String url = String.format(Constants.INFO_URL, new Object[] { uid,
				token });

		JSONObject json = HttpComm.sendJSONToServer(url);
		if (json == null)
			return null;
		return json;
	}

	/**
	 * 订阅
	 */
	public static JSONObject subText(String textid, long curtime) {
		if (BookApp.getUser() == null) {
			return null;
		}
		String uid = BookApp.getUser().getUid();
		long t = curtime;
		String auth = Util.md5(
				Util.md5(uid + t + Constants.PRIVATE_KEY)
						+ Constants.PRIVATE_KEY).substring(0, 10);
		long srcT = curtime;
		String channel = CommonUtils.getCustomChannel(BookApp.getInstance());
		String srcAuth = CommonUtils.getCustomAuth(srcT);
		// String model = PhoneUtils.getPhoneModel();
		String model = CommonUtils.getChannel(BookApp.getInstance());
		String imei = PhoneUtils.getPhoneImei(BookApp.getInstance());
		String url = String.format(Constants.SUB_TEXT_URL, new Object[] {
				textid, uid, BookApp.getUser().getToken(), auth, srcT, channel,
				srcAuth, model.toUpperCase(), imei });

		JSONObject json = HttpComm.sendJSONToServer(url);
		// LogUtils.info("订阅结果："+json+"|"+url);
		if (json == null)
			return null;
		return json;
	}

	/**
	 * 订阅全部信息
	 */
	public static OrderAllMsg subTextMsg(String aid) {
		if (BookApp.getUser() == null) {
			return null;
		}
		String uid = BookApp.getUser().getUid();
		String channel = CommonUtils.getCustomChannel(BookApp.getInstance());
		// String model = PhoneUtils.getPhoneModel();
		String model = CommonUtils.getChannel(BookApp.getInstance());
		String imei = PhoneUtils.getPhoneImei(BookApp.getInstance());
		String url = String.format(Constants.TEXT_ALL_URL, new Object[] { aid,
				uid, BookApp.getUser().getToken(), channel,
				model.toUpperCase(), imei });

		JSONObject json = HttpComm.sendJSONToServer(url);
		if (json == null)
			return null;
		return JsonToBean.JsonToOrderAll(json);
	}

	// /**
	// <<<<<<< .working
	// * 订阅折扣书信息,用户获得章节信息的时候会获取这些。
	// * 根据章节信息显示是不是会调用折扣模式，根据服务端返回来的对书的运营策略 NIMEIDE
	// */
	// public static OrderAllMsg subTextDiscountMsg(String aid) {
	// if (BookApp.getUser() == null) {
	// return null;
	// }
	// String uid = BookApp.getUser().getUid();
	// // 根据则中提供的url地址
	// Long time = System.currentTimeMillis();
	// String token = Util.md5(Util.md5(uid + time + Constants.PRIVATE_KEY) +
	// Constants.PRIVATE_KEY).substring(0, 10);
	// String url = String.format(Constants.ALL_DISCOUNT_URL, new Object[] {
	// aid, uid, BookApp.getUser().getToken(), time, token });
	//
	// // http://opendata.local.com/web/book.php?a=go_sub_discount_book&
	// // uid=8562289&
	// // articleid=65886
	// // &token=876b7ba8c4&
	// // t=1231231231&
	// // auth=f252ab87d4
	// JSONObject json = HttpComm.sendJSONToServer(url);
	// if (json == null) {
	// Log.i("msg", "订阅折扣信息获得为空");
	// return null;
	// }
	// Log.i("msg+订阅折扣还回信息", json.toString());
	// // 进行解析，添加折扣的信息，根据则中返回的数据进行调整
	// return JsonToBean.JsonToOrderDiscountAll(json);
	// }

	// /**
	// * 订阅全部折扣章节
	// *
	// */
	//
	// public static JSONObject subDiscountTextAll(String aid, long curtime) {
	// if (BookApp.getUser() == null) {
	// return null;
	// }
	// String uid = BookApp.getUser().getUid();
	// // long t = curtime;
	// // String auth = Util.md5(Util.md5(uid + t + Constants.PRIVATE_KEY) +
	// Constants.PRIVATE_KEY).substring(0, 10);
	// // long srcT = curtime;
	// // String channel = CommonUtils.getCustomChannel(BookApp.getInstance());
	// // String srcAuth = CommonUtils.getCustomAuth(srcT);
	// // String model = PhoneUtils.getPhoneModel();
	// // String model = CommonUtils.getChannel(BookApp.getInstance());
	// // String imei = PhoneUtils.getPhoneImei(BookApp.getInstance());
	//
	// Long time = System.currentTimeMillis();
	// String token = Util.md5(Util.md5(uid + time + Constants.PRIVATE_KEY) +
	// Constants.PRIVATE_KEY).substring(0, 10);
	// String url = String.format(Constants.SUB_ALL_DISCOUNT_URL, new Object[] {
	// aid, uid, BookApp.getUser().getToken(), time, token });
	//
	// JSONObject json = HttpComm.sendJSONToServer(url);
	// if (json == null)
	// return null;
	// return json;
	// }

	/**
	 * 订阅折扣书信息,用户获得章节信息的时候会获取这些。 根据章节信息显示是不是会调用折扣模式，根据服务端返回来的对书的运营策略 NIMEIDE
	 */
	public static OrderAllMsg subTextDiscountMsg(String aid) {
		if (BookApp.getUser() == null) {
			return null;
		}
		String uid = BookApp.getUser().getUid();

		// 根据则中提供的url地址
		Long time = System.currentTimeMillis();
		String token = Util.md5(
				Util.md5(uid + time + Constants.PRIVATE_KEY)
						+ Constants.PRIVATE_KEY).substring(0, 10);
		String url = String.format(Constants.ALL_DISCOUNT_URL, new Object[] {
				aid, uid, BookApp.getUser().getToken(), time, token });

		JSONObject json = HttpComm.sendJSONToServer(url);
		if (json == null) {
			return null;
		}

		// 进行解析，添加折扣的信息，根据则中返回的数据进行调整
		return JsonToBean.JsonToOrderDiscountAll(json);
	}

	/**
	 * 订阅全部折扣章节
	 * 
	 */
	public static JSONObject subDiscountTextAll(String aid, long curtime) {
		if (BookApp.getUser() == null) {
			return null;
		}
		String uid = BookApp.getUser().getUid();

		Long time = System.currentTimeMillis();
		String token = Util.md5(
				Util.md5(uid + time + Constants.PRIVATE_KEY)
						+ Constants.PRIVATE_KEY).substring(0, 10);
		String url = String.format(Constants.SUB_ALL_DISCOUNT_URL,
				new Object[] { aid, uid, BookApp.getUser().getToken(), time,
						token });

		JSONObject json = HttpComm.sendJSONToServer(url);
		if (json == null)
			return null;
		return json;
	}

	/**
	 * >>>>>>> .merge-right.r31258 订阅全部
	 */
	public static JSONObject subTextAll(String aid, long curtime) {
		if (BookApp.getUser() == null) {
			return null;
		}
		String uid = BookApp.getUser().getUid();
		long t = curtime;
		String auth = Util.md5(
				Util.md5(uid + t + Constants.PRIVATE_KEY)
						+ Constants.PRIVATE_KEY).substring(0, 10);

		long srcT = curtime;
		String channel = CommonUtils.getCustomChannel(BookApp.getInstance());
		String srcAuth = CommonUtils.getCustomAuth(srcT);
		// String model = PhoneUtils.getPhoneModel();
		String model = CommonUtils.getChannel(BookApp.getInstance());
		String imei = PhoneUtils.getPhoneImei(BookApp.getInstance());

		String url = String.format(Constants.SUB_ALL_URL, new Object[] { aid,
				uid, BookApp.getUser().getToken(), auth, srcT, channel,
				srcAuth, model.toUpperCase(), imei });

		JSONObject json = HttpComm.sendJSONToServer(url);
		if (json == null)
			return null;
		return json;
	}

	/**
	 * 手机包月状态
	 */
	public static Month MonthForPhone(String uid, String token) {
		String url = String.format(Constants.MONTH_URL, new Object[] { uid,
				token });

		JSONObject json = HttpComm.sendJSONToServer(url);
		if (json == null)
			return null;

		LogUtils.info("包月状态查询|uid:" + uid + "|" + url + "|" + json.toString());

		return JsonToBean.JsonToMonth(json);
	}

	/**
	 * 修改资料
	 */
	public static String updateInfo(String uid, String token, String tel,
			String email) {
		String url = String.format(Constants.UPDATE_USER_INFO, new Object[] {
				uid, token, tel, email });

		JSONObject json = HttpComm.sendJSONToServer(url);
		if (json == null) {
			return null;
		}
		LogUtils.info("修改资料返回json:   " + json.toString());
		try {
			if (!json.isNull("code")) {
				String code;
				code = json.getString("code");
				return code;
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 书评论
	 */
	public static Shupinginfo Shupinginfo(String id, int page) {
		String url = String.format(Constants.SHUPING_URL, new Object[] { id,
				page });

		JSONObject json = HttpComm.sendJSONToServer(url);
		if (json == null)
			return null;
		return JsonToBean.JsonToShupinginfo(json);
	}

	/**
	 * 书评回复
	 */
	public static Shuping_huifu Shuping_huifu(String id, int tid, int page) {
		String url = String.format(Constants.SHUPING_DETAIL_URL, new Object[] {
				id, tid, page });

		JSONObject json = HttpComm.sendJSONToServer(url);
		if (json == null)
			return null;
		return JsonToBean.JsonToShupinghuifu(json);
	}

	/**
	 * 书架同步数据接口
	 */
	public static Vector<BFBook> syncBFBook(String uid, String token) {
		String url = String.format(Constants.MY_FAVOR_URL, new Object[] { uid,
				token });

		JSONObject json = HttpComm.sendJSONToServer(url, 1);
		if (json == null) {
			return null;
		}
		return JsonToBean.jsonToBFBook(json);
	}

	/**
	 * 我的vip同步接口
	 */
	public static Vector<BFBook> syncVipBF(String uid, String token) {
		String url = String.format(Constants.MY_VIP_URL, new Object[] { uid,
				token });

		JSONObject json = HttpComm.sendJSONToServer(url, 1);
		if (json == null) {
			return null;
		}
		return JsonToBean.jsonToVip(json);
	}

	/**
	 * 已订阅章节信息
	 */
	public static Subed_chapters_info Subed_chapters_info(String aid,
			String uid, String token) {
		String url = String.format(Constants.GET_SUBED_CHAPTERS_INFO_URL,
				new Object[] { aid, uid, token });

		JSONObject json = HttpComm.sendJSONToServer(url);
		if (json == null)
			return null;

		// LogUtils.info("已订阅章节信息："+json+"|"+url);

		return JsonToBean.JsonToSubedchaptersinfo(json);
	}

	/**
	 * 新用户注册、老用户登录发送机器信息
	 */
	public static void sendPhoneInfoFeedBack(String uid, String token,
			String v1, String v2, String v3, String v4, int flag) {
		String url = String.format(Constants.FEEDBACK_URL, new Object[] { uid,
				token, flag });
		HttpComm.postMsg(url, v1, v2, v3, v4);

	}

	/**
	 * 安装成功发送机器信息
	 */
	public static boolean sendPhoneInfoInstall(String v1, String v2, String v3,
			String v4) {
		String token = Util.md5(0 + Constants.PRIVATE_KEY).substring(0, 10);
		String url = String.format(Constants.INSTALLED_INFO_URL,
				new Object[] { token });
		return HttpComm.postMsg(url, v1, v2, v3, v4);
	}

	/**
	 * 公告
	 */
	public static NoticeCheck noticeCheck() {
		String curChannel = CommonUtils.getChannel(BookApp.getInstance());

		String model = PhoneUtils.getPhoneModel();
		String imei = PhoneUtils.getPhoneImei(BookApp.getInstance());// 手机imei
		int versioncode = PhoneUtils.getVersionCode();// 得到应用版本号
		String uid = BookApp.getUser() != null ? BookApp.getUser().getUid()
				: "0";// 用户id

		String url = String.format(Constants.NOTICE_CHECK_URL, new Object[] {
				curChannel, model, imei, versioncode, uid });
		JSONObject json = HttpComm.sendJSONToServer(url);

		if (json == null)
			return null;

		NoticeCheck noticeCheck = JsonUtils.fromJson(json.toString(),
				NoticeCheck.class);

		return noticeCheck;
	}

	/**
	 * 支付宝app
	 */
	public static AliPayBean alipayApp(String uId, double money) {

		String url = String.format(Constants.ALIPAY_APP_URL, new Object[] {
				uId, money });
		JSONObject json = HttpComm.sendJSONToServer(url);

		if (json == null)
			return null;

		AliPayBean aliPayBean = JsonUtils.fromJson(json.toString(),
				AliPayBean.class);

		return aliPayBean;
	}

	/**
	 * 支付宝钱包合作app
	 */
	public static AliPayBean alipayWalletApp(String uId, double money,
			String token) {

		String url = String.format(Constants.ALIPAY_APP_WALLET_URL, uId, money,
				token);
		JSONObject json = HttpComm.sendJSONToServer(url);

		if (json == null)
			return null;

		AliPayBean aliPayBean = JsonUtils.fromJson(json.toString(),
				AliPayBean.class);

		return aliPayBean;
	}
}
