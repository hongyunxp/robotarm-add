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
public class MusicContentActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_content);
		
		initView();
		initListener();

		task = new AlbumSyncTask(this, MokoClient.MUSIC);

	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.music_title);
		background(R.drawable.music);
		
		if (!isInit) {
			task.execute();// 执行任务
		}
	}

	public void clickImage(View view) {
		Intent intent = new Intent(this, TouchImageViewActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());// 压入数据
		intent.putExtras(mBundle);
		startActivity(intent);
	}
}
