package com.xs.cn.activitys;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.adapters.BookMarkAdapter;
import com.eastedge.readnovel.beans.BookMark;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.db.DBAdapter;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

public class BookMarkActivity extends Activity
{

	private Button novel_mulu_back;
	private RadioButton novel_mulu_gybs;
	private RadioButton novel_mulu_mulu;
	private Button bookmark_cancle;
	private TextView textView1;
	private ImageView imageView1;
	private ListView listview;
	private String aid;
	private DBAdapter dbAdapter;
	private String inetntTag = "BookMarkActivity";
	private BookMarkAdapter adapter;
	private ArrayList<BookMark> list;

	public Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)	
			{
				case 1:
					dbAdapter.open();
					list = dbAdapter.queryAllBookMark(aid,1);
					dbAdapter.close();
					adapter = new BookMarkAdapter(BookMarkActivity.this, list,
					        inetntTag, handler);
					listview.setAdapter(adapter);
					if (list.size() == 0)
					{
						imageView1.setVisibility(View.VISIBLE);
						textView1.setVisibility(View.VISIBLE);
					}
					// adapter.notifyDataSetChanged();

			}
		}
	};

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmark);
		
		CloseActivity.add(this);

		novel_mulu_back = (Button) findViewById(R.id.novel_mulu_back);
		novel_mulu_gybs = (RadioButton) findViewById(R.id.novel_mulu_gybs);
		novel_mulu_mulu = (RadioButton) findViewById(R.id.novel_mulu_mulu);
		bookmark_cancle = (Button) findViewById(R.id.bookmark_cancle);
		textView1 = (TextView) findViewById(R.id.textView1);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		listview = (ListView) findViewById(R.id.bookmark_list);

		aid = getIntent().getStringExtra("aid");
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		list = dbAdapter.queryAllBookMark(aid,1);
		dbAdapter.close();
		if (list.size() != 0)
		{
			listview.setVisibility(View.VISIBLE);
			imageView1.setVisibility(View.GONE);
			textView1.setVisibility(View.GONE);
			adapter = new BookMarkAdapter(BookMarkActivity.this, list,
			        inetntTag, handler);
			listview.setAdapter(adapter);
		}

		novel_mulu_back.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				Intent intent = new Intent(BookMarkActivity.this,
				        Novel_sbxxy_mulu.class);
				Bundle bundle = new Bundle();
				bundle.putString("Articleid", aid);
				intent.putExtra("newbook", bundle);
				startActivity(intent);
				finish();
			}
		});
		novel_mulu_gybs.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				Intent intent = new Intent(BookMarkActivity.this,
				        Novel_sbxxy.class);
				Bundle bundle = new Bundle();
				bundle.putString("Articleid", aid);
				intent.putExtra("newbook", bundle);
				startActivity(intent);
				finish();
			}
		});
		novel_mulu_mulu.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
//				Intent intent = new Intent(BookMarkActivity.this,
//				        Novel_sbxxy_mulu.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("Articleid", aid);
//				intent.putExtra("newbook", bundle);
//				startActivity(intent);
				finish();
			}
		});
		bookmark_cancle.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				dbAdapter.open();
				ArrayList<BookMark> list = dbAdapter.queryAllBookMark(aid,1);
				if (list.size() == 0)
				{
					Toast.makeText(BookMarkActivity.this, "没有书签可以清除",
					        Toast.LENGTH_SHORT).show();
					dbAdapter.close();
					return;
				}
				dbAdapter.deleteAllMark(aid,1);
				listview.setVisibility(View.GONE);
				imageView1.setVisibility(View.VISIBLE);
				textView1.setVisibility(View.VISIBLE);
				dbAdapter.close();
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			// Intent intent = new Intent(BookMarkActivity.this,
			// Novel_sbxxy_mulu.class);
			// Bundle bundle = new Bundle();
			// bundle.putString("Articleid", aid);
			// intent.putExtra("newbook", bundle);
			// startActivity(intent);
			finish();
		}
		return false;
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
