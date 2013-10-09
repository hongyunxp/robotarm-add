package com.eastedge.readnovel.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.eastedge.readnovel.beans.BookType;
import com.eastedge.readnovel.common.HCData;
import com.xs.cn.R;
import com.xs.cn.activitys.MenuFenleiItem;

public class FenleiAdapter extends BaseAdapter
{
	private Activity context;
	public ArrayList<BookType> list;

	public FenleiAdapter(Activity context, ArrayList<BookType> list)
	{
		this.context = context;
		if(list==null)list=new ArrayList<BookType>();
		this.list = list;
	}

	public int getCount()
	{
		return list.size();
	}

	public Object getItem(int position)
	{
		return null;
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final fenleiItemHolder tag;
		LayoutInflater inflater = context.getLayoutInflater();
		tag = new fenleiItemHolder();
		View view = inflater.inflate(R.layout.novel_feilei_item, null);
		tag.novel_fenlei_tv = (Button) view.findViewById(R.id.novel_fenlei_tv);

		BookType info = list.get(position);
		tag.novel_fenlei_tv.setText(info.getTitle());
		tag.novel_fenlei_tv.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
			
				Intent intent = new Intent();
				BookType info = HCData.list.get(position);
				Bundle bundle = new Bundle();
				bundle.putString("sortId", info.getSortId());
				bundle.putString("title", info.getTitle());

				intent.putExtra("conditions", bundle);
				intent.setClass(context, MenuFenleiItem.class);
				context.startActivity(intent);
			}
		});
		return view;
	}

	private static final class fenleiItemHolder
	{
		public TextView novel_fenlei_tv;

	}
}
