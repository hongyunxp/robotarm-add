package com.readnovel.book.base;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.readnovel.book.base.adapter.BookTagAdapter;
import com.readnovel.book.base.db.MyDataBase;
import com.readnovel.book.base.entity.BookTag;
import com.readnovel.book.base.sp.StyleSaveUtil;
import com.readnovel.book.base.utils.LogUtils;

public class BookTagActivity extends ToolsActivity {
	private ListView list;
	private MyDataBase mydata;
	private Cursor c;
	private List<BookTag> bookTagList;// 书签对象集合
	private String title, foreText, time, percent, fileName, pagenum;// 文章标题，前几个文字，时间，百分比
	private int fontSize, lastRead;// 字号，上次阅读的位置
	private BookTag bookTag;// 书签对象
	private LinearLayout ll;
	private String nowTime;// 时间
	private static BaseAdapter adapter;
	private StyleSaveUtil util;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取手机窗口的Display 来初始化DisplayMetrics 对象
		// getManager()获取显示定制窗口的管理器。 
		// 获取默认显示Display对象
		// 通过Display 对象的数据来初始化一个DisplayMetrics 对象
		// 进行初始化
		init();
		bookTagList = new ArrayList<BookTag>();
		adapter = new BookTagAdapter(this, bookTagList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int posiont, long id) {
				// 跳转进行的处理
				stepTo(posiont);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		bookTagList.clear();
		query();
		adapter.notifyDataSetChanged();
	}

	public void init() {
		list = (ListView) this.findViewById(R.id.booktag_lv);
		util = new StyleSaveUtil(BookTagActivity.this);
		ll = (LinearLayout) this.findViewById(R.id.nobooktag_ll);
		bookTagList = new ArrayList<BookTag>();
	}

	// 获得当前的时间
	public String getCurrentTime() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String time = year + "-" + month + "-" + day;
		return time;
	}

	// 查询操作
	public void query() {
		mydata = new MyDataBase(BookTagActivity.this);
		SQLiteDatabase db = mydata.getReadableDatabase();
		String orderBy = "id desc";
		String[] columns = new String[] { MyDataBase.BookTitle, MyDataBase.ForeText, MyDataBase.Time, MyDataBase.PerCent, MyDataBase.FontSize,
				MyDataBase.LastOffset, MyDataBase.FileName, MyDataBase.PAGENUM };
		try {
			c = db.query(MyDataBase.TABLE_NAME_BOOKTAG, columns, null, null, null, null, orderBy);
			nowTime = getCurrentTime();
			while (c.moveToNext()) {
				title = c.getString(c.getColumnIndex(MyDataBase.BookTitle));
				foreText = c.getString(c.getColumnIndex(MyDataBase.ForeText));
				time = c.getString(c.getColumnIndex(MyDataBase.Time));
				percent = c.getString(c.getColumnIndex(MyDataBase.PerCent));
				fontSize = c.getInt(c.getColumnIndex(MyDataBase.FontSize));
				lastRead = c.getInt(c.getColumnIndex(MyDataBase.LastOffset));
				fileName = c.getString(c.getColumnIndex(MyDataBase.FileName));
				pagenum = c.getString(c.getColumnIndex(MyDataBase.PAGENUM));
				bookTag = new BookTag();
				bookTag.setFileName(title);
				bookTag.setFontSize(fontSize);
				bookTag.setForeText(foreText);
				bookTag.setLastRead(lastRead);
				bookTag.setPercent(percent);
				bookTag.setTime(time);
				bookTag.setChapterFileName(fileName);
				bookTag.setPagenum(pagenum);
				bookTagList.add(bookTag);
			}
			if (bookTagList.size() > 0) {
				ll.setVisibility(View.GONE);
			} else {
				ll.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			closeCursor();
		}
	}

	// 计算章节的int型，即数字形式
	public int culNum(String name) {
		int end = name.indexOf(".");
		int mstart = name.indexOf("_") + 1;
		String number = name.substring(mstart, end);
		int a = Integer.valueOf(number);
		return a;
	}

	public void stepTo(int position) {
		Intent intent = new Intent(getApplicationContext(), PageFlipActivity.class);
		Bundle bundle = new Bundle();
		/**
		 * 传递一些信息到阅读页 阅读断点 记录书签所读到的文字 章节名等
		 */
		bundle.putString("booktagfilename", bookTagList.get(position).getChapterFileName());
		bundle.putInt("lastread", bookTagList.get(position).getLastRead());
		bundle.putString("chaptername", bookTagList.get(position).getFileName());
		bundle.putString("pagenum", bookTagList.get(position).getPagenum());
		bundle.putString("foretext", bookTagList.get(position).getForeText());
		// 保存继续阅读的位置
		util.savescollery(culNum(bookTagList.get(position).getChapterFileName()) - 1);
		// 1代表从书签进入阅读页
		intent.putExtras(bundle);
		// intent.putExtra("bookList", (ArrayList<Book>) bookList);// 传递对像

		util.saveReadPath(1);
		startActivity(intent);
		// closeCursor();
	}

	// 关闭Cursor
	private void closeCursor() {
		try {
			if (c != null && c.isClosed() != true) {
				c.close();
				c = null;
			}
		} catch (Exception e) {
			Log.e("xs", "SingleBook onDestroy:" + e.toString());
		}
	}

	@Override
	protected int getContentView() {
		return R.layout.booktag;
	}
}
