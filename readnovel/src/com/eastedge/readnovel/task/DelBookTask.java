package com.eastedge.readnovel.task;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.beans.SynMyfavorResultBean;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.db.DBAdapter;
import com.eastedge.readnovel.db.LastReadTable;
import com.eastedge.readnovel.fragment.BookShelfFragment;
import com.readnovel.base.common.NetType;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.JsonUtils;
import com.readnovel.base.util.NetUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.http.HttpImpl;

public class DelBookTask extends EasyTask<BookShelfFragment, Void, Void, Boolean> {
	private ProgressDialog pd;

	private BFBook bfBook;

	public DelBookTask(BookShelfFragment caller, BFBook bfBook) {
		super(caller);

		this.bfBook = bfBook;
	}

	@Override
	public void onPreExecute() {
		//打加载对话框
		pd = ViewUtils.progressLoading(caller.getActivity(), "删除中,请稍后...");
	}

	@Override
	public void onPostExecute(Boolean result) {
		//关闭加载对话框
		pd.cancel();

		if (result) {
			// 刷新列表
			caller.getAdapt().notifyDataSetChanged();
			if (caller.getCurpop() != null && caller.getCurpop().isShowing())
				caller.getCurpop().dismiss();

			caller.setLastP(caller.getListView().getSelectedItemPosition());

		} else {
			// 刷新列表
			if (caller.getCurpop() != null && caller.getCurpop().isShowing())
				caller.getCurpop().dismiss();

			Toast.makeText(caller.getActivity(), "书架删除同步失败", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public Boolean doInBackground(Void... params) {

		try {
			if (BookApp.getUser() != null && BookApp.getUser().getUid() != null) {//已登陆用户
				if (NetUtils.checkNet().equals(NetType.TYPE_NONE)) {
					return false;//无网络
				}

				//与主站收藏同步(删除)
				JSONObject jo = HttpImpl.synMyfavor(BookApp.getUser().getUid(), BookApp.getUser().getToken(), "", bfBook.getArticleid());
				SynMyfavorResultBean smfResultBean = JsonUtils.fromJson(jo.toString(), SynMyfavorResultBean.class);

				if (smfResultBean != null && "1".equals(smfResultBean.getDel())) {//服务器删除成功
					delLocal();

					caller.getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(caller.getActivity(), "书架删除同步成功", Toast.LENGTH_SHORT).show();
						}
					});

					return true;
				} else {
					return false;
				}
			} else {//未登陆用户
				delLocal();

				caller.getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(caller.getActivity(), "书架删除成功", Toast.LENGTH_SHORT).show();
					}
				});

				return true;
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return false;
	}

	private void delLocal() {
		// 删除阅读记录表中的记录
		LastReadTable rd = new LastReadTable(caller.getActivity());
		rd.open();
		rd.remove(bfBook.getArticleid());
		rd.close();

		// 删除关系表中的 当前书的关系
		DBAdapter dbAdapter = new DBAdapter(caller.getActivity());
		dbAdapter.open();
		dbAdapter.deleteGxOne1(bfBook.getArticleid(), LocalStore.getLastUid(caller.getActivity()), caller.getIsvip());

		//删除书架的书
		dbAdapter.open();
		dbAdapter.deleteBookById(bfBook.getArticleid());
	}
}
