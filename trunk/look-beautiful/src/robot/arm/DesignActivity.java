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
public class DesignActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.design_content);
	}

	@Override
	protected void onResume() {
		super.onResume();
		title(R.layout.design_title);
		background(R.drawable.design);
	}
}
