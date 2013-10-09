package com.eastedge.readnovel.fragment;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.adapters.CategrayAdapter;
import com.eastedge.readnovel.adapters.GalleryAdapter;
import com.eastedge.readnovel.adapters.TuijianAdapter;
import com.eastedge.readnovel.beans.Image;
import com.eastedge.readnovel.beans.Tuijian;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.HCData;
import com.eastedge.readnovel.threads.TuijianThread;
import com.eastedge.readnovel.view.DetialGallery;
import com.xs.cn.R;
import com.xs.cn.activitys.MainActivity;
import com.xs.cn.activitys.MenuTheme;
import com.xs.cn.activitys.Novel_sbxxy;

/**
 * 推荐
 * 
 * @author li.li
 * 
 *         Aug 30, 2013
 */
public class MenuTuijianFragment extends Fragment implements OnClickListener {
	private ListView listView;
	private TuijianAdapter adapt;
	private GalleryAdapter adapter;
	private ArrayList<Tuijian> al = null;
	private Button left1, right1, login;
	private TuijianThread t;
	private DetialGallery gallery;
	private LinearLayout ly;
	private ArrayList<ImageView> dianList = new ArrayList<ImageView>();
	private int lastP;
	private int time;
	private boolean finish;
	private String aid;
	private ProgressDialog mWaitDg1 = null;
	private View view3;
	private ArrayList<Image> ali;
	private User user;
	private ImageView img;
	private int sortid;
	// ///////////////////////////////////////////////////////
	private ListView listview_left, listview_right;
	private ArrayList<String> categray = new ArrayList<String>();
	private CategrayAdapter cAaAdapter;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				// 推荐列表数据获取成功
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
					// img.setVisibility(View.VISIBLE);
					// view3.setVisibility(View.VISIBLE);
				}
				al = t.al;
				if (al.size() >= 20) {
					Toast.makeText(getActivity(), "嗯，不错，》20",
							Toast.LENGTH_SHORT).show();
				}
				// 判断是否是可以分页显示的
				if (al.size() < 20) {
					loadingLayout.removeAllViews();
				} else {
					loadingLayout.removeAllViews();
					isAdd = false;
					// loadingLayout.addView(loadMoreBtn);
				}

				adapt = new TuijianAdapter(getActivity(), al);
				// 设置推荐列表Adapter
				listview_right.setAdapter(adapt);
				// case 123:
				// try {
				// // 自动向后跳转图片
				// gallery.setSelection((gallery.getSelectedItemPosition() + 1)
				// % dianList.size());
				// } catch (Exception e) {
				// LogUtils.error(e.getMessage(), e);
				// }
				// // 重置时间
				// time = 0;
				break;
			case 101:
				// 加载更多需要的操作
				// if (al.size() < 20) {
				// loadingLayout.removeAllViews();
				// } else {
				// loadingLayout.removeAllViews();
				// isAdd = false;
				// // loadingLayout.addView(loadMoreBtn);
				// }

				adapt.notifyDataSetChanged();
				break;
			case 102:
				// 加载更多需要的操作
				isAdd = false;
				loadingLayout.removeAllViews();

				adapt.notifyDataSetChanged();
				break;
			case 444:
				// 点击列表item进入主题页面
				if (msg.obj == null)
					return;
				aid = msg.obj.toString();
				if (msg.arg1 != 1) {
					Intent intent = new Intent(getActivity(), MenuTheme.class);
					intent.putExtra("aid", aid);
					intent.putExtra("sorted", "high");// 默认男生专区
					intent.putExtra("page", 1);// 默认第一页
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(), Novel_sbxxy.class);
					Bundle bundle = new Bundle();
					bundle.putString("Articleid", aid);
					intent.putExtra("newbook", bundle);
					startActivity(intent);
				}

				break;
			case 44:// 网络不给力
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
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_tuijian, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 图片的展示区域
		// gallery = (DetialGallery) getActivity().findViewById(R.id.gallery);
		// listView = (ListView) getActivity().findViewById(
		// R.id.menu_tuijian_listview);
		// img = (ImageView) getActivity().findViewById(R.id.tuijian_view2);
		// view3 = (View) getActivity().findViewById(R.id.tuijian_view3);
		// listView.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// Message msg = new Message();
		// msg.what = 444;
		// msg.obj = al.get(position).getId();
		// handler.sendMessage(msg);
		// }
		// });
		// // 判断缓存是否存在推荐列表数据
		// if (HCData.mTuijianMain == null) {
		// img.setVisibility(View.GONE);
		// view3.setVisibility(View.GONE);
		// // 如果缓存数据为空 启动线程获取列表数据 ，弹出等待框
		// mWaitDg1 = ProgressDialog.show(getActivity(), "正在加载数据...",
		// "请稍候...", true, true);
		// mWaitDg1.show();
		// t = new TuijianThread(handler, getActivity());
		// t.start();
		// } else {
		// // 加载缓存数据
		// al = HCData.mTuijianMain.getTuijianList();
		// adapt = new TuijianAdapter(getActivity(), al);
		// listView.setAdapter(adapt);
		// setGallery();
		// }
		// setTopBar();
		// /////////////////////////////////////////////////////////////////////////////
		categray.add("精品");
		categray.add("热门");
		categray.add("推荐");
		categray.add("玄幻");
		categray.add("武侠");
		categray.add("都市");
		categray.add("言情");
		categray.add("官场");
		categray.add("乡土");
		categray.add("军事");
		categray.add("历史");
		categray.add("限免");
		cAaAdapter = new CategrayAdapter(getActivity(), categray, 5);
		listview_left = (ListView) getActivity().findViewById(
				R.id.menu_tuijian_listview_left);
		listview_left.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				cAaAdapter.selected = arg2;
				sortid = arg2;
				// 弹出提示对话框
				mWaitDg1 = ProgressDialog.show(getActivity(), "正在加载数据...",
						"请稍候...", true, true);
				mWaitDg1.show();
				// 调用线程去加载数据
				t = new TuijianThread(handler, getActivity(), arg2, 1);
				t.start();
			}
		});
		listview_right = (ListView) getActivity().findViewById(
				R.id.menu_tuijian_listview_right);
		listview_right.addFooterView(showLayout());
		listview_right.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 跳转到详情界面，二级菜单
				Intent gotoNovel_sbxxyIntent = new Intent(getActivity(),
						Novel_sbxxy.class);
				String book_id = al.get(arg2).getId();
				String book_name = al.get(arg2).getTitle();
				String book_author = al.get(arg2).getAuther();
				// String book_descible = al.get(arg2).getAuther();
				gotoNovel_sbxxyIntent.putExtra("book_id", book_id);
				gotoNovel_sbxxyIntent.putExtra("book_name", book_name);
				gotoNovel_sbxxyIntent.putExtra("book_author", book_author);
				getActivity().startActivity(gotoNovel_sbxxyIntent);
			}
		});
		// 监听listview滑动事件
		listview_right.setOnScrollListener(new ListView.OnScrollListener() {

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
		listview_left.setAdapter(cAaAdapter);

		// 判断缓存是否存在推荐列表数据
		if (HCData.mTuijianMain == null) {
			// 如果缓存数据为空 启动线程获取列表数据 ，弹出等待框
			mWaitDg1 = ProgressDialog.show(getActivity(), "正在加载数据...",
					"请稍候...", true, true);
			mWaitDg1.show();
			t = new TuijianThread(handler, getActivity(), 3, 1);
			sortid = 3;
			t.start();
		} else {
			// 加载缓存数据
			al = HCData.mTuijianMain.getTuijianList();
			adapt = new TuijianAdapter(getActivity(), al);
			listview_right.setAdapter(adapt);
		}
	}

	// private OnItemSelectedListener myItemListener = new
	// OnItemSelectedListener() {
	// public void onItemSelected(AdapterView<?> parent, View view,
	// int position, long id) {
	// // 把最后一次的点设置为 未选中图片 ，把当前的点设置为选中图片 把当前的点赋给最后次点 重置自动跳转的时间
	// dianList.get(lastP).setImageResource(R.drawable.m1);
	// dianList.get(position).setImageResource(R.drawable.m2);
	// lastP = position;
	// time = 0;
	// gallery.setSelection((gallery.getSelectedItemPosition())
	// % dianList.size());
	// }
	//
	// public void onNothingSelected(AdapterView<?> parent) {
	// }
	//
	// };

	/**
	 * 设置标题栏的显示
	 */
	// private void setTopBar() {
	// RelativeLayout mrl = (RelativeLayout) getActivity().findViewById(
	// R.id.tuijian_top_bar);
	//
	// if (mrl == null)
	// return;
	//
	// left1 = (Button) mrl.findViewById(R.id.title_btn_left1);
	// left1.setText("登录");
	// right1 = (Button) mrl.findViewById(R.id.title_btn_right1);
	// right1.setText("书架");
	// TextView title_tv = (TextView) mrl.findViewById(R.id.title_tv);
	// title_tv.setText("特别推荐");
	// login = (Button) mrl.findViewById(R.id.title_btn_logined);
	// user = BookApp.getUser();
	// if (user != null && user.getUid() != null) {
	// left1.setVisibility(View.GONE);
	// login.setVisibility(View.VISIBLE);
	// } else {
	// left1.setVisibility(View.VISIBLE);
	// login.setVisibility(View.GONE);
	// }
	// right1.setVisibility(View.VISIBLE);
	// left1.setOnClickListener(this);
	// right1.setOnClickListener(this);
	// login.setOnClickListener(this);
	// }

	public void onClick(View v) {
		// if (v.getId() == left1.getId()) {
		// // 跳转到登录页面
		// Intent intent = new Intent(getActivity(), LoginActivity.class);
		// startActivity(intent);
		// } else if (v.getId() == R.id.title_btn_logined) {
		// // 跳转到个人中心
		// ((MainActivity) getActivity()).goToUserCenter();
		// } else if (v.getId() == right1.getId()) {
		// // 跳转到书架
		// ((MainActivity) getActivity()).goToBookShelf();
		// }
	}

	public void addLoadView() {
		if (isAdd) {
			return;
		}

		loadingLayout.addView(searchLayout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		g++;
		// g为页码，type为书的类型
		// nb = new NewBookThread(getActivity(), handler, g, type);
		// nb.start();
		// 弹出提示对话框
		// mWaitDg1 = ProgressDialog.show(getActivity(), "正在加载数据...",
		// "请稍候...", true, true);
		// mWaitDg1.show();
		// 调用线程去加载数据
		t = new TuijianThread(handler, getActivity(), sortid, g);
		t.start();
		isAdd = true;
	}

	// 以下为分页显示代码
	private int g = 1;
	private LinearLayout searchLayout = null;
	private boolean isAdd = false;
	private LinearLayout loadingLayout = null;

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

	public void onResume() {
		super.onResume();
		// finish = false;
		// // 启动线程 定时跳转图片
		// new Thread() {
		// public void run() {
		// while (!finish) {
		// try {
		// Thread.sleep(500);
		// time += 500;
		// if (time >= 3000 && !finish && dianList.size() > 0) {
		// handler.sendEmptyMessage(123);
		// }
		// } catch (InterruptedException e) {
		// LogUtils.error(e.getMessage(), e);
		// }
		// }
		// }
		// }.start();
	}

	// private void setGallery() {
	// ly = (LinearLayout) getActivity().findViewById(R.id.ly);
	// // 判断缓存是否有数据
	// if (HCData.mTuijianMain == null) {
	// ali = t.imgList;
	// } else {
	// ali = HCData.mTuijianMain.getImgList();
	// }
	// adapter = new GalleryAdapter(getActivity(), ali, handler);
	// gallery.setAdapter(adapter);
	// // 动态添加点
	// for (int i = 0; i < ali.size(); i++) {
	// ImageView imageView = new ImageView(getActivity());
	// imageView.setImageResource(R.drawable.m1);
	// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
	// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	// lp.setMargins(8, 0, 8, 0);
	// imageView.setLayoutParams(lp);
	// ly.addView(imageView);
	// dianList.add(imageView);
	// }
	// // 设置选中事件
	// gallery.setOnItemSelectedListener(myItemListener);
	// // 设置点击事件
	// gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	//
	// public void onItemClick(AdapterView<?> parent, View view,
	// int position, long id) {
	// if (ali.get(position) != null
	// && gallery.getSelectedItemPosition() == position) {
	// Message msg = new Message();
	// msg.what = 444;
	// msg.arg1 = 0;
	// Image img = ali.get(position);
	// String aid = "";
	// if (img.getAid() == null) {
	// msg.arg1 = 1;
	// aid = img.getArticle();
	// } else {
	// aid = img.getAid();
	// }
	// msg.obj = aid;
	// handler.sendMessage(msg);
	// }
	//
	// }
	//
	// });
	// }

	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// if (gallery.onInterceptTouchEvent(ev)) {
	// ev.setAction(MotionEvent.ACTION_CANCEL);
	// }
	// return super.dispatchTouchEvent(ev);
	// }

}
