/**
 * 
 */
package robot.arm;

import robot.arm.common.BaseActivity;
import android.os.Bundle;

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
	}

	@Override
	protected void onResume() {
		super.onResume();
		tabInvHandler.setTitle(R.layout.model_title);
	}
}
