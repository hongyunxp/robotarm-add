package com.xs.cn.http;

import org.json.JSONObject;

import android.app.Activity;
import android.widget.Toast;

import com.eastedge.readnovel.beans.Shubenmulu;
import com.eastedge.readnovel.beans.SynMyfavorResultBean;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.common.NetType;
import com.readnovel.base.util.JsonUtils;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.NetUtils;
import com.xs.cn.activitys.BookApp;

/**
 * 书下载
 * 
 * @author li.li
 */
public class DownFile {

	public String aid;
	public String filelocation;
	public int fileLen;
	public int downLen;
	public int isOK;
	public String title;
	public Shubenmulu mul;
	private Activity act;
	public String file_URL;
	public String mulu_URL;

	public DownFile(Activity act, String aid, String title) {
		this.act = act;
		this.aid = aid;
		this.title = title;
	}

	public DownFile(Activity act, String aid, String title, String file_url,
			String mulu_url) {
		this.act = act;
		this.aid = aid;
		this.title = title;
		this.file_URL = file_url;
		this.mulu_URL = mulu_url;
	}

	public void start() {
		// 检测当前SD卡空间是否够
		boolean canContinue = CommonUtils.sdCardCheck(act);
		if (!canContinue) {
			isOK = 1;
			return;
		}

		// 执行下载
		new Thread() {
			public void run() {
				doDown();
			};
		}.start();
	}

	public void doDown() {
		try {
			if (NetUtils.checkNet().equals(NetType.TYPE_NONE)) {
				act.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(act, "当前无网络，下载失败", Toast.LENGTH_SHORT)
								.show();
					}
				});

				return;
			}

			// 与主站收藏同步(添加)
			if (BookApp.getUser() != null && BookApp.getUser().getUid() != null) {// 已登陆用户
				if (NetUtils.checkNet().equals(NetType.TYPE_NONE)) {
					act.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(act, "加入书架同步失败", Toast.LENGTH_SHORT)
									.show();
						}
					});

					return;
				}

				JSONObject jo = HttpImpl.synMyfavor(BookApp.getUser().getUid(),
						BookApp.getUser().getToken(), aid, "");
				SynMyfavorResultBean smfResultBean = JsonUtils.fromJson(
						jo.toString(), SynMyfavorResultBean.class);

				if (smfResultBean != null && "1".equals(smfResultBean.getAdd())) {// 服务器同步成功
					act.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(act, "加入书架同步成功", Toast.LENGTH_SHORT)
									.show();
						}
					});

					// 下载目录
					mul = HttpImpl.ShubenmuluAll(aid);
					// 下载书
					HttpImpl.downBook(DownFile.this, mul);

					if (mul != null) {
						isOK = 1;
					} else {
						isOK = -1;
					}
				} else {
					act.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(act, "加入书架同步失败", Toast.LENGTH_SHORT)
									.show();
						}
					});
					return;
				}
			} else {// 未登陆用户
				// 下载目录
				mul = HttpImpl.ShubenmuluAll(aid);
				// 下载书
				HttpImpl.downBook(DownFile.this, mul);

				if (mul != null) {
					isOK = 1;
				} else {
					isOK = -1;
				}
			}

		} catch (Throwable e) {
			isOK = -1;
			LogUtils.error(e.getMessage(), e);
		} finally {
			Thread.interrupted();
		}
	}
}
