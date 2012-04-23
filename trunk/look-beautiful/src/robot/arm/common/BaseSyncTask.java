/**
 * 
 */
package robot.arm.common;

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
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.mokoclient.core.MokoClient;
import com.mokoclient.core.bean.PostBean;

/**
 * @author li.li
 * 
 *         Apr 23, 2012
 * 
 */
public class BaseSyncTask extends AsycTask<BaseActivity> {
	
	// （接口穿透）初始化参数
	private List<PostBean> postBeanList = act.getList();
	private int curPage = act.getCurPage();
	private AlbumCoverAdapter adapter = act.getImageAdapter();
	private ListView listView = act.getImageListView();
	private View more = act.getMore();
	private Button moreButton = act.getMoreButton();
	private TabInvHandler tabInvHandler = act.getTabInvHandler();
	private volatile Builder builder = act.getBuilder();

	private MokoClient client;

	/**
	 * @param activity
	 */
	public BaseSyncTask(BaseActivity activity, MokoClient client) {
		super(activity);

		this.client = client;
	}

	@Override
	public void doCall() {
		loadList(client, curPage, postBeanList);
	}

	@Override
	public void doResult() {
		if (postBeanList != null && postBeanList.size() > 0) {

			adapter = new AlbumCoverAdapter(act, postBeanList);

			listView.post(new Runnable() {

				@Override
				public void run() {
					listView.addFooterView(more);
					listView.setAdapter(adapter);
					moreButton.setBackgroundResource(R.drawable.model);

					BaseUtils.setListViewHeight(listView);// 设置listview真实高度
					tabInvHandler.loading(act.getClass(), false);
				}
			});
		}

	}

	/**
	 * 当网络不可用返回空
	 * 
	 * @param mClient
	 * @param curPage
	 * @return
	 */
	protected void loadList(final MokoClient mClient, final int curPage, final List<PostBean> list) {

		if (!NetUtils.checkNet().available) {
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

		} else {
			if (list != null) {
				list.addAll(Util.getPostList(mClient, curPage));
			}
		}

	}

}
