/**
 * 
 */
package robot.arm.readerman.common;

import robot.arm.core.TabInvHandler;
import android.app.Activity;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

/**
 * @author li.li
 * 
 *         Apr 16, 2012
 * 
 */
public abstract class BaseActivity extends Activity {
	private TabInvHandler tabInvHandler;// 父级,当不存在时为空

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();

	}

	private void init() {
		tabInvHandler = (TabInvHandler) getParent();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	public TabInvHandler getTabInvHandler() {
		return tabInvHandler;
	}

}
