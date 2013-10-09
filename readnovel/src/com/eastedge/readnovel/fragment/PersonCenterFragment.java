package com.eastedge.readnovel.fragment;

import java.io.IOException;
import java.util.Set;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.Constants.LoginType;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.task.AliPayUserSyncTask;
import com.eastedge.readnovel.threads.SyncUserInfoThread;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.common.NetType;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.NetUtils;
import com.readnovel.base.util.StringUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.activitys.LoginActivity;
import com.xs.cn.activitys.MainActivity;
import com.xs.cn.activitys.PhoneUnbindActivity;
import com.xs.cn.activitys.UpdateInfo;
import com.xs.cn.activitys.UpdateMiMa;

public class PersonCenterFragment extends Fragment implements OnClickListener {
	private Button left2, right2, updatemima, consume;
	private TextView tv;
	private RelativeLayout rl;
	private TextView username, email, readbi, uIdTextView;
	private ImageView icon;
	private User user;//用户基本
	private Button edit;
	private LinearLayout bindViewLayout;//手机绑定 layout
	private LinearLayout bindTelLayout;//绑定手机号TextView layout
	private TextView unBindTextView;//解绑TextView
	private LinearLayout unbindTelLayout;
	private TextView phonetv;//绑定手机号TextView
	private String uid, token;

