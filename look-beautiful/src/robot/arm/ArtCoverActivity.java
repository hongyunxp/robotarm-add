/**
 * 
 */
package robot.arm;

import robot.arm.common.BaseActivity;
import robot.arm.common.CoverSyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.mokoclient.core.MokoClient;
import com.waps.AdView;

/**
 * @author li.li
 * 
 *         Apr 12, 2012
 * 
 */
public class ArtCoverActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.art_cover);
		
		//广告
		LinearLayout container =(LinearLayout)findViewById(R.id.AdLinearLayout); 
		new AdView(this,container).DisplayAd();
		
		initView();
		initListener();

		// 创建任务
		task = new CoverSyncTask(this, MokoClient.ARTS);
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

	public void clickImage(View view) {
		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());// 压入数据
		tabInvHandler.startSubActivity(R.id.tab_art, ArtContentActivity.class, mBundle);
	}

}
