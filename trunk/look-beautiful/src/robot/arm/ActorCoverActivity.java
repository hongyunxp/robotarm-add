/**
 * 
 */
package robot.arm;

import java.util.Arrays;
import java.util.List;

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
public class ActorCoverActivity extends BaseActivity {
	private static final String url1 = "http://img1.moko.cc/users/6/1812/543827/post/00/img1_cover_5557714.jpg";
	private static final String url2 = "http://img1.moko.cc/users/15/4616/1384849/post/f1/img1_cover_5556929.jpg";
	private static String[] array={url1,url2};
	private static final List<String[]> list = Arrays.asList(array,array,array,array);

	private ListView imageListView;
	private AlbumCoverAdapter imageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.actor_cover);

		imageListView = (ListView) findViewById(R.id.images);
		imageAdapter = new AlbumCoverAdapter(this, list);//图片数组
		View more = LayoutInflater.from(this).inflate(R.layout.common_show_more, null);
		imageListView.addFooterView(more);
		imageListView.setAdapter(imageAdapter);

		Button b = (Button) more.findViewById(R.id.button_images_more);
		b.setBackgroundResource(R.drawable.actor);

		BaseUtils.setListViewHeight(imageListView);// 设置listview真实高度
	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.actor_title);
		background(R.drawable.actor);
	}

	public void more(View view) {
		imageAdapter.addList(list);// 增加元素
		imageAdapter.notifyDataSetChanged();// 通知更新视图
		view.post(new Runnable() {
			
			@Override
			public void run() {
				BaseUtils.setListViewHeight(imageListView);// 设置listview高度
				
			}
		});

	}

	public void clickImage(View view) {
		tabInvHandler.startSubActivity(R.id.tab_actor, ActorContentActivity.class);
	}

}
