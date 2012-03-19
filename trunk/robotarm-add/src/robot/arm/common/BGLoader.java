package robot.arm.common;

import robot.arm.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.RadioGroup;
/**
 * 
 * @author li.li
 *
 * Mar 15, 2012
 *
 */
public class BGLoader {
	private View loading;
	private volatile Class<? extends Activity> loadType;
	private int disWidth;// 屏幕宽度
	private int disHeight;// 屏幕高度
	private int statusBar;// 状态栏高度
	private int toolsBar;// 工具栏高度
	private RadioGroup tools;
	private WindowManager vm;

	private BGLoader(final Activity act) {
		this.vm = (WindowManager) act.getSystemService(Context.WINDOW_SERVICE);
		this.loading = LayoutInflater.from(act).inflate(R.layout.loading, null);

		loading.post(new Runnable() {// 初始化loading
					@Override
					public void run() {

						Rect rect = new Rect();
						// 背景高度
						Display display = act.getWindowManager().getDefaultDisplay();
						act.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
						disWidth = display.getWidth();
						disHeight = display.getHeight();
						statusBar = rect.top;
						tools = (RadioGroup) act.findViewById(R.id.tab_indictor);
						toolsBar = tools == null || tools.getVisibility() != View.VISIBLE ? 0 : tools.getHeight();

						vm.addView(loading, new WindowManager.LayoutParams());
					}

				});
	}

	/**
	 * 开始加载
	 */
	public void start(Class<? extends Activity> actClazz) {
		loading(actClazz, true);
	}

	/**
	 * 结束加载
	 */
	public void stop(Class<? extends Activity> actClazz) {
		loading(actClazz, false);
	}

	/**
	 * 还原加载
	 */
	public void restore(Class<? extends Activity> actClazz) {
		start(actClazz);
		stop(actClazz);
	}

	private void loading(final Class<? extends Activity> actClazz, final boolean visible) {

		loading.post(new Runnable() {
			@Override
			public void run() {
				// 重新计算布局
				int height = tools == null || tools.getVisibility() == View.GONE ? disHeight - statusBar : disHeight - statusBar - toolsBar;
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams(disWidth, height, LayoutParams.FLAG_SHOW_WHEN_LOCKED, LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
				lp.gravity = Gravity.TOP;
				vm.updateViewLayout(loading, lp);

				if (visible) {// 显示层
					loadType = actClazz;
					loading.setVisibility(View.VISIBLE);
				} else if (actClazz.equals(loadType)) // 隐藏层
					loading.setVisibility(View.GONE);
			}
		});
	}

	public View getLoading() {
		return loading;
	}

	public static BGLoader newInstance(Activity act) {
		return new BGLoader(act);
	}

}
