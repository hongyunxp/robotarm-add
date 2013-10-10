package com.readnovel.book.base.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class StyleSaveUtil {
	SharedPreferences sp;
	SharedPreferences.Editor editor;
	int fontsize;
	int fontcolor;
	int a, b;
	int bg;

	public StyleSaveUtil(Context context) {
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

	}
	public void setpay(Boolean b) {
		Editor editor = sp.edit();
		editor.putBoolean("ispay", b);
		editor.commit();
	}

	// 获取电量值
	public Boolean getpay() {
		return sp.getBoolean("ispay", false);
	}
	// 添加电量值
		public void setlight(int light) {
			Editor editor = sp.edit();
			editor.putInt("ic_light", light);
			editor.commit();
		}

		// 获取电量值
		public int getlight() {
			return sp.getInt("ic_light", 50);
		}
	public boolean installed() {
		boolean installed = sp.getBoolean("installed", false);
		return installed;
	}

	public void installed(boolean installed) {
		Editor editor = sp.edit();
		editor.putBoolean("installed", installed);
		editor.commit();
	}

	public boolean isDefFengMian() {
		boolean installed = sp.getBoolean("isDefFengMian", false);
		return installed;
	}

	public void isDefFengMian(boolean installed) {
		Editor editor = sp.edit();
		editor.putBoolean("isDefFengMian", installed);
		editor.commit();
	}

	public void first(boolean first) {
		Editor editor = sp.edit();
		editor.putBoolean("first", first);
		editor.commit();
	}

	public boolean getfirst() {
		boolean b = sp.getBoolean("first", true);
		return b;
	}

	public void setFontsize(int fontsize) {
		Editor editor = sp.edit();
		editor.putInt("fontSize", fontsize);
		editor.commit();
	}

	public int getFontsize() {
		int b = sp.getInt("fontSize", 0);
		return b;
	}

	public void setFontcolor(int fontcolor) {
		Editor editor = sp.edit();
		editor.putInt("fontColor", fontcolor);
		editor.commit();
	}

	// 设置默认的背景
	public void setreadbg(int bgid) {
		Editor editor = sp.edit();
		editor.putInt("readbg", bgid);
		editor.commit();
	}

	public int getreadbg() {
		int bgid = sp.getInt("readbg", 0);
		return bgid;
	}

	public int getFontcolor() {

		int a = sp.getInt("fontColor", 0);
		return a;
	}

	// 记录章节列表显示章节的位置信息
	public void savescollery(int d) {
		Editor editor = sp.edit();
		editor.putInt("scroll_y", d);
		editor.commit();
	}

	public int writescollery() {
		int d = sp.getInt("scroll_y", 0);
		return d;
	}

	public void saveChapterName(String g) {
		Editor editor = sp.edit();
		editor.putString("chapterName", g);
		editor.commit();
	}

	public String writeChapterName() {
		String g = sp.getString("chapterName", null);
		return g;
	}

	// 屏幕亮度值
	public void saveLight(float light_num) {
		Editor editor = sp.edit();
		editor.putFloat("k", light_num);
		editor.commit();
	}

	public float writeLight() {
		float k = sp.getFloat("k", 0);
		return k;
	}

	// seekbar的值
	public void saveSeekbar(int d) {
		Editor editor = sp.edit();
		editor.putInt("seekbar", d);
		editor.commit();
	}

	public int writeSeekbar() {
		int d = sp.getInt("seekbar", 0);
		return d;
	}

	public void saveListLocal(float light_num) {
		Editor editor = sp.edit();
		editor.putFloat("l", light_num);
		editor.commit();
	}

	public float writeListLocal() {
		float l = sp.getFloat("l", 0);
		return l;
	}

	// 记录上次读得位置
	public void saveLastOffSet(int d) {
		Editor editor = sp.edit();
		editor.putInt("lastoffset", d);
		editor.commit();
	}

	public int writeLastOffSet() {
		int d = sp.getInt("lastoffset", 0);
		return d;
	}

	// 记录上次读得页码
	public void saveReadPage(String d) {
		Editor editor = sp.edit();
		editor.putString("readpage", d);
		editor.commit();
	}

	public String writeReadPage() {
		String d = sp.getString("readpage", null);
		return d;
	}

	// 记录是从书签进的还是目录页来阅读
	public void saveReadPath(int d) {
		Editor editor = sp.edit();
		editor.putInt("readpath", d);
		editor.commit();
	}

	public int writeReadPath() {
		int d = sp.getInt("readpath", 0);
		return d;
	}

	// 判断第一次进入目录页
	public void saveFirstChapter(int d) {
		Editor editor = sp.edit();
		editor.putInt("firstchapter", d);
		editor.commit();
	}

	public int writeFirstChapter() {
		int d = sp.getInt("firstchapter", 0);
		return d;
	}

	// 判断是否是第一次就如阅读页
	public void saveFirstRead(int d) {
		Editor editor = sp.edit();
		editor.putInt("firstread", d);
		editor.commit();
	}

	public int writeFirstRead() {
		int d = sp.getInt("firstread", 0);
		return d;
	}

	/*
	 *  判断时间计时服务是否存在
	 */
	public void saveTimeCalculateRun(Boolean f) {
		Editor editor = sp.edit();
		editor.putBoolean("timecalculaterun", f);
	}

	public Boolean getTimeCalculateRunState() {
		Boolean state = sp.getBoolean("timecalculaterun", false);
		return state;
	}

	// 判断是否是第一次进行章节跳转的比较
	public void saveFirstStep(int d) {
		Editor editor = sp.edit();
		editor.putInt("firststep", d);
		editor.commit();
	}

	public int writeFirstStep() {
		int d = sp.getInt("firststep", 0);
		return d;
	}

	// 主要用于存储阅读记录跳章时第一次读得章节
	public void saveFirstReadChapter(int d) {
		Editor editor = sp.edit();
		editor.putInt("firstreadchapter", d);
		editor.commit();
	}

	public int writeFirstReadChapter() {
		int d = sp.getInt("firstreadchapter", 0);
		return d;
	}

	// 主要用于存储阅读记录跳章后所读得章节位置
	public void saveStepReadChapter(int d) {
		Editor editor = sp.edit();
		editor.putInt("stepreadchapter", d);
		editor.commit();
	}

	public int writeStepReadChapter() {
		int d = sp.getInt("stepreadchapter", 0);
		return d;
	}

	// 章节名
	public void saveFirstReadChapterName(String d) {
		Editor editor = sp.edit();
		editor.putString("firstreadchaptername", d);
		editor.commit();
	}

	public String writeFirstReadChapterName() {
		String d = sp.getString("firstreadchaptername", null);
		return d;
	}

	// 章节开头的文字
	public void saveFirstReadChapterForeText(String d) {
		Editor editor = sp.edit();
		editor.putString("firstreadchapterforetext", d);
		editor.commit();
	}

	public String writeFirstReadChapterForeText() {
		String d = sp.getString("firstreadchapterforetext", null);
		return d;
	}

	// 章节记录百分比
	public void saveFirstReadChapterPer(String d) {
		Editor editor = sp.edit();
		editor.putString("firstreadchapterper", d);
		editor.commit();
	}

	public String writeFirstReadChapterPer() {
		String d = sp.getString("firstreadchapterper", null);
		return d;
	}

	// 章节记录时间
	public void saveFirstReadChapterTime(String d) {
		Editor editor = sp.edit();
		editor.putString("firstreadchaptertime", d);
		editor.commit();
	}

	public String writeFirstReadChapterTime() {
		String d = sp.getString("firstreadchaptertime", null);
		return d;
	}

	// 章节记录字号
	public void saveFirstReadChapterFontSize(int d) {
		Editor editor = sp.edit();
		editor.putInt("firstreadchapterfontsize", d);
		editor.commit();
	}

	public int writeFirstReadChapterFontSize() {
		int d = sp.getInt("firstreadchapterfontsize", 0);
		return d;
	}

	// 章节记录所读位置
	public void saveFirstReadChapterLast(int d) {
		Editor editor = sp.edit();
		editor.putInt("firstreadchapterlast", d);
		editor.commit();
	}

	public int writeFirstReadChapterLast() {
		int d = sp.getInt("firstreadchapterlast", 0);
		return d;
	}

	// 章节文件名
	public void saveFirstReadChapterFileName(String d) {
		Editor editor = sp.edit();
		editor.putString("firstreadchapterfilename", d);
		editor.commit();
	}

	public String writeFirstReadChapterFileName() {
		String d = sp.getString("firstreadchapterfilename", null);
		return d;
	}

	// 章节记录所读页数
	public void saveFirstReadChapterPageNum(String d) {
		Editor editor = sp.edit();
		editor.putString("firstreadchapterpagenum", d);
		editor.commit();
	}

	public String writeFirstReadChapterPageNum() {
		String d = sp.getString("firstreadchapterpagenum", null);
		return d;
	}

	//是否开启计算页码线程
	public void saveCulPageNumThread(int d) {
		Editor editor = sp.edit();
		editor.putInt("pagenumthread", d);
		editor.commit();
	}

	public int writeCulPageNumThread() {
		int d = sp.getInt("pagenumthread", 0);
		return d;
	}

	// 是否阅读到最后三章，显示提示
	public Boolean getLastThirdTipState() {
		Boolean state = sp.getBoolean("lastthirdstate", false);
		return state;
	}

	public void setLastThirdTipState(Boolean b) {
		Editor editor = sp.edit();
		editor.putBoolean("lastthirdstate", b);
		editor.commit();
	}
}
