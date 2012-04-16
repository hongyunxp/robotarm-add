package robot.arm.core;

import java.lang.annotation.Annotation;
import java.util.Stack;

import junit.framework.Assert;
import robot.arm.R;
import robot.arm.common.BGLoader;
import robot.arm.core.annotation.Resume;
import robot.arm.core.view.SoftInputListener;
import robot.arm.core.view.Tab;
import robot.arm.core.view.TabGroup;
import robot.arm.core.view.TabView;
import robot.arm.utils.AppExit;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
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
public abstract class TabInvHandler extends ActivityGroup implements Tabable, OnCheckedChangeListener, SoftInputListener {
	private static final int DEFAULT_SOFT_INPUT_MODE = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;// 默认软键盘

	private Stack<Record> statusStack;// 状态栈
	private TabView tabView;// 包含title、content、tabs
	private BGLoader loader;// 背景加载器
	private boolean checkLock;
	private boolean needCloseSoftInput;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab_view);// 加载视图

		statusStack = new Stack<Record>();
		tabView = initTabView(R.layout.tabs);
		checkLock = false;
		needCloseSoftInput = false;
		loader = BGLoader.newInstance(this);

		selectTab(tabView.getTabGroup().getChildAt(0).getId());// 默认选择第一个

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

			if (!statusStack.isEmpty() && statusStack.size() > 1) {

				Record record = getNextAvailableRecord();

				checkLock = true;// 锁
				tabView.getTabGroup().check(record.getId());
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
		Assert.assertNotNull(resouceId);

		tabView.getTitle().addView(LayoutInflater.from(this).inflate(resouceId, null));
	}

	/**
	 * 设置显示内容
	 */
	public void setContent(View view) {
		Assert.assertNotNull(view);

		tabView.getContent().addView(view);
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
		System.out.println("@@@@@@@@@@@@@@@@@@tabVisible" + "|" + visible);
		if (visible) {
			tabView.getTabGroup().setVisibility(View.VISIBLE);
		} else {
			tabView.getTabGroup().setVisibility(View.GONE);
		}

	}

	public void needCloseSoftInput(boolean visible) {
		needCloseSoftInput = visible;
	}

	/**
	 * 工具栏是否可见
	 */
	public boolean tabVisible() {

		return tabView.getTabGroup().getVisibility() == View.VISIBLE ? true : false;
	}

	/**
	 * 清除工具栏选择
	 */
	public void tabClear() {
		tabView.getTabGroup().clearCheck();
	}

	/**
	 * 切换到指定的项
	 */
	public void selectTab(int tabId) {
		tabView.getTabGroup().check(tabId);
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
			intent.getExtras().putAll(map);

		newActivity(id, intent, toActClazz);

		statusStack.push(new Record(id, intent, resumable, toActClazz));// 入栈

	}

	private void newActivity(final int id, final Intent intent, final Class<? extends Activity> toActClazz) {

		// 关闭上次的背景loading,多执行也无害
		if (View.VISIBLE == loader.getLoading().getVisibility()) {
			restoreLoading(toActClazz);// 还原loading
		}

		tabView.getTitle().removeAllViews();
		tabView.getContent().removeAllViews();
		getWindow().setSoftInputMode(DEFAULT_SOFT_INPUT_MODE);// 默认soft_input_mode

		setContent(getLocalActivityManager().startActivity(String.valueOf(id), intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)).getDecorView());

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

	public void loading(Class<? extends Activity> actClazz, boolean visible) {

		if (visible) {
			loader.start(actClazz);
		} else {
			loader.stop(actClazz);
		}

	}

	/**
	 * 还原loading层
	 */
	public void restoreLoading(Class<? extends Activity> actClazz) {
		loader.restore(actClazz);
	}

	private TabView initTabView(int tabs) {
		tabView = (TabView) findViewById(R.id.tab_view);

		TabGroup tabGroup = (TabGroup) LayoutInflater.from(this).inflate(tabs, tabView, false);
		initTabGroup(tabGroup);// 初始化tabs

		tabView.addChildView(tabGroup);// 创建tool,并将tools工具栏加入容器中
		tabView.setSoftInputListener(this);// 键盘监听器

		// bind tabView child event
		for (int i = 0; i < tabView.getTabGroup().getChildCount(); i++) {
			((RadioButton) tabView.getTabGroup().getChildAt(i)).setOnCheckedChangeListener(this);
		}

		return tabView;

	}

	private void initTabGroup(TabGroup tabGroup) {
		// 初始化tabs
		for (int i = 0; i < tabGroup.getChildCount(); i++) {

			Tab child = (Tab) tabGroup.getChildAt(i);

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

}