package com.eastedge.readnovel.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eastedge.readnovel.beans.Book;
import com.xs.cn.R;

public class SearchAdapter extends BaseAdapter
{

	private Activity context;
	private ArrayList<Book> list;

	public SearchAdapter(Activity context, ArrayList<Book> list)
	{
		this.context = context;
		this.list = list;
	}

	public int getCount()
	{
		return list.size();
	}

	public Object getItem(int position)
	{
		return list.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{

		final ViewHolder tag;
		LayoutInflater inflater = context.getLayoutInflater();
		tag = new ViewHolder();
		View view = inflater.inflate(R.layout.theme_list_item, null);
		tag.icon = (ImageView) view.findViewById(R.id.theme_list_img);
		tag.title = (TextView) view.findViewById(R.id.theme_list_title);
		tag.clickNum = (TextView) view.findViewById(R.id.theme_list_clicknum);
		tag.type = (TextView) view.findViewById(R.id.theme_list_type);
		tag.author = (TextView) view.findViewById(R.id.theme_list_author);
		tag.tag = (ImageView) view.findViewById(R.id.theme_list_tag);

		Book info = list.get(position);

		tag.title.setText(info.getBookName());
		tag.clickNum.setText(info.getBookPoint() + "");
		tag.type.setText(info.getBookType());
		tag.author.setText(info.getBookEditer());

		return view;
	}

	private static final class ViewHolder
	{
		ImageView icon;
		TextView title;
		TextView clickNum;
		TextView type;
		TextView author;
		ImageView tag;
	}

}
