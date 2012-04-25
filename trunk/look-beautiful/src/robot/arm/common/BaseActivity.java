/**
 * 
 */
package robot.arm.common;

import java.util.ArrayList;
import java.util.List;

import robot.arm.R;
import robot.arm.core.TabInvHandler;
import robot.arm.provider.asyc.AsycTask;
import robot.arm.provider.view.MyScrollView;
import robot.arm.provider.view.MyScrollView.OnScrollListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.mokoclient.core.bean.PostBean;

/**
 * @author li.li
 * 
 *         Apr 16, 2012
 * 
 */
public class BaseActivity extends Activity {
	protected AsycTask<BaseActivity> task;
	protected int curPage = 0;
	protected List<PostBean> list = new ArrayList<PostBean>();
	protected View more;
	protected TextView moreButton;
	protected ListView imageListView;
	protected TabInvHandler tabInvHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tabInvHandler = ((TabInvHandler) getParent());
		// tabInvHandler.loading(getClass(), true);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void more(View view) {
		task.execute();

	}

	public void background(int resId) {
		TableLayout tl = (TableLayout) findViewById(R.id.images_content);
		tl.setBackgroundResource(resId);

		if (moreButton != null)
			moreButton.setBackgroundResource(resId);// 按钮背景
	}

	public void title(int resId) {
		tabInvHandler.setTitle(resId);
	}

	protected void initView() {
		imageListView = (ListView) findViewById(R.id.images);
		more = LayoutInflater.from(this).inflate(R.layout.common_show_more, null);
		moreButton = (TextView) more.findViewById(R.id.button_images_more);

	}

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		ScrollView content = tabInvHandler.getTabView().getContent();
		((MyScrollView) content).setOnScrollListener(onScrollListener);
	}

	public List<PostBean> getList() {
		return list;
	}

	public int getCurPage() {
		return curPage;
	}

	public ListView getImageListView() {
		return imageListView;
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
