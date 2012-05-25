/**
 * 
 */
package robot.arm.common;

import robot.arm.R;
import robot.arm.core.TabInvHandler;
import robot.arm.provider.asyc.AsycTask;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.waps.AdView;

/**
 * @author li.li
 * 
 *         Apr 16, 2012
 * 
 */
public abstract class BaseActivity extends Activity {
	private final String TAG = getClass().getName();
	private static final int MORE_LOADING_DELAY = 500;

	protected AsycTask<BaseActivity> task;
	protected ViewGroup more;
	protected TextView moreButton;
	protected ListView listView;
	protected TabInvHandler tabInvHandler;
	protected Handler handler = new Handler();
	protected boolean isInit = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");

		tabInvHandler = ((TabInvHandler) getParent());

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");

		ad();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");

	}

	public void background(int resId) {
		TableLayout tl = (TableLayout) findViewById(R.id.images_content);
		tl.setBackgroundResource(resId);

		if (moreButton != null)
			moreButton.setBackgroundResource(resId);// 按钮背景
	}

	protected void initView() {
		listView = (ListView) findViewById(R.id.images);
		more = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.common_show_more, null);
		moreButton = (TextView) more.findViewById(R.id.button_images_more);

	}

	protected void initListener() {

		/**
		 * 监听listview滚到最底部(最后一个元素)
		 * 
		 * 当不滚动时判断滚动到底部
		 */
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					Log.d(TAG, "已经停止：SCROLL_STATE_IDLE");

					showMore(view);

					break;

				case OnScrollListener.SCROLL_STATE_FLING:
					Log.d(TAG, "正在滚动(非触摸)：SCROLL_STATE_FLING");

					break;

				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					Log.d(TAG, "正在滚动(触摸)：SCROLL_STATE_TOUCH_SCROLL");

					break;

				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}

		});
	}

	private void showMore(AbsListView view) {
		if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
			listView.setSelection(view.getLastVisiblePosition());// 滚动到底

			if (more.getVisibility() == View.VISIBLE)
				return;

			more.setVisibility(View.VISIBLE);
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// 执行
					task.execute();
				}
			}, MORE_LOADING_DELAY);
		}
	}

	// 广告
	protected void ad() {

		View adLayout = tabInvHandler.getAd();
		new AdView(this, (LinearLayout) adLayout).DisplayAd();
	}

	public boolean isInit() {
		return isInit;
	}

	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}

	public void title(int resId) {
		tabInvHandler.setTitle(resId);
	}

	public ListView getImageListView() {
		return listView;
	}

	public View getMore() {
		return more;
	}

	public TextView getMoreButton() {
		return moreButton;
	}

	public TabInvHandler getTabInvHandler() {
		return tabInvHandler;
	}

}
