package robot.arm.common;

import java.util.HashMap;
import java.util.Map;

import robot.arm.ActorActivity;
import robot.arm.ArtActivity;
import robot.arm.DesignActivity;
import robot.arm.ModelActivity;
import robot.arm.MovieActivity;
import robot.arm.R;
import robot.arm.R.id;
import robot.arm.core.TabInvHandler;
import android.app.Activity;
import android.view.MenuItem;

public class MyTabActivity extends TabInvHandler {

	@Override
	public Map<Integer, Class<? extends Activity>> newTabs() {
		Map<Integer, Class<? extends Activity>> tabMap = new HashMap<Integer, Class<? extends Activity>>(5);
		
		tabMap.put(R.id.model, ModelActivity.class);
		tabMap.put(R.id.test2, DesignActivity.class);
		tabMap.put(R.id.test3, ActorActivity.class);
		tabMap.put(R.id.test4, MovieActivity.class);
		tabMap.put(R.id.test5, ArtActivity.class);
		
		return tabMap;
	}

	@Override
	public boolean optionsItemSelected(MenuItem item) {
		return false;
	}

}