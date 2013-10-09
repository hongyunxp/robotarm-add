package com.xs.cn.activitys;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.JsonToBean;
import com.eastedge.readnovel.threads.UpdateInfoThread;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;
import com.xs.cn.http.HttpImpl;

/** 
 * @author ninglv 
 * @version Time：2012-3-27 下午3:00:22 
 */
public class UpdateInfo extends Activity implements OnClickListener {
	private Button left1, right1, update;
	private TextView tv;
	private RelativeLayout rl;
	private TextView username;
	private EditText email;
	private User user, user2;
	private String uid, token, e;
	private UpdateInfoThread upInfoT;
	private ProgressDialog progress;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 123:
				Toast.makeText(UpdateInfo.this, getString(R.string.network_err), Toast.LENGTH_LONG).show();
				finish();
				break;
			case 124:
				username = (TextView) findViewById(R.id.updateInfo_username);
				username.setText(user2.getUsername());
				email.setText(user2.getEmail());
				break;
			case 1:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				Toast.makeText(UpdateInfo.this, "修改成功！", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(UpdateInfo.this, MainActivity.class);
				intent.putExtra("id", R.id.main_usercenter);
				startActivity(intent);

				BookApp.getUser().setEmail(e);

				finish();
				break;
			case 2:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				Toast.makeText(UpdateInfo.this, "修改失败，请稍后再试！", Toast.LENGTH_SHORT).show();

				Intent i = new Intent(UpdateInfo.this, MainActivity.class);
				i.putExtra("id", R.id.main_usercenter);
				startActivity(i);

				finish();
				break;
			}

		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updateinfo);
		CloseActivity.add(this);

		setTopBar();

		email = (EditText) findViewById(R.id.updateInfo_email);

		update = (Button) findViewById(R.id.updateInfo_over);
		update.setOnClickListener(this);

		user = BookApp.getUser();
		uid = user.getUid();
		token = user.getToken();

		if (user == null) {
			Toast.makeText(UpdateInfo.this, "您尚未登录，请先登录！", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(UpdateInfo.this, LoginActivity.class);
			startActivity(intent);
			finish();
			return;
		}

		new Thread() {
			public void run() {
				JSONObject json = HttpImpl.syncUserInfo(uid, token);
				if (json == null) {
					handler.sendEmptyMessage(123);
					return;
				}
				user2 = JsonToBean.JsonToUser(json);
				if (user2 != null) {
					handler.sendEmptyMessage(124);
				}
			};
		}.start();

		right1.setOnClickListener(this);
	}

	private void setTopBar() {
		rl = (RelativeLayout) findViewById(R.id.updateInfo_top_bar);
		left1 = (Button) rl.findViewById(R.id.title_btn_left2);
		right1 = (Button) rl.findViewById(R.id.title_btn_right2);
		tv = (TextView) rl.findViewById(R.id.title_tv);

		left1.setText("  返回");
		right1.setText("退出登录");
		tv.setText("修改资料");

		left1.setVisibility(View.VISIBLE);
		right1.setVisibility(View.VISIBLE);

		left1.setOnClickListener(this);
		right1.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.title_btn_left2) {
			finish();
		} else if (v.getId() == R.id.center_modifyword) {
			Intent intent = new Intent(UpdateInfo.this, UpdateMiMa.class);
			startActivity(intent);
			finish();
		} else if (v.getId() == R.id.title_btn_right2) {
			user = null;
			BookApp.setUser(null);
			Toast.makeText(UpdateInfo.this, "成功注销！", Toast.LENGTH_LONG).show();
			finish();
		} else if (v.getId() == R.id.updateInfo_over) {

			e = email.getText().toString();

			if (e.length() == 0) {
				Toast.makeText(UpdateInfo.this, "请输入邮箱！", Toast.LENGTH_LONG).show();
				return;
			}
			if (e.indexOf('@') == -1) {
				Toast.makeText(UpdateInfo.this, "请输入正确的邮箱！", Toast.LENGTH_LONG).show();
				return;
			}

			progress = ProgressDialog.show(UpdateInfo.this, "温馨提示", "正在修改信息...", true);
			progress.show();

			upInfoT = new UpdateInfoThread(UpdateInfo.this, handler, uid, token, e, null);
			upInfoT.start();

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
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
