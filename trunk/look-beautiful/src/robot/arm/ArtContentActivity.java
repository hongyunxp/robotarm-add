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
public class ArtContentActivity extends BaseActivity {
	private List<String> list2;
	private static int curPage = 1;
	private static String detailUrl;
	private ListView imageListView;
	private AlbumAdapter imageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.art_content);

		Bundle bundle = getIntent().getExtras();    
	    detailUrl=bundle.getString(getString(R.string.detailUrl));//读出数据  
	    
		list2 = Util.getPostDetail(MokoClient.ARTS, detailUrl, curPage);
	    
		imageListView = (ListView) findViewById(R.id.images);
		imageAdapter = new AlbumAdapter(this, list2);
		View more = LayoutInflater.from(this).inflate(R.layout.common_show_more, null);
		imageListView.addFooterView(more);
		imageListView.setAdapter(imageAdapter);

		Button b = (Button) more.findViewById(R.id.button_images_more);
		b.setBackgroundResource(R.drawable.art);

		BaseUtils.setListViewHeight(imageListView);// 设置listview真实高度
	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.art_title);
		background(R.drawable.art);
	}

	public void more(View view) {
		curPage ++;
		list2 = Util.getPostDetail(MokoClient.ARTS, detailUrl, curPage);
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
