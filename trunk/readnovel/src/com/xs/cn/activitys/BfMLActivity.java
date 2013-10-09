package com.xs.cn.activitys;

import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.adapters.BMDAdapter;
import com.eastedge.readnovel.adapters.BfMLAdapter;
import com.eastedge.readnovel.beans.BookMark;
import com.eastedge.readnovel.beans.Chapterinfo;
import com.eastedge.readnovel.beans.Shubenmulu;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.db.DBAdapter;
import com.eastedge.readnovel.threads.SubedchaptersinfoThread;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.R;

/**
 * 本地阅读 目录-书签页
 */
public class BfMLActivity extends Activity {

	private TextView novel_sbxxy_mulu_author; //作者
	private TextView novel_sbxxy_mulu_status; // 完结状态
	private Button novel_mulu_back, cancle; //返回 ，取消按钮
	private ListView listView; //目录列表
	private TextView novel_item_detail_title; //题目
	private RadioGroup radioGroup; //导航栏按钮组
	private RadioButton novel_mulu_mulu, novel_mulu_shuqian, novel_mulu_gybs; //novel_mulu_mulu 目录  novel_mulu_shuqian书签 novel_mulu_gybs 关于书本
	private Shubenmulu mul; //目录对象
	private RelativeLayout show1, show2; // show1 目录布局  show2 书签布局
	private String aid, curtxid; // aid 书本id  curtxid当前章节id
	private int curf; //当前位置
	private ArrayList<Chapterinfo> info; //章节列表
	private ArrayList<BookMark> bmlist; //书签集合
	private TextView textView1; // 没有书签的时候 提示 还没加书签文本
	private ImageView imageView1; // 没有书签的时候 提示的背景
	private ListView bmlistview; //书签列表
	private BMDAdapter bmadapter; //书签列表adapter
	private SubedchaptersinfoThread ci; //查询已订阅过的章节的线程
	private BfMLAdapter adapter; //目录列表adapter
	private HashSet<String> set = new HashSet<String>(); //已订阅过章节的集合
	private int nowckid; //当前选中的 是目录 还是书签
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1: //查询已订阅过的章节的线程有数据返回
				set.clear();
				if (ci.scif.getSubed_textid_list() != null) {
					set.addAll(ci.scif.getSubed_textid_list());
				}
				adapter.notifyDataSetChanged();
				break;
			case 123: //设置目录页内容
				info = mul.getMulist();
				adapter = new BfMLAdapter(BfMLActivity.this, info, curtxid, set);
				listView.setAdapter(adapter);
				curf = curf - 4;
				if (curf < 0)
					curf = 0;
				listView.setSelection(curf);
				novel_item_detail_title.setText("《" + mul.getTitle() + "》");
				novel_sbxxy_mulu_author.setText(mul.getAuthor());
				if (mul.getFinishflag() == 0) {
					novel_sbxxy_mulu_status.setText("连载中");
				} else {
					novel_sbxxy_mulu_status.setText("已完结");
				}
				break;
			case 124: //选择章节后返回阅读
				Intent intent = new Intent();
				intent.putExtra("beg", msg.arg1);
				intent.putExtra("txid", msg.obj.toString());
				setResult(RESULT_OK, intent);
				finish();
				break;
			}

		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bf_mulu);
		
		CloseActivity.add(this);

		aid = getIntent().getStringExtra("aid"); //获取书本id
		curtxid = getIntent().getStringExtra("nowtxid"); //获取章节id
		curf = getIntent().getIntExtra("curf", 0); //获取当前位置
		DBAdapter dbAdapter = new DBAdapter(this);
		dbAdapter.open();

		bmlist = dbAdapter.queryAllBookMark(aid, 0); //查询书签集合
		dbAdapter.close();
		findId();

		mul = Util.read(aid); //读取本地的目录
		if (mul == null) {
			Toast.makeText(BfMLActivity.this, "获取不了目录", Toast.LENGTH_SHORT).show();
		} else {
			handler.sendEmptyMessage(123);
		}

		novel_mulu_mulu.setChecked(true); //选中目录

		if (bmlist.size() != 0) {
			bmlistview.setVisibility(View.VISIBLE);
			imageView1.setVisibility(View.GONE);
			textView1.setVisibility(View.GONE);
			bmadapter = new BMDAdapter(BfMLActivity.this, bmlist, handler);
			bmlistview.setAdapter(bmadapter);
		}
		//判断有没用好登录 获取该用好订阅过的章节信息
		if (BookApp.getUser() != null && BookApp.getUser().getUid() != null) {
			ci = new SubedchaptersinfoThread(this, handler, aid, BookApp.getUser().getUid(), BookApp.getUser().getToken());
			ci.start();
		}

	}

	private void findId() {

		novel_item_detail_title = (TextView) findViewById(R.id.novel_item_detail_title);
		novel_sbxxy_mulu_author = (TextView) findViewById(R.id.novel_sbxxy_mulu_author);
		novel_sbxxy_mulu_status = (TextView) findViewById(R.id.novel_sbxxy_mulu_status);
		listView = (ListView) findViewById(R.id.novel_sbxxy_mulu_lv);
		novel_mulu_back = (Button) findViewById(R.id.novel_mulu_back);
		cancle = (Button) findViewById(R.id.bookmark_cancle);
		novel_mulu_gybs = (RadioButton) findViewById(R.id.novel_mulu_gybs);
		novel_mulu_mulu = (RadioButton) findViewById(R.id.novel_mulu_mulu);
		novel_mulu_shuqian = (RadioButton) findViewById(R.id.novel_mulu_shuqian);
		radioGroup = (RadioGroup) findViewById(R.id.bf_radioGroup);
		show1 = (RelativeLayout) findViewById(R.id.show1);
		show2 = (RelativeLayout) findViewById(R.id.show2);
		textView1 = (TextView) findViewById(R.id.bmtx);
		imageView1 = (ImageView) findViewById(R.id.bmim);
		bmlistview = (ListView) findViewById(R.id.bookmark_list);

		novel_mulu_back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == novel_mulu_mulu.getId()) {
					nowckid = checkedId;
					show1.setVisibility(View.VISIBLE);
					show2.setVisibility(View.GONE);
				} else if (checkedId == novel_mulu_shuqian.getId()) {
					nowckid = checkedId;
					show2.setVisibility(View.VISIBLE);
					show1.setVisibility(View.GONE);
				} else if (checkedId == novel_mulu_gybs.getId()) {
					radioGroup.check(nowckid);
					Intent intent = new Intent(BfMLActivity.this, Novel_sbxxy.class);
					Bundle bundle = new Bundle();
					bundle.putString("Articleid", aid);
					intent.putExtra("newbook", bundle);
					startActivity(intent);
				}
			}
		});
		listView.setFastScrollEnabled(true);

		//清除书签
		cancle.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DBAdapter dbAdapter = null;
				try {
					dbAdapter = new DBAdapter(BfMLActivity.this);
					dbAdapter.open();
					ArrayList<BookMark> list = dbAdapter.queryAllBookMark(aid, 0);
					if (list.size() == 0) {
						Toast.makeText(BfMLActivity.this, "没有书签可以清除", Toast.LENGTH_SHORT).show();
						return;
					}
					dbAdapter.deleteAllMark(aid, 0);
					bmlistview.setVisibility(View.GONE);
					imageView1.setVisibility(View.VISIBLE);
					textView1.setVisibility(View.VISIBLE);
				} catch (Throwable e) {
					LogUtils.error(e.getMessage(), e);
				} finally {
					if (dbAdapter != null)
						dbAdapter.close();
				}
			}
		});
		setListViewLs();
	}

	private void setListViewLs() {
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Chapterinfo cp = info.get(arg2);
				//判断是否为vip  vip跳转到本地阅读
				if (cp.getIs_vip() == 1) {
					Intent intent = new Intent(BfMLActivity.this, Readbook.class);
					intent.putExtra("textid", cp.getId());
					intent.putExtra("isVip", 1);
					intent.putExtra("title", mul.getTitle());
					intent.putExtra("aid", aid);
					intent.putExtra("imgFile", getIntent().getStringExtra("imgFile"));
					startActivity(intent);
					CloseActivity.closeRd();
				} else if (curtxid.equals(cp.getId())) { //选中为当前的章节 直接返回
					finish();
				} else { //跳转到选中的章节
					Intent intent = new Intent();
					intent.putExtra("txid", cp.getId());
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}

	protected void onResume() {
		super.onResume();
		CloseActivity.curActivity = this;
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPause(this);
		super.onPause();
	}

	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}
}
