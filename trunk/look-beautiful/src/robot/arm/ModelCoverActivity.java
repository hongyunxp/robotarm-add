/**
 * 
 */
package robot.arm;

import robot.arm.common.AlbumCoverAdapter;
import robot.arm.common.BaseActivity;
import robot.arm.common.Util;
import robot.arm.utils.BaseUtils;
import android.os.Bundle;
import android.view.View;

import com.mokoclient.core.MokoClient;

/**
 * @author li.li
 * 
 *         Apr 12, 2012
 * 
 */
public class ModelCoverActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.model_cover);

		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.model_title);
		background(R.drawable.model);

		// 创建并执行异步任务
		new Thread() {

			@Override
			public void run() {
				loadList(MokoClient.MODEL, curPage, list);

				if (list != null && list.size() > 0) {

					imageAdapter = new AlbumCoverAdapter(ModelCoverActivity.this, list);

					imageListView.post(new Runnable() {

						@Override
						public void run() {
							imageListView.addFooterView(more);
							imageListView.setAdapter(imageAdapter);
							moreButton.setBackgroundResource(R.drawable.model);

							BaseUtils.setListViewHeight(imageListView);// 设置listview真实高度
						}
					});
				}
			}
		}.start();

	}

	public void more(View view) {
		curPage++;
		list = Util.getPostList(MokoClient.MODEL, curPage);
		imageAdapter.addList(this, list);// 增加元素
		imageAdapter.notifyDataSetChanged();// 通知更新视图

		BaseUtils.setListViewHeight(imageListView);// 设置listview高度

	}

	public void clickImage(View view) {
		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());// 压入数据
		tabInvHandler.startSubActivity(R.id.tab_model, ModelContentActivity.class, mBundle);
	}
}
