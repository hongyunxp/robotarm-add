/**
 * 
 */
package robot.arm;

import robot.arm.common.BaseActivity;
import robot.arm.common.ImagesAdapter;
import robot.arm.utils.BaseUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

/**
 * @author li.li
 * 
 *         Apr 12, 2012
 * 
 */
public class MovieActivity extends BaseActivity {
	private ListView imageListView;
	private ImagesAdapter imageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		imageListView = (ListView) findViewById(R.id.images);
		imageAdapter = new ImagesAdapter(this, list);
		View more = LayoutInflater.from(this).inflate(R.layout.images_show_more, null);
		imageListView.addFooterView(more);
		imageListView.setAdapter(imageAdapter);

		BaseUtils.setListViewHeight(imageListView);// 设置listview真实高度
	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.actor_title);
		background(R.drawable.movie);
	}

	public void more(View view) {
		imageAdapter.addList(list);//增加元素
		imageAdapter.notifyDataSetChanged();//通知更新视图
		
		BaseUtils.setListViewHeight(imageListView);//设置listview高度

	}
}
