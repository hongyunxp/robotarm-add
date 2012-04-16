/**
 * 
 */
package robot.arm;

import robot.arm.core.TabInvHandler;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * @author li.li
 * 
 *         Apr 12, 2012
 * 
 */
public class ActorActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.actor_content);
	}

	@Override
	protected void onResume() {
		super.onResume();
		((TabInvHandler) getParent()).setTitle(R.layout.actor_title);
	}

	public void details(View view) {
		((TabInvHandler) getParent()).startSubActivity(R.id.tab_actor, TouchImageViewActivity.class);
	}
}
