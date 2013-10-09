package com.xs.cn.activitys;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.JsonToBean;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.threads.RegistThread;
import com.eastedge.readnovel.threads.SendLogRegInfo;
import com.eastedge.readnovel.utils.CommonUtils;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.openapi.QZoneAble;
import com.xs.cn.R;
import com.xs.cn.http.HttpImpl;

/**
 * 注册
 */
public class RegistActivity extends QZoneAble {
	private EditText userName;
	private EditText passWord;
	private EditText repassWord;
	private EditText check1;
	private EditText check2;
	private EditText check3;
	private EditText check4;
	private Button registe, left2, right;
	private ProgressDialog progress;
	private String username = null;
	private String password = null;

	private ImageView userIcon, passIcon1, passIcon2;//注册输入框 提示
	private String intenttag;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:

				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}

				User user = null;
				JSONObject json = HttpImpl.login(username, password, false);
				if (json == null) {
					return;
				}
				user = JsonToBean.JsonToUser(json);

				String token = Util.md5(user.getUid() + Constants.PRIVATE_KEY);
				token = token.substring(0, 10);
				user.setToken(token);

				BookApp.setUser(user);

				Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_LONG).show();

				//新用户注册发送机器码等信息
				new SendLogRegInfo(RegistActivity.this, BookApp.getUser().getUid(), BookApp.getUser().getToken(), 1).start();

				if ("readbook".equals(intenttag)) {
					finish();
					return;
				}

				Intent intent = new Intent(RegistActivity.this, MainActivity.class);
				intent.putExtra("id", R.id.main_usercenter);
				RegistActivity.this.startActivity(intent);
				RegistActivity.this.finish();
				break;
			case 2:

				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}

				check1.setText("用户名已存在！");
				check1.setVisibility(View.VISIBLE);
				userIcon.setImageResource(R.drawable.cha);
				userName.setText("");
				userName.requestFocus();
				passWord.setText("");
				repassWord.setText("");
				passIcon1.setImageResource(R.drawable.xing);
				passIcon2.setImageResource(R.drawable.xing);
				break;
			case 3:

				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}

				check2.setText("您输入的密码不符合要求！");
				check3.setText("您输入的密码不符合要求！");
				check2.setVisibility(View.VISIBLE);
				check3.setVisibility(View.VISIBLE);
				passIcon1.setImageResource(R.drawable.cha);
				passIcon2.setImageResource(R.drawable.cha);
				passWord.setText("");
				repassWord.setText("");
				passWord.requestFocus();
				break;
			case 4:

				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}

				Toast.makeText(RegistActivity.this, "注册异常失败，请稍后再试！", Toast.LENGTH_LONG).show();
				break;
			case 0:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				Toast.makeText(RegistActivity.this, "网络连接错误，请稍后再试！", Toast.LENGTH_LONG).show();
				break;
			}
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist);
		CloseActivity.add(this);
		intenttag = getIntent().getStringExtra("Tag");

		init();

		setTopBar();

		registe.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				username = userName.getText().toString();
				password = passWord.getText().toString();

				int nameLength = username.length();
				int wordLength = password.length();

				int reWordLength = repassWord.getText().toString().length();
				if (nameLength < 6 || nameLength > 15) {//长度不够
					check1.setVisibility(View.VISIBLE);
					userIcon.setImageResource(R.drawable.cha);
					return;
				} else {
					check1.setVisibility(View.GONE);
					userIcon.setImageResource(R.drawable.gou);
				}

				if (wordLength < 6 || wordLength > 15) {//长度不够
					check2.setVisibility(View.VISIBLE);
					passIcon1.setImageResource(R.drawable.cha);
					passWord.requestFocus();
					return;
				} else {
					check2.setVisibility(View.GONE);
					passIcon1.setImageResource(R.drawable.gou);
				}

				if (reWordLength < 6 || reWordLength > 15) {//长度不够

					if (!passWord.getText().toString().equals(repassWord.getText().toString()) && repassWord.getText().length() > 0) {//两次密码不一致
						check4.setVisibility(View.VISIBLE);
						check3.setVisibility(View.GONE);
						passIcon1.setImageResource(R.drawable.cha);
						passIcon2.setImageResource(R.drawable.cha);
						passWord.setText("");
						repassWord.setText("");
						passWord.requestFocus();
						return;
					}

					check3.setVisibility(View.VISIBLE);
					check4.setVisibility(View.GONE);
					passIcon2.setImageResource(R.drawable.cha);
					repassWord.requestFocus();

					return;
				} else {
					check3.setVisibility(View.GONE);
					passIcon2.setImageResource(R.drawable.gou);
				}

				if (!passWord.getText().toString().equals(repassWord.getText().toString())) {//两次密码不一致
					check4.setVisibility(View.VISIBLE);
					check3.setVisibility(View.GONE);
					passIcon1.setImageResource(R.drawable.cha);
					passIcon2.setImageResource(R.drawable.cha);
					passWord.setText("");
					repassWord.setText("");
					passWord.requestFocus();
					return;
				} else {
					check4.setVisibility(View.GONE);
					passIcon1.setImageResource(R.drawable.gou);
					passIcon2.setImageResource(R.drawable.gou);
				}

				RegistThread rt = new RegistThread(RegistActivity.this, username, password, handler);
				rt.start();

				progress = ProgressDialog.show(RegistActivity.this, "温馨提示", "正在注册中...", true, true);
				progress.show();
			}
		});
	}

	private void init() {
		userName = (EditText) findViewById(R.id.registe_username);
		passWord = (EditText) findViewById(R.id.registe_password1);
		repassWord = (EditText) findViewById(R.id.registe_password2);
		check1 = (EditText) findViewById(R.id.registe_tishi1);
		check2 = (EditText) findViewById(R.id.registe_tishi2);
		check3 = (EditText) findViewById(R.id.registe_tishi3);
		check4 = (EditText) findViewById(R.id.registe_tishi4);
		registe = (Button) findViewById(R.id.registe_sure);

		//注册提示
		userIcon = (ImageView) findViewById(R.id.regist_username_icon);
		passIcon1 = (ImageView) findViewById(R.id.regist_password_icon);
		passIcon2 = (ImageView) findViewById(R.id.regist_password2_icon);

	}

	private void setTopBar() {
		left2 = (Button) findViewById(R.id.title_btn_left2);

		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText("注册");

		left2.setVisibility(View.VISIBLE);
		left2.setText("返回");

		left2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

		right = (Button) findViewById(R.id.title_btn_right1);
		right.setVisibility(View.VISIBLE);
		right.setText("登陆");
		right.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				goToLogin();
			}
		});
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

	public void register(View view) {
		CommonUtils.register(this);
	}

	/**
	 * 去登陆页面
	 */
	private void goToLogin() {
		startActivity(new Intent(this, LoginActivity.class));
		finish();
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
