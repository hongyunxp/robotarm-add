/**
 * 
 */
package robot.arm.common;

import java.util.ArrayList;
import java.util.List;

import robot.arm.R;
import robot.arm.core.TabInvHandler;
import robot.arm.provider.asyc.AsycTask;
import robot.arm.utils.AppExit;
import robot.arm.utils.BaseUtils;
import robot.arm.utils.NetUtils;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.mokoclient.core.MokoClient;

/**
 * @author li.li
 * 
 *         Apr 24, 2012
 * 
 */
public class AlbumSyncTask extends AsycTask<BaseActivity> {
	private List<String> list2 = new ArrayList<String>();

	protected int curPage = act.getCurPage();
	private ListView listView = act.getImageListView();
	private View more = act.getMore();
	protected TabInvHandler tabInvHandler = act.getTabInvHandler();
	private volatile Builder builder;

	private MokoClient client;
	private AlbumAdapter adapter;

	private String detailUrl;
	private Handler handler = new Handler();

	/**
	 * @param activity
	 */
	public AlbumSyncTask(BaseActivity activity, MokoClient client) {
		super(activity);

		this.client = client;

		Bundle bundle = act.getIntent().getExtras();
		detailUrl = bundle.getString(act.getString(R.string.detailUrl));// 读出数据
	}

	@Override
	public void doCall() {

		loadList(MokoClient.MODEL, ++curPage, list2);

	}

	@Override
	public void doResult() {

		if (list2 != null && list2.size() > 0) {

			if (adapter == null)
				adapter = new AlbumAdapter();

			adapter.addList(act, list2);

			listView.post(new Runnable() {

				@Override
				public void run() {
					if (listView.getFooterViewsCount() == 0)
						listView.addFooterView(more);

					if (listView.getAdapter() == null)
						listView.setAdapter(adapter);

					BaseUtils.setListViewHeight(listView);// 设置listview真实高度

				}
			});
		}

	}

	private void loadList(final MokoClient mClient, final int curPage, final List<String> list) {

		if (!NetUtils.checkNet().available) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (builder == null) {
						builder = NetUtils.confirm(tabInvHandler, new OnClickListener() {

							@Override
							public void onClick(DialogInterface paramDialogInterface, int paramInt) {
								loadList(mClient, curPage, list);// 重试

							}

						}, new OnClickListener() {

							@Override
							public void onClick(DialogInterface paramDialogInterface, int paramInt) {
								AppExit.getInstance().exit(tabInvHandler);// 取消/退出
							}

						});
					} else {
						builder.show();
					}

				}
			});

		} else {
			if (list != null) {
				list.clear();
				list.addAll(Util.getPostDetail(client, detailUrl, curPage));
			}
		}

	}

}
