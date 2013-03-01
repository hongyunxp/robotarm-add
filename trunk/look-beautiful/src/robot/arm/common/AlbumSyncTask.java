/**
 * 
 */
package robot.arm.common;

import java.util.ArrayList;
import java.util.List;

import robot.arm.R;
import robot.arm.core.TabInvHandler;
import robot.arm.provider.LoaderPrivider;
import robot.arm.provider.asyc.EasyTask;
import robot.arm.utils.AppExit;
import robot.arm.utils.NetType;
import robot.arm.utils.NetUtils;
import robot.arm.utils.ViewUtils;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mokoclient.core.MokoClient;
import com.mokoclient.core.bean.PostDetailBean;

/**
 * @author li.li
 * 
 *         Apr 24, 2012
 * 
 *         专辑明细异步任务
 * 
 */
public class AlbumSyncTask extends EasyTask<BaseActivity, Void, Void, Void> {
	private List<String> list2 = new ArrayList<String>();

	private int curPage = 0;
	private ListView listView = caller.getImageListView();
	private View more = caller.getMore();
	protected TabInvHandler tabInvHandler = caller.getTabInvHandler();

	private MokoClient client;
	private AlbumAdapter adapter;

	private String detailUrl;// 专辑明细的url
	private String title;// 专辑的标题
	private int pageCount;// 页数
	private LoaderPrivider loader;

	public AlbumSyncTask(BaseActivity activity, MokoClient client) {
		super(activity);

		this.client = client;
		listView.addFooterView(more);
		adapter = new AlbumAdapter();

		Bundle bundle = caller.getIntent().getExtras();
		detailUrl = bundle.getString(caller.getString(R.string.detailUrl));// 读出数据
		title = bundle.getString(caller.getString(R.string.postTitle));

		loader = LoaderPrivider.newInstance(tabInvHandler);
		loader.show();
	}

	@Override
	public Void doInBackground(Void... params) {
		loadList(MokoClient.MODEL, ++curPage, list2);
		return null;
	}

	@Override
	public void onPostExecute(Void result) {
		try {

			updateView();

			caller.setInit(true);// 已初始化
		} finally {
			loader.hide();
		}
	}

	private void updateView() {
		if (list2.isEmpty())
			return;

		adapter.addList(caller, list2);

		if (listView.getAdapter() == null)
			listView.setAdapter(adapter);

		pagePrompt();

	}

	private void pagePrompt() {
		// 设置页数
		TextView tvPage = (TextView) caller.getTabInvHandler().getTabView().getTitle().findViewById(R.id.title_page);
		tvPage.setText(curPage + "/" + pageCount);
		// 设置标题
		TextView tvText = (TextView) caller.getTabInvHandler().getTabView().getTitle().findViewById(R.id.title_text_post);

		if (!String.valueOf(tvText.getText()).contains(title))
			tvText.append(" - " + title);
	}

	private void loadList(final MokoClient mClient, final int curPage, final List<String> list) {

		if (NetUtils.checkNet() == NetType.TYPE_NONE) {

			HANDLER.post(new Runnable() {

				@Override
				public void run() {
					ViewUtils.confirm(caller, "温馨提示", "网络不给力请重试", new OnClickListener() {

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
				}
			});

		} else {
			if (list != null) {
				list.clear();
				PostDetailBean result = Util.getPostDetail(client, detailUrl, curPage);
				if (result == null || result.getPostDetailList().size() == 0) {
					caller.hideMore();

				} else {
					pageCount = result.getPageCount();
					list.addAll(result.getPostDetailList());
				}
			}
		}

	}
}
