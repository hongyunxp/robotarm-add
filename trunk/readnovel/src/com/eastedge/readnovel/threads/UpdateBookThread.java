package com.eastedge.readnovel.threads;

import android.app.Activity;
import android.os.Handler;

import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.db.DBAdapter;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.http.DownFile;

/**
 * 更新章节线程
 * @author li.li
 *
 * Aug 8, 2012
 */
public class UpdateBookThread extends Thread {

	private String aid;
	private String title;
	private Handler handler;
	private Activity act;

	public UpdateBookThread(Activity act, String aid, String title, Handler handler) {
		this.act = act;
		this.aid = aid;
		this.title = title;
		this.handler = handler;
	}

	public void run() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			LogUtils.error(e.getMessage(), e);
		}

		DownFile downFile = new DownFile(act, aid, title);
		downFile.doDown();

		if (downFile.isOK == 1) {//成功
			//TODO 更新章节成功后处理阅读记录
			//			LastReadTable tb = new LastReadTable(BookApp.getInstance());
			//			RDBook rd = tb.queryLastBook(aid);
			//
			//			Shubenmulu mulu = Util.read(aid);
			//
			//			for (Chapterinfo chapter : mulu.getMulist()) {
			//				if (chapter.getId().equals(rd.getTextId()) && chapter.getIs_vip() != rd.getIsVip()) {
			//					rd.setIsVip(chapter.getIs_vip());
			//					tb.insertLastRead(rd);
			//					break;
			//				}
			//			}

			if (CloseActivity.curActivity != null) {
				DBAdapter dbAdapter = new DBAdapter(CloseActivity.curActivity);
				dbAdapter.open();
				dbAdapter.isNeedUp(aid, 0);//设置状态，不需要再更新
				dbAdapter.upLasttime(aid, downFile.mul.getLastuptime());
				handler.sendEmptyMessage(336);//发送成功消息
			}
		} else {
			handler.sendEmptyMessage(335);//发送失败消息
		}
	}

}
