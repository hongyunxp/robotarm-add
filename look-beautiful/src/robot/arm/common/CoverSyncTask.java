/**
 * 
 */
package robot.arm.common;

import java.util.ArrayList;
import java.util.List;

import robot.arm.common.bean.AlbumCover;
import robot.arm.core.TabInvHandler;
import robot.arm.provider.asyc.AsycTask;
import robot.arm.utils.AppExit;
import robot.arm.utils.NetUtils;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.ListView;

import com.mokoclient.core.MokoClient;
import com.mokoclient.core.bean.PostBean;

/**
 * @author li.li
 * 
 *         Apr 23, 2012
 * 
 */
public class CoverSyncTask extends AsycTask<BaseActivity> {

	// （接口穿透）初始化参数
	private List<PostBean> postBeanList = new ArrayList<PostBean>();
	private int curPage = 0;
	private ListView listView = act.getImageListView();
	private View more = act.getMore();
	private TabInvHandler tabInvHandler = act.getTabInvHandler();
	private volatile Builder builder;

	private MokoClient client;
	private AlbumCoverAdapter adapter;

	/**
	 * @param activity
	 */
	public CoverSyncTask(BaseActivity activity, MokoClient client) {
		super(activity);

		this.client = client;

		listView.addFooterView(more);
	}

	@Override
	public void doCall() {

		loadList(client, ++curPage, postBeanList, false);
	}

	@Override
	public void doResult() {

		updateView();
	}

	/**
	 * 更新视图
	 */
	private void updateView() {
		if (postBeanList.isEmpty())
			return;

		adapter = new AlbumCoverAdapter(AlbumCover.coverList(act, postBeanList));
		listView.setAdapter(adapter);
		adapter.notifyDataSetInvalidated();

		more.setVisibility(View.GONE);// 加载完成后不显示加载

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
				list.addAll(Util.getPostList(mClient, curPage));

				if (upView)
					updateView();// 更新视图
			}
		}

	}

}
