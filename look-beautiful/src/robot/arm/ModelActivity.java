/**
 * 
 */
package robot.arm;

import robot.arm.common.BaseActivity;
import robot.arm.common.ImagesAdapter;
import robot.arm.utils.BaseUtils;
import android.os.Bundle;
import android.widget.ListView;

/**
 * @author li.li
 * 
 *         Apr 12, 2012
 * 
 */
public class ModelActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.model_content);

		ListView lv = (ListView) findViewById(R.id.images);
		ImagesAdapter ia = new ImagesAdapter(this, list);
		lv.setAdapter(ia);
		BaseUtils.setListViewHeight(lv);// 设置listview真实高度
	}

	@Override
	protected void onResume() {
		super.onResume();
		title(R.layout.model_title);
		background(R.drawable.model);
	}
}
