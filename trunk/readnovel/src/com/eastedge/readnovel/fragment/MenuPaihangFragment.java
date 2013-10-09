package com.eastedge.readnovel.fragment;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.adapters.PaihangAdapter;
import com.eastedge.readnovel.beans.Image;
import com.eastedge.readnovel.beans.Paihang;
import com.eastedge.readnovel.beans.PaihangMain;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.HCData;
import com.eastedge.readnovel.threads.PaihangThread;
import com.readnovel.base.http.LoadImgProvider;
import com.xs.cn.R;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.activitys.LoginActivity;
import com.xs.cn.activitys.MainActivity;
import com.xs.cn.activitys.PaihangDetail;

/**
 *  导航 -- 排行
 */
public class MenuPaihangFragment extends Fragment implements OnClickListener {
	private ListView listView;
	private PaihangAdapter adapt;
	private ArrayList<Paihang> al = null;
	private Button left1, login;
	private Button right1;
	private PaihangThread mphThread;
	private ImageView img1, img2, img3, img4;
	private ProgressDialog mWaitDg1 = null;
	private User user;
	ArrayList<ImageView> li = null;
	private static final String TAG = "MenuPaihang";
	private static PaihangMain paihangMain;
	private View view1, view2;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 111:
				//获取排行列表成功

				paihangMain = HCData.paihangMain;
				ArrayList<Image> ali = paihangMain.getImgList();
				for (int i = 0; i < ali.size() && i < li.size(); i++) {
					LoadImgProvider.getInstance(Constants.READNOVEL_IMGCACHE_ABS).load(ali.get(i).getImageURL(), li.get(i));// 异步加载图片
				}

				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();

					view1.setVisibility(View.VISIBLE);
					view2.setVisibility(View.VISIBLE);

				}
				al = mphThread.alp;
				//	al2 = mphThread.ali;
				listView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						String sortId = al.get(position).getSortId();
						String topTitle = al.get(position).getTitle();
						Intent i = new Intent(getActivity(), PaihangDetail.class);
						i.putExtra("sortId", sortId);
						i.putExtra("topTitle", topTitle);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
					}

				});

				adapt = new PaihangAdapter(getActivity(), al);
				listView.setAdapter(adapt);
				break;
			case 222:
				//刷新排行列表
				adapt.notifyDataSetChanged();
				break;
			//			case 333:
			//				al2 = mphThread.ali;
			//				for(int i = 0; i < al2.size(); i++){
			//					li.get(i).setImageDrawable(al2.get(i).getImgDrawable());
			//				}
			//				break;
			case 44:
				//获取数据失败
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				Toast.makeText(getActivity(), getString(R.string.network_err), Toast.LENGTH_SHORT).show();
				handler.postDelayed(new Runnable() {

					public void run() {
						((MainActivity) getActivity()).goToBookShelf();
					}
				}, 1000);
				break;
			}

		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_paihang, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		topFourInit();

		listView = (ListView) getActivity().findViewById(R.id.menu_paihang_listview);
		view1 = (View) getActivity().findViewById(R.id.paihang_line1);
		view2 = (View) getActivity().findViewById(R.id.paihang_line2);

		if (HCData.paihangMain == null) {
			view1.setVisibility(View.GONE);
			view2.setVisibility(View.GONE);

			mWaitDg1 = ProgressDialog.show(getActivity(), "正在加载数据...", "请稍候...", true, true);
			mWaitDg1.show();

			mphThread = new PaihangThread(handler, getActivity(), li);
			mphThread.start();
		} else {

			paihangMain = HCData.paihangMain;

			al = paihangMain.getPaihangList();

			ArrayList<Image> ali = paihangMain.getImgList();
			for (int i = 0; i < ali.size() && i < li.size(); i++) {
				LoadImgProvider.getInstance(Constants.READNOVEL_IMGCACHE_ABS).load(ali.get(i).getImageURL(), li.get(i));// 异步加载图片
			}

			adapt = new PaihangAdapter(getActivity(), al);
			listView.setAdapter(adapt);

			listView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String sortId = al.get(position).getSortId();
					String topTitle = al.get(position).getTitle();
					Intent i = new Intent(getActivity(), PaihangDetail.class);
					i.putExtra("sortId", sortId);
					i.putExtra("topTitle", topTitle);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}

			});

			adapt.notifyDataSetChanged();

			//			al2 = paihangMain.getImgList();
			//			for(int i = 0; i < al2.size(); i++){
			//				li.get(i).setImageDrawable(al2.get(i).getImgDrawable());
			//			}
		}
	}

	private void topFourInit() {
		li = new ArrayList<ImageView>();
		img1 = (ImageView) getActivity().findViewById(R.id.menu_paihang_totle);
		img1.setOnClickListener(this);
		li.add(img1);
		img2 = (ImageView) getActivity().findViewById(R.id.menu_paihang_vip);
		img2.setOnClickListener(this);
		li.add(img2);
		img3 = (ImageView) getActivity().findViewById(R.id.menu_paihang_over);
		img3.setOnClickListener(this);
		li.add(img3);
		img4 = (ImageView) getActivity().findViewById(R.id.menu_paihang_mustread);
		img4.setOnClickListener(this);
		li.add(img4);
	}

	@Override
	public void onResume() {
		super.onResume();
		setTopBar();
	}

	/**
	 * 设置标题栏
	 */
	private void setTopBar() {
		RelativeLayout rl = (RelativeLayout) getActivity().findViewById(R.id.paihang_top_bar);

		if (rl == null)
			return;

		left1 = (Button) rl.findViewById(R.id.title_btn_left1);
		left1.setText("登录");
		right1 = (Button) rl.findViewById(R.id.title_btn_right1);
		right1.setText("书架");
		TextView title_tv = (TextView) rl.findViewById(R.id.title_tv);
		title_tv.setText("小说排行");
		login = (Button) rl.findViewById(R.id.title_btn_logined);
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

	public void onClick(View v) {
		if (v.getId() == left1.getId()) {
			//跳转到登录页
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.title_btn_logined) {
			//跳转到个人中心
			((MainActivity) getActivity()).goToUserCenter();
		} else if (v.getId() == right1.getId()) {
			//跳转到书架
			((MainActivity) getActivity()).goToBookShelf();
		}

		//排行上面4个图片的点击事件m
		else if (v.getId() == R.id.menu_paihang_totle) {
			String type = paihangMain.getImgList().get(0).getType();
			String title = paihangMain.getImgList().get(0).getTitle();
			Intent intent = new Intent(getActivity(), PaihangDetail.class);
			intent.putExtra("sortId", type);
			intent.putExtra("topTitle", title);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

		} else if (v.getId() == R.id.menu_paihang_vip) {
			String type = paihangMain.getImgList().get(1).getType();
			String title = paihangMain.getImgList().get(1).getTitle();
			Intent intent = new Intent(getActivity(), PaihangDetail.class);
			intent.putExtra("sortId", type);
			intent.putExtra("topTitle", title);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else if (v.getId() == R.id.menu_paihang_over) {
			String type = paihangMain.getImgList().get(2).getType();
			String title = paihangMain.getImgList().get(2).getTitle();
			Intent intent = new Intent(getActivity(), PaihangDetail.class);
			intent.putExtra("sortId", type);
			intent.putExtra("topTitle", title);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else if (v.getId() == R.id.menu_paihang_mustread) {
			String type = paihangMain.getImgList().get(3).getType();
			String title = paihangMain.getImgList().get(3).getTitle();
			Intent intent = new Intent(getActivity(), PaihangDetail.class);
			intent.putExtra("sortId", type);
			intent.putExtra("topTitle", title);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

}
