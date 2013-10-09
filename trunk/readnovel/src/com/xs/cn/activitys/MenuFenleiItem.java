package com.xs.cn.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.adapters.NewbookAdapter;
import com.eastedge.readnovel.beans.FenleiList;
import com.eastedge.readnovel.beans.NewBook;
import com.eastedge.readnovel.beans.ZiFenleiList;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.threads.FenleiItemThread;
import com.eastedge.readnovel.threads.ZiFenleiThread;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

public class MenuFenleiItem extends Activity implements OnClickListener {
	private Button right1;
	private TextView left2;
	private ListView fenlei_item_listView;
	private NewbookAdapter adapter;
	private FenleiList list;
	private FenleiList list1;
	// private RadioButton radio0, radio1, radio2, radio3, radio4;
	private String SortId;
	private FenleiItemThread fli;
	private Intent intent;
	private TextView fenlei_item_num;
	private String title;
	private LinearLayout loadingLayout = null;
	int i = 1;
	int a = 1;
	ProgressDialog mWaitDg1 = null;
	private ZiFenleiThread ZFl;
	// private Button button;
	private LinearLayout fenlei_item_zibtn;
	private ZiFenleiList listZi = null;
	private ZiFenleiList listZi1 = null;
	private String SortIdZi;
	private boolean Fenlei = true;
	private Button nowcheckbtn;

	public Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 111:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();

