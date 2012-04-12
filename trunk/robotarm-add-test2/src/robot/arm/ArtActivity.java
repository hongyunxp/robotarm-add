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
public class ArtActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.art_content);
	}

	@Override
	protected void onResume() {
		super.onResume();
		((TabInvHandler) getParent()).setTitle(R.layout.art_title);
	}
}
