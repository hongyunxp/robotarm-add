/**
 * 
 */
package robot.arm.common;

import java.util.List;

import robot.arm.R;
import robot.arm.core.TabInvHandler;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;

import com.mokoclient.core.bean.PostBean;

/**
 * @author li.li
 * 
 *         Apr 16, 2012
 * 
 */
public class BaseActivity extends Activity {
	protected static List<PostBean> list;

	protected static List<String> list2;

	protected static List<Integer> album;

	protected TabInvHandler tabInvHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tabInvHandler = ((TabInvHandler) getParent());
	}

	public void background(int resId) {
		TableLayout tl = (TableLayout) findViewById(R.id.images_content);
		tl.setBackgroundResource(resId);
	}

	public void title(int resId) {
		tabInvHandler.setTitle(resId);
	}

}
