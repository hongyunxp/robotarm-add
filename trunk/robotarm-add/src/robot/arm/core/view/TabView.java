package robot.arm.core.view;

import robot.arm.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * 
 * @author li.li
 * 
 *         Mar 15, 2012
 * 
 */
public class TabView extends RelativeLayout {

	private FrameLayout title;// 标题
	private ScrollView content;// 内容
	private TabGroup tabGroup;// 工具栏
	private SoftInputListener softInputListener;// 软键盘监听器
	private int maxHeight;

	public TabView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 初始化布局
		View title = LayoutInflater.from(context).inflate(R.layout.tab_title, this, false);
		View content = LayoutInflater.from(context).inflate(R.layout.tab_content, this, false);

		addChildView(title);
		addChildView(content);

		Log.e("MyTabView", "" + title.getHeight() + "|" + content.getHeight() + "|" + title.getHeight() + content.getHeight());
	}

	public FrameLayout getTitle() {
		return title;
	}

	public ScrollView getContent() {
		return content;
	}

	public TabGroup getTabGroup() {
		return tabGroup;
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		post(new Runnable() {

			@Override
			public void run() {
				maxHeight = Math.max(maxHeight, heightMeasureSpec);

				if (softInputListener != null)
					if (heightMeasureSpec == maxHeight) // 已关闭输入法
						softInputListener.isGone();
					else if (heightMeasureSpec < maxHeight) // 已打开输入法
						softInputListener.isShow();

			}
		});

	}

	public void addChildView(View child) {

		addView(child);

		if (getResources().getString(R.string.tab_title_tag).equals(child.getTag()))
			title = (FrameLayout) child;
		if (getResources().getString(R.string.tab_content_tag).equals(child.getTag()))
			content = (ScrollView) child;
		if (getResources().getString(R.string.tab_group_tag).equals(child.getTag()))
			tabGroup = (TabGroup) child;
	}

	public void setSoftInputListener(SoftInputListener softInputListener) {
		this.softInputListener = softInputListener;
	}

}
