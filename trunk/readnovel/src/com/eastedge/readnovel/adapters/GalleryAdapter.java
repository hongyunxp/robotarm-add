package com.eastedge.readnovel.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.eastedge.readnovel.beans.Image;
import com.eastedge.readnovel.common.Constants;
import com.readnovel.base.R;
import com.readnovel.base.cache.Cache;
import com.readnovel.base.cache.view.ViewCacheProvider;
import com.readnovel.base.http.LoadImgProvider;

/**
 * 推荐顶部gallery
 * 
 * @author li.li
 * 
 *         Jul 28, 2012
 */
public class GalleryAdapter extends BaseAdapter {
	private static final ViewGroup.LayoutParams LAYOUT_PARAMS = new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

	public Context context;
	public ArrayList<Image> list;
	private Handler handler;
	// 视图缓存
	private final Cache<View> VIEW_CACHE = ViewCacheProvider.getInstance();

	public GalleryAdapter(Context context, ArrayList<Image> list, Handler handler) {

		this.context = context;
		this.list = list;
		this.handler = handler;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
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
			curView = new ImageView(context);
			holder = new ViewHolder();
			holder.iv = (ImageView) curView;
			curView.setTag(holder);

			// View加入缓存
			VIEW_CACHE.put(key, curView);

		} else
			holder = (ViewHolder) curView.getTag();

		// 设置数据
		Image img = list.get(position);
		if (img.getImageURL() != null) {
			holder.iv.setScaleType(ScaleType.FIT_XY);
			holder.iv.setLayoutParams(LAYOUT_PARAMS);
			holder.iv.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			LoadImgProvider.getInstance(Constants.READNOVEL_IMGCACHE_ABS).load(img.getImageURL(), holder.iv);// 异步加载图片

		} else {
			holder.iv.setBackgroundResource(R.drawable.download);
		}

		return curView;
	}

	private static final class ViewHolder {
		ImageView iv;
	}

}
