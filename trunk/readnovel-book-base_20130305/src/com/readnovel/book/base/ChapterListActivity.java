package com.readnovel.book.base;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.readnovel.book.base.adapter.ChapterListAdapter;
import com.readnovel.book.base.entity.Book;
import com.readnovel.book.base.entity.Chapter;
import com.readnovel.book.base.sp.StyleSaveUtil;
import com.readnovel.book.base.utils.BookListProvider;
import com.readnovel.book.base.utils.CommonUtils;

public class ChapterListActivity extends ToolsActivity {
	int intLevel;
	int intScale;
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			/* 如果捕捉到的action是ACTION_BATTERY_CHANGED，
			 * 就执行onBatteryInfoReceiver() */
			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				Log.i("msg", "ChapterListActivity 接受到电量广播");
				intLevel = intent.getIntExtra("level", 0);
				if (sst != null) {
					Log.i("msg", "设置电量成功");
					sst.setlight(intLevel);
				} else {
					Log.i("msg", "设置电量失败");
				}
			}
		}
	};

	/* 拦截到ACTION_BATTERY_CHANGED时要执行的   */
	public void onBatteryInfoReceiver(int intLevel, int intScale) {
		unregisterReceiver(mBatInfoReceiver);
	}

	private ListView listview;
	private List<Chapter> list;// 书信息集合
	private StyleSaveUtil util;
	private String chapterFileName, chapterName;// 文件名，章名
	private DisplayMetrics displaysMetrics;// 分辨率
	private BaseAdapter bookAdapter;
	private Book book;
	private TextView title;
	private TextView author;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		displaysMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
		util = new StyleSaveUtil(this);
		listview = (ListView) this.findViewById(R.id.listview);
		title = (TextView) findViewById(R.id.title);
		author = (TextView) findViewById(R.id.author);
		book = BookListProvider.getInstance(this).getBook();
		title.setText(book.getName());
		author.setText(book.getAuthor());
		//章节进入阅读界面          
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				stepTo(position);
			}
		});
		listview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			}
		});
		// 解析XML显示目录信息
		list = BookListProvider.getInstance(this).getBook().getChapters();
		// 需要传递adapter数据	 
		if (list != null && !list.isEmpty()) {
			bookAdapter = new ChapterListAdapter(this, list);
			listview.setAdapter(bookAdapter);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		listview.setAdapter(bookAdapter);
		// 显示继续阅读的位置
		if (util.writescollery() != 0) {
			listview.setSelection(util.writescollery() - 2);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			close();
		}
		return false;
	}

	// 跳入阅读页进行的处理
	private void stepTo(int position) {
		Chapter chapter = list.get(position);
		//记录状态
		util.saveReadPath(0);
		util.savescollery(position);
		util.saveFirstChapter(1);
		chapterName = chapter.getTitle();
		chapterFileName = chapter.getFileName();
		//是否跳转到其它付费逻辑页面
		if (!CommonUtils.goToPay(this, chapter.getId()))
			CommonUtils.goToFlipRead(this, chapterFileName, chapterName, chapter.getId());//进入章节内容
	}

	@Override
	protected int getContentView() {
		return R.layout.chapter_list;
	}
}
