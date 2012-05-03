/**
 * 
 */
package robot.arm;

import robot.arm.common.AlbumSyncTask;
import robot.arm.common.BaseActivity;
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
public class ArtContentActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.art_content);

		initView();
		initListener();

		task = new AlbumSyncTask(this, MokoClient.ACTOR);

	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.art_title);
		background(R.drawable.art);
		
		if (!isInit) {
			tabInvHandler.loading(getClass(), true);// 打开loading
			task.execute();// 执行任务
		}
	}

	public void more(View view) {
		task.execute();

	}

	public void clickImage(View view) {
		Intent intent = new Intent(this, TouchImageViewActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());// 压入数据
		intent.putExtras(mBundle);
		startActivity(intent);
	}

}
