package com.xs.cn.activitys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.eastedge.readnovel.common.CloseActivity;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.R;

public class Ran extends Activity {
	private ArrayList<Integer> colorList = new ArrayList<Integer>();
	private ArrayList<String> wordList = new ArrayList<String>();
	private LinearLayout ly1, ly2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ran);
		
		CloseActivity.add(this);

		String[] str = getResources().getStringArray(R.array.color);
		String[] wordarr = getResources().getStringArray(R.array.word);
		for (int i = 0; i < wordarr.length; i++) {
			wordList.add(wordarr[i]);
		}
		int a = 8, b = 16;
		if (wordList.size() < 16) {
			a = wordList.size() / 2;
			b = wordList.size();
		} else {
			Collections.shuffle(wordList); //随机的方法
		}

		for (int i = 0; i < str.length; i++) {
			Color color = new Color();
			colorList.add(color.parseColor(str[i]));
		}

		ly1 = (LinearLayout) findViewById(R.id.ly1);
		ly2 = (LinearLayout) findViewById(R.id.ly2);

		Random ran = new Random();
		int v = ran.nextInt(2);

		for (int i = 0; i < a; i++) {

			TextView tx1 = new TextView(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 50);

			tx1.setText(wordList.get(i));
			int l = 0, t = 0, r = 0, d = 0;
			if (i % 2 == v) {
				l = ran.nextInt(60) + 10;
				t = ran.nextInt(30) + 10;
				lp.setMargins(l, t, r, d);
				LogUtils.info("======坐标=" + i + "====== l: " + l + "   t: " + t);
			} else {
				l = ran.nextInt(60) + 80;
				t = ran.nextInt(30) + 10;
				lp.setMargins(l, t, r, d);
				LogUtils.info("======坐标=" + i + "====== l: " + l + "   t: " + t);
			}
			tx1.setLayoutParams(lp);
			tx1.setTextColor(colorList.get(ran.nextInt(colorList.size())));
			ly1.addView(tx1);
		}

		for (int i = a; i < b; i++) {

			TextView tx1 = new TextView(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 50);

			tx1.setText(wordList.get(i));
			int l = 0, t = 0, r = 0, d = 0;
			if (i % 2 == v) {
				l = ran.nextInt(60);
				t = ran.nextInt(30) + 10;
				lp.setMargins(l, t, r, d);
				Log.e("======坐标======", "======坐标=" + i + "====== l: " + l + "   t: " + t);
			} else {
				l = ran.nextInt(60) + 80;
				t = ran.nextInt(30) + 10;
				lp.setMargins(l, t, r, d);
				Log.e("======坐标======", "======坐标=" + i + "====== l: " + l + "   t: " + t);
			}
			tx1.setLayoutParams(lp);
			tx1.setTextColor(colorList.get(ran.nextInt(colorList.size())));
			ly2.addView(tx1);
		}
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
