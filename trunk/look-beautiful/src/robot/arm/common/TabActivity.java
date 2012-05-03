package robot.arm.common;

import java.util.HashMap;
import java.util.Map;

import robot.arm.ActorCoverActivity;
import robot.arm.ArtCoverActivity;
import robot.arm.DesignCoverActivity;
import robot.arm.ModelCoverActivity;
import robot.arm.MovieCoverActivity;
import robot.arm.R;
import robot.arm.WelcomeActivity;
import robot.arm.core.TabInvHandler;
import robot.arm.utils.AppExit;
import android.app.Activity;
import android.view.MenuItem;

public class TabActivity extends TabInvHandler {

	@Override
	public Map<Integer, Class<? extends Activity>> newTabs() {
		Map<Integer, Class<? extends Activity>> tabMap = new HashMap<Integer, Class<? extends Activity>>(5);

		tabMap.put(R.id.tab_model, ModelCoverActivity.class);
		tabMap.put(R.id.tab_design, DesignCoverActivity.class);
		tabMap.put(R.id.tab_actor, ActorCoverActivity.class);
		tabMap.put(R.id.tab_movie, MovieCoverActivity.class);
		tabMap.put(R.id.tab_art, ArtCoverActivity.class);

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

	@Override
	public Class<? extends Activity> welcomeClazz() {
		return WelcomeActivity.class;
		// return null;
	}

}