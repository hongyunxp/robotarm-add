package com.eastedge.readnovel.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.adapters.NewbookAdapter;
import com.eastedge.readnovel.beans.NewBook;
import com.eastedge.readnovel.beans.SearchResult;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.HCData;
import com.eastedge.readnovel.threads.HottahThread;
import com.eastedge.readnovel.threads.SearchThread;
import com.xs.cn.R;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.activitys.LoginActivity;
import com.xs.cn.activitys.MainActivity;
import com.xs.cn.activitys.Novel_sbxxy;

public class MenuSearchFragment extends Fragment implements OnClickListener {
	private EditText search_et;
	private ImageView search_im;
	private TextView search_tv;
	private ListView search_listView;
	private NewbookAdapter adapter;
	private SearchResult list1 = null;
	private LinearLayout loadingLayout = null;
	private Button left1;
	private Button right1;
	private LinearLayout menu_search_ly_lv, menu_search_ly_gjz, ly1, ly2, menu_search_ly_num, menu_search_ly_gdch;
	private Button menu_bt_gdch;
	private View menu_search_xian;
	private ArrayList<Integer> colorList = new ArrayList<Integer>();
	SearchThread st;
	HottahThread ht;
	int z = 1;
	ProgressDialog mWaitDg1 = null;
	private String word1;
	private String word2;
	private boolean dj;
	private int zong;
	private Button login;
	private User user;

	public Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 111:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				//				setRefreshSearch(true);
				list1 = st.list;
				menu_search_ly_gjz.setVisibility(View.GONE);
				menu_search_ly_gdch.setVisibility(View.GONE);
				menu_search_ly_lv.setVisibility(View.VISIBLE);
				menu_search_ly_num.setVisibility(View.VISIBLE);
				menu_search_xian.setVisibility(View.VISIBLE);
				if (list1.getBookList() == null || list1.getBookList().size() < 10) {
					loadingLayout.removeAllViews();
				} else {
					loadingLayout.removeAllViews();
					isAdd = false;
				}

