package com.xs.cn.activitys;

import java.util.Vector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.threads.LoginThread;
import com.eastedge.readnovel.threads.SendLogRegInfo;
import com.eastedge.readnovel.utils.CommonUtils;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.openapi.QZoneAble;
import com.readnovel.base.openapi.TencentAPI;
import com.xs.cn.R;

/**
 * 登陆
 */
public class LoginActivity extends QZoneAble {
	private EditText loginName;
	private EditText loginPassWord;
	private Button login, left2, right;

	private String name, password;
	private static final String TAG = "LoginActivity";
	private ProgressDialog progress;
	private String intenttag;
	private Vector<BFBook> al;
	private LoginThread lt;
	private Vector<BFBook> alVip;
	private String uid;
	private TextView forgetPassword;
	private TencentAPI tencentAPI;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:

				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}

				Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_LONG).show();

				// 是否为老用户第一次登录
				if (LocalStore.getFirstLogin(LoginActivity.this) == 0) {
					LocalStore.setFirstLogin(LoginActivity.this, 1);
					User user = BookApp.getUser();
					if (user != null) {
						SendLogRegInfo sendInfo = new SendLogRegInfo(LoginActivity.this, user.getUid(), user.getToken(), 2);
						sendInfo.start();
					}
				}

				if ("readbook".equals(intenttag)) {
					finish();
					return;
				}

				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("id", R.id.main_usercenter);
				LoginActivity.this.startActivity(intent);
				finish();
				break;
			case 2:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				Toast.makeText(LoginActivity.this, "用户名不存在！", Toast.LENGTH_LONG).show();
				break;
			case 3:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				loginPassWord.setText("");
				Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_LONG).show();
				break;
			case 4:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				Toast.makeText(LoginActivity.this, "登陆异常，请稍后再试！", Toast.LENGTH_LONG).show();
				break;
			case 5:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				Toast.makeText(LoginActivity.this, "网络连接错误，请稍后再试！", Toast.LENGTH_LONG).show();
				break;
			}
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		CloseActivity.add(this);

		setTopBar();
		intenttag = getIntent().getStringExtra("Tag");
		loginName = (EditText) findViewById(R.id.login_username);
		loginPassWord = (EditText) findViewById(R.id.login_password);
		login = (Button) findViewById(R.id.login);
		forgetPassword = (TextView) findViewById(R.id.forget_password_tv);
		forgetPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

		login.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				name = loginName.getText().toString();
				password = loginPassWord.getText().toString();

				if (name.length() == 0) {
					Toast.makeText(LoginActivity.this, "输入的用户名不能为空！", Toast.LENGTH_LONG).show();
					return;
				} else if (password.length() == 0) {
					Toast.makeText(LoginActivity.this, "输入的密码不能为空！", Toast.LENGTH_LONG).show();
					return;
				} else {
					lt = new LoginThread(LoginActivity.this, name, password, handler);
					lt.start();

					progress = ProgressDialog.show(LoginActivity.this, "温馨提示", "正在登录中...", true, true);
					progress.show();
				}

			}
		});
	}

	private void setTopBar() {
		left2 = (Button) findViewById(R.id.title_btn_left2);

		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText("小说阅读网");

		left2.setVisibility(View.VISIBLE);
		left2.setText("返回");

		left2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

		right = (Button) findViewById(R.id.title_btn_right1);
		right.setVisibility(View.VISIBLE);
		right.setText("注册");
		right.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				goToReg();
			}
		});
	}

	/**
	 * 去注册页面
	 */
	private void goToReg() {
		Intent intent = new Intent(this, RegistActivity.class);
		startActivity(intent);
		finish();
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

	/**
	 * 页面点击忘记密码
	 * @param view
	 */
	public void forgetPassword(View view) {
		Intent intent = new Intent(this, ForgetPasswordActivity.class);
		startActivity(intent);
	}

	/**
	 * 点击QQ登陆
	 * @param view
	 */
	public void loginForQQ(View view) {
		CommonUtils.loginForQQ(this);
	}

	/**
	 * 点击新浪登陆
	 * @param view
	 */
	public void loginForSina(View view) {
		CommonUtils.loginForSina(this);
	}

}
