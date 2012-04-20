/**
 * 
 */
package robot.arm;

import com.mokoclient.core.MokoClient;

import robot.arm.common.AlbumCoverAdapter;
import robot.arm.common.BaseActivity;
import robot.arm.utils.BaseUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/**
 * @author li.li
 * 
 *         Apr 12, 2012
 * 
 */
public class ModelCoverActivity extends BaseActivity {
	private ListView imageListView;
	private AlbumCoverAdapter imageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.model_cover);

		try {
			list = MokoClient.MODEL.getPostList(1);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		imageListView = (ListView) findViewById(R.id.images);
		imageAdapter = new AlbumCoverAdapter(this, list);
		View more = LayoutInflater.from(this).inflate(R.layout.common_show_more, null);
		imageListView.addFooterView(more);
		imageListView.setAdapter(imageAdapter);

		Button b = (Button) more.findViewById(R.id.button_images_more);
		b.setBackgroundResource(R.drawable.model);

		BaseUtils.setListViewHeight(imageListView);// 设置listview真实高度
	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.model_title);
		background(R.drawable.model);
	}

	public void more(View view) {
		imageAdapter.addList(this, list);// 增加元素
		imageAdapter.notifyDataSetChanged();// 通知更新视图

		BaseUtils.setListViewHeight(imageListView);// 设置listview高度

	}

	public void clickImage(View view) {
		Bundle mBundle = new Bundle();
        mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());//压入数据  
		tabInvHandler.startSubActivity(R.id.tab_model, ModelContentActivity.class, mBundle);
	}
}
