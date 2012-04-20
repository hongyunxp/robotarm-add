/**
 * 
 */
package robot.arm;

import java.util.List;

import com.mokoclient.core.MokoClient;

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
 *         Apr 18, 2012
 * 
 */
public class ActorContentActivity extends BaseActivity {
	private ListView imageListView;
	private AlbumAdapter imageAdapter;
	protected static List<String> list2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.actor_content);

		imageListView = (ListView) findViewById(R.id.images);
		
		Bundle bundle = getIntent().getExtras();    
	    String detailUrl=bundle.getString(getString(R.string.detailUrl));//读出数据  
	    
	    try {
			list2 = MokoClient.ACTOR.getPostDetail(detailUrl);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		imageAdapter = new AlbumAdapter(this, list2);
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
		imageAdapter.addList(list2);// 增加元素
		imageAdapter.notifyDataSetChanged();// 通知更新视图

		BaseUtils.setListViewHeight(imageListView);// 设置listview高度

	}
	
	public void clickImage(View view) {
		Intent intent = new Intent(this, TouchImageViewActivity.class);
		startActivity(intent);
	}
}
