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

public class CartAdapter2 extends BaseAdapter {
	private List<String> list;

	private Activity act;

	public CartAdapter2(Activity act, List<String> list) {
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


		convertView = LayoutInflater.from(act).inflate(R.layout.product, null);

		EditText edit = (EditText) convertView.findViewById(R.id.product_edit);
		Button button = (Button) convertView.findViewById(R.id.product_button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("@@@@@@@@@@@@@点击按钮");
			}

		});

		ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
		
		edit.setText("内容" + position);
		button.setText(list.get(position));
		iv.setImageResource(R.drawable.girl5);


		return convertView;
	}

}
