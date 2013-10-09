package com.xs.cn.activitys;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eastedge.readnovel.adapters.HelpImageAdapter;
import com.eastedge.readnovel.beans.UseHelp;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.view.FlingGallery;
import com.eastedge.readnovel.view.OnGalleryChangeListener;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

/**
 * @author ninglv
 * @version Time：2012-3-23 下午12:47:35
 */
public class UseHelpActivity extends Activity implements OnClickListener
{
	private FlingGallery gallery;
	private int p;
	private Button left2;
	private TextView title_tv, title;
	private ArrayList<ImageView> dianList = new ArrayList<ImageView>();
	LinearLayout ly,ll;
	private ArrayList<String> log;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.use_help);
		CloseActivity.add(this);

		setTopBar();
		title = (TextView) findViewById(R.id.help_title);
		setGallery();

	}

	private void setGallery()
	{
		gallery = new FlingGallery(this);
		ly = (LinearLayout) findViewById(R.id.help_bottom_ll);
		ll = (LinearLayout) findViewById(R.id.help_ll);
		
		ArrayList<UseHelp> al = getSrc();
		
		gallery.setAdapter(new HelpImageAdapter(UseHelpActivity.this, al));
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);

		layoutParams.setMargins(10, 10, 10, 10);
		
		layoutParams.gravity=Gravity.CENTER;
        ll.addView(gallery,layoutParams);
//		gallery.setPaddingWidth(70);

		for (int i = 0; i < 6; i++)
		{
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(R.drawable.m1);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(7, 7);
			lp.setMargins(5, 0, 5, 0);
			imageView.setLayoutParams(lp);
			ly.addView(imageView);
			dianList.add(imageView);
		}
		
		dianList.get(0).setBackgroundResource(R.drawable.m1);
		
		gallery.addGalleryChangeListener(new OnGalleryChangeListener() {
			
			public void onGalleryChange(int currentItem) {
				for (int i = 0; i < 6; i++)
				{
					if (i == currentItem)
					{
						dianList.get(i).setBackgroundResource(R.drawable.m2);
					}
					else
					{
						dianList.get(i).setBackgroundResource(R.drawable.m1);
					}
				}
			}
		});
	}

	private ArrayList<UseHelp> getSrc() {
		ArrayList<UseHelp> al = new ArrayList<UseHelp>();
		
		Integer[] mImage = {R.drawable.help1, R.drawable.help2, R.drawable.help3
				, R.drawable.help5, R.drawable.help6, R.drawable.help7};
		
		log = new ArrayList<String>();
		log.add("书架主要功能按钮");
		log.add("收藏超过9本后，横向滑屏即可滚动书架");
		log.add("把小说下载到手机，随时看还省流量");
		log.add("连载小说更新提醒，追书不再疯狂刷新");
		log.add("这书不入法眼？长按封面试试");
		log.add("试试手机菜单键，也有小惊喜哦！");
		
		UseHelp help = null;
		for(int i = 0; i < 6; i++){
			help = new UseHelp();
			help.setTitle(log.get(i));
			
			Bitmap bm = BitmapFactory.decodeResource(getResources(), mImage[i]);
			BitmapDrawable bd = new BitmapDrawable(bm);
			help.setImg(bd);
			
			al.add(help);
		}
		return al;
	}
	
	private void setTopBar()
	{
		left2 = (Button) findViewById(R.id.title_btn_left2);
		left2.setText("  返回");

		title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText("使用帮助");

		left2.setVisibility(View.VISIBLE);

		left2.setOnClickListener(this);
	}

	public void onClick(View v)
	{
		if (v.getId() == R.id.title_btn_left2)
		{
			finish();
		}

	}

	public boolean onTouchEvent(MotionEvent event)
	{
        return gallery.onGalleryTouchEvent(event);
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
