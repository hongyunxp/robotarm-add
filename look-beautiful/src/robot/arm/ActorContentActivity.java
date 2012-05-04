/**
 * 
 */
package robot.arm;

import robot.arm.common.AlbumSyncTask;
import robot.arm.common.BaseActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.mokoclient.core.MokoClient;
import com.waps.AdView;

/**
 * @author li.li
 * 
 *         Apr 18, 2012
 * 
 */
public class ActorContentActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.actor_content);
		
		//广告
		LinearLayout container =(LinearLayout)findViewById(R.id.AdLinearLayout); 
		new AdView(this,container).DisplayAd();
		
		initView();
		initListener();

		task = new AlbumSyncTask(this, MokoClient.ACTOR);

	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.actor_title);
		background(R.drawable.actor);
		
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
