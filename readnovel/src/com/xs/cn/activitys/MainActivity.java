package com.xs.cn.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.actionbarsherlock.app.ActionBar;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.fragment.BookShelfFragment;
import com.eastedge.readnovel.fragment.MainGroupFragment;
import com.eastedge.readnovel.fragment.MainSlidingMenu;
import com.eastedge.readnovel.fragment.PersonCenterFragment2;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.util.PhoneUtils;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.xs.cn.R;

/**
 * 主菜单切换
 * 
 * @author li.li
 * 
 *         Aug 27, 2013
 */
public class MainActivity extends SlidingFragmentActivity implements
		OnCheckedChangeListener, OnOpenedListener {
	private RadioGroup tabs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
		setTheme(R.style.Theme_Sherlock);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.hide();

		setContentView(R.layout.main);// 主页面布局
		setBehindContentView(R.layout.main_sliding_menu);// 菜单抽屉布局
		CloseActivity.add(this);
		initView();// 初始化View
		initTabs();// 初始化标签
		initDrawerMenu();// 初始化抽屉
	}

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
	protected void onDestroy() {
		CloseActivity.remove(this);
		super.onDestroy();
	}

	/**
	 * 切换选项
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.main_bookshelf:
			if (isChecked) {
				doOnCheckedChanged(new BookShelfFragment());
				buttonView.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.main_navigation_bookshelf_pressed, 0, 0);
			} else
				buttonView.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.main_navigation_bookshelf, 0, 0);
			break;
		case R.id.main_bookcity:
			if (isChecked) {
				doOnCheckedChanged(new MainGroupFragment());
				buttonView.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.main_navigation_bookcity_pressed, 0, 0);
			} else
				buttonView.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.main_navigation_bookcity, 0, 0);
			break;
		case R.id.main_usercenter:

			if (isChecked) {
				doOnCheckedChanged(new PersonCenterFragment2());
				buttonView.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.main_navigation_usercenter_pressed, 0, 0);
			} else
				buttonView.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.main_navigation_usercenter, 0, 0);
			break;
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_UP) {
			if (getSlidingMenu().isMenuShowing())
				getSlidingMenu().showContent();
			else
				BookApp.exitApp(this);
		}
		return true;
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		tabs = (RadioGroup) findViewById(R.id.main_tools);
	}

	/**
	 * 初始化选择卡
	 */
	private void initTabs() {
		for (int i = 0; i < tabs.getChildCount(); i++) {
			((RadioButton) tabs.getChildAt(i)).setOnCheckedChangeListener(this);
		}
		int id = getIntent().getIntExtra("id", 0);
		if (R.id.main_bookcity == id)
			goToBookCity();
		else if (R.id.main_usercenter == id)
			goToUserCenter();
		else
			goToBookShelf();
	}

	/**
	 * 初始化抽屉
	 */
	private void initDrawerMenu() {
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffset((int) (PhoneUtils.getScreenPix(this)[0] * 0.15));
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setOnOpenedListener(this);
	}

	/**
	 * 选择标签
	 * 
	 * @param fragment
	 */
	private void doOnCheckedChanged(Fragment fragment) {
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right)//
				.replace(R.id.main_content, fragment)//
				.commit();
	}

	public void goToBookCity() {
		tabs.getChildAt(1).performClick();
	}

	public void goToUserCenter() {
		tabs.getChildAt(2).performClick();
	}

	public void goToBookShelf() {
		tabs.getChildAt(0).performClick();
	}

	/**
	 * 打开导航抽屉
	 */
	public void openSlidingMenu(View view) {
		toggle();
	}

	@Override
	public void onOpened() {
		loadSecondData();
	}

	public void retry(View view) {
		if (getSlidingMenu().isMenuShowing()) {
			loadSecondData();
		} else {
			if (R.id.main_usercenter == tabs.getCheckedRadioButtonId())
				doOnCheckedChanged(new PersonCenterFragment2());
		}
	}

	private void loadSecondData() {
		getSupportFragmentManager()//
				.beginTransaction()//
				.replace(R.id.main_sliding_menu_content, new MainSlidingMenu())//
				.commit();
	}
}
