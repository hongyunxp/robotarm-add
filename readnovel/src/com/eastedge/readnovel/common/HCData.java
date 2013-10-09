package com.eastedge.readnovel.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.eastedge.readnovel.beans.BookType;
import com.eastedge.readnovel.beans.NewBookList;
import com.eastedge.readnovel.beans.PaihangMain;
import com.eastedge.readnovel.beans.RDBook;
import com.eastedge.readnovel.beans.Shubenmulu;
import com.eastedge.readnovel.beans.TuijianMain;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.http.DownFile;

/**
 * 缓存
 */
public class HCData {
	public static TuijianMain mTuijianMain;
	public static PaihangMain paihangMain;
	public static NewBookList listgirl = new NewBookList();
	public static NewBookList listboy = new NewBookList();
	public static NewBookList listschool = new NewBookList();
	public static ArrayList<BookType> list = new ArrayList<BookType>();
	public static ArrayList<String> wordList = new ArrayList<String>();
	public static Hashtable<String, RDBook> bookMap = new Hashtable<String, RDBook>();
	public static Hashtable<String, Shubenmulu> mulumap = new Hashtable<String, Shubenmulu>();
	public static Hashtable<String, String> readtextidmap = new Hashtable<String, String>();
	public static Hashtable<String, DownFile> downingBook = new Hashtable<String, DownFile>();
	public static Hashtable<String, String> nowtextid = new Hashtable<String, String>();
	public static HashMap<String, String> downOK = new HashMap<String, String>();
	public static Hashtable<String, Integer> nowp = new Hashtable<String, Integer>();

	public static void clearAll() {
		mTuijianMain = null;
		paihangMain = null;
		BookApp.setUser(null);
		listgirl.clear();
		listboy.clear();
		listschool.clear();
		list.clear();
		wordList.clear();
		bookMap.clear();
		mulumap.clear();
		readtextidmap.clear();
		downingBook.clear();
		nowtextid.clear();
		downOK.clear();
		nowp.clear();
	}
}
