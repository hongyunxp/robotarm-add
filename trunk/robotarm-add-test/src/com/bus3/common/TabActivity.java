/**
 * 
 */
package com.bus3.common;

import java.util.HashMap;
import java.util.Map;

import robot.arm.core.TabInvHandler;
import android.app.Activity;
import android.view.MenuItem;
import android.widget.Toast;

import com.bus3.R;
import com.bus3.test.CartActivity;
import com.bus3.test.HomeActivity;
import com.bus3.test.MoreActivity;
import com.bus3.test.SearchActivity;
import com.bus3.test.SortActivity;
import com.bus3.test.WelcomeActivity;

/**
 * @author li.li
 * 
 *         Mar 15, 2012
 * 
 */
public class TabActivity extends TabInvHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see robot.arm.core.Tabable#newTabs()
	 */
	@Override
	public Map<Integer, Class<? extends Activity>> newTabs() {
		Map<Integer, Class<? extends Activity>> tabMap = new HashMap<Integer, Class<? extends Activity>>(5);

		tabMap.put(R.id.main_tools_index, HomeActivity.class);
		tabMap.put(R.id.main_tools_sort, SortActivity.class);
		tabMap.put(R.id.main_tools_cart, CartActivity.class);
		tabMap.put(R.id.main_tools_search, SearchActivity.class);
		tabMap.put(R.id.main_tools_more, MoreActivity.class);

		return tabMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see robot.arm.core.Tabable#optionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean optionsItemSelected(MenuItem item) {

		// 处理options点击事件
		switch (item.getItemId()) {
		case R.id.menu1:
			Toast.makeText(this, "R.id.menu1|" + item.getTitle(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu2:
			Toast.makeText(this, "R.id.menu2|" + item.getTitle(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu3:
			Toast.makeText(this, "R.id.menu3|" + item.getTitle(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu4:
			Toast.makeText(this, "R.id.menu4|" + item.getTitle(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu5:
			Toast.makeText(this, "R.id.menu5|" + item.getTitle(), Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu6:
			Toast.makeText(this, "R.id.menu6|" + item.getTitle(), Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu7:
			Toast.makeText(this, "R.id.menu7|" + item.getTitle(), Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}

		return true;
	}

	@Override
	public Class<? extends Activity> welcomeClazz() {
		return WelcomeActivity.class;
	}

}
