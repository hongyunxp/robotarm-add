/**
 * 
 */
package robot.arm.common;

import robot.arm.R;
import robot.arm.TouchImageViewActivity;
import robot.arm.core.TabInvHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

/**
 * @author li.li
 * 
 *         Apr 16, 2012
 * 
 */
public class BaseActivity extends Activity {
	protected TabInvHandler tabInvHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tabInvHandler = ((TabInvHandler) getParent());
	}

	public void details(View view) {
		Intent intent = new Intent(this, TouchImageViewActivity.class);
		startActivity(intent);
	}

	public void background(int resId) {
		TableLayout tl = (TableLayout) findViewById(R.id.images_content);
		tl.setBackgroundResource(resId);
	}
	
	public void title(int resId){
		tabInvHandler.setTitle(resId);
	}
}
