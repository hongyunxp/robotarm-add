package robot.arm.core;

import java.lang.annotation.Annotation;
import java.util.Stack;

import junit.framework.Assert;
import robot.arm.R;
import robot.arm.core.annotation.Resume;
import robot.arm.core.view.SoftInputListener;
import robot.arm.core.view.Tab;
import robot.arm.core.view.TabGroup;
import robot.arm.core.view.TabView;
import robot.arm.utils.AppExit;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

/**
 * 
 * TAB框架
 * 
 * @author li.li
 * 
 */
public abstract class TabInvHandler extends ActivityGroup implements Tabable, Welable, OnCheckedChangeListener, SoftInputListener {
	private static final String TAG = TabInvHandler.class.getName();
	private static final int DEFAULT_SELECT_TAB = 0;
	private static final int DEFAULT_SOFT_INPUT_MODE = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;// 默认软键盘
	public static final int REQUEST_IF_OK = 0;

	private Stack<Record> statusStack;// 状态栈
	private TabView tabView;// 包含title、content、tabs
	private boolean checkLock;
	private boolean needCloseSoftInput;
	private LocalActivityManager activityManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab_view);// 加载视图

		activityManager = getLocalActivityManager();

		statusStack = new Stack<Record>();
		tabView = initTabView(R.layout.tabs);
		checkLock = false;
		needCloseSoftInput = false;

		goWelcome();// 去欢迎界面
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {// 回调
		Log.d(TAG, "onActivityResult|" + requestCode + "|" + resultCode);

		if (requestCode == REQUEST_IF_OK && resultCode == RESULT_OK)
			selectTab();// 默认选择第一个
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

			if (!statusStack.isEmpty() && statusStack.size() > 1) {

				Record record = getNextAvailableRecord();

				checkLock = true;// 锁
				tabView.getTabBar().getTabScroll().getTabGroup().check(record.getId());
				tabVisible(true);
				newActivity(record.getId(), record.getIntent(), record.getActClazz());
				checkLock = false;// 恢复

			} else {

				AppExit.getInstance().exit(this);// 退出程序
			}
		} else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {// 弹出菜单

			openOptionsMenu();
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCheckedChanged(CompoundButton button, boolean isChecked) {
		Assert.assertTrue("button或button.getTag()不能为空", button != null && button.getTag() != null);

		if (!isChecked || checkLock)
			return;

		if (button.getTag() instanceof Class<?>)
			startSubActivity(button.getId(), (Class<? extends Activity>) button.getTag());
		else
			new AssertionError("Activity非法：应该为Class<? extends Activity>类型的子Activity|" + button.getTag().getClass().getName());

	}

	/**
	 * 设置标题
	 */
	public void setTitle(int resouceId) {

		tabView.getTitle().setTitle(resouceId);

	}

	public View getAd() {

		return tabView.getTitle().getAd();

	}

	/**
	 * 设置显示内容
	 */
	public void setContent(final View child) {

		tabView.getContent().simpleShow(child);
		// tabView.getContent().animShow(child);
	}

	public void titleVisible(boolean visible) {
		Assert.assertNotNull(visible);

		if (visible) {
			tabView.getTitle().setVisibility(View.VISIBLE);
		} else {
			tabView.getTitle().setVisibility(View.GONE);
		}

	}

	/**
	 * 设置工具栏是否可见
	 */
	public void tabVisible(boolean visible) {
		Assert.assertNotNull(visible);
		if (visible) {
			tabView.getTabBar().setVisibility(View.VISIBLE);
		} else {
			tabView.getTabBar().setVisibility(View.GONE);
		}

	}

	public void needCloseSoftInput(boolean visible) {
		needCloseSoftInput = visible;
	}

	/**
	 * 工具栏是否可见
	 */
	public boolean tabVisible() {

		return tabView.getTabBar().getVisibility() == View.VISIBLE ? true : false;
	}

	/**
	 * 清除工具栏选择
	 */
	public void tabClear() {
		tabView.getTabBar().getTabScroll().getTabGroup().clearCheck();
	}

	/**
	 * 切换到指定的项
	 */
	public void selectTab(int tabId) {
		tabView.getTabBar().getTabScroll().getTabGroup().check(tabId);
	}

	/**
	 * 新启动子activity
	 */
	public void startSubActivity(int id, Class<? extends Activity> toActClazz) {
		startSubActivity(id, toActClazz, null);
	}

	public void startSubActivity(int id, Class<? extends Activity> toActClazz, Bundle map) {
		boolean resumable = resumable(toActClazz);

		Intent intent = new Intent(this, toActClazz);

		if (map != null && !map.isEmpty())
			intent.putExtras(map);

		newActivity(id, intent, toActClazz);

		statusStack.push(new Record(id, intent, resumable, toActClazz));// 入栈

	}

	@Override
	public void isGone() {
		if (!tabVisible() && !needCloseSoftInput) {
			tabVisible(true);
		}
	}

	@Override
	public void isShow() {
		if (tabVisible()) {
			tabVisible(false);
		}
	}

	// /**
	// * 更新message数量
	// */
	// public void message(final int count) {
	// // 提示信息
	// TextView tv = (TextView) findViewById(R.id.tab_message);
	// tv.setText(String.valueOf(count));
	// tv.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
	//
	// // 定位提示信息到合适位置
	// RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
	// tv.getLayoutParams();
	// params.setMargins(BaseUtils.getScreen(this).widthPixels / 2, 0, 0, 0);
	//
	// }

	private TabView initTabView(int tabs) {
		tabView = (TabView) findViewById(R.id.tab_view);

		TabGroup tabGroup = (TabGroup) LayoutInflater.from(this).inflate(tabs, tabView.getTabBar().getTabScroll(), false);
		initTabGroup(tabGroup);// 初始化tabs

		tabView.getTabBar().getTabScroll().addChildView(tabGroup);// 创建tool,并将tools工具栏加入容器中
		tabView.setSoftInputListener(this);// 键盘监听器

		// bind tabView child event
		for (int i = 0; i < tabView.getTabBar().getTabScroll().getTabGroup().getChildCount(); i++) {
			((RadioButton) tabView.getTabBar().getTabScroll().getTabGroup().getChildAt(i)).setOnCheckedChangeListener(this);
		}

		return tabView;

	}

	private void initTabGroup(TabGroup tabGroup) {
		// 窗口的宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		// 初始化tabs
		for (int i = 0; i < tabGroup.getChildCount(); i++) {

			Tab child = (Tab) tabGroup.getChildAt(i);
			// 设置每个选项卡的宽度
			child.setWidth(screenWidth / 5);
			child.setTag(newTabs().get(child.getId()));

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.menu, menu);

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		return optionsItemSelected(item);

	}

	private void newActivity(final int id, final Intent intent, final Class<? extends Activity> toActClazz) {

		tabView.getTitle().removeAllViews();
		getWindow().setSoftInputMode(DEFAULT_SOFT_INPUT_MODE);// 默认soft_input_mode

		// activityManager.removeAllActivities();// 销毁activitys

		Window window = activityManager.startActivity(String.valueOf(id), intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
		View view = window.getDecorView();

		setContent(view);

	}

	private Record getNextAvailableRecord() {
		Record record = null;
		do {
			statusStack.pop();// 出栈
			record = statusStack.peek();// 读取栈顶元素

		} while (!record.isResumable() || statusStack.isEmpty());

		return record;
	}

	private boolean resumable(Class<? extends Activity> toActClazz) {
		for (Annotation ant : toActClazz.getAnnotations()) {

			if (ant instanceof Resume)
				return ((Resume) ant).resumable();
		}

		return true;
	}

	private void selectTab() {
		selectTab(tabView.getTabBar().getTabScroll().getTabGroup().getChildAt(DEFAULT_SELECT_TAB).getId());// 默认选择第一个
	}

	private void goWelcome() {
		Class<? extends Activity> clazz = welcomeClazz();

		if (clazz != null) {

			Intent intent = new Intent(this, clazz);
			startActivityForResult(intent, REQUEST_IF_OK);

		} else {// 没有欢迎界面直接选择默认tab
			selectTab();
		}
	}

	public TabView getTabView() {
		return tabView;
	}

}