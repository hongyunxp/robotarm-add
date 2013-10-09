package com.eastedge.readnovel.threads;

import java.util.HashSet;
import java.util.Vector;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.db.DBAdapter;
import com.eastedge.readnovel.db.UserBookTable;
import com.readnovel.base.common.NetType;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.NetUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.http.HttpImpl;

/** 
 * @author ninglv 
 * @version Time：2012-4-13 下午4:45:37 
 */
public class SyncBFBookThread extends Thread {
	public Vector<BFBook> al;
	private String uid, token;
	private Handler handler;
	private boolean needUp;
	private DBAdapter db;

	public SyncBFBookThread(String uid, String token) {
		super();
		this.uid = uid;
		this.token = token;
	}

	public SyncBFBookThread(String uid, String token, Handler handler, boolean needUp) {
		super();
		this.uid = uid;
		this.token = token;
		this.handler = handler;
		this.needUp = needUp;
	}

	@Override
	public void run() {
		NetType netType = NetUtils.checkNet();
		if (NetType.TYPE_NONE.equals(netType)) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Context ctx = BookApp.getInstance();
					Toast.makeText(ctx, ctx.getString(R.string.net_err_need_check_msg), Toast.LENGTH_SHORT).show();
				}
			});
			return;
		}

		//		if (handler != null) {
		//			handler.post(new Runnable() {
		//				@Override
		//				public void run() {
		//					Toast.makeText(BookApp.getInstance(), "书架同步开始", Toast.LENGTH_SHORT).show();
		//				}
		//			});
		//		}

		try {
			if (db == null) {
				db = new DBAdapter(CloseActivity.curActivity);
			}
			if (needUp) {
				CheckUpdateBookThread ck = new CheckUpdateBookThread(db);
				ck.run();
			}

			HashSet<String> set1 = new HashSet<String>();

			//同步书架
			al = HttpImpl.syncBFBook(uid, token);
			if (al != null && al.size() > 0) {

				//从关系表中获取 当前用户的所有的书的集合
				db.open();
				HashSet<String> bookset = db.queryGxBook(uid);
				//遍历从网上获取的所有书的集合
				for (BFBook book : al) {
					String aid = book.getArticleid();
					//如果关系表中存在
					if (bookset.contains(aid)) {
						//从集合删除
						bookset.remove(aid);
						continue;
					}
					//不存在关系表中 且不存在书架内
					if (!db.exitBookBF1(aid)) {
						book.setImgFile(Util.saveImgFile(CloseActivity.curActivity, book.getImagefname()));
						//添加书到书架
						db.insertBook(book);
					}
					//添加到关系表
					db.insertGx(aid, uid, 0);
				}

				if (bookset != null && bookset.size() > 0) {
					//有多余的数据  从关系表删除
					for (String aid1 : bookset) {
						db.deleteGxOne1(aid1, uid, 0);
					}
				}
				set1 = db.updateUid(uid);
				LogUtils.info("al 长度：" + set1.size());
			}
			HashSet<String> set2 = new HashSet<String>();

			//同步vip书架
			al = HttpImpl.syncVipBF(uid, token);
			if (al != null && al.size() > 0) {
				UserBookTable bt = new UserBookTable(CloseActivity.curActivity, db.getDB());

				for (BFBook book : al) {
					String aid = book.getArticleid();
					//判断是否同步过一次
					if (!bt.exitBook(aid, uid, 1)) {
						//标记为同步过
						bt.insert(aid, uid, 1);
						//书不存在书架内
						if (!db.exitBookBF1(aid)) {
							book.setImgFile(Util.saveImgFile(CloseActivity.curActivity, book.getImagefname()));
							//添加书到书架
							db.insertBook(book);
						}
						//添加到关系表
						if (!db.exitBookGxVip(aid, uid)) {
							db.insertGx(aid, uid, 1);
						}
						if (!db.exitBookGx(aid, uid)) {
							db.insertGx(aid, uid, 0);
							set2.add(aid);
						}
					}
				}
			}
			if (set2.size() > 0 || set1.size() > 0) {
				set1.addAll(set2);
				//与主站同步
				SynUpBook s = new SynUpBook(uid, token, Util.setToString(set1), "");
				s.start();
			}
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			if (db != null)
				db.close();

			if (handler != null) {
				//				handler.post(new Runnable() {
				//
				//					@Override
				//					public void run() {
				//						Toast.makeText(BookApp.getInstance(), "书架同步完成", Toast.LENGTH_SHORT).show();
				//					}
				//				});
				handler.sendEmptyMessage(2);//登陆完成
			}
		}

	}
}
