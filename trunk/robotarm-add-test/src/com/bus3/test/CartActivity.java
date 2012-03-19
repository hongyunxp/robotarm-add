package com.bus3.test;

import java.util.Arrays;
import java.util.List;

import robot.arm.utils.BaseUtils;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bus3.R;
import com.bus3.common.activity.BaseActivity;

public class CartActivity extends BaseActivity implements OnItemClickListener {
	private List<String> list = Arrays.asList("按纽1", "按纽2", "按纽3", "按纽4", "按纽5", "按纽6", "按纽7", "按纽8");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_content);
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

		ListView lv = (ListView) findViewById(R.id.products);
		BaseUtils.setListViewHeight(lv);// 设置listview高度
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	}

}
