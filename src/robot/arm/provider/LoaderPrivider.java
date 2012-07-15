/**
 * 
 */
package robot.arm.provider;

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
 * @author li.li
 * 
 * 加载背景
 * 
 *         May 25, 2012
 * 
 */
public class LoaderPrivider {
	private static volatile LoaderPrivider instance;
	private Activity parent;
	private View loading;

	private LoaderPrivider(Activity parent) {
		this.parent = parent;

		init();// 初始化
	}

	private void init() {
		final WindowManager vm = (WindowManager) parent.getSystemService(Context.WINDOW_SERVICE);

		loading = LayoutInflater.from(parent).inflate(R.layout.loading, null);
		loading.post(new Runnable() {

			@Override
			public void run() {
				// vm.addView(loading, new WindowManager.LayoutParams());
				Rect rect = new Rect();
				Display display = parent.getWindowManager().getDefaultDisplay();
				parent.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
				int disWidth = display.getWidth();
				int disHeight = display.getHeight();
				int statusBar = rect.top;
				RadioGroup tools = (RadioGroup) parent.findViewById(R.id.tab_indictor);
				int toolsBar = tools == null || tools.getVisibility() != View.VISIBLE ? 0 : tools.getHeight();
				int height = tools == null || tools.getVisibility() == View.GONE ? disHeight - statusBar : disHeight - statusBar - toolsBar;

				System.out.println(disWidth + "|" + disHeight + "|" + statusBar + "|" + toolsBar + "|" + height);
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams(disWidth, height, LayoutParams.FLAG_SHOW_WHEN_LOCKED,
						LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
				lp.gravity = Gravity.TOP;
				vm.addView(loading, lp);

			}
		});
	}

	public void show() {
		loading.setVisibility(View.VISIBLE);
	}

	public void hide() {
		loading.setVisibility(View.GONE);
	}

	public static LoaderPrivider newInstance(Activity parent) {

		instance = new LoaderPrivider(parent);

		return instance;
	}

}
