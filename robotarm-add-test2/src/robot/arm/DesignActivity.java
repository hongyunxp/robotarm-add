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
public class DesignActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.design_content);
	}

	@Override
	protected void onResume() {
		super.onResume();
		((TabInvHandler) getParent()).setTitle(R.layout.design_title);
	}
}
