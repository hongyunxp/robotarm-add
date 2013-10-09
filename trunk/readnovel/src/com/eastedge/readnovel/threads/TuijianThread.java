package com.eastedge.readnovel.threads;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.eastedge.readnovel.beans.Image;
import com.eastedge.readnovel.beans.Tuijian;
import com.eastedge.readnovel.beans.TuijianMain;
import com.eastedge.readnovel.common.HCData;
import com.xs.cn.http.HttpImpl;

public class TuijianThread extends Thread {
	private Handler handler;
	private Context context;
	public static final String TAG = "TuijianThread";
	public ArrayList<Tuijian> al;
	public ArrayList<Image> imgList;
	public int sortid;
	public int page;

	public TuijianThread(Handler handler, Context context, int sortid, int page) {
		this.handler = handler;
		this.context = context;
		this.sortid = sortid;
		this.page = page;
	}

	@Override
	public void run() {
		Log.i("smqhe", " " + page);
		TuijianMain mTuijianMain = HttpImpl.tuijian(sortid, page);

		if (mTuijianMain == null) {
			handler.sendEmptyMessage(44);
			return;
		}
		// 进行添加数据操作,判断是否是第一次
		if (page > 1) {
			if (mTuijianMain.getTuijianList().size() < 20) {
				HCData.mTuijianMain.getImgList().addAll(
						mTuijianMain.getImgList());
				HCData.mTuijianMain.getTuijianList().addAll(
						mTuijianMain.getTuijianList());
				al = HCData.mTuijianMain.getTuijianList();
				imgList = HCData.mTuijianMain.getImgList();
				handler.sendEmptyMessage(102);
			} else {
				HCData.mTuijianMain.getImgList().addAll(
						mTuijianMain.getImgList());
				HCData.mTuijianMain.getTuijianList().addAll(
						mTuijianMain.getTuijianList());
				al = HCData.mTuijianMain.getTuijianList();
				imgList = HCData.mTuijianMain.getImgList();
				handler.sendEmptyMessage(101);
			}
		} else {
			HCData.mTuijianMain = mTuijianMain;
			al = mTuijianMain.getTuijianList();
			imgList = mTuijianMain.getImgList();
			handler.sendEmptyMessage(1);
		}
		// al = mTuijianMain.getTuijianList();
		// imgList = mTuijianMain.getImgList();
		// handler.sendEmptyMessage(1);

	}
}
