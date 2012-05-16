package robot.arm.common;

import java.util.HashMap;
import java.util.Map;

import robot.arm.ActorCoverActivity;
import robot.arm.AdsCoverActivity;
import robot.arm.ArtCoverActivity;
import robot.arm.DesignCoverActivity;
import robot.arm.ModelCoverActivity;
import robot.arm.MoreCoverActivity;
import robot.arm.MovieCoverActivity;
import robot.arm.MusicCoverActivity;
import robot.arm.PhotographyCoverActivity;
import robot.arm.R;
import robot.arm.WelcomeActivity;
import robot.arm.core.TabInvHandler;
import robot.arm.utils.AppExit;
import android.app.Activity;
import android.view.MenuItem;

import com.waps.AppConnect;

public class TabActivity extends TabInvHandler {

	@Override
	public Map<Integer, Class<? extends Activity>> newTabs() {
		Map<Integer, Class<? extends Activity>> tabMap = new HashMap<Integer, Class<? extends Activity>>(5);

		tabMap.put(R.id.tab_photography, PhotographyCoverActivity.class);
		tabMap.put(R.id.tab_model, ModelCoverActivity.class);
		tabMap.put(R.id.tab_design, DesignCoverActivity.class);
		tabMap.put(R.id.tab_actor, ActorCoverActivity.class);
		tabMap.put(R.id.tab_music, MusicCoverActivity.class);
		tabMap.put(R.id.tab_movie, MovieCoverActivity.class);
		tabMap.put(R.id.tab_ads, AdsCoverActivity.class);
		tabMap.put(R.id.tab_art, ArtCoverActivity.class);
		tabMap.put(R.id.tab_more, MoreCoverActivity.class);

		return tabMap;
	}

	@Override
	public boolean optionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.main_menu_exit:
			//广告
			AppConnect.getInstance(this).finalize();
			AppExit.getInstance().exit(this);// 退出程序
			break;

		case R.id.main_menu_about:
			CommonUtils.dialogAbout(this);
			
			break;

		case R.id.main_menu_setup:
			CommonUtils.dialogFeedback(this);
			break;

		default:
			break;
		}

		return true;
	}

	@Override
	public Class<? extends Activity> welcomeClazz() {
		return WelcomeActivity.class;
//		 return null;
	}

}