package com.eastedge.readnovel.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.beans.BookMark;
import com.readnovel.base.util.LogUtils;

/**
 * @author Wuyexiong
 * 
 */
public class DBAdapter extends BaseDBAccess {
	public static final String DB_BF = "booksf"; // 书架表
	public static final String DB_SQ = "booksq"; // 书签表
	public static final String DB_GX = "bookuser"; // 用户 书本 关系表
	public static final String KEY_ID = "_id"; // 表属性ID
	public static final String KEY_articleid = "articleid"; // 书ID
	public static final String KEY_bookFile = "bookFile"; // 书本地路径
	public static final String KEY_bookURL = "bookURL"; // 书URL路径
	public static final String KEY_imgFile = "imgFile"; // 本地图片路径
	public static final String KEY_isvip1 = "isvip"; // 是否为vip书架的书
	public static final String KEY_finishFlag = "finishFlag"; // 是否完结
	public static final String KEY_userid = "uid"; // 用户uid
	public static final String KEY_title = "title"; // 书标题
	public static final String KEY_hasupdate = "hasupdate"; // 是否有更新
	public static final String KEY_lasttime = "lasttime"; // 最进一次打开
	public static final String KEY_lastuptime = "lastuptime"; // 最进一次更新

	public static final String KEY_textid = "textid"; // 章节id
	public static final String KEY_texttitle = "texttitle"; // 章节标题
	public static final String KEY_textjj = "textjj"; // 章节简介
	public static final String KEY_time = "time"; // 添加时间
	public static final String KEY_location = "location"; // 位置
	public static final String KEY_length = "length"; // 长度
	public static final String KEY_isD = "isd"; // 是否是本地
	public static final String KEY_isVip = "isv"; // 是否是VIP 书签

	public static final String KEY_IMEI = "imei";// 手机机器码
	public static final String KEY_MODEL = "model";// 手机型号
	public static final String KEY_SCREENPIX = "screenpix";// 手机分辨率

	/**
	 * 书架表
	 */
	public static final String DB_CREATE = "CREATE TABLE " + DB_BF + " ("
			+ KEY_ID + " integer primary key autoincrement, " + KEY_articleid
			+ " text , " + KEY_bookURL + " text ," + KEY_bookFile + " text ,"
			+ KEY_imgFile + " text ," + KEY_userid + " text ," + KEY_lasttime
			+ " integer ," + KEY_lastuptime + " integer ," + KEY_hasupdate
			+ " integer ," + KEY_finishFlag + " integer ," + KEY_title
			+ " text);";

	/**
	 * 用户 书本 关系表
	 */
	public static final String DB_CREATE_GX = "CREATE TABLE " + DB_GX + " ("
			+ KEY_ID + " integer primary key autoincrement, " + KEY_articleid
			+ " text ," + KEY_isvip1 + " integer ," + KEY_userid + " text);";

	/**
	 * 书签表
	 */
	public static final String DB_TABLE_SQ = "CREATE TABLE " + DB_SQ + " ("
			+ KEY_ID + " integer primary key autoincrement, " + KEY_articleid
			+ " text , " + KEY_textid + " text ," + KEY_texttitle + " text ,"
			+ KEY_textjj + " text ," + KEY_time + " integer ," + KEY_isD
			+ " integer ," + KEY_isVip + " integer ," + KEY_length
			+ " integer ," + KEY_location + " integer);";

	public DBAdapter(Context context) {
		super(context);
	}

