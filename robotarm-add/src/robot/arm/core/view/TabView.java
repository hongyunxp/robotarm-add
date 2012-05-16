package robot.arm.core.view;

import robot.arm.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ViewAnimator;

/**
 * 
 * @author li.li
 * 
 *         Mar 15, 2012
 * 
 */
public class TabView extends RelativeLayout {

	private FrameLayout title;// 标题
	private ViewAnimator content;// 内容
	private TabBar tabBar;// 工具栏

	private SoftInputListener softInputListener;// 软键盘监听器
	private int maxHeight;
	private Animation inRightToLeft;
	private Animation outRightToLeft;

	public TabView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 初始化布局
		View title = LayoutInflater.from(context).inflate(R.layout.tab_title, this, false);
		View content = LayoutInflater.from(context).inflate(R.layout.tab_content, this, false);
		View tabBar = LayoutInflater.from(context).inflate(R.layout.tab_bar, this, false);

		inRightToLeft = AnimationUtils.loadAnimation((getContext()), R.anim.in_right_to_left);
		outRightToLeft = AnimationUtils.loadAnimation((getContext()), R.anim.out_right_to_left);
		
		addChildView(title);
		addChildView(content);
		addChildView(tabBar);

		Log.e("MyTabView", "" + title.getHeight() + "|" + content.getHeight() + "|" + title.getHeight() + content.getHeight());
	}

	public FrameLayout getTitle() {
		return title;
	}

	public ViewAnimator getContent() {
		return content;
	}

	public TabBar getTabBar() {
		return tabBar;
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

		Log.d(child.getTag().toString(), String.valueOf(getResources().getString(R.string.tab_bar_tag).equals(child.getTag())));
		addView(child);

		if (getResources().getString(R.string.tab_title_tag).equals(child.getTag()))
			title = (FrameLayout) child;
		if (getResources().getString(R.string.tab_content_tag).equals(child.getTag())) {
			content = (ViewAnimator) child;
			
			content.setInAnimation(inRightToLeft);
			content.setOutAnimation(outRightToLeft);
		}

		if (getResources().getString(R.string.tab_bar_tag).equals(child.getTag()))
			tabBar = (TabBar) child;
	}

	public void setSoftInputListener(SoftInputListener softInputListener) {
		this.softInputListener = softInputListener;
	}

}
