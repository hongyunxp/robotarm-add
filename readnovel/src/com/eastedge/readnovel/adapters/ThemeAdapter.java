package com.eastedge.readnovel.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eastedge.readnovel.beans.NewBook;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.cache.Cache;
import com.readnovel.base.cache.view.ViewCacheProvider;
import com.readnovel.base.common.NetType;
import com.readnovel.base.http.LoadImgProvider;
import com.readnovel.base.util.NetUtils;
import com.xs.cn.R;

/**
 * 推荐的具体主题页面
 * 
 * @author li.li
 *
 * Jul 30, 2012
 */
public class ThemeAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<NewBook> mData;
	private final Cache<View> VIEW_CACHE = ViewCacheProvider.getInstance();// 视图缓存
	private NetType netType;

	public ThemeAdapter(Context mContext, ArrayList<NewBook> mDate) {
		super();
		this.mContext = mContext;
		this.mData = mDate;
		this.netType = NetUtils.checkNet(mContext);
	}

	public int getCount() {
		return mData.size();
	}

	public Object getItem(int position) {
		return mData.get(position);
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
			curView = LayoutInflater.from(mContext).inflate(R.layout.theme_list_item, null);
			holder = new ViewHolder();
			holder.imgContainer = curView.findViewById(R.id.imgContainer);
			holder.img = (ImageView) curView.findViewById(R.id.theme_list_img);
			holder.title = (TextView) curView.findViewById(R.id.theme_list_title);
			holder.totalviews = (TextView) curView.findViewById(R.id.theme_list_clicknum);
			holder.sortname = (TextView) curView.findViewById(R.id.theme_list_type);
			holder.author = (TextView) curView.findViewById(R.id.theme_list_author);
			holder.finishflag = (ImageView) curView.findViewById(R.id.theme_list_tag);
			holder.discount_view = (TextView) curView.findViewById(R.id.discount_view);
			curView.setTag(holder);
			VIEW_CACHE.put(key, curView);// View加入缓存
		} else
			holder = (ViewHolder) curView.getTag();
		// 设置数据
		NewBook info = mData.get(position);
		if (info.getImgURL() != null) {
			if (CommonUtils.isNoNeedPic(netType)) //2G不加载图片
				holder.imgContainer.setVisibility(View.GONE);
			else
				LoadImgProvider.getInstance(Constants.READNOVEL_IMGCACHE_ABS).load(info.getImgURL(), holder.img);
		} else
			holder.img.setBackgroundResource(R.drawable.download);

		String mTitle = info.getTitle();
		if (mTitle.length() > 10)
			holder.title.setText("《" + mTitle.substring(0, 10) + "》");
		else
			holder.title.setText("《" + mTitle + "》");

		holder.totalviews.setText(info.getTotalviews());
		holder.sortname.setText(info.getSortname());
		holder.author.setText(info.getAuthor());
		holder.finishflag.setImageResource(R.drawable.tag);
		// 显示折扣的代码逻辑
		if (info.getDiscount() != null) {
			holder.discount_view.setVisibility(View.VISIBLE);
			holder.discount_view.setText("(" + info.getDiscount() + "折)");
		}

		if (info.getFinishflag().equals("1"))
			holder.finishflag.setImageResource(R.drawable.tag);
		else if (info.getFinishflag().equals("0"))
			holder.finishflag.setImageResource(R.drawable.tag0);
		return curView;
	}

	private static final class ViewHolder {
		View imgContainer;
		ImageView img;
		TextView title;
		TextView totalviews;
		TextView sortname;
		TextView author;
		ImageView finishflag;
		TextView discount_view;// 显示折扣的view
	}

}
