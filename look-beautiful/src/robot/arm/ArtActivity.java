/**
 * 
 */
package robot.arm;

import java.util.Arrays;
import java.util.List;

import robot.arm.common.BaseActivity;
import robot.arm.utils.BaseUtils;
import android.os.Bundle;
import android.widget.ListView;

/**
 * @author li.li
 * 
 *         Apr 12, 2012
 * 
 */
public class ArtActivity extends BaseActivity {
	private static List<Integer> list = Arrays.asList(R.layout.images_content_row, R.layout.images_content_row, R.layout.images_content_row,
			R.layout.images_content_row);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.art_content);

		ListView lv = (ListView) findViewById(R.id.images);
		ImagesAdapter ia = new ImagesAdapter(this, list);
		lv.setAdapter(ia);
		BaseUtils.setListViewHeight(lv);// 设置listview真实高度
	}

	@Override
	protected void onResume() {
		super.onResume();

		title(R.layout.art_title);
		background(R.drawable.art);
	}

}
