package com.readnovel.book.base.adapter;

import java.util.ArrayList;
import java.util.List;

import com.readnovel.book.base.entity.More;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MoreMessageAdapter extends BaseAdapter{
	private final Context context;
	private List<More> listmore = new ArrayList<More>();// 存放更多对象的集合
	private String[] moremessage = {
	        "查看更多商品",	
	};

	public MoreMessageAdapter(Context context) {
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listmore.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listmore.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewgroup) {
		// TODO Auto-generated method stub
		return null;
	}

}
