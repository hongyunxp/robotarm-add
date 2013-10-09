package com.xs.cn.activitys;

import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.adapters.ShubenmuluAdapter;
import com.eastedge.readnovel.beans.Chapterinfo;
import com.eastedge.readnovel.beans.Muludb;
import com.eastedge.readnovel.beans.Shubenmulu;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.HCData;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.db.Bookmuluadb;
import com.eastedge.readnovel.threads.ShubenmuluThread;
import com.eastedge.readnovel.threads.SubedchaptersinfoThread;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

public class Novel_sbxxy_mulu extends Activity {
	private TextView novel_sbxxy_mulu_author;
	private TextView novel_sbxxy_mulu_status;
	private ListView novel_sbxxy_mulu_lv;
	ShubenmuluThread sbmlth;
	private Intent intent;
	private ShubenmuluAdapter adapter;
	private TextView novel_item_detail_title;
	private RadioButton novel_mulu_gybs;
	private RadioButton novel_mulu_mulu, novel_mulu_shuqian;
	private String Articleid;
	ProgressDialog mWaitDg1 = null;
	private Button novel_mulu_back;
	private Shubenmulu getdatasbml;
	private LinearLayout view1;
	private LinearLayout view2;
	private String nowtextid = "";
	private String title;
	private String inetntTag = "Novel_sbxxy_mulu";
	private SubedchaptersinfoThread scifth;
	private User user;
	private HashSet<String> ydy = new HashSet<String>();
	private int p = 0;
	private ArrayList<Chapterinfo> list = null;
	private int beg = 0;
	// 以下为分页显示代码
	private Muludb mldb = null;

	public Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				if (sbmlth.sbml == null) {
					Toast.makeText(Novel_sbxxy_mulu.this, getString(R.string.network_err), Toast.LENGTH_SHORT).show();
					return;
				}
				getdatasbml = sbmlth.sbml;
				HCData.mulumap.put(Articleid, sbmlth.sbml);
				getData();

				view1.setVisibility(View.VISIBLE);
				view2.setVisibility(View.VISIBLE);
				list = getdatasbml.getMulist();
				int a = 0;
				for (int i = 0; i < list.size(); i++) {
					if (nowtextid.equals(list.get(i).getId())) {
						a = i;
						break;
					}
				}
				adapter = new ShubenmuluAdapter(Novel_sbxxy_mulu.this, list, nowtextid, ydy);
				novel_sbxxy_mulu_lv.setAdapter(adapter);
				if (p != 0 && p > 8) {
					novel_sbxxy_mulu_lv.setSelection(p - 4);
				} else {
					if (a > 4) {
						novel_sbxxy_mulu_lv.setSelection(a - 4);
					} else {
						novel_sbxxy_mulu_lv.setSelection(0);
					}
				}

				//存目录信息到本地
				save(Articleid);

				if (user != null && user.getUid() != null) {
					scifth = new SubedchaptersinfoThread(Novel_sbxxy_mulu.this, mhandler, Articleid, user.getUid(), user.getToken());
					scifth.start();
				}
				break;
			case 331:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
				break;
			case 3:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				Toast.makeText(Novel_sbxxy_mulu.this, getString(R.string.network_err), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	public Handler mhandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				if (scifth.scif != null && scifth.scif.getSubed_textid_list() != null && ydy != null && scifth.scif.getSubed_textid_list().size() > 0) {
					ydy.addAll(scifth.scif.getSubed_textid_list());
				}
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novel_sbxxy_mulu);
		CloseActivity.add(this);
		user = BookApp.getUser();

		novel_item_detail_title = (TextView) findViewById(R.id.novel_item_detail_title);
		novel_sbxxy_mulu_author = (TextView) findViewById(R.id.novel_sbxxy_mulu_author);
		novel_sbxxy_mulu_status = (TextView) findViewById(R.id.novel_sbxxy_mulu_status);
		novel_sbxxy_mulu_lv = (ListView) findViewById(R.id.novel_sbxxy_mulu_lv);
		novel_sbxxy_mulu_lv.setFastScrollEnabled(true);
		novel_mulu_gybs = (RadioButton) findViewById(R.id.novel_mulu_gybs);
		novel_mulu_mulu = (RadioButton) findViewById(R.id.novel_mulu_mulu);
		novel_mulu_shuqian = (RadioButton) findViewById(R.id.novel_mulu_shuqian);
		novel_mulu_back = (Button) findViewById(R.id.novel_mulu_back);
		view1 = (LinearLayout) findViewById(R.id.linearLayout1);
		view2 = (LinearLayout) findViewById(R.id.linearLayout2);

		intent = getIntent();
		Bundle bundle = intent.getBundleExtra("newbook");
		beg = bundle.getInt("beg");
		Articleid = bundle.getString("Articleid");

		Bookmuluadb bookmuluadb = new Bookmuluadb(Novel_sbxxy_mulu.this);
		bookmuluadb.open();
		mldb = bookmuluadb.queryDatas(Articleid);
		bookmuluadb.close();

		if (mldb.getTid() == null || "".equals(mldb.getTid())) {
			if (bundle.containsKey("p")) {
				p = bundle.getInt("p");
			} else {
				if (HCData.nowp.containsKey(Articleid)) {
					p = HCData.nowp.get(Articleid);
				}
			}
			if (bundle.containsKey("nowtextid")) {
				nowtextid = bundle.getString("nowtextid");
			} else {
				if (HCData.nowtextid.containsKey(Articleid)) {
					nowtextid = HCData.nowtextid.get(Articleid);
				}
			}
		} else {
			p = mldb.getBookp();
			beg = mldb.getBookpbeg();
			nowtextid = mldb.getTid();
		}

		//读本地目录
		read(Articleid);

