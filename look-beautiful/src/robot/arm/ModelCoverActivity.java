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
public class ModelCoverActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.model_cover);

		initView();

		// 创建异步任务
		task = new CoverSyncTask(this, MokoClient.MODEL);
		
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

		title(R.layout.model_title);
		background(R.drawable.model);

	}

	public void more(View view) {

		// 执行
		task.execute();

	}

	public void clickImage(View view) {
		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());// 压入数据
		tabInvHandler.startSubActivity(R.id.tab_model, ModelContentActivity.class, mBundle);
	}
}
