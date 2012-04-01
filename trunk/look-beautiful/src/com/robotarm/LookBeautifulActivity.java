package com.robotarm;

import java.util.HashMap;
import java.util.Map;

import robot.arm.core.TabInvHandler;
import android.app.Activity;
import android.view.MenuItem;

public class LookBeautifulActivity extends TabInvHandler {
	@Override
	public Map<Integer, Class<? extends Activity>> newTabs() {
		Map<Integer, Class<? extends Activity>> tabMap = new HashMap<Integer, Class<? extends Activity>>(5);

		tabMap.put(R.id.home, TestActivity.class);
		tabMap.put(R.id.model, TestActivity.class);
		tabMap.put(R.id.art, TestActivity.class);
		tabMap.put(R.id.stylist, TestActivity.class);
		tabMap.put(R.id.other, TestActivity.class);

		return tabMap;
	}

	@Override
	public boolean optionsItemSelected(MenuItem item) {
		return false;
	}

}