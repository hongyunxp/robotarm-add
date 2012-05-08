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
public class ModelCoverActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.model_cover);
		
		initView();
		initListener();

		// 创建任务
		task = new CoverSyncTask(this, MokoClient.MODEL);

	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.model_title);
		background(R.drawable.model);

		if (!isInit) {
			tabInvHandler.loading(getClass(), true);// 打开loading
			task.execute();// 执行任务
		}

	}

	public void clickImage(View view) {

		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.detailUrl), String.valueOf(view.getTag(R.string.detailUrl)));
		mBundle.putString(getString(R.string.postTitle), String.valueOf(view.getTag(R.string.postTitle)));
		
		tabInvHandler.startSubActivity(R.id.tab_model, ModelContentActivity.class, mBundle);
	}

}