					HorizontalScrollView view1 = (HorizontalScrollView) findViewById(R.id.fenlei_view1);
					LinearLayout view2 = (LinearLayout) findViewById(R.id.linearLayout2);
					ImageView view3 = (ImageView) findViewById(R.id.fenlei_view3);
					view1.setVisibility(View.VISIBLE);
					view2.setVisibility(View.VISIBLE);
					view3.setVisibility(View.VISIBLE);
				}
				Fenlei = true;
				list = fli.list;
				adapter = new NewbookAdapter(MenuFenleiItem.this, list.getFllist());
				fenlei_item_listView.setAdapter(adapter);
				fenlei_item_num.setText("(共有" + list.getBkcount() + "部作品)");

				for (int j = 0; j < list.getChildFl().size(); j++) {
					final int f = j;
					final Button button = new Button(MenuFenleiItem.this);
					LinearLayout.LayoutParams bt = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 45);
					button.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							a = 1;
							listZi = null;
							listZi1 = null;

							SortIdZi = list.getChildFl().get(f).getSortId();

							if (nowcheckbtn != null) {
								nowcheckbtn.setBackgroundResource(R.drawable.fenleiitem_tabkb);
								nowcheckbtn.setTextColor(Color.BLACK);
							}
							button.setBackgroundResource(R.drawable.fenleiitem_tab);
							button.setTextColor(Color.WHITE);
							nowcheckbtn = (Button) v;
							mWaitDg1 = ProgressDialog.show(MenuFenleiItem.this, "正在加载数据...", "请稍候...", true, true);
							mWaitDg1.show();
							ZFl = new ZiFenleiThread(MenuFenleiItem.this, mhandler, a, SortIdZi);
							ZFl.start();
						}
					});
					bt.leftMargin = 8;
					bt.rightMargin = 8;
					button.setLayoutParams(bt);
					button.setText(list.getChildFl().get(j).getTitle());
					button.setPadding(0, 0, 0, 0);
					button.setTextSize(14);
					button.setBackgroundResource(R.drawable.fenleiitem_tabkb);
					fenlei_item_zibtn.addView(button);
				}

				break;
			case 222:
				list1 = fli.list;
				list.getFllist().addAll(list1.getFllist());
				// if (list1.getFllist().size() < 30)
				// {
				// loadingLayout.removeAllViews();
				// }
				// else
				// {
				// loadingLayout.removeAllViews();
				// loadingLayout.addView(loadMoreBtn);
				// }
				if (list1.getFllist().size() < 30) {
					loadingLayout.removeAllViews();
				} else {
					loadingLayout.removeAllViews();
					isAdd = false;
				}
				adapter.notifyDataSetChanged();
				break;
			case 333:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				Toast.makeText(MenuFenleiItem.this, getString(R.string.network_err), Toast.LENGTH_SHORT).show();
				handler.postDelayed(new Runnable() {

					public void run() {
						Intent intent = new Intent(MenuFenleiItem.this, MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
					}
				}, 1000);
			}
		};
	};

	public Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 1:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				Fenlei = false;
				listZi = ZFl.list;
				fenlei_item_num.setText("(共有" + listZi.getBkcount() + "部作品)");
				adapter = new NewbookAdapter(MenuFenleiItem.this, listZi.getBklist());
				fenlei_item_listView.setAdapter(adapter);
				break;
			case 2:
				listZi1 = ZFl.list;
				listZi.getBklist().addAll(listZi1.getBklist());
				if (listZi1.getBklist().size() < 30) {
					loadingLayout.removeAllViews();
				} else {
					loadingLayout.removeAllViews();
					isAdd = false;
				}
				adapter.notifyDataSetChanged();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_fenlei_item);
		CloseActivity.add(this);

		intent = getIntent();
		Bundle bundle = intent.getBundleExtra("conditions");
		SortId = bundle.getString("sortId");
		title = bundle.getString("title");

		mWaitDg1 = ProgressDialog.show(MenuFenleiItem.this, "正在加载数据...", "请稍候...", true, true);
		mWaitDg1.show();

		fli = new FenleiItemThread(MenuFenleiItem.this, handler, SortId, i);
		fli.start();

		fenlei_item_num = (TextView) findViewById(R.id.fenlei_item_num);
		setTopBar();

		fenlei_item_listView = (ListView) findViewById(R.id.fenlei_item_listView);
		fenlei_item_listView.addFooterView(showLayout());

		fenlei_item_zibtn = (LinearLayout) findViewById(R.id.fenlei_item_zibtn);

		fenlei_item_listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				NewBook info = null;
				if (Fenlei == false) {
					info = listZi.getBklist().get(position);
				} else {
					info = list.getFllist().get(position);
				}

				// bundle.putString("Author", info.getAuthor());
				// bundle.putString("Articleid", info.getArticleid());
				// bundle.putString("Finishflag", info.getFinishflag());
				// bundle.putString("Sortname", info.getSortname());
				// bundle.putString("Title", info.getTitle());
				// bundle.putString("Totalviews", info.getTotalviews());
				// Constants.drawable = info.getImgDrawable();
				bundle.putString("Articleid", info.getArticleid());
				bundle.putString("Sortname", info.getSortname());
				intent.putExtra("newbook", bundle);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(MenuFenleiItem.this, Novel_sbxxy.class);
				startActivity(intent);
			}

		});

		fenlei_item_listView.setOnScrollListener(new ListView.OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && view.getLastVisiblePosition() == (view.getCount() - 1)) {
					addLoadView();
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
	}

	private void setTopBar() {
		left2 = (Button) findViewById(R.id.title_btn_left2);
		left2.setText("  返回");

		right1 = (Button) findViewById(R.id.title_btn_right1);
		right1.setText("书架");
		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText(title);

		left2.setVisibility(View.VISIBLE);
		right1.setVisibility(View.VISIBLE);

		left2.setOnClickListener(this);
		right1.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == left2.getId()) {
			finish();
		} else if (v.getId() == right1.getId()) {
			Intent intent = new Intent(MenuFenleiItem.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

	// 以下为分页显示代码
	private LinearLayout searchLayout = null;
	private boolean isAdd = false;

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
		if (Fenlei == true) {
			i++;
			fli = new FenleiItemThread(MenuFenleiItem.this, handler, SortId, i);
			fli.start();
		}
		if (Fenlei == false) {
			a++;
			ZFl = new ZiFenleiThread(MenuFenleiItem.this, mhandler, a, SortIdZi);
			ZFl.start();
		}
		isAdd = true;
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
