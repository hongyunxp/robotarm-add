/**
 * 
 */
package robot.arm;

import robot.arm.common.BaseActivity;
import robot.arm.common.CoverSyncTask;
import robot.arm.provider.view.MyScrollView.OnScrollListener;
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
		task = new CoverSyncTask(this, MokoClient.ACTOR);
		// 执行
		task.execute();
		
		setOnScrollListener(new OnScrollListener() {

			@Override
			public void onBottom() {
				task.execute();//执行显示更多
			}

			@Override
			public void onTop() {
			}

			@Override
			public void onScroll() {
			}

		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.actor_title);
		background(R.drawable.actor);

	}

	public void more(View view) {
		task.execute();

	}

	public void clickImage(View view) {
		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());// 压入数据
		tabInvHandler.startSubActivity(R.id.tab_actor, ActorContentActivity.class, mBundle);
	}

}
