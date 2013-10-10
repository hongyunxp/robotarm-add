package com.readnovel.book.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.readnovel.book.base.R;
import com.readnovel.book.base.entity.BookTag;
import com.readnovel.book.base.utils.CommonUtils;

public class BookTagAdapter extends BaseAdapter {
	private Context context;
	private List<BookTag> bookTagList;

	public BookTagAdapter(Context context, List<BookTag> bookTagList) {
		this.context = context;
		this.bookTagList = bookTagList;
	}

	@Override
	public int getCount() {
		return bookTagList.size();
	}

	@Override
	public Object getItem(int position) {
		return bookTagList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.booktag_list_item, null);
		v.setBackgroundResource(R.color.bg);
//		v.setMinimumHeight(160);
		TextView title_tv = (TextView) v.findViewById(R.id.booktag_chaptername_tv);
		TextView foretext_tv = (TextView) v.findViewById(R.id.booktag_foreText_tv);
		TextView time_tv = (TextView) v.findViewById(R.id.booktag_time_tv);
		TextView percent_tv = (TextView) v.findViewById(R.id.booktag_percent_tv);
		// title_tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		title_tv.setText(bookTagList.get(position).getFileName());
		foretext_tv.setText(bookTagList.get(position).getForeText() + "......");
		String time = bookTagList.get(position).getTime();
		String nowTime = CommonUtils.getCurrentTime();
		if (nowTime.equals(time.substring(0, time.indexOf(" ")))) {
			time_tv.setText("今天  " + time.substring(time.indexOf(" ") + 1, time.length()));
		} else {
			time_tv.setText(time);
		}
		percent_tv.setText("[" + bookTagList.get(position).getPercent() + "]");

		return v;
	}
}
