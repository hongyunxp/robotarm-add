package com.eastedge.readnovel.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eastedge.readnovel.beans.Shuping_maininfo;
import com.xs.cn.R;
import com.xs.cn.activitys.Novel_shuping_huifu;

public class AdapterForLinearLayout extends BaseAdapter {

	private Activity context;
	private ArrayList<Shuping_maininfo> data;

	public AdapterForLinearLayout(Activity context, ArrayList<Shuping_maininfo> data) {
		this.context = context;
		this.data = data;
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
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
		tag.ly = (View) view.findViewById(R.id.shuping_ly);
		tag.ly_textView4 = (LinearLayout) view.findViewById(R.id.ly_textView4);

		final Shuping_maininfo info = data.get(position);

		tag.tv1.setText(info.getSubject());
		tag.tv21.setText(info.getAuthor());

		String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(info.getDateline())));
		tag.tv22.setText(" 于 " + data + "写道：");
		tag.tv3.setText(info.getMessage());
		if (info.getReplies() != 0) {
			tag.ly_textView4.setVisibility(View.VISIBLE);
			tag.tv4.setText("共" + info.getReplies() + "条回复");
		}

		tag.ly.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// bundle.putString("Author", info.getAuthor());
				// bundle.putString("Dateline", info.getDateline());
				// bundle.putString("Message", info.getMessage());
				bundle.putString("Subject", info.getSubject());
				bundle.putInt("Tid", info.getTid());
				bundle.putString("Lastpost", info.getLastpost());
				bundle.putString("Lastposter", info.getLastposter());
				intent.putExtra("newbook", bundle);
				intent.setClass(context, Novel_shuping_huifu.class);
				context.startActivity(intent);
			}
		});

		return view;
	}

	//	/**
	//	 * 绑定视图
	//	 * 
	//	 * @param view
	//	 * @param item
	//	 * @param from
	//	 */
	//	private void bindView(View view, Map<String, ?> item, String from)
	//	{
	//		Object data = item.get(from);
	//		if (view instanceof TextView)
	//		{
	//			((TextView) view).setText(data == null ? "" : data.toString());
	//		}
	//	}

	private static final class ShupingHolder {
		TextView tv1;
		TextView tv21;
		TextView tv22;
		TextView tv3;
		TextView tv4;
		View ly;
		LinearLayout ly_textView4;

	}
}
