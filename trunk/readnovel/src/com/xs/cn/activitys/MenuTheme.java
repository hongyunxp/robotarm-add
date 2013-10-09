package com.xs.cn.activitys;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.adapters.ThemeAdapter;
import com.eastedge.readnovel.beans.NewBook;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.threads.ThemeThread;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

public class MenuTheme extends Activity implements OnClickListener {
	private ListView listView;
	private ThemeAdapter adapter;
	private ArrayList<NewBook> bookList = new ArrayList<NewBook>();
	private Button left2, right1;
	private ThemeThread tt;
	private String aid;
	private ProgressDialog mWaitDg1;
	private int page = 1;
	private String sorted;
	private Button high_b, mid_b, half_b, low_b, fy_b;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.discount_s_view1);
				if (tt.theme.getIsDiscount() == true) {
					hsv.setVisibility(View.VISIBLE);
				}
				TextView title_tv = (TextView) findViewById(R.id.title_tv);
				title_tv.setText(tt.topTitle);
				ArrayList<NewBook> al = tt.al;
				bookList.addAll(al);
				if (al.size() < 20 || bookList.size() >= tt.theme.getNub()) {
					loadingLayout.removeAllViews();
				} else {
					loadingLayout.removeAllViews();
					isAdd = false;
				}
				adapter = new ThemeAdapter(MenuTheme.this, bookList);
				listView.setAdapter(adapter);
				break;
			case 2:
				ArrayList<NewBook> al2 = tt.al;
				bookList.addAll(al2);
				if (al2.size() < 20 || bookList.size() >= tt.theme.getNub()) {
					loadingLayout.removeAllViews();
				} else {
					loadingLayout.removeAllViews();
					isAdd = false;
				}
				adapter.notifyDataSetChanged();
				break;
			case 3:
				adapter.notifyDataSetChanged();
				break;
			case 5:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				ArrayList<NewBook> a2 = tt.al;
				bookList.clear();
				bookList.addAll(a2);
				if (a2.size() < 20 || bookList.size() >= tt.theme.getNub()) {
					loadingLayout.removeAllViews();
				} else {
					loadingLayout.removeAllViews();
					isAdd = false;
				}
				// 设置点击按钮之后进行的刷新显示界面high,middle,half,low,fy])
				if ("high".equals(sorted)) {
					high_b.setBackgroundResource(R.drawable.fenleiitem_tab);
					high_b.setTextColor(Color.WHITE);
					mid_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					mid_b.setTextColor(Color.BLACK);
					half_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					half_b.setTextColor(Color.BLACK);
					low_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					low_b.setTextColor(Color.BLACK);
					fy_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					fy_b.setTextColor(Color.BLACK);
				} else if ("middle".equals(sorted)) {
					mid_b.setBackgroundResource(R.drawable.fenleiitem_tab);
					mid_b.setTextColor(Color.WHITE);
					high_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					high_b.setTextColor(Color.BLACK);
					half_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					half_b.setTextColor(Color.BLACK);
					low_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					low_b.setTextColor(Color.BLACK);
					fy_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					fy_b.setTextColor(Color.BLACK);
				} else if ("half".equals(sorted)) {
					half_b.setBackgroundResource(R.drawable.fenleiitem_tab);
					half_b.setTextColor(Color.WHITE);
					mid_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					mid_b.setTextColor(Color.BLACK);
					high_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					high_b.setTextColor(Color.BLACK);
					low_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					low_b.setTextColor(Color.BLACK);
					fy_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					fy_b.setTextColor(Color.BLACK);
				} else if ("low".equals(sorted)) {
					low_b.setBackgroundResource(R.drawable.fenleiitem_tab);
					low_b.setTextColor(Color.WHITE);
					mid_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					mid_b.setTextColor(Color.BLACK);
					half_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					half_b.setTextColor(Color.BLACK);
					high_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					high_b.setTextColor(Color.BLACK);
					fy_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					fy_b.setTextColor(Color.BLACK);
				} else if ("fy".equals(sorted)) {
					fy_b.setBackgroundResource(R.drawable.fenleiitem_tab);
					fy_b.setTextColor(Color.WHITE);
					mid_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					mid_b.setTextColor(Color.BLACK);
					half_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					half_b.setTextColor(Color.BLACK);
					low_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					low_b.setTextColor(Color.BLACK);
					high_b.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					high_b.setTextColor(Color.BLACK);
				}

				adapter = new ThemeAdapter(MenuTheme.this, bookList);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				break;
			case 44:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				Toast.makeText(MenuTheme.this, getString(R.string.network_err), Toast.LENGTH_SHORT).show();
				handler.postDelayed(new Runnable() {

					public void run() {
						Intent intent = new Intent(MenuTheme.this, MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
					}
				}, 1000);
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.theme);
		CloseActivity.add(this);
		setTopBar();
		listView = (ListView) findViewById(R.id.theme_list);
		listView.addFooterView(showLayout());
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				NewBook info = bookList.get(position);
				bundle.putString("Articleid", info.getArticleid());
				intent.putExtra("newbook", bundle);
				intent.setClass(MenuTheme.this, Novel_sbxxy.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		listView.setOnScrollListener(new ListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && view.getLastVisiblePosition() == (view.getCount() - 1)) {
					addLoadView();
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});

		Intent intent = getIntent();
		aid = intent.getStringExtra("aid");
		page = intent.getIntExtra("page", 1);
		sorted = intent.getStringExtra("sorted");
		mWaitDg1 = ProgressDialog.show(MenuTheme.this, "正在加载数据...", "请稍后...", true, true);
		mWaitDg1.show();
		high_b = (Button) findViewById(R.id.high_b);
		mid_b = (Button) findViewById(R.id.midd_b);
		low_b = (Button) findViewById(R.id.low_b);
		half_b = (Button) findViewById(R.id.half_b);
		fy_b = (Button) findViewById(R.id.fy_b);
		if ("99999".equals(aid)) {
			high_b.setBackgroundResource(R.drawable.fenleiitem_tab);
			high_b.setTextColor(Color.WHITE);
		}
		tt = new ThemeThread(handler, MenuTheme.this, aid, page, sorted, false);
		tt.start();
	}

	private void setTopBar() {
		left2 = (Button) findViewById(R.id.title_btn_left2);
		left2.setText("返回");
		right1 = (Button) findViewById(R.id.title_btn_right1);
		right1.setText("书架");

		left2.setVisibility(View.VISIBLE);
		right1.setVisibility(View.VISIBLE);

		left2.setOnClickListener(this);
		right1.setOnClickListener(this);
	}

	// 点击折扣区的按钮  [high,middle,half,low,fy])
	public void discount_high(View v) {
		mWaitDg1 = ProgressDialog.show(MenuTheme.this, "正在加载数据...", "请稍后...", true, true);
		mWaitDg1.show();
		tt = new ThemeThread(handler, MenuTheme.this, aid, page, "high", true);
		tt.start();
		sorted = "high";
	}

	public void discount_middle(View v) {
		mWaitDg1 = ProgressDialog.show(MenuTheme.this, "正在加载数据...", "请稍后...", true, true);
		mWaitDg1.show();
		tt = new ThemeThread(handler, MenuTheme.this, aid, page, "middle", true);
		tt.start();
		sorted = "middle";
	}

	public void discount_half(View v) {
		mWaitDg1 = ProgressDialog.show(MenuTheme.this, "正在加载数据...", "请稍后...", true, true);
		mWaitDg1.show();
		tt = new ThemeThread(handler, MenuTheme.this, aid, page, "half", true);
		tt.start();
		sorted = "half";
	}

	public void discount_low(View v) {
		mWaitDg1 = ProgressDialog.show(MenuTheme.this, "正在加载数据...", "请稍后...", true, true);
		mWaitDg1.show();
		tt = new ThemeThread(handler, MenuTheme.this, aid, page, "low", true);
		tt.start();
		sorted = "low";
	}

	public void discount_fy(View v) {
		mWaitDg1 = ProgressDialog.show(MenuTheme.this, "正在加载数据...", "请稍后...", true, true);
		mWaitDg1.show();
		tt = new ThemeThread(handler, MenuTheme.this, aid, page, "fy", true);
		tt.start();
		sorted = "fy";
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_btn_left2:
			finish();
			break;
		case R.id.title_btn_right1:
			Intent intentR = new Intent(MenuTheme.this, MainActivity.class);
			intentR.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentR);
			finish();
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

	// 以下为分页显示代码
	private LinearLayout searchLayout = null;
	private boolean isAdd = false;
	private LinearLayout loadingLayout = null;

	public LinearLayout showLayout() {
		searchLayout = new LinearLayout(this);
		searchLayout.setOrientation(LinearLayout.HORIZONTAL);

		ProgressBar progressBar = new ProgressBar(this);
		progressBar.setPadding(0, 0, 15, 0);
		searchLayout.addView(progressBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		TextView textView = new TextView(this);
		textView.setText("加载中...");
		textView.setGravity(Gravity.CENTER_VERTICAL);
		searchLayout.addView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		searchLayout.setGravity(Gravity.CENTER);

		loadingLayout = new LinearLayout(this);
		loadingLayout.setGravity(Gravity.CENTER);

		return loadingLayout;
	}

	public void addLoadView() {
		if (isAdd) {
			return;
		}

		loadingLayout.addView(searchLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		page++;
		tt = new ThemeThread(handler, MenuTheme.this, aid, page, sorted, false);
		tt.start();
		isAdd = true;
	}

	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}
}
