/**
 * 
 */
package robot.arm;

import robot.arm.common.BaseActivity;
import robot.arm.common.CoverSyncTask;
import android.os.Bundle;
import android.view.View;

import com.mokoclient.core.MokoClient;

/**
 * @author li.li
 * 
 *         Apr 12, 2012
 * 
 */
public class MoreCoverActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_cover);
		
		initView();
		initListener();

		// 创建任务
		task = new CoverSyncTask(this, MokoClient.MORE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.more_title);
		background(R.drawable.more);
		
		if (!isInit) {
			task.execute();// 执行任务
		}

	}

	public void clickImage(View view) {
		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());// 压入数据
		mBundle.putString(getString(R.string.postTitle), view.getTag(R.string.postTitle).toString());// 压入数据
		tabInvHandler.startSubActivity(R.id.tab_more, MoreContentActivity.class, mBundle);
	}

}