				if (st.list.isNoResult() == false) {
					search_tv.setTextSize(15);
					zong = list1.getBkcount();
					search_tv.setText("(搜索到" + zong + "部作品)");
				} else {
					LinearLayout.LayoutParams bt = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 90);
					menu_search_ly_num.setLayoutParams(bt);
					search_tv.setTextSize(12);
					search_tv.setText("   没有搜索到符合条件的小说，请尝试其他关键字搜索。\n" + "    " + "推荐您阅读以下与搜索关键字类型相同的经典小说。");
				}
				adapter = new NewbookAdapter(getActivity(), list1.getBookList());
				search_listView.setAdapter(adapter);

				break;
			case 222:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				//				setRefreshSearch(true);
				list1.getBookList().addAll(st.list.getBookList());

				if (st.list.getBookList().size() < 10) {
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
			}
		};
	};

	public Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				HCData.wordList = ht.list;
				draw();
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_search, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		menu_search_ly_num = (LinearLayout) getActivity().findViewById(R.id.menu_search_ly_num);
		menu_search_ly_lv = (LinearLayout) getActivity().findViewById(R.id.menu_search_ly_lv);
		menu_search_ly_gjz = (LinearLayout) getActivity().findViewById(R.id.menu_search_ly_gjz);
		menu_search_ly_gdch = (LinearLayout) getActivity().findViewById(R.id.menu_search_ly_gdch);
		menu_search_xian = (View) getActivity().findViewById(R.id.menu_search_xian);

		ly1 = (LinearLayout) getActivity().findViewById(R.id.ly1);
		ly2 = (LinearLayout) getActivity().findViewById(R.id.ly2);

		menu_bt_gdch = (Button) getActivity().findViewById(R.id.menu_bt_gdch);
		search_et = (EditText) getActivity().findViewById(R.id.search_et);
		search_im = (ImageView) getActivity().findViewById(R.id.search_im);
		search_tv = (TextView) getActivity().findViewById(R.id.search_tv);
		search_listView = (ListView) getActivity().findViewById(R.id.search_listView);
		search_listView.addFooterView(showLayout());

		search_listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				NewBook info = list1.getBookList().get(position);
				bundle.putString("Articleid", info.getArticleid());
				bundle.putString("Sortname", info.getSortname());
				intent.putExtra("newbook", bundle);
				intent.setClass(getActivity(), Novel_sbxxy.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}

		});
		// 滚动监听
		search_listView.setOnScrollListener(new ListView.OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && view.getLastVisiblePosition() == (view.getCount() - 1)) {

					if (st != null && st.list != null && view.getCount() < st.list.getBkcount()) {
						addLoadView();
					}
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});

		ht = new HottahThread(getActivity(), mhandler);
		ht.start();

		menu_bt_gdch.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				draw();
			}

		});

		search_im.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				z = 1;
				if (search_et.getText().toString().trim().equals("")) {
					Toast.makeText(getActivity(), "请输入或选择关键字", Toast.LENGTH_SHORT).show();
				} else {
					mWaitDg1 = ProgressDialog.show(getActivity(), "正在加载数据...", "请稍候...", true, true);
					mWaitDg1.show();
					st = new SearchThread(getActivity(), handler, search_et.getText().toString(), z);
					st.start();
				}
			}

		});
	}

	public void draw() {
		ly1.removeAllViews();
		ly2.removeAllViews();

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int h = display.getHeight();
		int w = display.getWidth();

		String[] str = getResources().getStringArray(R.array.color);

		int a = 7, b = 14;
		if (HCData.wordList.size() < 14) {
			a = HCData.wordList.size() / 2;
			b = HCData.wordList.size();
		}
		Collections.shuffle(HCData.wordList); // 随机的方法

		for (int i = 0; i < str.length; i++) {
			Color color = new Color();
			colorList.add(color.parseColor(str[i]));
		}

		Random ran = new Random();
		int v = ran.nextInt(2);

		for (int i = 0; i < a; i++) {

			final int f = i;
			TextView tx1 = new TextView(getActivity());
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			tx1.setText(HCData.wordList.get(i));
			tx1.setTextSize(20);
			int l = 0, t = 0, r = 0, d = 0;
			if (i % 2 == v) {
				l = ran.nextInt(w / 12) + w / 12;
				t = ran.nextInt(h / 32);
				lp.setMargins(l, t, r, d);
			} else {
				l = ran.nextInt(w / 6) + w / 6;
				t = ran.nextInt(h / 32) + w / 50;
				lp.setMargins(l, t, r, d);
			}
			tx1.setLayoutParams(lp);
			tx1.setTextColor(colorList.get(ran.nextInt(colorList.size())));
			ly1.addView(tx1);
			tx1.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					z = 1;
					word1 = HCData.wordList.get(f);
					dj = true;
					search_et.setText("");
					mWaitDg1 = ProgressDialog.show(getActivity(), "正在加载数据...", "请稍候...", true, true);
					mWaitDg1.show();

					st = new SearchThread(getActivity(), handler, word1, z);
					search_et.setText(word1);
					st.start();
				}
			});
		}

		for (int i = a; i < b; i++) {
			final int f = i;

			TextView tx1 = new TextView(getActivity());
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			tx1.setText(HCData.wordList.get(i));
			tx1.setTextSize(20);
			int l = 0, t = 0, r = 0, d = 0;
			if (i % 2 != v) {
				l = ran.nextInt(w / 12) + w / 12;
				t = ran.nextInt(h / 30);
				lp.setMargins(l, t, r, d);
			} else {
				l = ran.nextInt(w / 6) + w / 6;
				t = ran.nextInt(h / 30) + w / 50;
				lp.setMargins(l, t, r, d);
			}
			tx1.setLayoutParams(lp);
			tx1.setTextColor(colorList.get(ran.nextInt(colorList.size())));
			ly2.addView(tx1);
			tx1.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					z = 1;
					word2 = HCData.wordList.get(f);
					dj = false;
					search_et.setText("");
					mWaitDg1 = ProgressDialog.show(getActivity(), "正在加载数据...", "请稍候...", true, true);
					mWaitDg1.show();

					st = new SearchThread(getActivity(), handler, word2, z);
					search_et.setText(word2);
					st.start();
				}
			});
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setTopBar();
	}

	/**
	 * 设置标题栏的显示
	 */
	private void setTopBar() {
		RelativeLayout rl = (RelativeLayout) getActivity().findViewById(R.id.search_top_bar);

		if (rl == null)
			return;

		left1 = (Button) rl.findViewById(R.id.title_btn_left1);
		left1.setText("登录");
		right1 = (Button) rl.findViewById(R.id.title_btn_right1);
		right1.setText("书架");
		TextView title_tv = (TextView) rl.findViewById(R.id.title_tv);
		title_tv.setText("搜索");

		login = (Button) getActivity().findViewById(R.id.title_btn_logined);
		user = BookApp.getUser();
		if (user != null && user.getUid() != null) {
			left1.setVisibility(View.GONE);
			login.setVisibility(View.VISIBLE);
		} else {
			login.setVisibility(View.GONE);
			left1.setVisibility(View.VISIBLE);
		}

		right1.setVisibility(View.VISIBLE);

		left1.setOnClickListener(this);
		right1.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == left1.getId()) {
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else if (v.getId() == right1.getId()) {
			((MainActivity) getActivity()).goToBookShelf();
		} else if (v.getId() == R.id.title_btn_logined) {
			((MainActivity) getActivity()).goToUserCenter();
			// finish();
		}
	}

	// 以下为分页显示代码
	private LinearLayout searchLayout = null;
	private boolean isAdd = false;

	public LinearLayout showLayout() {
		searchLayout = new LinearLayout(getActivity());
		searchLayout.setOrientation(LinearLayout.HORIZONTAL);

		ProgressBar progressBar = new ProgressBar(getActivity());
		progressBar.setPadding(0, 0, 15, 0);
		searchLayout.addView(progressBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		TextView textView = new TextView(getActivity());
		textView.setText("加载中...");
		textView.setGravity(Gravity.CENTER_VERTICAL);
		searchLayout.addView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		searchLayout.setGravity(Gravity.CENTER);

		loadingLayout = new LinearLayout(getActivity());
		loadingLayout.setGravity(Gravity.CENTER);

		return loadingLayout;
	}

	public void addLoadView() {
		if (isAdd) {
			return;
		}

		loadingLayout.addView(searchLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		z++;
		if (search_et.getText().toString().trim().equals("")) {
			if (dj == true) {
				st = new SearchThread(getActivity(), handler, word1, z);
			} else {
				st = new SearchThread(getActivity(), handler, word2, z);
			}
		} else {
			st = new SearchThread(getActivity(), handler, search_et.getText().toString(), z);
		}
		st.start();
		isAdd = true;
	}

	//	@Override
	//	public boolean onKeyDown(int keyCode, KeyEvent event) {
	//
	//		if (KeyEvent.KEYCODE_BACK == keyCode) {
	//			MainGroupActivity m = (MainGroupActivity) getActivity().getParent();
	//			if (m.isRefreshSearch()) {
	//				m.doBtn5();
	//				return true;
	//			}
	//		}
	//		return super.onKeyDown(keyCode, event);
	//	}

	//	private void setRefreshSearch(boolean flag) {
	//		MainGroupFragment m = (MainGroupFragment) getParentFragment();
	//		m.setRefreshSearch(flag);
	//	}

}
