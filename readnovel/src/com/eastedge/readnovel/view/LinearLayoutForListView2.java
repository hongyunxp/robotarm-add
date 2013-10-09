package com.eastedge.readnovel.view;

import com.eastedge.readnovel.adapters.AdapterForLinearLayout2;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class LinearLayoutForListView2 extends LinearLayout
{

	private AdapterForLinearLayout2 adapter;
	private OnClickListener onClickListener = null;
	private View foodview;

	/**
	 * 绑定布局
	 */
	public void bindLinearLayout()
	{
		int count = adapter.getCount();
		for (int i = 0; i < count; i++)
		{

			View v = adapter.getView(i, null, null);

			v.setOnClickListener(this.onClickListener);
			// if (i == count - 1) {
			// LinearLayout ly = (LinearLayout) v;
			// ly.removeViewAt(2);
			// }
			addView(v, i);
		}
		if (foodview != null)
		{
			addView(foodview, count + 1);
		}
		Log.v("countTAG", "" + count);
	}

	public LinearLayoutForListView2(Context context)
	{
		super(context);

	}

	public LinearLayoutForListView2(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub

	}

	/**
	 * 获取Adapter
	 * 
	 * @return adapter
	 */
	public AdapterForLinearLayout2 getAdpater()
	{
		return adapter;
	}

	/**
	 * 设置数据
	 * 
	 * @param adapter2
	 */
	public void setAdapter(AdapterForLinearLayout2 adapter2)
	{
		this.adapter = adapter2;
		bindLinearLayout();
	}

	/**
	 * 获取点击事件
	 * 
	 * @return
	 */
	public OnClickListener getOnclickListner()
	{
		return onClickListener;
	}

	/**
	 * 设置点击事件
	 * 
	 * @param onClickListener
	 */
	public void setOnclickLinstener(OnClickListener onClickListener)
	{
		this.onClickListener = onClickListener;
	}

	public void addFooterView(View view)
	{
		foodview = view;
	}

}