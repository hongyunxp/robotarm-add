package com.readnovel.book.base.task;

import java.util.List;

import android.content.Intent;

import com.readnovel.book.base.ChapterListActivity;
import com.readnovel.book.base.FirstActivity;
import com.readnovel.book.base.bean.VipPayInterval;
import com.readnovel.book.base.common.Constants;
import com.readnovel.book.base.db.table.VipPayIntervalRecord;
import com.readnovel.book.base.entity.Book;
import com.readnovel.book.base.entity.Chapter;
import com.readnovel.book.base.sp.StyleSaveUtil;
import com.readnovel.book.base.sync.EasyTask;
import com.readnovel.book.base.utils.BookInfoUtils;
import com.readnovel.book.base.utils.BookListProvider;
import com.readnovel.book.base.utils.CommonUtils;

public class InitTask extends EasyTask<FirstActivity, Void, Void, Void> {
	private StyleSaveUtil util;
	private Book book;

	public InitTask(FirstActivity caller) {
		super(caller);
		util = new StyleSaveUtil(caller);
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		caller.getAnim().start();
	}

	@Override
	public Void doInBackground(Void... params) {
		init();
		return null;
	}

	@Override
	public void onPostExecute(Void result) {
		goToNext();
	}

	private void init() {
		//初始化书信息
		initBookInfo();
		//初始化书文件
		if (util.getfirst()) {
			initBookFile();
			//初始化VIP，只初始化一次,只付费书执行
			initVipChapters();
		}
	}

	/**
	 * 初始化书文件
	 */
	private void initBookFile() {
		new Thread() {
			@Override
			public void run() {
				CommonUtils.writeFile(caller);
			}
		}.start();
	}

	private void initBookInfo() {
		book = BookListProvider.getInstance(caller).getBook();
	}

	/**
	 * 初始化VIP章节
	 */
	private void initVipChapters() {
		//初始化VIP章节
		// 分段
		List<VipPayInterval> vpis = BookInfoUtils.bookSections(book, Constants.SECTION_SIZE);
		//存到数据库
		VipPayIntervalRecord vpir = new VipPayIntervalRecord(caller);
		vpir.add(vpis, VipPayInterval.UN_ORDER);
//		vpir.add(vpis, VipPayInterval.PAY_SUCCESS);
	}

	private void goToNext() {
		{
			Book book = BookListProvider.getInstance(caller).getBook();
			if (util.getfirst()) {// 第一次 进入第一章的阅读位置
				util.first(false);
				CommonUtils.goToFlipRead(caller, book.getChapters().get(0).getFileName(), book.getChapters().get(0).getTitle(), book.getChapters()
						.get(0).getId());//进入章节内容
			} else {// 不是第一次
				Chapter c = book.getChapters().get(util.writescollery());
				if (c.isVip()) {
					Intent intent = new Intent(caller, ChapterListActivity.class);
					caller.startActivity(intent);
				} else {
					CommonUtils.goToFlipRead(caller, c.getFileName(), c.getTitle(), c.getId());//进入章节内容
				}
			}
		}
	}

}
