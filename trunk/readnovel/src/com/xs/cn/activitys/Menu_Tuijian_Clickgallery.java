package com.xs.cn.activitys;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.eastedge.readnovel.adapters.SearchAdapter;
import com.eastedge.readnovel.beans.Book;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.Data;
import com.xs.cn.R;

public class Menu_Tuijian_Clickgallery extends Activity implements OnClickListener {
	private Button left1;
	private Button right1;
	private ListView tuijian_clickgallery_listView;
	private ArrayList<Book> list;
	private SearchAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tuijian_clickgallery);
		CloseActivity.add(this);

		setTopBar();

		list = Data.getNewBookList();
		tuijian_clickgallery_listView = (ListView) findViewById(R.id.tuijian_clickgallery_listView);
		adapter = new SearchAdapter(Menu_Tuijian_Clickgallery.this, list);
		tuijian_clickgallery_listView.setAdapter(adapter);

	}

	private void setTopBar() {
		left1 = (Button) findViewById(R.id.title_btn_left1);
		left1.setText("登录");
		right1 = (Button) findViewById(R.id.title_btn_right1);
		right1.setText("书架");
		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText("穿越盛宴");

		left1.setVisibility(View.VISIBLE);
		right1.setVisibility(View.VISIBLE);

		left1.setOnClickListener(this);
		right1.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == left1.getId()) {
			Intent intent = new Intent(Menu_Tuijian_Clickgallery.this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else if (v.getId() == right1.getId()) {
			Intent intent = new Intent(Menu_Tuijian_Clickgallery.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}

	}

	protected void onResume() {
		super.onResume();
		CloseActivity.curActivity = this;
	}

	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}
}
