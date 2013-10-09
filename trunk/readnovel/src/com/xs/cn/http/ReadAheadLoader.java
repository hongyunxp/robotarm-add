package com.xs.cn.http;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.eastedge.readnovel.beans.RDBook;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.StringUtils;

/**
 * 在线预读加载
 * 
 * @author li.li
 *
 * Aug 16, 2012
 */
public class ReadAheadLoader {
	private static final ReadAheadLoader instance = new ReadAheadLoader();
	private static final ExecutorService WORKER = Executors.newSingleThreadExecutor();

	private ReadAheadLoader() {
	}

	public static ReadAheadLoader getInstance() {
		return instance;
	}

	public RDBook execute(final String textId, final boolean isVip) {
		//同步加载当前章节
		RDBook rdBook = readSync(textId, isVip);

		// 异步读下一章节
		if (rdBook != null) {
			boolean nextIsVip = rdBook.getNextVip() == 1 ? true : false;
			readAsyn(rdBook.getNextId(), nextIsVip);
		}

		return rdBook;//返回当前章节

	}

	/**
	 * 同步读章节
	 * @param textId
	 * @param isVip
	 */
	private RDBook readSync(final String textId, final boolean isVip) {
		RDBook rdBook = null;
		
		try {
			//同步加载当前章节
			Future<RDBook> future = WORKER.submit(new Callable<RDBook>() {
				@Override
				public RDBook call() throws Exception {

					return read(textId, isVip);
				}
			});
			//取同步加载结果
			rdBook = future.get();
		} catch (Throwable e) {
			LogUtils.error(e.getMessage(), e);
		}

		return rdBook;
	}

	/**
	 * 异步读章节
	 */
	private void readAsyn(final String textId, final boolean isVip) {

		if (StringUtils.isNotBlank(textId))

			WORKER.execute(new Runnable() {
				@Override
				public void run() {
					read(textId, isVip);
				}
			});

	}

	/**
	 * 读章节，失败后强制读
	 * @param textId
	 * @param isVip
	 * @return
	 */
	private RDBook read(String textId, boolean isVip) {
		RDBook rdBook = null;

		if (StringUtils.isNotBlank(textId)) {
			//普通预读
			rdBook = readSimple(textId, isVip);

			//执行普通预读失败，强制预读
			if (rdBook!=null && !"1".equals(rdBook.getCode()))
				readForce(textId);//强制预读的结果不返回，只作为缓存存在
		}

		return rdBook;
	}

	/**
	 * 普通读章节
	 * @return
	 */
	private RDBook readSimple(String textId, boolean isVip) {
		RDBook rdBook = null;
		if (isVip) {
			rdBook = HttpImpl.downVipText(textId);

		} else
			rdBook = HttpImpl.downText(textId);

		return rdBook;
	}

	/**
	 * 强制读章节
	 * @return
	 */
	private RDBook readForce(String textId) {
		RDBook rdBook = null;

		rdBook = HttpImpl.prepareVipText(textId);

		return rdBook;
	}

}
