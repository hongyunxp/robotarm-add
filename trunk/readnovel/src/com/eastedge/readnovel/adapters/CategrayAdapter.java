package com.eastedge.readnovel.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xs.cn.R;

public class CategrayAdapter extends BaseAdapter {
	private ArrayList<String> mDate;
	private Context mContext;
	public int selected;

	public CategrayAdapter(Context ctx, ArrayList<String> mDate, int i) {
		this.mDate = mDate;
		this.mContext = ctx;
		this.selected = i;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public void notify(int i) {
		this.selected = i;
	}

	@Override
	public int getCount() {
		return mDate.size();
	}

	@Override
	public Object getItem(int position) {
		return mDate.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View curView = null;
		ViewHolder holder = null;

		if (curView == null) {
			curView = LayoutInflater.from(mContext).inflate(
					R.layout.bookstore_category_item, null);
			holder = new ViewHolder();
			holder.cate = (TextView) curView
					.findViewById(R.id.bookstore_category_item_name);
			holder.llLayout = (LinearLayout) curView.findViewById(R.id.ll);
			holder.cate.setText(mDate.get(position));

			curView.setTag(holder);
		} else
			holder = (ViewHolder) curView.getTag();

		if (position == selected) {
			curView.setSelected(true);
			holder.llLayout.setEnabled(true);
		}

		return curView;
	}

	private static final class ViewHolder {
		TextView cate;
		LinearLayout llLayout;
	}
}
