/**
 * 
 */
package robot.arm.common;

import java.util.List;

import robot.arm.R;
import robot.arm.core.TabInvHandler;
import robot.arm.utils.AppExit;
import robot.arm.utils.NetUtils;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
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

	protected TabInvHandler tabInvHandler;
	private volatile Builder builder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tabInvHandler = ((TabInvHandler) getParent());
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void background(int resId) {
		TableLayout tl = (TableLayout) findViewById(R.id.images_content);
		tl.setBackgroundResource(resId);
	}

	public void title(int resId) {
		tabInvHandler.setTitle(resId);
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
						AppExit.getInstance().exit(tabInvHandler);// 取消退出
					}

				});
			} else {
				builder.show();
			}

		} else {
			if (list != null)
				list.addAll(Util.getPostList(mClient, curPage));
		}

	}

}
