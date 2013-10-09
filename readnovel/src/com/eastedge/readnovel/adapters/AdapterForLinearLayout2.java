package com.eastedge.readnovel.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eastedge.readnovel.beans.Shuping_huifuinfo;
import com.xs.cn.R;

public class AdapterForLinearLayout2 extends BaseAdapter {

	private Activity context;
	private ArrayList<Shuping_huifuinfo> data;
	private String Lastpost;
	private String Lastposter;

	public AdapterForLinearLayout2(Activity context, ArrayList<Shuping_huifuinfo> data, String lastpost, String lastposter) {
		this.context = context;
		this.data = data;
		this.Lastpost = lastpost;
		this.Lastposter = lastposter;
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
		View view = inflater.inflate(R.layout.novel_sbxxy_sphf, null);
		tag.tv21 = (TextView) view.findViewById(R.id.textView21);
		tag.tv22 = (TextView) view.findViewById(R.id.textView22);
		tag.tv3 = (TextView) view.findViewById(R.id.textView3);
		tag.tv4 = (TextView) view.findViewById(R.id.textView4);
		tag.ly_textView4 = (LinearLayout) view.findViewById(R.id.ly_textView4);

		if (position == 0) {
			tag.ly_textView4.setVisibility(View.VISIBLE);
			tag.tv4.setText("本条评论最有由  " + Lastposter + " 于 " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(Lastpost)))
					+ "编辑");
		}
		Shuping_huifuinfo info = data.get(position);

		tag.tv21.setText(info.getAuthor());

		String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(info.getDateline())));
		tag.tv22.setText(" 于 " + data + "写道：");
		tag.tv3.setText(info.getMessage());

		return view;
	}

	private static final class ShupingHolder {
		TextView tv1;
		TextView tv21;
		TextView tv22;
		TextView tv3;
		TextView tv4;
		LinearLayout ly_textView4;

	}

	// /**
	// * 绑定视图
	// *
	// * @param view
	// * @param item
	// * @param from
	// */
	// private void bindView(View view, Map<String, ?> item, String from)
	// {
	// Object data = item.get(from);
	// if (view instanceof TextView)
	// {
	// ((TextView) view).setText(data == null ? "" : data.toString());
	// }
	// }
}
