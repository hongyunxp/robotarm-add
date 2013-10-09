package com.xs.cn.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.threads.UpdateCodeThread;
import com.eastedge.readnovel.utils.CommonUtils;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.util.StringUtils;
import com.xs.cn.R;

/**
 * 修改密码
 */
public class UpdateMiMa extends Activity implements OnClickListener {

	private Button left2, over;
	private TextView title_tv, username;
	private RelativeLayout rl;

	private EditText oldpass, newpass1, newpass2;

	private TextView bindTextView;
	private LinearLayout bindTextViewLayout;
	private LinearLayout unbindTextViewLayout;

	private String old, new1, new2;

	private static final String TAG = "UpdateMiMa";

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(UpdateMiMa.this, "修改成功！", Toast.LENGTH_LONG).show();
				Intent i = new Intent(UpdateMiMa.this, MainActivity.class);
				i.putExtra("id", R.id.main_usercenter);
				startActivity(i);
				finish();
				break;
			case 2:
				Toast.makeText(UpdateMiMa.this, "用户名不存在！", Toast.LENGTH_LONG).show();
				break;
			case 3:
				Toast.makeText(UpdateMiMa.this, "您输入的原始密码错误！", Toast.LENGTH_LONG).show();
				oldpass.setText("");
				newpass1.setText("");
				newpass2.setText("");

				break;
			case 4:
				Toast.makeText(UpdateMiMa.this, "修改密码失败！", Toast.LENGTH_LONG).show();
				break;
			}
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.updatemima);
		CloseActivity.add(this);

		rl = (RelativeLayout) findViewById(R.id.updatemima_top_bar);
		oldpass = (EditText) findViewById(R.id.update_oldcode);
		newpass1 = (EditText) findViewById(R.id.update_newcode1);
		newpass2 = (EditText) findViewById(R.id.update_newcode2);
		bindTextView = (TextView) findViewById(R.id.forget_password_personcenter_modify_bind_tv);
		bindTextViewLayout = (LinearLayout) findViewById(R.id.forget_password_personcenter_modify_bind);
		unbindTextViewLayout = (LinearLayout) findViewById(R.id.forget_password_personcenter_modify_unbind);

		String bindTel = BookApp.getUser().getBtel();

		if (StringUtils.isNotBlank(bindTel)) {
			bindTextView.setText(Html.fromHtml(String.format(getString(R.string.forget_password_personcenter_modify_bind_tv),
					new Object[] { bindTel })));

			bindTextViewLayout.setVisibility(View.VISIBLE);
			unbindTextViewLayout.setVisibility(View.GONE);
		} else {

			bindTextViewLayout.setVisibility(View.GONE);
			unbindTextViewLayout.setVisibility(View.VISIBLE);
		}

		over = (Button) findViewById(R.id.update_over);
		over.setOnClickListener(this);

		setTopBar();

		username = (TextView) findViewById(R.id.update_username);
		User user = BookApp.getUser();
		if (user == null) {
		} else {
			username.setText(user.getUsername());
		}
	}

	private void setTopBar() {
		left2 = (Button) rl.findViewById(R.id.title_btn_left2);
		left2.setText("返回");

		title_tv = (TextView) rl.findViewById(R.id.title_tv);
		title_tv.setText("修改密码");

		left2.setVisibility(View.VISIBLE);

		left2.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.title_btn_left2) {
			finish();
		}
		if (v.getId() == R.id.update_over) {

			old = oldpass.getText().toString();
			new1 = newpass1.getText().toString();
			new2 = newpass2.getText().toString();
			if (old.length() == 0) {
				Toast.makeText(UpdateMiMa.this, "请输入您的原始密码！", Toast.LENGTH_LONG).show();
				return;
			} else if (new1.length() == 0 || new2.length() == 0) {
				Toast.makeText(UpdateMiMa.this, "请输入您的新密码！", Toast.LENGTH_LONG).show();
				return;
			} else if (!new1.equals(new2)) {
				Toast.makeText(UpdateMiMa.this, "两次输入的密码不一致！", Toast.LENGTH_LONG).show();
				newpass1.setText("");
				newpass2.setText("");
				return;
			} else {
				UpdateCodeThread uct = new UpdateCodeThread(old, new1, handler);
				uct.start();
			}

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

	/**
	 * 绑定手机
	 * @param view
	 */
	public void bindPhone(View view) {
		CommonUtils.bindPhone(this);
	}

	/**
	 * 解绑
	 * @param view
	 */
	public void unBindPhone(View view) {
		Intent intent = new Intent(this, PhoneUnbindActivity.class);
		startActivity(intent);
	}
}
