package com.eastedge.readnovel.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eastedge.readnovel.beans.Tuijian;
import com.eastedge.readnovel.common.Constants;
import com.readnovel.base.cache.Cache;
import com.readnovel.base.cache.view.ViewCacheProvider;
import com.readnovel.base.http.LoadImgProvider;
import com.xs.cn.R;

/**
 * 推荐列表 list
 * 
 * @author li.li
 * 
 *         Jul 28, 2012
 */
public class TuijianAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Tuijian> mDate;
	private final Cache<View> VIEW_CACHE = ViewCacheProvider.getInstance();// 视图缓存

	public TuijianAdapter(Context mContext, ArrayList<Tuijian> mDate) {
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
			curView = LayoutInflater.from(mContext).inflate(
					R.layout.menu_tuijian_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) curView
					.findViewById(R.id.tuijian_list_img);
			holder.title = (TextView) curView
					.findViewById(R.id.tuijian_list_title);
			holder.content = (TextView) curView
					.findViewById(R.id.tuijian_list_content);
			curView.setTag(holder);

			VIEW_CACHE.put(key, curView);// View加入缓存
		} else
			holder = (ViewHolder) curView.getTag();
		// 设置数据
		Tuijian info = mDate.get(position);
		if (info.getImageURL() != null)
			LoadImgProvider.getInstance(Constants.READNOVEL_IMGCACHE_ABS).load(
					info.getImageURL(), holder.icon);// 异步加载图片
		else
			holder.icon.setBackgroundResource(R.drawable.download);
		holder.title.setText(info.getTitle().trim());
		holder.content.setText(info.getAuther().trim());
		return curView;
	}

	private static final class ViewHolder {
		ImageView icon;
		TextView title;
		TextView content;
	}

}
