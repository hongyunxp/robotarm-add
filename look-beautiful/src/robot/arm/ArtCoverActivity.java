/**
 * 
 */
package robot.arm;

import java.util.ArrayList;
import java.util.List;

import robot.arm.common.AlbumCoverAdapter;
import robot.arm.common.BaseActivity;
import robot.arm.common.Util;
import robot.arm.utils.BaseUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.mokoclient.core.MokoClient;
import com.mokoclient.core.bean.PostBean;

/**
 * @author li.li
 * 
 *         Apr 12, 2012
 * 
 */
public class ArtCoverActivity extends BaseActivity {
	private List<PostBean> list;
	private static int curPage = 1;
	private ListView imageListView;
	private AlbumCoverAdapter imageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.art_cover);
		list = new ArrayList<PostBean>();

	}

	@Override
	protected void onResume() {
		super.onResume();
		loadList(MokoClient.ARTS, curPage, list);

		if (list != null && list.size() > 0) {

			imageListView = (ListView) findViewById(R.id.images);
			imageAdapter = new AlbumCoverAdapter(this, list);
			View more = LayoutInflater.from(this).inflate(R.layout.common_show_more, null);
			imageListView.addFooterView(more);
			imageListView.setAdapter(imageAdapter);

			Button b = (Button) more.findViewById(R.id.button_images_more);
			b.setBackgroundResource(R.drawable.art);

			BaseUtils.setListViewHeight(imageListView);// 设置listview真实高度
		}

		title(R.layout.art_title);
		background(R.drawable.art);
	}

	public void more(View view) {

		curPage++;
		list = Util.getPostList(MokoClient.ARTS, curPage);

		imageAdapter.addList(this, list);// 增加元素
		imageAdapter.notifyDataSetChanged();// 通知更新视图

		BaseUtils.setListViewHeight(imageListView);// 设置listview高度

	}

	public void clickImage(View view) {
		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());// 压入数据
		tabInvHandler.startSubActivity(R.id.tab_art, ArtContentActivity.class, mBundle);
	}

}
