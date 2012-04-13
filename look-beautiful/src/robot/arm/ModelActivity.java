/**
 * 
 */
package robot.arm;

import robot.arm.core.TabInvHandler;
import android.app.Activity;
import android.os.Bundle;

/**
 * @author li.li
 * 
 *         Apr 12, 2012
 * 
 */
public class ModelActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.model_content);
	}

	@Override
	protected void onResume() {
		super.onResume();
		((TabInvHandler) getParent()).setTitle(R.layout.model_title);
	}
}
