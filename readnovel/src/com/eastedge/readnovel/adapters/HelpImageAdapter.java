package com.eastedge.readnovel.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eastedge.readnovel.beans.UseHelp;
import com.xs.cn.R;

/** 
 * @author ninglv 
 * @version Time：2012-3-23 下午1:46:02 
 */
public class HelpImageAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<UseHelp> al;
	final LayoutInflater inflater;
	
	public HelpImageAdapter(Context context, ArrayList<UseHelp> al) {
		super();
		inflater = LayoutInflater.from(context);
		this.al = al;
		this.context = context;
	}

	public int getCount() {
		return al.size();
	}

	public Object getItem(int position) {
		return al.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
//		if(convertView == null){
		convertView = inflater.inflate(R.layout.help_item, null);
		holder = new ViewHolder();
		holder.title = (TextView)convertView.findViewById(R.id.help_title);
		holder.img = (ImageView)convertView.findViewById(R.id.help_image);
//			convertView.setTag(holder);
//		}else{
//			holder = (ViewHolder)convertView.getTag();
//		}
//		
		UseHelp help = al.get(position);
		holder.title.setText(help.getTitle());
		holder.img.setImageDrawable(help.getImg());
		
		return convertView;
		
	}
	
	private static final class ViewHolder{
		TextView title;
		ImageView img;
	}
}
