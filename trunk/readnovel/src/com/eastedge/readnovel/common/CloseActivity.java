package com.eastedge.readnovel.common;

import java.util.LinkedList;

import android.app.Activity;

import com.xs.cn.activitys.BookApp;
import com.xs.cn.activitys.ReadbookDown;

public class CloseActivity {
	private static LinkedList<Activity> acts = new LinkedList<Activity>();

	public static Activity curActivity;

	public static void add(Activity acy) {
		acts.add(acy);
	}

	public static void remove(Activity acy) {
		acts.remove(acy);
	}

	public static void closeRd() {
		for (int i = acts.size() - 1; i >= 0; i--) {
			if (acts.get(i).getClass() == ReadbookDown.class) {
				if (!acts.get(i).isFinishing()) {
					acts.get(i).finish();
				}
			}
		}
	}

	public static void close() {
		BookApp.isInit = false;

		// 关闭所有的Activity
		for (Activity act : acts) {
			if (!act.isFinishing()) {
				act.finish();
			}
		}
		//		// 关闭图片线程池
		//		LoadImgProvider.shutdown();

		// 关闭当前进程
		// android.os.Process.killProcess(android.os.Process.myPid());
	}
}
