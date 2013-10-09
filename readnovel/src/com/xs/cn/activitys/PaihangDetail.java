package com.xs.cn.activitys;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.eastedge.readnovel.adapters.ThemeAdapter;
import com.eastedge.readnovel.beans.NewBook;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.threads.PaihangDetailThread;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

public class PaihangDetail extends Activity implements OnClickListener {

	private Button left2, right1;
	private ArrayList<NewBook> arrayList;
	private String id;
	PaihangDetailThread phDetailThread;
	private String topTitle;
	private ThemeAdapter adapter;
	private ListView lv;
	private ProgressDialog mWaitDg1 = null;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				arrayList = phDetailThread.arrayList;
				adapter = new ThemeAdapter(PaihangDetail.this, arrayList);
				lv.setAdapter(adapter);
				break;
			case 2:
				adapter.notifyDataSetChanged();
				break;
			}
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.paihang_detail);
		CloseActivity.add(this);

		Intent intent = getIntent();

		id = intent.getStringExtra("sortId");
		topTitle = intent.getStringExtra("topTitle");

		setTopBar();

		phDetailThread = new PaihangDetailThread(PaihangDetail.this, handler, id);
		phDetailThread.start();

		lv = (ListView) findViewById(R.id.ph_dt_list);
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				NewBook info = arrayList.get(position);
				bundle.putString("Author", info.getAuthor());
				bundle.putString("Articleid", info.getArticleid());
				bundle.putString("Finishflag", info.getFinishflag());
				bundle.putString("Sortname", info.getSortname());
				bundle.putString("Title", info.getTitle());
				bundle.putString("Totalviews", info.getTotalviews());

				intent.putExtra("newbook", bundle);
				intent.setClass(PaihangDetail.this, Novel_sbxxy.class);
				startActivity(intent);

			}

		});

		mWaitDg1 = ProgressDialog.show(PaihangDetail.this, "正在加载数据...", "请稍候...", true, true);
		mWaitDg1.show();

	}

	private void setTopBar() {
		left2 = (Button) findViewById(R.id.title_btn_left2);
		left2.setText("返回");
		right1 = (Button) findViewById(R.id.title_btn_right1);
		right1.setText("书架");
		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText(topTitle);

		left2.setVisibility(View.VISIBLE);
		right1.setVisibility(View.VISIBLE);

		left2.setOnClickListener(this);
		right1.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_btn_left2:
			finish();
			break;
		case R.id.title_btn_right1:
			Intent intentR = new Intent(PaihangDetail.this, MainActivity.class);
			startActivity(intentR);
			break;
		}
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		CloseActivity.curActivity = this;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}
}