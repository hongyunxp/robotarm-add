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
import android.widget.Button;
import android.widget.ListView;

/**
 * @author li.li
 * 
 *         Apr 12, 2012
 * 
 */
public class DesignActivity extends BaseActivity {
	private ListView imageListView;
	private ImagesAdapter imageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.design_content);

		imageListView = (ListView) findViewById(R.id.images);
		imageAdapter = new ImagesAdapter(this, list);
		View more = LayoutInflater.from(this).inflate(R.layout.images_show_more, null);
		imageListView.addFooterView(more);
		imageListView.setAdapter(imageAdapter);

		Button b = (Button) more.findViewById(R.id.button_images_more);
		b.setBackgroundResource(R.drawable.design);

		BaseUtils.setListViewHeight(imageListView);// 设置listview真实高度
	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.design_title);
		background(R.drawable.design);
	}

	public void more(View view) {
		imageAdapter.addList(list);// 增加元素
		imageAdapter.notifyDataSetChanged();// 通知更新视图

		BaseUtils.setListViewHeight(imageListView);// 设置listview高度

	}
}
