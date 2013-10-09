package com.eastedge.readnovel.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eastedge.readnovel.beans.Shuping_maininfo;
import com.xs.cn.R;

public class ShupingAdapter extends BaseAdapter {
	private Activity context;
	private ArrayList<Shuping_maininfo> data;

	public ShupingAdapter(Activity context, ArrayList<Shuping_maininfo> data) {
		this.context = context;
		this.data = data;
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ShupingHolder tag;
		LayoutInflater inflater = context.getLayoutInflater();
		tag = new ShupingHolder();
		View view = inflater.inflate(R.layout.novel_sbxxy_pinglun, null);
		tag.tv1 = (TextView) view.findViewById(R.id.textView1);
		tag.tv21 = (TextView) view.findViewById(R.id.textView21);
		tag.tv22 = (TextView) view.findViewById(R.id.textView22);
		tag.tv3 = (TextView) view.findViewById(R.id.textView3);
		tag.tv4 = (TextView) view.findViewById(R.id.textView4);

		Shuping_maininfo info = data.get(position);

		tag.tv1.setText(info.getSubject());
		tag.tv21.setText(info.getAuthor());

		String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(info.getDateline())));
		tag.tv22.setText(" 于 " + data + "写道：");
		tag.tv3.setText(info.getMessage());
		tag.tv4.setText("共" + info.getReplies() + "条回复");

		return view;
	}

	private static final class ShupingHolder {
		TextView tv1;
		TextView tv21;
		TextView tv22;
		TextView tv3;
		TextView tv4;

	}
}
