package com.xs.cn.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.eastedge.readnovel.beans.NewBook;
import com.eastedge.readnovel.common.LocalStore;
import com.readnovel.base.common.NetType;
import com.readnovel.base.util.NetUtils;
import com.xs.cn.http.HttpImpl;

public class PreInstallService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		//初始化一批书
		if (!LocalStore.getPreInstall(this)) {
			new Thread() {

				@Override
				public void run() {
					NetType netType = NetUtils.checkNet(PreInstallService.this);

					if (!NetType.TYPE_NONE.equals(netType)) {
						//101总排行，102VIP订阅榜，103完结经典，104必读经典
						List<NewBook> newBooks = HttpImpl.paihangItemList("103");
						for (NewBook newBook : newBooks.subList(0, 10)) {
							//							CommonUtils.addBookToBF(PreInstallService.this, newBook.getArticleid(), newBook.getTitle(), newBook.getImgURL(), 1);
						}
						LocalStore.setPreInstall(PreInstallService.this, true);
					}

				}

			}.start();
		}

	}
}
