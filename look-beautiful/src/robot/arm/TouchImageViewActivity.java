/**
 * 
 */
package robot.arm;

import robot.arm.core.TabInvHandler;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * @author li.li
 * 
 *         Apr 16, 2012
 * 
 */
public class TouchImageViewActivity extends Activity {
	Handler handler=new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.touch_image_content);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		((TabInvHandler) getParent()).titleVisible(false);
		((TabInvHandler) getParent()).tabVisible(false);
		((TabInvHandler) getParent()).needCloseSoftInput(true);
	}
}
