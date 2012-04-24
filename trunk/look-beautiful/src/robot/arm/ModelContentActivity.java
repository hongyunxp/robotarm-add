/**
 * 
 */
package robot.arm;

import java.util.List;

import robot.arm.common.AlbumAdapter;
import robot.arm.common.BaseActivity;
import robot.arm.common.Util;
import robot.arm.utils.BaseUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.mokoclient.core.MokoClient;

/**
 * @author li.li
 * 
 *         Apr 12, 2012
 * 
 */
public class ModelContentActivity extends BaseActivity {
	private List<String> list2;
	private String detailUrl;
	private ListView imageListView;
	private AlbumAdapter imageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.model_content);

		initView();

		Bundle bundle = getIntent().getExtras();
		detailUrl = bundle.getString(getString(R.string.detailUrl));// 读出数据

		list2 = Util.getPostDetail(MokoClient.MODEL, detailUrl, ++curPage);
		imageAdapter = new AlbumAdapter(this, list2);
		imageListView.addFooterView(more);
		imageListView.setAdapter(imageAdapter);

		BaseUtils.setListViewHeight(imageListView);// 设置listview真实高度

	}

	protected void initView() {
		imageListView = (ListView) findViewById(R.id.images);
		more = LayoutInflater.from(this).inflate(R.layout.common_show_more, null);
		moreButton = (Button) more.findViewById(R.id.button_images_more);

	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.model_title);
		background(R.drawable.model);
	}

	public void more(View view) {
		curPage++;
		list2 = Util.getPostDetail(MokoClient.MODEL, detailUrl, curPage);
		imageAdapter.addList(this, list2);// 增加元素
		imageAdapter.notifyDataSetChanged();// 通知更新视图

		BaseUtils.setListViewHeight(imageListView);// 设置listview高度

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
