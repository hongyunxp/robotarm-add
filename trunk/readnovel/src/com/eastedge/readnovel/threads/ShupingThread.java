package com.eastedge.readnovel.threads;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.eastedge.readnovel.beans.Shupinginfo;
import com.xs.cn.http.HttpImpl;

public class ShupingThread extends Thread {
	public Handler mhandler;
	public Context ctx;
	public String id;
	public int page;
	public Shupinginfo spif;
	public Button loadMoreBtn;

	public ShupingThread(Context ctx, Handler mhandler, String id, int page, Button loadMoreBtn) {
		this.mhandler = mhandler;
		this.ctx = ctx;
		this.id = id;
		this.page = page;
		this.loadMoreBtn = loadMoreBtn;
	}

	public void run() {
		spif = HttpImpl.Shupinginfo(id, page);
		if (spif == null) {
			return;
		}
		if (page == 1) {
			mhandler.sendEmptyMessage(1);
		} else {
			mhandler.sendEmptyMessage(2);
		}

		//到最后一页不显示“更多评论按钮”
		if (loadMoreBtn != null) {
			int totalPageNumber = spif.getTotal_page_number();//总共页数
			if (totalPageNumber == page) {
				loadMoreBtn.setVisibility(View.GONE);
			}
		}
	}
}
