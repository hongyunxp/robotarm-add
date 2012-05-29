/**
 * 
 */
package robot.arm.common;

import java.util.ArrayList;
import java.util.List;

import robot.arm.R;
import robot.arm.core.TabInvHandler;
import robot.arm.provider.LoaderPrivider;
import robot.arm.provider.asyc.AsycTask;
import robot.arm.utils.AppExit;
import robot.arm.utils.NetUtils;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mokoclient.core.MokoClient;
import com.mokoclient.core.bean.PostBean;

/**
 * @author li.li
 * 
 *         Apr 23, 2012
 * 
 */
public class CoverSyncTask extends AsycTask<BaseActivity> {

	// 初始化参数
	private List<PostBean> postBeanList = new ArrayList<PostBean>();
	private ListView listView = act.getImageListView();
	private View more = act.getMore();
	private TabInvHandler tabInvHandler = act.getTabInvHandler();
	private LoaderPrivider loader;

	private int curPage = 0;
	private MokoClient client;
	private AlbumCoverAdapter adapter;
	private volatile Builder builder;

	/**
	 * @param activity
	 */
	public CoverSyncTask(BaseActivity activity, MokoClient client) {
		super(activity);

		this.client = client;

		listView.addFooterView(more);
		adapter = new AlbumCoverAdapter();
		loader = LoaderPrivider.newInstance(tabInvHandler);
		loader.show();
	}

	@Override
	public void doCall() {

		loadList(client, ++curPage, postBeanList, false);

	}

	@Override
	public void doResult() {

		try {

			updateView();

			act.setInit(true);// 已初始化
		} finally {
			loader.hide();
		}

	}

	/**
	 * 更新视图
	 */
	private void updateView() {
		if (postBeanList.isEmpty())
			return;

		adapter.addList(act, postBeanList);

		if (listView.getAdapter() == null)
			listView.setAdapter(adapter);

		pagePrompt();

	}

	private void pagePrompt() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				TextView tv = (TextView) act.getTabInvHandler().getTabView().getTitle().findViewById(R.id.title_page);
				tv.setText(curPage + "/" + Util.PAGE_COUNT);
			}
		});
	}

	/**
	 * 加载列表
	 * 
	 * @param mClient
	 *            加载类型
	 * @param curPage
	 *            当前页
	 * @param list
	 *            加载的列表list
	 * @param upView
	 *            加载后是否更新视图
	 */
	private void loadList(final MokoClient mClient, final int curPage, final List<PostBean> list, final boolean upView) {

		if (!NetUtils.checkNet().available) {

			handler.post(new Runnable() {

				@Override
				public void run() {

					if (builder == null) {
						builder = NetUtils.confirm(tabInvHandler, new OnClickListener() {

							@Override
							public void onClick(DialogInterface paramDialogInterface, int paramInt) {
								loadList(mClient, curPage, list, true);// 重试
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
				list.clear();// 清空
				List<PostBean> result = Util.getPostList(mClient, curPage);
				if (result != null && result.size() > 0) {
					list.addAll(result);
					if (upView)
						updateView();// 更新视图
				} else if (result == null || result.isEmpty()) {

					act.hideMore();

				}
			}
		}

	}

}
