package com.eastedge.readnovel.threads;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import com.eastedge.readnovel.beans.Image;
import com.eastedge.readnovel.beans.Paihang;
import com.eastedge.readnovel.beans.PaihangMain;
import com.eastedge.readnovel.common.HCData;
import com.xs.cn.http.HttpImpl;

public class PaihangThread extends Thread {
	private Handler handler;
	private Context context;
	public ArrayList<Paihang> alp;
	private PaihangMain paihangMain;
	public ArrayList<Image> ali;
	private final List<ImageView> topImgs;

	public PaihangThread(Handler handler, Context context, List<ImageView> topImgs) {
		super();
		this.handler = handler;
		this.context = context;
		this.topImgs = topImgs;
	}

	@Override
	public void run() {
		paihangMain = HttpImpl.paihang();

		if (paihangMain == null) {
			handler.sendEmptyMessage(44);
			return;
		}

		HCData.paihangMain = paihangMain;

		alp = paihangMain.getPaihangList();

		ali = paihangMain.getImgList();

		handler.sendEmptyMessage(111);

	}

}
