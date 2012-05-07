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
import robot.arm.utils.NetUtils;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mokoclient.core.MokoClient;

/**
 * @author li.li
 * 
 *         Apr 24, 2012
 * 
 */
public class AlbumSyncTask extends AsycTask<BaseActivity> {
	private List<String> list2 = new ArrayList<String>();

	private int curPage = 0;
	private ListView listView = act.getImageListView();
	private View more = act.getMore();
	protected TabInvHandler tabInvHandler = act.getTabInvHandler();
	private volatile Builder builder;

	private MokoClient client;
	private AlbumAdapter adapter;

	private String detailUrl;

	/**
	 * @param activity
	 */
	public AlbumSyncTask(BaseActivity activity, MokoClient client) {
		super(activity);

		this.client = client;
		listView.addFooterView(more);
		adapter = new AlbumAdapter();

		Bundle bundle = act.getIntent().getExtras();
		detailUrl = bundle.getString(act.getString(R.string.detailUrl));// 读出数据
	}

	@Override
	public void doCall() {

		loadList(MokoClient.MODEL, ++curPage, list2);

	}

	@Override
	public void doResult() {

		try {

			updateView();

			act.setInit(true);// 已初始化
		} finally {

			tabInvHandler.loading(act.getClass(), false);// 多执行无害
		}

	}

	private void updateView() {
		if (list2.isEmpty())
			return;

		adapter.addList(act, list2);

		if (listView.getAdapter() == null)
			listView.setAdapter(adapter);

		more.setVisibility(View.GONE);// 加载完成后不显示加载

		pagePrompt();

	}

	private void pagePrompt() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				TextView tv = (TextView) act.getTabInvHandler().getTabView().getTitle().findViewById(R.id.title_page);
				tv.setText(curPage + "/" + "x");
			}
		});
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
				List<String> result = Util.getPostDetail(client, detailUrl, curPage);
				if (result == null)
					listView.removeFooterView(more);
				else {
					list.addAll(result);
				}
			}
		}

	}

}
