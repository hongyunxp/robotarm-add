package com.readnovel.book.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.mobclick.android.MobclickAgent;
import com.readnovel.book.base.common.Constants;


public abstract class ToolsActivity extends BaseActivity implements OnClickListener {
	protected int height;// 屏幕的高
	protected int wide;// 屏幕的宽
	protected DisplayMetrics displaysMetrics;// 分辨率
	protected PopupWindow popMenu;//底部菜单
	protected PopupWindow popSize, popBookTag, popMore;// 弹出浮窗,改变字大小，书签，更多(page界面用)
	protected Button bookTagBtn;// 章节,书签和更多按钮

	// 当前activity的content view
	protected abstract int getContentView();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getContentView());
		displaysMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
		wide = getWindowManager().getDefaultDisplay().getWidth(); // 获得屏宽
		height = getWindowManager().getDefaultDisplay().getHeight(); // 获得屏高
	}

	@Override
	protected void onStart() {
		super.onStart();
		hidePopMenu();
	}

	/**
	 * 点击章节列表
	 */
	public void goChapter(View view) {
		Intent intent = new Intent(this, ChapterListActivity.class);
		startActivity(intent);
	}

	/**
	 * 点击改变字大小
	 * @param view
	 */
	public void goResizeFont(View view) {
	if (popSize!=null) {
		popSize.dismiss();
	}else {
		
	}
	}
	/**
	 * 点击改变字大小
	 * @param view
	 */
	public void resumeBackground(View view) {
		Intent intent = new Intent(ToolsActivity.this, MoreforWebViewActivity.class);
		startActivity(intent);
	}
	/**
	 * 点击书签
	 */
	public void goBookTag(View view) {
		Intent intent = new Intent(this, BookTagActivity.class);
		startActivity(intent);
	}

	/**
	 * 点击更多
	 */
	public void goMore(View view) {

	}

	protected void hidePopMenu() {
		
		if (popMore != null) {
			popMore.dismiss();
		}
		if(popMenu != null){
			popMenu.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		int id = v.getId();
		if (id == R.id.more_ll) {
			//统计点击更多
			MobclickAgent.onEvent(this, Constants.UMENG_EVENT_CLICK_MORE);

			hidePopMenu();

		} else if (id == R.id.exit_ll) {
			close();
		} else if (id == R.id.pay_record_ll) {
			hidePopMenu();
		}
	}
}
