package com.xs.cn.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.TextView;

import com.eastedge.readnovel.adapters.ShupingAdapter;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.threads.ShupingThread;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

public class Novel_sbxxy_shuping extends Activity {
	private ListView novel_sbxxy_splv;
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	ShupingThread spth;

	public Handler handler = new Handler() {

		private ShupingAdapter adapter;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				textView1.setText(spth.spif.getTotal_number() + "");
				textView2.setText(spth.spif.getCur_page_number() + "");
				textView3.setText(spth.spif.getTotal_page_number() + "");
				adapter = new ShupingAdapter(Novel_sbxxy_shuping.this, spth.spif.getSPlist());
				novel_sbxxy_splv.setAdapter(adapter);
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novel_sbxxy_shuping);
		CloseActivity.add(this);

		novel_sbxxy_splv = (ListView) findViewById(R.id.novel_sbxxy_splv);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);

		spth = new ShupingThread(Novel_sbxxy_shuping.this, handler, "35055", 1, null);
		spth.start();
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
