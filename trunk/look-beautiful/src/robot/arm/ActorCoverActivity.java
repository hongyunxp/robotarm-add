/**
 * 
 */
package robot.arm;

import robot.arm.common.BaseActivity;
import robot.arm.common.BaseSyncTask;
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
public class ActorCoverActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actor_content);
		initView();

		// 创建异步任务
		task = new BaseSyncTask(this, MokoClient.ACTOR);
	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.actor_title);
		background(R.drawable.actor);

		// 执行异步任务
		task.execute();
	}

	public void more(View view) {

		curPage++;
		list = Util.getPostList(MokoClient.ACTOR, curPage);

		imageAdapter.addList(this, list);// 增加元素
		imageAdapter.notifyDataSetChanged();// 通知更新视图

		view.post(new Runnable() {
			@Override
			public void run() {
				BaseUtils.setListViewHeight(imageListView);// 设置listview高度
			}
		});

	}

	public void clickImage(View view) {
		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());// 压入数据
		tabInvHandler.startSubActivity(R.id.tab_actor, ActorContentActivity.class, mBundle);
	}

}
