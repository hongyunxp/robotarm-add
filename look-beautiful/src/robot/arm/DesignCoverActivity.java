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
public class DesignCoverActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.design_cover);

		initView();

		// 创建异步任务
		task = new BaseSyncTask(this, MokoClient.DESIGN);
		// 执行
		task.execute();
	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.design_title);
		background(R.drawable.design);

	}

	public void clickImage(View view) {
		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());// 压入数据
		tabInvHandler.startSubActivity(R.id.tab_design, DesignContentActivity.class, mBundle);
	}

}
