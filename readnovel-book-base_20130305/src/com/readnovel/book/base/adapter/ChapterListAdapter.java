package com.readnovel.book.base.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.readnovel.book.base.R;
import com.readnovel.book.base.bean.VipPayInterval;
import com.readnovel.book.base.cache.Cache;
import com.readnovel.book.base.cache.view.ViewCacheProvider;
import com.readnovel.book.base.db.table.VipPayIntervalRecord;
import com.readnovel.book.base.entity.Chapter;
import com.readnovel.book.base.sp.StyleSaveUtil;
import com.readnovel.book.base.utils.BookInfoUtils;
import com.readnovel.book.base.utils.CommonUtils;
import com.readnovel.book.base.utils.LogUtils;

public class ChapterListAdapter extends BaseAdapter {
//	private final Cache<View> VIEW_CACHE = ViewCacheProvider.getInstance();// 视图缓存
	private Context context;
	private List<Chapter> bookList;
	private StyleSaveUtil util;
	private boolean isFree;
	private VipPayIntervalRecord vpir;

	public ChapterListAdapter(Context context, List<Chapter> bookList) {
		this.context = context;
		this.bookList = bookList;
		util = new StyleSaveUtil(context);
		isFree = BookInfoUtils.isFree(context);
		
		vpir = new VipPayIntervalRecord(context);
	}

	@Override
	public int getCount() {
		return bookList.size();
	}

	@Override
	public Object getItem(int position) {
		return bookList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View curView = null;
		ViewHolder holder = null;
		// 先从缓存中取
		curView = convertView;
		// 缓存里没有创建新的
		if (curView == null) {
			curView = LayoutInflater.from(context).inflate(R.layout.chapter_list_item, null);
			holder = new ViewHolder();// 创建View holder
			holder.dapterid_tv = (TextView) curView.findViewById(R.id.chapter_id);//章节内容
			holder.daptertype_tv = (TextView) curView.findViewById(R.id.chapter_type);//vip 免费
			curView.setTag(holder);
		} else
			holder = (ViewHolder) curView.getTag();
		holder.dapterid_tv.setText(bookList.get(position).getTitle());
		//判断是否为VIP章节
			holder.daptertype_tv.setVisibility(View.VISIBLE);
			Chapter chapter = bookList.get(position);
			if (chapter.isVip()) {
				int chapterId = chapter.getId();
				VipPayInterval vpi = vpir.getByChapterId(chapterId);
				if (VipPayInterval.UN_ORDER == vpi.getState()) {//未订购
					holder.daptertype_tv.setTextColor(CommonUtils.color(context, R.color.vip_chapter_list_unreadable));
					holder.daptertype_tv.setText("VIP");
				} else if (VipPayInterval.PAY_SUCCESS == vpi.getState()) {//验证成功
					holder.daptertype_tv.setTextColor(CommonUtils.color(context, R.color.vip_chapter_list_readable));
					holder.daptertype_tv.setText("已订");
				}
			} else {
				holder.daptertype_tv.setText("免费");
				holder.daptertype_tv.setTextColor(Color.BLACK);
			}
			
		if (position == util.writescollery()) {
			if (util.writeFirstChapter() == 1) {
				String chaptercontent = holder.dapterid_tv.getText().toString();
			
				holder.dapterid_tv.setText(chaptercontent);
				holder.dapterid_tv.setTextColor(context.getResources().getColor(R.color.pink));
				//设置跑马灯
				holder.dapterid_tv.setSelected(true);
				holder.dapterid_tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
				holder.dapterid_tv.setMarqueeRepeatLimit(-1);
			}
		} else {
			holder.dapterid_tv.setTextColor(Color.BLACK);
			holder.dapterid_tv.setSelected(false);
		}
		return curView;
	}

	private static final class ViewHolder {
		TextView dapterid_tv;
		TextView daptertype_tv;
	}
}
