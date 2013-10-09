package com.eastedge.readnovel.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.adapters.FenleiAdapter;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.HCData;
import com.eastedge.readnovel.threads.FenleiThread;
import com.xs.cn.R;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.activitys.LoginActivity;
import com.xs.cn.activitys.MainActivity;

public class MenuFenleiFragment extends Fragment implements OnClickListener {
	private GridView fenleigridView;
	private Button left1;
	private TextView right1;
	private FenleiThread fl;
	private FenleiAdapter Adapter;
	ProgressDialog mWaitDg1 = null;

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				HCData.list = fl.list;
				Adapter = new FenleiAdapter(getActivity(), HCData.list);
				fenleigridView.setAdapter(Adapter);
				break;
			case 2:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				Toast.makeText(getActivity(), getString(R.string.network_err), Toast.LENGTH_SHORT).show();
				handler.postDelayed(new Runnable() {

					public void run() {
						((MainActivity) getActivity()).goToBookShelf();
					}
				}, 1000);
			}
		};
	};
	private Button login;
	private User user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_fenlei, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		fenleigridView = (GridView) getActivity().findViewById(R.id.fenleigridView);
		if (HCData.list != null && HCData.list.size() > 0) {
			Adapter = new FenleiAdapter(getActivity(), HCData.list);
			fenleigridView.setAdapter(Adapter);
		} else {
			mWaitDg1 = ProgressDialog.show(getActivity(), "正在加载数据...", "请稍候...", true, true);
			mWaitDg1.show();
			fl = new FenleiThread(getActivity(), handler);
			fl.start();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setTopBar();
	}

	private void setTopBar() {
		RelativeLayout rl = (RelativeLayout) getActivity().findViewById(R.id.fenlei_top_bar);

		if (rl == null)
			return;

		left1 = (Button) rl.findViewById(R.id.title_btn_left1);
		left1.setText("登录");
		right1 = (Button) rl.findViewById(R.id.title_btn_right1);
		right1.setText("书架");
		TextView title_tv = (TextView) rl.findViewById(R.id.title_tv);
		title_tv.setText("小说分类");

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

	public void onClick(View v) {
		if (v.getId() == left1.getId()) {
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
			getActivity().finish();
		} else if (v.getId() == right1.getId()) {
			((MainActivity) getActivity()).goToBookShelf();
		} else if (v.getId() == R.id.title_btn_logined) {
			((MainActivity) getActivity()).goToUserCenter();
			//			finish();
		}

	}

}
