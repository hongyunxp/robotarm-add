package com.eastedge.readnovel.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.adapters.NewbookAdapter;
import com.eastedge.readnovel.beans.NewBook;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.HCData;
import com.eastedge.readnovel.threads.NewBookThread;
import com.xs.cn.R;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.activitys.LoginActivity;
import com.xs.cn.activitys.MainActivity;
import com.xs.cn.activitys.Novel_sbxxy;

public class MenuNewBookFragment extends Fragment implements OnClickListener {
	private ListView newbook_listView;
	private RadioButton newbook_gril;
	private RadioButton newbook_boy;
	private RadioButton newbook_shcool;
	private NewbookAdapter Adapter;
	private LinearLayout loadingLayout = null;
	private Button left1;
	private RadioGroup mRadioGroup;
	private Button right1;
	private TextView xinshu_num;
	ProgressDialog mWaitDg1 = null;
	NewBookThread nb;
	private int b = 1;
	private int g = 1;
	private int s = 1;
	private int type = 1;
	private Button login;
	private User user;
	private LinearLayout view1, view3;
	private ImageView view2, view4;

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 111:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();

					view1.setVisibility(View.VISIBLE);
					view2.setVisibility(View.VISIBLE);
					view3.setVisibility(View.VISIBLE);
					view4.setVisibility(View.VISIBLE);
				}
				if (nb.list.getBklist().size() < 20) {
					loadingLayout.removeAllViews();
				} else {
					loadingLayout.removeAllViews();
					isAdd = false;
					// loadingLayout.addView(loadMoreBtn);
				}
				if (type == 1) {

					HCData.listgirl = nb.list;
					xinshu_num.setText("(共有" + HCData.listgirl.getBkcount()
							+ "部作品)");
					Adapter = new NewbookAdapter(getActivity(),
							HCData.listgirl.getBklist());
					newbook_listView.setAdapter(Adapter);
				}
				if (type == 2) {
					HCData.listboy = nb.list;

					xinshu_num.setText("(共有" + HCData.listboy.getBkcount()
							+ "部作品)");
					Adapter = new NewbookAdapter(getActivity(),
							HCData.listboy.getBklist());
					newbook_listView.setAdapter(Adapter);
				}
				if (type == 3) {
					HCData.listschool = nb.list;

					xinshu_num.setText("(共有" + HCData.listschool.getBkcount()
							+ "部作品)");
					Adapter = new NewbookAdapter(getActivity(),
							HCData.listschool.getBklist());
					newbook_listView.setAdapter(Adapter);
				}
				break;
			case 222:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				if (nb.list != null && nb.list.getBklist().size() < 20) {
					loadingLayout.removeAllViews();
				} else {
					loadingLayout.removeAllViews();
					isAdd = false;
				}
				if (type == 1) {
					HCData.listgirl.getBklist().addAll(nb.list.getBklist());
				}
				if (type == 2) {
					HCData.listboy.getBklist().addAll(nb.list.getBklist());
				}

				if (type == 3) {
					HCData.listschool.getBklist().addAll(nb.list.getBklist());
				}
				Adapter.notifyDataSetChanged();
				break;
			case 333:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				Toast.makeText(getActivity(), getString(R.string.network_err),
						Toast.LENGTH_SHORT).show();
				handler.postDelayed(new Runnable() {

					public void run() {
						((MainActivity) getActivity()).goToBookShelf();
					}
				}, 1000);

			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_newbook, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		xinshu_num = (TextView) getActivity().findViewById(R.id.xinshu_num);
		mRadioGroup = (RadioGroup) getActivity().findViewById(R.id.radioGroup);
		newbook_gril = (RadioButton) getActivity().findViewById(
				R.id.newbook_gril);
		newbook_boy = (RadioButton) getActivity()
				.findViewById(R.id.newbook_boy);
		newbook_shcool = (RadioButton) getActivity().findViewById(
				R.id.newbook_shcool);
		newbook_gril.setTextColor(Color.BLACK);
		newbook_boy.setTextColor(Color.rgb(153, 153, 153));
		newbook_shcool.setTextColor(Color.rgb(153, 153, 153));

		newbook_listView = (ListView) getActivity().findViewById(
				R.id.newbook_listView);
		newbook_listView.addFooterView(showLayout());

		view1 = (LinearLayout) getActivity().findViewById(R.id.linearLayout1);
		view2 = (ImageView) getActivity().findViewById(R.id.newbook_view2);
		view4 = (ImageView) getActivity().findViewById(R.id.newbook_view4);
		view3 = (LinearLayout) getActivity().findViewById(R.id.linearLayout2);

		if (HCData.listgirl.getBklist() != null
				&& HCData.listgirl.getBklist().size() > 0) {
			xinshu_num.setText("（共有" + HCData.listgirl.getBkcount() + "部作品）");
			Adapter = new NewbookAdapter(getActivity(),
					HCData.listgirl.getBklist());
			newbook_listView.setAdapter(Adapter);
		} else {
			view1.setVisibility(View.GONE);
			view2.setVisibility(View.GONE);
			view3.setVisibility(View.GONE);
			view4.setVisibility(View.GONE);

			mWaitDg1 = ProgressDialog.show(getActivity(), "正在加载数据...",
					"请稍候...", true, true);
			mWaitDg1.show();
			nb = new NewBookThread(getActivity(), handler, g, type);
			nb.start();
		}

		// 点击女生新书
		newbook_gril.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				type = 1;
				newbook_gril.setTextColor(Color.BLACK);
				newbook_boy.setTextColor(Color.rgb(153, 153, 153));
				newbook_shcool.setTextColor(Color.rgb(153, 153, 153));
				if (HCData.listgirl.getBklist() != null
						&& HCData.listgirl.getBklist().size() > 0) {
					xinshu_num.setText("（共有" + HCData.listgirl.getBkcount()
							+ "部作品）");
					Adapter = new NewbookAdapter(getActivity(), HCData.listgirl
							.getBklist());
					newbook_listView.setAdapter(Adapter);
				} else {
					mWaitDg1 = ProgressDialog.show(getActivity(), "正在加载数据...",
							"请稍候...", true, true);
					mWaitDg1.show();
					nb = new NewBookThread(getActivity(), handler, g, type);
					nb.start();
				}

			}

		});

		// 点击男生新书
		newbook_boy.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				type = 2;
				newbook_boy.setTextColor(Color.BLACK);
				newbook_shcool.setTextColor(Color.rgb(153, 153, 153));
				newbook_gril.setTextColor(Color.rgb(153, 153, 153));
				if (HCData.listboy.getBklist() != null
						&& HCData.listboy.getBklist().size() > 0) {
					xinshu_num.setText("（共有" + HCData.listboy.getBkcount()
							+ "部作品）");
					Adapter = new NewbookAdapter(getActivity(), HCData.listboy
							.getBklist());
					newbook_listView.setAdapter(Adapter);
				} else {
					mWaitDg1 = ProgressDialog.show(getActivity(), "正在加载数据...",
							"请稍候...", true, true);
					mWaitDg1.show();
					nb = new NewBookThread(getActivity(), handler, b, type);
					nb.start();
				}
			}

		});

		// 点击校园新书
		newbook_shcool.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				type = 3;
				newbook_shcool.setTextColor(Color.BLACK);
				newbook_gril.setTextColor(Color.rgb(153, 153, 153));
				newbook_boy.setTextColor(Color.rgb(153, 153, 153));
				if (HCData.listschool.getBklist() != null
						&& HCData.listschool.getBklist().size() > 0) {
					xinshu_num.setText("（共有" + HCData.listschool.getBkcount()
							+ "部作品）");
					Adapter = new NewbookAdapter(getActivity(),
							HCData.listschool.getBklist());
					newbook_listView.setAdapter(Adapter);
				} else {
					mWaitDg1 = ProgressDialog.show(getActivity(), "正在加载数据...",
							"请稍候...", true, true);
					mWaitDg1.show();
					nb = new NewBookThread(getActivity(), handler, s, type);
					nb.start();
				}

			}

		});

		// 点击listview的item项进入书本明细页面
		newbook_listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				if (type == 1) {
					NewBook info = HCData.listgirl.getBklist().get(position);
					bundle.putString("Articleid", info.getArticleid());
				}
				if (type == 2) {
					NewBook info = HCData.listboy.getBklist().get(position);
					bundle.putString("Articleid", info.getArticleid());
				}
				if (type == 3) {
					NewBook info = HCData.listschool.getBklist().get(position);
					bundle.putString("Articleid", info.getArticleid());
				}
				intent.putExtra("newbook", bundle);
				intent.setClass(getActivity(), Novel_sbxxy.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}

		});

		// 监听listview滑动事件
		newbook_listView.setOnScrollListener(new ListView.OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& view.getLastVisiblePosition() == (view.getCount() - 1)) {
					addLoadView();
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		setTopBar();
	}

	private void setTopBar() {
		RelativeLayout rl = (RelativeLayout) getActivity().findViewById(
				R.id.newbook_top_bar);

		if (rl == null)
			return;

		left1 = (Button) rl.findViewById(R.id.title_btn_left1);
		left1.setText("登录");
		right1 = (Button) rl.findViewById(R.id.title_btn_right1);
		right1.setText("书架");
		TextView title_tv = (TextView) rl.findViewById(R.id.title_tv);
		title_tv.setText("新书崛起");

		login = (Button) getActivity().findViewById(R.id.title_btn_logined);
		user = BookApp.getUser();
		if (user != null && user.getUid() != null) {
			left1.setVisibility(View.GONE);
			login.setVisibility(View.VISIBLE);
		} else {
			left1.setVisibility(View.VISIBLE);
			login.setVisibility(View.GONE);
		}

		right1.setVisibility(View.VISIBLE);

		left1.setOnClickListener(this);
		right1.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	// 以下为分页显示代码
	private LinearLayout searchLayout = null;
	private boolean isAdd = false;

	public LinearLayout showLayout() {
		searchLayout = new LinearLayout(getActivity());
		searchLayout.setOrientation(LinearLayout.HORIZONTAL);

		ProgressBar progressBar = new ProgressBar(getActivity());
		progressBar.setPadding(0, 0, 15, 0);
		searchLayout.addView(progressBar, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		TextView textView = new TextView(getActivity());
		textView.setText("加载中...");
		textView.setGravity(Gravity.CENTER_VERTICAL);
		searchLayout.addView(textView, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		searchLayout.setGravity(Gravity.CENTER);

		loadingLayout = new LinearLayout(getActivity());
		loadingLayout.setGravity(Gravity.CENTER);

		return loadingLayout;
	}

	public void addLoadView() {
		if (isAdd) {
			return;
		}

		loadingLayout.addView(searchLayout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		if (type == 1) {
			g++;

			// g为页码，type为书的类型
			nb = new NewBookThread(getActivity(), handler, g, type);
			nb.start();
		}
		if (type == 2) {
			b++;
			nb = new NewBookThread(getActivity(), handler, b, type);
			nb.start();
		}
		if (type == 3) {
			s++;
			nb = new NewBookThread(getActivity(), handler, s, type);
			nb.start();
		}
		isAdd = true;
	}

	public void onClick(View v) {
		if (v.getId() == left1.getId()) {
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
		} else if (v.getId() == right1.getId()) {
			((MainActivity) getActivity()).goToBookShelf();
		} else if (v.getId() == R.id.title_btn_logined) {
			((MainActivity) getActivity()).goToUserCenter();
			// finish();
		}
	}

}
