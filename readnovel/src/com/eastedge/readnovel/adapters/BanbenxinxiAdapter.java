package com.eastedge.readnovel.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xs.cn.R;

public class BanbenxinxiAdapter extends BaseAdapter
{	
	private Activity context;
	private String[] data;
	
	public BanbenxinxiAdapter(Activity context,String[] data){
		this.context = context;
		this.data = data;
	}
	public int getCount()
    {
	    return data.length;
    }

	public Object getItem(int position)
    {
	    return null;
    }

	public long getItemId(int position)
    {
	    return position;
    }

	public View getView(int position, View convertView, ViewGroup parent)
    {	
		final banbenxinxiHolder tag;
		LayoutInflater inflater = context.getLayoutInflater();
		tag = new banbenxinxiHolder();
		View view = inflater.inflate(R.layout.banbenxinxi_item, null);
		tag.info = (TextView)view.findViewById(R.id.textView1);
		tag.info.setText(position+1+"、"+data[position]+"。");
	    return view;
    }
	
	private static final class banbenxinxiHolder{
		TextView info;
	}
}
