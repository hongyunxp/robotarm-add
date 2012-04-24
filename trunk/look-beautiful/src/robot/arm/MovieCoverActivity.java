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
public class MovieCoverActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_content);

		initView();

		// 创建异步任务
		task = new CoverSyncTask(this, MokoClient.MOVIES);
		// 执行
		task.execute();
	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.movie_title);
		background(R.drawable.movie);

	}

	public void clickImage(View view) {
		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());// 压入数据
		tabInvHandler.startSubActivity(R.id.tab_movie, MovieContentActivity.class, mBundle);
	}

}
