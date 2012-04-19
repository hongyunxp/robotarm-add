package robot.arm.common;

import java.util.HashMap;
import java.util.Map;

import robot.arm.ActorCoverActivity;
import robot.arm.ArtActivity;
import robot.arm.DesignActivity;
import robot.arm.ModelActivity;
import robot.arm.MovieActivity;
import robot.arm.R;
import robot.arm.core.TabInvHandler;
import robot.arm.utils.AppExit;
import android.app.Activity;
import android.view.MenuItem;

public class TabActivity extends TabInvHandler {

	@Override
	public Map<Integer, Class<? extends Activity>> newTabs() {
		Map<Integer, Class<? extends Activity>> tabMap = new HashMap<Integer, Class<? extends Activity>>(5);

		tabMap.put(R.id.model, ModelActivity.class);
		tabMap.put(R.id.test2, DesignActivity.class);
		tabMap.put(R.id.test3, ActorCoverActivity.class);
		tabMap.put(R.id.test4, MovieActivity.class);
		tabMap.put(R.id.test5, ArtActivity.class);

		return tabMap;
	}

	@Override
	public boolean optionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.main_menu_exit:
			AppExit.getInstance().exit(this);// 退出程序
			break;

		case R.id.main_menu_about:
			break;

		case R.id.main_menu_help:
			break;

		case R.id.main_menu_setup:
			break;

		default:
			break;
		}

		return true;
	}

}