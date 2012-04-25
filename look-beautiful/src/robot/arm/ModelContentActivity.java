/**
 * 
 */
package robot.arm;

import robot.arm.common.AlbumSyncTask;
import robot.arm.common.BaseActivity;
import robot.arm.provider.view.MyScrollView.OnScrollListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mokoclient.core.MokoClient;

/**
 * @author li.li
 * 
 *         Apr 12, 2012
 * 
 */
public class ModelContentActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.model_content);

		initView();

		task = new AlbumSyncTask(this, MokoClient.MODEL);

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

		task.execute();

	}

	public void details(View view) {
		tabInvHandler.startSubActivity(R.id.tab_model, ModelContentActivity.class);
	}

	public void clickImage(View view) {
		Intent intent = new Intent(this, TouchImageViewActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());// 压入数据
		intent.putExtras(mBundle);
		startActivity(intent);
	}
}
