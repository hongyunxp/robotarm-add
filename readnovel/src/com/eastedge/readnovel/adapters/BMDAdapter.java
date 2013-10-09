package com.eastedge.readnovel.adapters;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastedge.readnovel.beans.BookMark;
import com.eastedge.readnovel.db.DBAdapter;
import com.xs.cn.R;

public class BMDAdapter extends BaseAdapter {
	private ArrayList<BookMark> list;
	private Context context;
	private DBAdapter dbAdapter;
	private Handler handler;

	DecimalFormat df = new DecimalFormat("##.##");

	public BMDAdapter(Context context, ArrayList<BookMark> list, Handler handler) {
		this.context = context;
		if (list == null) {
			list = new ArrayList<BookMark>();
		}
		this.list = list;
		this.handler = handler;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		MarkListItem item = null;
		if (convertView == null) {
			item = new MarkListItem();
			LayoutInflater inflate = LayoutInflater.from(context);
			View v = inflate.inflate(R.layout.bookmark_item, null);
			item.rl = (RelativeLayout) v.findViewById(R.id.mark_item_rl);
			item.title = (TextView) v.findViewById(R.id.mark_item_title);
			item.jj = (TextView) v.findViewById(R.id.mark_item_jj);
			item.time = (TextView) v.findViewById(R.id.mark_item_time);
			item.p = (TextView) v.findViewById(R.id.mark_item_p);
			item.bt_delete = (Button) v.findViewById(R.id.bt_delete);
			convertView = v;
			convertView.setTag(item);
		} else {
			item = (MarkListItem) convertView.getTag();
		}

		final BookMark bm = list.get(position);
		final String textid = bm.getTextid();
		final int beg = bm.getLocation();
		item.title.setText(bm.getTexttitle());
		item.jj.setText(bm.getTextjj());
		String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(bm.getTime()));
		item.time.setText(data + "");
		item.rl.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Message msg = new Message();
				msg.what = 124;
				msg.arg1 = beg;
				msg.obj = textid;
				handler.sendMessage(msg);
			}
		});

		item.bt_delete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dbAdapter = new DBAdapter(context);
				dbAdapter.open();
				dbAdapter.deleteOneMark(bm.getId());
				dbAdapter.close();
				handler.sendEmptyMessage(1);
			};
		});
		float fPercent = (float) (bm.getLocation() * 1.0 / bm.getLength());
		String strPercent = df.format(fPercent * 100) + "%";
		item.p.setText("[" + strPercent + "]");

		return convertView;
	}

	private static final class MarkListItem {
		TextView title;
		TextView jj;
		TextView time;
		TextView p;
		RelativeLayout rl;
		Button bt_delete;
	}
}
