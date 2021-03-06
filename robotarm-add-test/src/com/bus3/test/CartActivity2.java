package com.bus3.test;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bus3.R;
import com.bus3.common.activity.BaseActivity;

public class CartActivity2 extends BaseActivity implements OnItemClickListener {
	private List<String> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_content2);

		list = new ArrayList<String>(5000);

		for (int i = 0; i < 50000; i++) {
			list.add("按纽" + (i + 1));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		System.out.println("@@@@@@@@@@@@@点击整个" + position);
	}

	@Override
	protected void onResume() {
		super.onResume();
		tabInvHandler().setTitle(R.layout.cart_title);
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		// listview和scrollview配合使用
		ListView products = (ListView) findViewById(R.id.products);
		products.setAdapter(new CartAdapter(this, list));
		products.setOnItemClickListener(this);

	}

}
