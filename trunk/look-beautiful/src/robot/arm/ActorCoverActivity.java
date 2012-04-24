/**
 * 
 */
package robot.arm;

import robot.arm.common.BaseActivity;
import robot.arm.common.BaseSyncTask;
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
		// 执行
		task.execute();
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