		//当目录信息不为空时使用本地目录
		if (getdatasbml != null && getdatasbml.getMulist() != null && getdatasbml.getMulist().size() > 0) {
			getData();
			list = getdatasbml.getMulist();
			int a = 0;
			for (int i = 0; i < list.size(); i++) {
				Chapterinfo chapter = list.get(i);
				if (chapter != null && nowtextid.equals(chapter.getId())) {
					a = i;
					break;
				}
			}
			adapter = new ShubenmuluAdapter(Novel_sbxxy_mulu.this, list, nowtextid, ydy);
			novel_sbxxy_mulu_lv.setAdapter(adapter);

			if (p != 0 && p > 8) {
				novel_sbxxy_mulu_lv.setSelection(p - 4);
			} else {
				if (a > 4) {
					novel_sbxxy_mulu_lv.setSelection(a - 4);
				} else {
					novel_sbxxy_mulu_lv.setSelection(0);
				}
			}

			view1.setVisibility(View.VISIBLE);
			view2.setVisibility(View.VISIBLE);

			if (user != null && user.getUid() != null) {
				scifth = new SubedchaptersinfoThread(Novel_sbxxy_mulu.this, mhandler, Articleid, user.getUid(), user.getToken());
				scifth.start();
			}

			//			new Thread(){
			//				public void run() {
			//					ArrayList<Chapterinfo> mu1list=getdatasbml.getMulist();
			//					Chapterinfo cinfo=mu1list.get(mu1list.size()-1);
			//					Shubenmulu mul2=HttpImpl.ShubenmuluText(Articleid, cinfo.getId());
			//					if(mul2==null||mul2.getMulist()==null||mul2.getMulist().size()==0){
			//						return ;
			//					}
			//					list.addAll(mul2.getMulist());
			//					handler.sendEmptyMessage(331);
			//								save(Articleid);
			//				};
			//			}.start();
		}

		else {//当目录信息为空时在线加载最新
			view1.setVisibility(View.GONE);
			view2.setVisibility(View.GONE);

			mWaitDg1 = ProgressDialog.show(Novel_sbxxy_mulu.this, "正在加载数据...", "请稍候...", true, true);
			mWaitDg1.show();

			sbmlth = new ShubenmuluThread(Novel_sbxxy_mulu.this, handler, Articleid);
			sbmlth.start();
		}

		novel_sbxxy_mulu_lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				p = position;
				Intent intent = new Intent(Novel_sbxxy_mulu.this, Readbook.class);
				intent.putExtra("textid", list.get(position).getId());
				intent.putExtra("isVip", list.get(position).getIs_vip());
				intent.putExtra("aid", Articleid);
				intent.putExtra("title", title);
				intent.putExtra("Tag", inetntTag);
				if (nowtextid.trim().equals(list.get(position).getId().trim())) {
					intent.putExtra("beg", beg);
				}
				intent.putExtra("fcdVip", getdatasbml.getFcvip());
				intent.putExtra("p", p);
				intent.putExtra("imgUrl", getIntent().getStringExtra("imgUrl"));
				intent.putExtra("imgFile", getIntent().getStringExtra("imgFile"));

				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		});
		novel_mulu_back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!"".equals(nowtextid)) {
					HCData.nowtextid.put(Articleid, nowtextid);
				}
				if (p != 0) {
					HCData.nowp.put(Articleid, Integer.valueOf(p));
				}
				finish();
			}
		});
		novel_mulu_gybs.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!"".equals(nowtextid)) {
					HCData.nowtextid.put(Articleid, nowtextid);
				}
				if (p != 0) {
					HCData.nowp.put(Articleid, Integer.valueOf(p));
				}
				Intent intent = new Intent(Novel_sbxxy_mulu.this, Novel_sbxxy.class);
				Bundle bundle = new Bundle();
				bundle.putString("Articleid", Articleid);
				intent.putExtra("newbook", bundle);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		});
		novel_mulu_shuqian.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Novel_sbxxy_mulu.this, BookMarkActivity.class);
				intent.putExtra("aid", Articleid);
				startActivity(intent);
			}
		});
	}

	private void getData() {
		novel_sbxxy_mulu_author.setText(getdatasbml.getAuthor());
		title = getdatasbml.getTitle();
		String t1, t2, t3;
		if (title.length() > 10) {
			t1 = title.substring(0, 10);
			t2 = title.substring(10);
			t3 = t1 + "\n" + t2;
		} else {
			t3 = title;
		}
		novel_item_detail_title.setText("《" + t3 + "》");

		if (getdatasbml.getFinishflag() == 0) {
			novel_sbxxy_mulu_status.setText("连载中");
		} else {
			novel_sbxxy_mulu_status.setText("已完结");
		}
	}

	private void save(String aid) {
		//存目录目录
		Util.write(aid, getdatasbml);

	}

	private void read(String aid) {

		getdatasbml = Util.read(aid);

	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		CloseActivity.curActivity = this;
		novel_mulu_mulu.setChecked(true);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Bookmuluadb bookmuluadb = new Bookmuluadb(Novel_sbxxy_mulu.this);
			bookmuluadb.open();
			bookmuluadb.insertBook(Articleid, nowtextid, p, beg);
			bookmuluadb.close();

			Intent intent = new Intent(Novel_sbxxy_mulu.this, Novel_sbxxy.class);
			Bundle bundle = new Bundle();
			bundle.putString("Articleid", Articleid);
			intent.putExtra("newbook", bundle);
			startActivity(intent);
			finish();
		}
		return false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	protected void onDestroy() {
		super.onDestroy();
		if (sbmlth != null) {
			sbmlth.stopRun();
		}
		if (scifth != null) {
			scifth.stopRun();
		}
		CloseActivity.remove(this);
	}
}
