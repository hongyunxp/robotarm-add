package com.eastedge.readnovel.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eastedge.readnovel.beans.Paihang;
import com.eastedge.readnovel.common.Constants;
import com.readnovel.base.cache.Cache;
import com.readnovel.base.cache.view.ViewCacheProvider;
import com.readnovel.base.http.LoadImgProvider;
import com.xs.cn.R;

public class PaihangAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Paihang> mDate;
	// 视图缓存
	private final Cache<View> VIEW_CACHE = ViewCacheProvider.getInstance();

	String id;
	String topTitle;

	public PaihangAdapter(Context mContext, ArrayList<Paihang> mDate) {
		super();
		this.mContext = mContext;
		this.mDate = mDate;
	}

	public int getCount() {
		return mDate.size();
	}

	public Object getItem(int position) {
		return mDate.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		String key = getClass().getName() + "|" + position;
		View curView = null;
		ViewHolder holder = null;

		// 先从缓存中取
		curView = VIEW_CACHE.get(key, null);

		// 缓存里没有创建新的
		if (curView == null) {
			curView = LayoutInflater.from(mContext).inflate(R.layout.menu_paihang_item, null);
			holder = new ViewHolder();
			holder.logo = (ImageView) curView.findViewById(R.id.paihang_list_img);
			holder.title = (TextView) curView.findViewById(R.id.paihang_list_text);
			holder.rl = (LinearLayout) curView.findViewById(R.id.relativeLayout);
			curView.setTag(holder);

			VIEW_CACHE.put(key, curView);// View加入缓存

		} else
			holder = (ViewHolder) curView.getTag();

		// 设置数据
		Paihang mPaiHang = mDate.get(position);

		if (mPaiHang.getLogoUrl() != null)
			LoadImgProvider.getInstance(Constants.READNOVEL_IMGCACHE_ABS).load(mPaiHang.getLogoUrl(), holder.logo);
		else
			holder.logo.setImageDrawable(null);

		holder.title.setText(mPaiHang.getTitle());

		id = mPaiHang.getSortId();
		topTitle = mPaiHang.getTitle();

		if (position % 2 == 0)
			holder.rl.setBackgroundResource(R.drawable.select_lv1);
		else
			holder.rl.setBackgroundResource(R.drawable.select_lv2);

		return curView;
	}

	private static final class ViewHolder {
		ImageView logo;
		TextView title;
		LinearLayout rl;
	}
}
