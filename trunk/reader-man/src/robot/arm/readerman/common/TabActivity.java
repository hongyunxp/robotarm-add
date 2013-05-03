package robot.arm.readerman.common;

import java.util.HashMap;
import java.util.Map;

import robot.arm.R;
import robot.arm.core.TabInvHandler;
import robot.arm.readerman.CartoonActivity;
import robot.arm.readerman.JokeActivity;
import robot.arm.readerman.NewsActivity;
import robot.arm.readerman.NovelActivity;
import robot.arm.readerman.WelcomeActivity;
import android.app.Activity;
import android.view.MenuItem;

public class TabActivity extends TabInvHandler {
	private Map<Integer, Class<? extends Activity>> tabMap;

	@Override
	public Map<Integer, Class<? extends Activity>> newTabs() {
		if (tabMap == null) {
			tabMap = new HashMap<Integer, Class<? extends Activity>>();

			tabMap.put(R.id.tab_novel, NovelActivity.class);
			tabMap.put(R.id.tab_cartoon, CartoonActivity.class);
			tabMap.put(R.id.tab_joke, JokeActivity.class);
			tabMap.put(R.id.tab_news, NewsActivity.class);
		}

		return tabMap;
	}

	@Override
	public Class<? extends Activity> welcomeClazz() {
		return WelcomeActivity.class;
	}

	@Override
	public boolean optionsItemSelected(MenuItem item) {
		return false;
	}

}