package com.eastedge.readnovel.utils;

import java.util.ArrayList;

import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.beans.Chapterinfo;
import com.eastedge.readnovel.beans.RDBook;
import com.eastedge.readnovel.beans.Shubenmulu;
import com.eastedge.readnovel.common.Util;

public class BookPageUtils {
	public static final int BLANK_LINE_HEIGHT = 13;// 段落间空白高度
	public static final int BLANK = 12288;// 全角空格（中文空格）
	public static final int EMPTY_LINE_HEIGHT = 20;// 空行高
	public static final int IGNORE_CHAR = 65533;
	public static final char EMPTY_CHAR = '\u0000';

	/**
	 * 判断是否是新段落
	 * 
	 * @param line
	 * @return
	 */
	public static boolean checkIsParaEnd(String line) {
		if (line != null && !"".equals(line) && line.charAt(0) == BLANK)
			return true;

		return false;
	}

	/**
	 * 验证当前是否为VIP章节
	 * 
	 * @param bf
	 * @param rd
	 * @return true 是 false 否
	 */
	public static boolean checkIsVipChapter(BFBook bf, RDBook rd) {
		if (bf != null && rd != null) 
			return checkIsVipChapter(bf.getArticleid(),rd.getTextId());

		return false;
	}

	public static boolean checkIsVipChapter(String aid, String tid) {
		Shubenmulu mul = Util.read(aid);// 读取目录
		if (mul != null)
			return isVipChapter(tid, mul.getMulist());

		return false;
	}

	/**
	 * 判断当前是否为VIP章节
	 * 
	 * @param cId
	 * @return
	 */
	public static boolean isVipChapter(String cId, ArrayList<Chapterinfo> mulist) {

		for (Chapterinfo cInfo : mulist) {
			if (cId != null && cId.equals(cInfo.getId())) {
				int isVip = cInfo.getIs_vip();

				if (isVip == 1)
					return true;

				break;
			}
		}

		return false;
	}
}
