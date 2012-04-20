/**
 * 
 */
package robot.arm;

import robot.arm.common.AlbumAdapter;
import robot.arm.common.BaseActivity;
import robot.arm.utils.BaseUtils;
import android.content.Intent;
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
public class MovieContentActivity extends BaseActivity {
	private ListView imageListView;
	private AlbumAdapter imageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_content);

		imageListView = (ListView) findViewById(R.id.images);
		imageAdapter = new AlbumAdapter(this, list2);
		View more = LayoutInflater.from(this).inflate(R.layout.common_show_more, null);
		imageListView.addFooterView(more);
		imageListView.setAdapter(imageAdapter);

		Button b = (Button) more.findViewById(R.id.button_images_more);
		b.setBackgroundResource(R.drawable.movie);

		BaseUtils.setListViewHeight(imageListView);// 设置listview真实高度
	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.movie_title);
		background(R.drawable.movie);
	}

	public void more(View view) {
		imageAdapter.addList(list2);// 增加元素
		imageAdapter.notifyDataSetChanged();// 通知更新视图

		BaseUtils.setListViewHeight(imageListView);// 设置listview高度

	}

	public void clickImage(View view) {
		Intent intent = new Intent(this, TouchImageViewActivity.class);
		Bundle mBundle = new Bundle();  
        mBundle.putString(getString(R.string.detailUrl), view.getTag(R.string.detailUrl).toString());//压入数据  
        intent.putExtras(mBundle);
		startActivity(intent);
	}
}
