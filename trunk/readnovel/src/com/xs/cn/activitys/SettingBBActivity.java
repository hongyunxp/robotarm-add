package com.xs.cn.activitys;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.LocalStore;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

/**
 * 版本更新设置
 */
public class SettingBBActivity extends Activity implements OnClickListener
{
	private Button left1;
	private ImageView setbb_iv1, setbb_iv2, setbb_iv3;
	private TextView setbb_tv1, setbb_tv2, setbb_tv3;
	private View novel_settingbb_mcqd, novel_settingbb_mt, novel_settingbb_mz;

	private static final int EVERY_START = 1, EVERY_DAY = 2, EVERY_WEEKEND = 3;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingbb);
		CloseActivity.add(this);

		setbb_iv1 = (ImageView) findViewById(R.id.setbb_iv1);
		setbb_iv2 = (ImageView) findViewById(R.id.setbb_iv2);
		setbb_iv3 = (ImageView) findViewById(R.id.setbb_iv3);

		setbb_tv1 = (TextView) findViewById(R.id.setbb_tv1);
		setbb_tv2 = (TextView) findViewById(R.id.setbb_tv2);
		setbb_tv3 = (TextView) findViewById(R.id.setbb_tv3);

		novel_settingbb_mcqd = (View) findViewById(R.id.novel_settingbb_mcqd);
		novel_settingbb_mt = (View) findViewById(R.id.novel_settingbb_mt);
		novel_settingbb_mz = (View) findViewById(R.id.novel_settingbb_mz);

		setTopBar();

		switch (LocalStore.getUpdate(SettingBBActivity.this))
		{
			case 0:
				setbb_iv1.setVisibility(View.VISIBLE);
				setbb_iv2.setVisibility(View.INVISIBLE);
				setbb_iv3.setVisibility(View.INVISIBLE);
				setbb_tv1.setTextColor(Color.rgb(0, 102, 184));
				setbb_tv2.setTextColor(Color.BLACK);
				setbb_tv3.setTextColor(Color.BLACK);
				LocalStore.setUpdate(SettingBBActivity.this, EVERY_START);

				// novel_settingbb_mcqd.performClick();
				break;
			case 1:
				setbb_iv1.setVisibility(View.VISIBLE);
				setbb_iv2.setVisibility(View.INVISIBLE);
				setbb_iv3.setVisibility(View.INVISIBLE);
				setbb_tv1.setTextColor(Color.rgb(0, 102, 184));
				setbb_tv2.setTextColor(Color.BLACK);
				setbb_tv3.setTextColor(Color.BLACK);
				LocalStore.setUpdate(SettingBBActivity.this, EVERY_START);

				// novel_settingbb_mcqd.performClick();
				break;
			case 2:
				setbb_iv2.setVisibility(View.VISIBLE);
				setbb_iv1.setVisibility(View.INVISIBLE);
				setbb_iv3.setVisibility(View.INVISIBLE);
				setbb_tv2.setTextColor(Color.rgb(0, 102, 184));
				setbb_tv1.setTextColor(Color.BLACK);
				setbb_tv3.setTextColor(Color.BLACK);
				LocalStore.setUpdate(SettingBBActivity.this, EVERY_DAY);

				// novel_settingbb_mt.performClick();
				break;
			case 3:
				setbb_iv3.setVisibility(View.VISIBLE);
				setbb_iv2.setVisibility(View.INVISIBLE);
				setbb_iv1.setVisibility(View.INVISIBLE);
				setbb_tv3.setTextColor(Color.rgb(0, 102, 184));
				setbb_tv2.setTextColor(Color.BLACK);
				setbb_tv1.setTextColor(Color.BLACK);
				LocalStore.setUpdate(SettingBBActivity.this, EVERY_WEEKEND);

				// novel_settingbb_mz.performClick();
				break;

		}

		novel_settingbb_mcqd.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				setbb_iv1.setVisibility(View.VISIBLE);
				setbb_iv2.setVisibility(View.INVISIBLE);
				setbb_iv3.setVisibility(View.INVISIBLE);
				setbb_tv1.setTextColor(Color.rgb(0, 102, 184));
				setbb_tv2.setTextColor(Color.BLACK);
				setbb_tv3.setTextColor(Color.BLACK);
				LocalStore.setUpdate(SettingBBActivity.this, EVERY_START);
			}
		});

		novel_settingbb_mt.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				setbb_iv2.setVisibility(View.VISIBLE);
				setbb_iv1.setVisibility(View.INVISIBLE);
				setbb_iv3.setVisibility(View.INVISIBLE);
				setbb_tv2.setTextColor(Color.rgb(0, 102, 184));
				setbb_tv1.setTextColor(Color.BLACK);
				setbb_tv3.setTextColor(Color.BLACK);
				LocalStore.setUpdate(SettingBBActivity.this, EVERY_DAY);
			}
		});

		novel_settingbb_mz.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				setbb_iv3.setVisibility(View.VISIBLE);
				setbb_iv2.setVisibility(View.INVISIBLE);
				setbb_iv1.setVisibility(View.INVISIBLE);
				setbb_tv3.setTextColor(Color.rgb(0, 102, 184));
				setbb_tv2.setTextColor(Color.BLACK);
				setbb_tv1.setTextColor(Color.BLACK);
				LocalStore.setUpdate(SettingBBActivity.this, EVERY_WEEKEND);
			}
		});

	}

	private void setTopBar()
	{
		left1 = (Button) findViewById(R.id.title_btn_left2);
		left1.setText("返回");
		left1.setGravity(Gravity.CENTER);

		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText("版本更新设置");

		left1.setVisibility(View.VISIBLE);

		left1.setOnClickListener(this);
	}

	public void onClick(View v)
	{
		if (v.getId() == left1.getId())
		{
			finish();
		}
	}
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		CloseActivity.curActivity=this;
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
