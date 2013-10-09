package com.eastedge.readnovel.adapters;

import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastedge.readnovel.beans.Chapterinfo;
import com.xs.cn.R;

public class BfMLAdapter extends BaseAdapter {
	private Activity context;
	private ArrayList<Chapterinfo> data;
	private RelativeLayout nowcheckly;
	private String nowtextid;
	private HashSet<String> ydy;

	public BfMLAdapter(Activity context, ArrayList<Chapterinfo> data, String nowtextid, HashSet<String> ydy) {
		this.context = context;
		this.data = data;
		this.nowtextid = nowtextid;
		this.ydy = ydy;
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
		final ShubenmuluHolder tag;
		if (convertView == null) {
			tag = new ShubenmuluHolder();
			View view = LayoutInflater.from(context).inflate(R.layout.novel_sbxxy_mulu_item, null);
			tag.zj = (TextView) view.findViewById(R.id.novel_sbxxy_muitem_zj);
			tag.vip = (TextView) view.findViewById(R.id.novel_sbxxy_muitem_vip);
			tag.ry = (RelativeLayout) view.findViewById(R.id.linearLayout1);
			tag.ly2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
			convertView = view;
			convertView.setTag(tag);
		} else {
			tag = (ShubenmuluHolder) convertView.getTag();
		}

		final Chapterinfo info = data.get(position);

		if (info.getId().trim().equals(nowtextid)) {
			tag.zj.setTextColor(Color.rgb(204, 0, 0));
			tag.vip.setVisibility(View.GONE);
			tag.ly2.setVisibility(View.VISIBLE);
		} else {
			tag.zj.setTextColor(Color.BLACK);
			tag.vip.setVisibility(View.VISIBLE);
			tag.ly2.setVisibility(View.GONE);
		}

		tag.zj.setText(info.getSubhead());

		if (ydy != null && ydy.contains(info.getId())) {
			tag.vip.setText("已订阅");
			tag.vip.setTextColor(Color.rgb(51, 153, 0));
		} else if (info.getIs_vip() == 0) {
			tag.vip.setText("免费");
			tag.vip.setTextColor(Color.parseColor("#666666"));
		} else if (info.getIs_vip() == 1) {
			tag.vip.setTextColor(Color.rgb(255, 102, 0));
			tag.vip.setText("VIP");
		}

		return convertView;
	}

	private static final class ShubenmuluHolder {
		TextView zj;
		TextView vip;
		RelativeLayout ry;
		LinearLayout ly2;

	}
}
