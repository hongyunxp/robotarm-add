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
import robot.arm.utils.AppExit;
import robot.arm.utils.NetUtils;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;

import com.mokoclient.core.MokoClient;
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
	protected Button moreButton;
	protected ListView imageListView;
	protected TabInvHandler tabInvHandler;
	private volatile Builder builder;

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
		moreButton = (Button) more.findViewById(R.id.button_images_more);

	}

	/**
	 * 当网络不可用返回空
	 * 
	 * @param mClient
	 * @param curPage
	 * @return
	 */
	protected void loadList(final MokoClient mClient, final int curPage, final List<PostBean> list) {

		if (!NetUtils.checkNet().available) {
			if (builder == null) {
				builder = NetUtils.confirm(tabInvHandler, new OnClickListener() {

					@Override
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						loadList(mClient, curPage, list);// 重试

					}

				}, new OnClickListener() {

					@Override
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						AppExit.getInstance().exit(tabInvHandler);// 取消/退出
					}

				});
			} else {
				builder.show();
			}

		} else {
			if (list != null) {
				list.addAll(Util.getPostList(mClient, curPage));
			}
		}

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

	public Button getMoreButton() {
		return moreButton;
	}

	public TabInvHandler getTabInvHandler() {
		return tabInvHandler;
	}

	public Builder getBuilder() {
		return builder;
	}

}
