package robot.arm;

import java.util.HashMap;
import java.util.Map;

import robot.arm.core.TabInvHandler;
import android.app.Activity;
import android.view.MenuItem;

public class MyTabActivity extends TabInvHandler {

	@Override
	public Map<Integer, Class<? extends Activity>> newTabs() {
		Map<Integer, Class<? extends Activity>> tabMap = new HashMap<Integer, Class<? extends Activity>>(5);
		
		tabMap.put(R.id.model, ModelActivity.class);
		tabMap.put(R.id.test2, ModelActivity.class);
		tabMap.put(R.id.test3, ModelActivity.class);
		tabMap.put(R.id.test4, ModelActivity.class);
		tabMap.put(R.id.test5, ModelActivity.class);
		
		return tabMap;
	}

	@Override
	public boolean optionsItemSelected(MenuItem item) {
		return false;
	}

}