	public void insertGx(String aid, String uid, int isvip) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_articleid, aid);
		newValues.put(KEY_userid, uid);
		newValues.put(KEY_isvip1, isvip);
		long a = db.insert(DB_GX, null, newValues);
	}

	public boolean exitBookGx(String aid, String uid) {
		Cursor cursor = db.query(DB_GX, new String[] { KEY_ID }, "articleid='"
				+ aid + "' and (uid='" + uid + "' or uid='-1') and isvip=0",
				null, null, null, null);
		if (cursor == null)
			return false;
		boolean flag = cursor.moveToFirst();
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return flag;
	}

	public boolean exitBookGx(String aid, String uid, int isvip) {
		Cursor cursor = db.query(DB_GX, new String[] { KEY_ID }, "articleid='"
				+ aid + "' and uid='" + uid + "'  and isvip=" + isvip, null,
				null, null, null);
		if (cursor == null)
			return false;
		boolean flag = cursor.moveToFirst();
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return flag;
	}

	public boolean exitBookGxVip(String aid, String uid) {
		Cursor cursor = db.query(DB_GX, new String[] { KEY_ID }, "articleid='"
				+ aid + "' and uid='" + uid + "'  and isvip=1", null, null,
				null, null);
		if (cursor == null)
			return false;
		boolean flag = cursor.moveToFirst();
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return flag;
	}

	public void deleteGxOne1(String aid, String uid, int isvip) {
		db.delete(DB_GX, "articleid='" + aid + "' and (uid='" + uid
				+ "' or uid='-1')  and isvip=" + isvip, null);
	}

	public void deleteGxOne(String aid, String uid, int isvip) {
		db.delete(DB_GX, "articleid='" + aid + "' and uid='" + uid
				+ "'   and isvip=" + isvip, null);
	}

	/**
	 * 添加书架
	 */
	public String insertBook(BFBook book) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_articleid, book.getArticleid());
		newValues.put(KEY_imgFile, book.getImgFile());
		newValues.put(KEY_bookFile, book.getBookFile());
		newValues.put(KEY_bookURL, book.getBookURL());
		newValues.put(KEY_finishFlag, book.getFinishFlag());
		newValues.put(KEY_userid, book.getUid());
		newValues.put(KEY_title, book.getTitle());
		newValues.put(KEY_lasttime, System.currentTimeMillis());
		long a = db.insert(DB_BF, null, newValues);
		if (a != -1) {
			return "加入书架成功";
		}
		return "加入书架失败";
	}

	// 是否存在本地
	public boolean exitBookBF1(String aid) {
		Cursor cursor = db.query(DB_BF, new String[] { KEY_ID }, "articleid='"
				+ aid + "' ", null, null, null, null);
		if (cursor == null)
			return false;
		boolean flag = cursor.moveToFirst();
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return flag;
	}

	public boolean exitBookVip1(String aid, String uid) {
		return exitBookGxVip(aid, uid);
	}

	public boolean exitBF1(String aid, String uid) {
		return exitBookGx(aid, uid);
	}

	/**
	 * 添加书签
	 */
	public String insertBookMark(BookMark bookMark, int isD) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_articleid, bookMark.getArticleid());
		newValues.put(KEY_textid, bookMark.getTextid());
		newValues.put(KEY_texttitle, bookMark.getTexttitle());
		newValues.put(KEY_textjj, bookMark.getTextjj());
		newValues.put(KEY_time, bookMark.getTime());
		newValues.put(KEY_length, bookMark.getLength());
		newValues.put(KEY_location, bookMark.getLocation());
		newValues.put(KEY_isD, isD);
		newValues.put(KEY_isVip, bookMark.getIsV());
		long a = db.insert(DB_SQ, null, newValues);
		if (a != -1) {
			return "添加书签成功";
		}
		return "添加书签失败";
	}

	/**
	 * 删除一本书
	 */
	public long deleteOneBook(long id) {
		return db.delete(DB_BF, KEY_ID + "=" + id, null);
	}

	/**
	 * 删除一条书签
	 */
	public long deleteOneMark(long id) {
		return db.delete(DB_SQ, KEY_ID + "=" + id, null);
	}

	/**
	 * 删除一本书根据bookId
	 */
	public long deleteBookById(String aid) {
		return db.delete(DB_BF, KEY_articleid + "=" + aid, null);
	}

	// 删除一个特定的一条书签
	public long deleteOneMarkaid(String aid, String textid, int location) {
		return db.delete(DB_SQ, "" + KEY_articleid + "='" + aid + "'" + " and "
				+ KEY_textid + "='" + textid + "'" + " and isd=1 and "
				+ KEY_location + "=" + location, null);
	}

	// 删除本地书的一条书签
	public long deleteOneMark(String aid, int location) {
		return db.delete(DB_SQ, "articleid='" + aid
				+ "' and isd=0 and location=" + location, null);
	}

	/**
	 * 删除所有书签
	 */
	public long deleteAllMark(String aid, int isd) {
		return db.delete(DB_SQ,
				KEY_articleid + "='" + aid + "' and isd=" + isd, null);
	}

	/**
	 * 设置最后一次打开时间
	 */
	public long updateSetBookLT(String articleid) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_lasttime, System.currentTimeMillis());
		return db.update(DB_BF, newValues, KEY_articleid + "='" + articleid
				+ "'", null);
	}

	/**
	 * 设置需要更新
	 */
	public long isNeedUp(String articleid, int val) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_hasupdate, val);
		return db.update(DB_BF, newValues, KEY_articleid + "='" + articleid
				+ "'", null);
	}

	/**
	 * 更新最新时间
	 */
	public long upLasttime(String articleid, long time) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_lastuptime, time);
		return db.update(DB_BF, newValues, KEY_articleid + "='" + articleid
				+ "'", null);
	}

	public ArrayList<BFBook> queryOneBook(String id) {
		Cursor result = db.query(DB_BF, new String[] { KEY_ID, KEY_articleid,
				KEY_bookFile, KEY_bookURL, KEY_imgFile, KEY_title,
				KEY_lastuptime, KEY_hasupdate, KEY_finishFlag }, KEY_articleid
				+ "='" + id + "'", null, null, null, null);
		return convertToBook(result);
	}

	/**
	 * 查询我的书架的书
	 */
	public Vector<BFBook> queryMyBFData(String uid) {
		Cursor cursor = db
				.rawQuery(
						"select b._id _id,b.articleid articleid,b.bookFile bookFile,b.bookURL bookURL,b.imgFile imgFile,"
								+ "b.title title,b.hasupdate hasupdate,b.finishFlag finishFlag,b.lastuptime  lastuptime from booksf b"
								+ " inner join  bookuser on b.articleid=bookuser.articleid "
								+ "where bookuser.isvip=0 and (bookuser.uid='-1' or bookuser.uid='"
								+ uid + "') order by b.lasttime desc", null);

		return convertToBookVec(cursor);
	}

	/**
	 * 查询vip书架的书
	 */
	public Vector<BFBook> queryAllVipData(String uid) {
		Cursor cursor = db
				.rawQuery(
						"select b._id _id,b.articleid articleid,b.bookFile bookFile,b.bookURL bookURL,b.imgFile imgFile,"
								+ "b.title title,b.hasupdate hasupdate,b.finishFlag finishFlag,b.lastuptime lastuptime from booksf b"
								+ " left join  bookuser on b.articleid=bookuser.articleid where bookuser.isvip=1 and bookuser.uid='"
								+ uid + "' order by b.lasttime desc", null);
		return convertToBookVec(cursor);
	}

	public HashSet<String> queryGxBook(String uid) {
		Cursor result = db.query(DB_GX, new String[] { KEY_articleid }, "uid='"
				+ uid + "' and isvip=0", null, null, null, null);
		HashSet<String> set = new HashSet<String>();
		if (result != null && result.moveToFirst()) {
			for (int i = 0; i < result.getCount(); i++) {
				result.moveToPosition(i);
				set.add(result.getString(0));
			}
		}
		if (result != null && !result.isClosed()) {
			result.close();
		}
		return set;
	}

	/**
	 * 查询书的位置
	 */
	public String queryBookFile(String aid) {
		Cursor result = db.query(DB_BF, new String[] { KEY_bookFile },
				KEY_articleid + "='" + aid + "'  and bookFile is not null",
				null, null, null, null);
		if (result != null && result.moveToFirst()) {
			result.moveToFirst();
			String p = result.getString(0);
			if (result != null && !result.isClosed()) {
				result.close();
			}
			return p;
		}
		if (result != null && !result.isClosed()) {
			result.close();
		}

		return null;
	}

	public HashMap<String, Long> queryBookToUp() {
		Cursor result = db.query(DB_BF, new String[] { KEY_articleid,
				KEY_lastuptime }, KEY_lastuptime + " is not null", null, null,
				null, null);
		HashMap<String, Long> map = new HashMap<String, Long>();
		if (result != null && result.moveToFirst()) {
			for (int i = 0; i < result.getCount(); i++) {
				result.moveToPosition(i);
				map.put(result.getString(0), result.getLong(1));
			}

		}
		if (result != null && !result.isClosed()) {
			result.close();
		}

		LogUtils.info("需要更新的书|" + result.getCount() + "|" + map);

		return map;
	}

	/**
	 * 查询全部书签
	 */
	public ArrayList<BookMark> queryAllBookMark(String articleid, int isd) {
		Cursor result = db.query(DB_SQ, new String[] { KEY_ID, KEY_articleid,
				KEY_textid, KEY_texttitle, KEY_textjj, KEY_time, KEY_length,
				KEY_isD, KEY_location, KEY_isVip }, KEY_articleid + "='"
				+ articleid + "' and isd=" + isd, null, null, null, null);
		return convertToBookMark(result);
	}

	/**
	 * 查询单本书全部书签位子
	 */
	public HashMap<String, Long> queryAllBookP(String articleid, int isd) {
		Cursor result = db.query(DB_SQ, new String[] { KEY_ID, KEY_location,
				KEY_textid }, KEY_articleid + "='" + articleid + "' and isd="
				+ isd, null, null, null, null);
		HashMap<String, Long> map = new HashMap<String, Long>();
		if (result != null && result.moveToFirst()) {
			for (int i = 0; i < result.getCount(); i++) {
				result.moveToPosition(i);
				long id = result.getLong(0);
				int p = result.getInt(1);
				String t = result.getString(2);
				map.put(t + p, id);
			}
		}
		if (result != null && !result.isClosed()) {
			result.close();
		}
		return map;
	}

	public HashSet<String> updateUid(String uid) {

		Cursor cursor = db.query(DB_GX, new String[] { KEY_ID, KEY_articleid },
				"uid='-1'", null, null, null, null);
		HashSet<String> set = new HashSet<String>();
		if (cursor != null && cursor.moveToFirst()) {
			LogUtils.info("长度          ======" + cursor.getCount());
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				Long id = cursor.getLong(0);
				String aid = cursor.getString(1);

				if (!exitBookGx(aid, uid, 0)) {
					ContentValues newValues = new ContentValues();
					newValues.put(KEY_userid, uid);
					db.update(DB_GX, newValues, KEY_ID + "=" + id, null);
					set.add(aid);
				} else {
					db.delete(DB_GX, KEY_ID + "=" + id, null);
				}
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return set;

	}

	/**
	 * 设置文件路径
	 */
	public long updateSetBookFile(String bookFile, String articleid, long time) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_bookFile, bookFile);
		newValues.put(KEY_lastuptime, time);
		return db.update(DB_BF, newValues, KEY_articleid + "='" + articleid
				+ "'", null);
	}

	private ArrayList<BFBook> convertToBook(Cursor cursor) {
		int resultCounts = cursor.getCount();
		ArrayList<BFBook> list = new ArrayList<BFBook>();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return list;
		}

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			BFBook bfbk = new BFBook();
			bfbk.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
			bfbk.setArticleid(cursor.getString(cursor
					.getColumnIndex(KEY_articleid)));
			bfbk.setBookFile(cursor.getString(cursor
					.getColumnIndex(KEY_bookFile)));
			bfbk.setBookURL(cursor.getString(cursor.getColumnIndex(KEY_bookURL)));
			bfbk.setImgFile(cursor.getString(cursor.getColumnIndex(KEY_imgFile)));
			bfbk.setTitle(cursor.getString(cursor.getColumnIndex(KEY_title)));
			bfbk.setLastuptime(cursor.getLong(cursor
					.getColumnIndex(KEY_lastuptime)));
			bfbk.setIsUp(cursor.getInt(cursor.getColumnIndex(KEY_hasupdate)));
			bfbk.setFinishFlag(cursor.getInt(cursor
					.getColumnIndex(KEY_finishFlag)));
			list.add(bfbk);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	private Vector<BFBook> convertToBookVec(Cursor cursor) {
		int resultCounts = cursor.getCount();
		Vector<BFBook> list = new Vector<BFBook>();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return list;
		}

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			BFBook bfbk = new BFBook();
			bfbk.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
			bfbk.setArticleid(cursor.getString(cursor
					.getColumnIndex(KEY_articleid)));
			bfbk.setBookFile(cursor.getString(cursor
					.getColumnIndex(KEY_bookFile)));
			bfbk.setBookURL(cursor.getString(cursor.getColumnIndex(KEY_bookURL)));
			bfbk.setImgFile(cursor.getString(cursor.getColumnIndex(KEY_imgFile)));
			bfbk.setTitle(cursor.getString(cursor.getColumnIndex(KEY_title)));
			bfbk.setLastuptime(cursor.getLong(cursor
					.getColumnIndex(KEY_lastuptime)));
			bfbk.setIsUp(cursor.getInt(cursor.getColumnIndex(KEY_hasupdate)));
			bfbk.setFinishFlag(cursor.getInt(cursor
					.getColumnIndex(KEY_finishFlag)));
			list.add(bfbk);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	private ArrayList<BookMark> convertToBookMark(Cursor cursor) {
		int resultCounts = cursor.getCount();
		ArrayList<BookMark> list = new ArrayList<BookMark>();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return list;
		}

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			BookMark mark = new BookMark();
			mark.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
			mark.setArticleid(cursor.getString(cursor
					.getColumnIndex(KEY_articleid)));
			mark.setTextid(cursor.getString(cursor.getColumnIndex(KEY_textid)));
			mark.setTexttitle(cursor.getString(cursor
					.getColumnIndex(KEY_texttitle)));
			mark.setTextjj(cursor.getString(cursor.getColumnIndex(KEY_textjj)));
			mark.setTime(cursor.getLong(cursor.getColumnIndex(KEY_time)));
			mark.setLength(cursor.getInt(cursor.getColumnIndex(KEY_length)));
			mark.setIsd(cursor.getInt(cursor.getColumnIndex(KEY_isD)));
			mark.setIsV(cursor.getInt(cursor.getColumnIndex(KEY_isVip)));
			mark.setLocation(cursor.getInt(cursor.getColumnIndex(KEY_location)));
			list.add(mark);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

}