	private ProgressDialog progress;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				user = SyncUserInfoThread.user;
				try {

					if (progress.isShowing() && progress != null) {
						progress.dismiss();
					}

					//					user.setHeadLogo(Util.getDrawableFromCache(PersonCenterActivity.this, user.getLogo()));
					//					icon.setImageDrawable(user.getHeadLogo());
					icon.setImageDrawable(Util.getDrawableFromCache(getActivity(), user.getLogo()));
					readbi.setText(user.getRemain());
					phonetv.setText(user.getTel());
					//更新老用户资料没有uid token 需要手动设置(80% brain cell was dead...)
					user.setUid(uid);
					user.setToken(token);
					email.setText(user.getEmail());

					setBindTel();

				} catch (IOException e) {
					LogUtils.error(e.getMessage(), e);
				}
				break;
			case 2:

				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}

				Toast.makeText(getActivity(), "用户信息不存在！", Toast.LENGTH_LONG).show();
				break;
			case 4:
				if (progress.isShowing() && progress != null) {
					progress.dismiss();
				}

				Toast.makeText(getActivity(), "网络连接错误！", Toast.LENGTH_LONG).show();

				break;
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.personcenter, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setTopBar();

		username = (TextView) getActivity().findViewById(R.id.center_name);
		email = (TextView) getActivity().findViewById(R.id.center_email);
		readbi = (TextView) getActivity().findViewById(R.id.center_readbi);
		icon = (ImageView) getActivity().findViewById(R.id.center_icon);
		consume = (Button) getActivity().findViewById(R.id.center_addmoney);
		uIdTextView = (TextView) getActivity().findViewById(R.id.center_uid);

		consume.setOnClickListener(this);

		edit = (Button) getActivity().findViewById(R.id.personcenter_edit);
		edit.setOnClickListener(this);

		updatemima = (Button) getActivity().findViewById(R.id.center_modifyword);
		updatemima.setOnClickListener(this);

		right2.setOnClickListener(this);

		bindViewLayout = (LinearLayout) getActivity().findViewById(R.id.personcenter_bindview);
		bindTelLayout = (LinearLayout) getActivity().findViewById(R.id.personcenter_bindphone_layout);
		phonetv = (TextView) getActivity().findViewById(R.id.center_phone);

		unBindTextView = (TextView) getActivity().findViewById(R.id.personcenter_unbind_tv);
		unbindTelLayout = (LinearLayout) getActivity().findViewById(R.id.personcenter_unbindview);

		//用户资料同步
		syncUserInfo();
	}

	/**
	 * 用户资料同步
	 */
	private void syncUserInfo() {
		NetType netType = NetUtils.checkNet();
		if (NetType.TYPE_NONE.equals(netType)) {
			ViewUtils.showDialog(getActivity(), "温馨提示", "网络不给力", null);
			return;
		}

		user = BookApp.getUser();
		if (user == null || user.getUid() == null) {
			Toast.makeText(getActivity(), "您尚未登录，请先登录！", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
			getActivity().finish();
		} else {

			LoginType loginType = LocalStore.getLastLoginType(getActivity());

			if (LoginType.alipay.equals(loginType)) {//当前为支付宝登陆时

				new AliPayUserSyncTask(getActivity()).execute();

			} else {//其它方式登陆
				progress = ProgressDialog.show(getActivity(), "温馨提示", "用户资料同步中...", true, true);
				progress.show();

				SyncUserInfoThread mThread = new SyncUserInfoThread(handler, user.getUid(), user.getToken());
				mThread.start();

			}

			try {
				if (user.getRemain() != null) {
					icon.setImageDrawable(Util.getDrawableFromCache(getActivity(), user.getLogo()));
					readbi.setText(user.getRemain());
				}
			} catch (Throwable e) {
				LogUtils.error(e.getMessage(), e);
			}

			username.setText(user.getUsername());
			email.setText(user.getEmail());
			uIdTextView.setText(user.getUid());
			uid = user.getUid();
			token = user.getToken();

		}
	}

	private void setTopBar() {
		rl = (RelativeLayout) getActivity().findViewById(R.id.user_center_top_bar);
		left2 = (Button) rl.findViewById(R.id.title_btn_left2);
		right2 = (Button) rl.findViewById(R.id.title_btn_right2);
		tv = (TextView) rl.findViewById(R.id.title_tv);

		left2.setText("  返回");
		right2.setText("退出登录");
		tv.setText("用户中心");

		left2.setVisibility(View.VISIBLE);
		right2.setVisibility(View.VISIBLE);

		left2.setOnClickListener(this);
		right2.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.title_btn_left2) {//点击返回到书架
			Intent i = new Intent(getActivity(), MainActivity.class);
			startActivity(i);
			getActivity().finish();
		} else if (v.getId() == R.id.center_modifyword) {
			Intent intent = new Intent(getActivity(), UpdateMiMa.class);
			startActivity(intent);
			getActivity().finish();
		} else if (v.getId() == R.id.title_btn_right2) {
			//清除登陆状态
			CommonUtils.logout(getActivity());
			Toast.makeText(getActivity(), "成功注销！", Toast.LENGTH_LONG).show();
			getActivity().finish();
		} else if (v.getId() == R.id.center_addmoney) {//点击充值中心跳到支付宝

			CommonUtils.goToConsume(getActivity());

		} else if (v.getId() == R.id.personcenter_edit) {
			Intent i = new Intent(getActivity(), UpdateInfo.class);
			//			i.putExtra("uid", user.getUid());
			//			i.putExtra("token", user.getToken());
			startActivity(i);
			getActivity().finish();
		}

	}

	//	@Override
	//	public boolean onKeyDown(int keyCode, KeyEvent event) {
	//		if (keyCode == KeyEvent.KEYCODE_BACK) {
	//			Intent i = new Intent(getActivity(), MainMenuActivity.class);
	//			startActivity(i);
	//			getActivity().finish();
	//		}
	//		return super.onKeyDown(keyCode, event);
	//	}

	/**
	 * 设置绑定手机号
	 */
	private void setBindTel() {
		if (StringUtils.isNotBlank(user.getBtel())) {
			phonetv.setText(user.getBtel());
			bindTelLayout.setVisibility(View.VISIBLE);
			bindViewLayout.setVisibility(View.GONE);

			String unbindContent = String.format(getString(R.string.person_center_bottom_unbind_tv), new Object[] { user.getBtel() });
			unBindTextView.setText(Html.fromHtml(unbindContent));
			unbindTelLayout.setVisibility(View.VISIBLE);
		} else {
			bindTelLayout.setVisibility(View.GONE);
			bindViewLayout.setVisibility(View.VISIBLE);
			unbindTelLayout.setVisibility(View.GONE);
		}

	}

	private String setToString(Set<String> set) {
		if (set != null && set.size() > 0) {
			String st = "";
			StringBuffer buff = new StringBuffer();
			for (String string : set) {
				buff.append(st + string);
				if ("".equals(st)) {
					st = ",";
				}
			}
			return buff.toString();
		}

		return "";
	}

	/**
	 * 绑定手机号
	 * @param view
	 */
	public void bindPhone(View view) {
		CommonUtils.bindPhone(getActivity());

	}

	/**
	 * 解绑
	 * @param view
	 */
	public void unBindPhone(View view) {
		Intent intent = new Intent(getActivity(), PhoneUnbindActivity.class);
		startActivity(intent);
	}
}
