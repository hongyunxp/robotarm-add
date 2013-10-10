package com.readnovel.book.base.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.readnovel.book.base.bean.VipPayInterval;
import com.readnovel.book.base.entity.Book;
import com.readnovel.book.base.entity.Chapter;

/**
 * 书信息获得工具类
 * 
 * @author li.li
 *
 * Aug 2, 2012
 */
public class BookInfoUtils {

	/**
	 * 当前书的id
	 * @param ctx
	 * @return
	 */
	public static Book getBook(Context ctx) {
		Book book = BookListProvider.getInstance(ctx).getBook();
		return book;
	}

	public static Chapter getByChapterId(Context ctx, int chapterId) {
		Book book = getBook(ctx);
		for (Chapter chapter : book.getChapters()) {
			if (chapter.getId() == chapterId)
				return chapter;
		}

		return null;
	}

	/**
	 * 当前书章节数
	 * @param ctx
	 * @return
	 */
	public static int getChapterNum(Context ctx) {
		Book book = BookListProvider.getInstance(ctx).getBook();
		return book.getChapters().size();
	}

	/**
	 * 当前书总字数
	 * @param ctx
	 * @return
	 */
	public static int getTotalnum(Context ctx) {
		Book book = BookListProvider.getInstance(ctx).getBook();
		String totalNum = book.getChapters().get(book.getChapters().size() - 1).getNum();
		return Integer.parseInt(totalNum);
	}

	/**
	 * 当前书章节的前缀
	 * @param ctx
	 * @return
	 */
	public static String getChapterPrefix(Context ctx) {
		String bookid = String.valueOf(getBook(ctx).getId());

		return "book_" + bookid + "_";
	}

	/**
	 * 得到第一章
	 * @param ctx
	 * @return
	 */
	public static String getChapterFirst(Context ctx) {
		String chapterPrefix = getChapterPrefix(ctx);

		return chapterPrefix + "1.txt";
	}

	/**
	 * 得到最后一章
	 * @param ctx
	 * @return
	 */
	public static String getChapterEnd(Context ctx) {
		String chapterPrefix = getChapterPrefix(ctx);
		int chapterNum = getChapterNum(ctx);

		return chapterPrefix + chapterNum + ".txt";
	}

	/**
	 * 为VIP章节分组
	 * @param book 书 
	 * @param groupSize 分段长度
	 * @return
	 */
	public static List<VipPayInterval> bookSections(Book book, int groupSize) {
		//分区间
		List<List<Chapter>> seps = new ArrayList<List<Chapter>>();
		List<Chapter> vipChapters = new ArrayList<Chapter>();
		for (Chapter c : book.getChapters()) {//取出vip章节
			if (c.isVip()) {
				vipChapters.add(c);
			}
		}

		List<Chapter> subList = new ArrayList<Chapter>();
		for (int i = 0; i < vipChapters.size(); i++) {

			subList.add(vipChapters.get(i));//章节加入组

			if ((i + 1) % groupSize == 0) {
				seps.add(subList);//每组加入数组
				subList = new ArrayList<Chapter>();//创建新组
			}
		}

		if (subList != null && !subList.isEmpty()) {//最后一组四舍五入加入数组
			if (subList.size() * 2 >= groupSize)
				seps.add(subList);//加入新组
			else {
				seps.get(seps.size() - 1).addAll(subList);//加入上一组
			}

		}

		//入库
		List<VipPayInterval> vpis = new ArrayList<VipPayInterval>();

		for (List<Chapter> cs : seps) {
			StringBuilder bookIds = new StringBuilder();
			for (int j = 0; j < cs.size(); j++) {
				Chapter c = cs.get(j);
				bookIds.append(c.getId());
				if (j != cs.size() - 1)
					bookIds.append(",");
			}
			VipPayInterval vpi = new VipPayInterval();
			vpi.setBookIds(bookIds.toString());
			vpi.setState(0);
			vpis.add(vpi);
		}
		return vpis;
	}

	/**
	 * 是否是免费版
	 * @return
	 */
	public static boolean isFree(Context ctx) {
		return true;
		//		return "free".equals(ctx.getString(R.string.bookType));
	}
}
