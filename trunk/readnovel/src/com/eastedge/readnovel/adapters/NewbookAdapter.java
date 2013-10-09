package com.eastedge.readnovel.adapters;

import java.util.ArrayList;

import android.app.Activity;
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
 * 新书listview适配器
 * 
 * @author li.li
 * 
 *         Jul 28, 2012
 */
public class NewbookAdapter extends BaseAdapter {
	private final Activity context;
	private final ArrayList<NewBook> newBooks;
	private final Cache<View> VIEW_CACHE = ViewCacheProvider.getInstance();// 视图缓存
	private NetType netType;

	public NewbookAdapter(Activity context, ArrayList<NewBook> newBooks) {
		this.context = context;
		this.newBooks = newBooks;
		this.netType = NetUtils.checkNet(context);
	}

	public int getCount() {
		return newBooks.size();
	}

	public Object getItem(int position) {
		return newBooks.get(position);
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
			curView = LayoutInflater.from(context).inflate(R.layout.menu_newbook_item, null);// 创建View
			holder = new ViewHolder();// 创建View holder
			holder.imgContainer = curView.findViewById(R.id.imgContainer);
			holder.icon = (ImageView) curView.findViewById(R.id.theme_list_img);
			holder.title = (TextView) curView.findViewById(R.id.theme_list_title);
			holder.clickNum = (TextView) curView.findViewById(R.id.theme_list_clicknum);
			holder.type = (TextView) curView.findViewById(R.id.theme_list_type);
			holder.author = (TextView) curView.findViewById(R.id.theme_list_author);
			holder.tag = (ImageView) curView.findViewById(R.id.theme_list_tag);
			curView.setTag(holder);

			VIEW_CACHE.put(key, curView);// View加入缓存

		} else
			holder = (ViewHolder) curView.getTag();

		// 设置数据
		NewBook book = newBooks.get(position);
		if (book.getImgURL() != null)
			if (CommonUtils.isNoNeedPic(netType)) //2G不加载图片
				holder.imgContainer.setVisibility(View.GONE);
			else
				LoadImgProvider.getInstance(Constants.READNOVEL_IMGCACHE_ABS).load(book.getImgURL(), holder.icon);// 图片异步加载
		
		else
			holder.icon.setImageDrawable(null);

		String mTitle = book.getTitle();
		if (mTitle.length() > 8)
			holder.title.setText("《" + mTitle.substring(0, 8) + "》");
		else
			holder.title.setText("《" + mTitle + "》");

		holder.clickNum.setText(book.getTotalviews());
		holder.type.setText(book.getSortname());
		holder.author.setText(book.getAuthor());

		if (book.getFinishflag().equals("1"))
			holder.tag.setBackgroundResource(R.drawable.tag);
		else
			holder.tag.setBackgroundResource(R.drawable.tag0);

		return curView;
	}

	private static final class ViewHolder {
		View imgContainer;
		ImageView icon;
		TextView title;
		TextView clickNum;
		TextView type;
		TextView author;
		ImageView tag;

	}

}
