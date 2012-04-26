package com.bus3.test;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

//		if (convertView == null) {
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + position);
			View product = LayoutInflater.from(act).inflate(R.layout.product, null);
			Button button = (Button) product.findViewById(R.id.product_button);
			button.setText(list.get(position));

			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					System.out.println("@@@@@@@@@@@@@点击按钮");
				}

			});

			convertView = product;
//		}

		return convertView;
	}

}
