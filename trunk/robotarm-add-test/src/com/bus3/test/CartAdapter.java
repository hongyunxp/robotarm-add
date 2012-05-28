package com.bus3.test;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bus3.R;

public class CartAdapter extends BaseAdapter {
	private List<String> list;

	private Activity act;

	public CartAdapter(Activity act, List<String> list) {
		this.act = act;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 性能可提高四倍
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder vHolder;

		if (convertView == null) {// 减少创建View
			convertView = LayoutInflater.from(act).inflate(R.layout.product, null);

			vHolder = new ViewHolder();
			vHolder.edit = (EditText) convertView.findViewById(R.id.product_edit);
			vHolder.button = (Button) convertView.findViewById(R.id.product_button);
			vHolder.button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					System.out.println("@@@@@@@@@@@@@点击按钮");
				}

			});

			vHolder.iv = (ImageView) convertView.findViewById(R.id.iv);

			convertView.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}

		vHolder.edit.setText("内容" + position);
		vHolder.button.setText(list.get(position));
		vHolder.iv.setImageResource(R.drawable.girl6);

		return convertView;
	}

	private static final class ViewHolder {
		private Button button;
		private ImageView iv;
		private EditText edit;
	}

}
