package com.xs.cn.activitys;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.adapters.AdapterForLinearLayout;
import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.beans.RDBook;
import com.eastedge.readnovel.beans.Shuping_maininfo;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.Constants.LoginType;
import com.eastedge.readnovel.common.HCData;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.db.DBAdapter;
import com.eastedge.readnovel.db.LastReadTable;
import com.eastedge.readnovel.threads.ShubenxinxiyeThread;
import com.eastedge.readnovel.threads.ShupingThread;
import com.eastedge.readnovel.utils.CommonUtils;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.openapi.QZoneAble;
import com.xs.cn.R;
import com.xs.cn.http.DownFile;

/**
 * 书本详细页
 * 
 * @author li.li
 */
public class Novel_sbxxy extends QZoneAble implements OnClickListener {
	private TextView novel_sbxxy_intro;
	private Button left2;
	private Button right1;
	private Button right2;
	private Intent intent;
	private TextView novel_sbxxy_title;
	private ImageView theme_list_img;
	private TextView novel_sbxxy_author;
	private TextView novel_sbxxy_bos;
	private TextView novel_sbxxy_type;
	private TextView novel_sbxxy_zishu;
	private TextView novel_sbxxy_tuijian;
	private Button sbxxy_mfsd;
	private Button sbxxy_jrsj;
	private TextView sbxxy_zxzj;
	private TextView sbxxy_gxri;
	private TextView novel_sbxxy_num;
	private ImageView novel_sbxxy_tag;
	private ShubenxinxiyeThread sbxxyth;
	// private View linearLayout5;
	private ProgressDialog mWaitDg1 = null;
	private ProgressDialog mWaitDg2 = null;
	public static String Sortname, title;
	private DBAdapter dbAdapter;
	private ShupingThread spth;
	private LinearLayout linearLayout1;
	private LinearLayout loadingLayout = null;
	private int s = 1;
	public static String Articleid;
	private ArrayList<Shuping_maininfo> list = null;
	private TextView shuping_zong;
	// 评论list
	// private LinearLayoutForListView novel_sbxxy_splv;
	// 评论list adapter
	private AdapterForLinearLayout adapter;
	private String first_chapter_id;
	private View ly_zxzj;
	private String lastUpdateid;
	private String inetntTag = "Novel_sbxxy";
	private TextView textView7;
	private ScrollView sv;
	private boolean dorun = true;

	String book_name;
	String book_author;
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 11:
				if (mWaitDg2 != null && mWaitDg2.isShowing()) {
					mWaitDg2.dismiss();
				}
				LastReadTable tb = new LastReadTable(Novel_sbxxy.this);
				tb.open();
				RDBook rd = tb.queryLastBook(Articleid, 2);
				tb.close();
				BFBook bk = dbAdapter.queryOneBook(Articleid).get(0);
				if (rd != null && rd.getIsVip() == 1) {
					Intent intent = new Intent(Novel_sbxxy.this, Readbook.class);
					intent.putExtra("textid", rd.getTextId());
					intent.putExtra("isVip", rd.getIsVip());
					intent.putExtra("aid", bk.getArticleid());
					intent.putExtra("beg", rd.getPosi());
					intent.putExtra("imgUrl", bk.getImgFile());
					startActivity(intent);
					return;
				} else {
					Intent intent = new Intent(Novel_sbxxy.this,
							ReadbookDown.class);
					intent.putExtra("aid", bk.getArticleid());
					intent.putExtra("bookFile", bk.getBookFile());
					if (rd != null) {
						intent.putExtra("beg", rd.getPosi());
						intent.putExtra("textid", rd.getTextId());
					}
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					return;
				}
				// BFBook bk = dbAdapter.queryOneBook(Articleid).get(0);
				// Intent intent = new Intent(Novel_sbxxy.this,
				// ReadbookDown.class);
				// intent.putExtra("aid", bk.getArticleid());
				// intent.putExtra("bookFile", bk.getBookFile());
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(intent);
			case 12:
				if (mWaitDg2 != null && mWaitDg2.isShowing()) {
					mWaitDg2.dismiss();
				}
				Toast.makeText(Novel_sbxxy.this,
						getString(R.string.network_err), Toast.LENGTH_SHORT)
						.show();
				break;

			case 1:
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				sv.setVisibility(View.VISIBLE);
				first_chapter_id = sbxxyth.sbxxy.getFirst_chapter_id();
				lastUpdateid = sbxxyth.sbxxy.getTextid();
				// novel_sbxxy_author.setText(sbxxyth.sbxxy.getAuthor());
				// novel_sbxxy_title.setText("《" + sbxxyth.sbxxy.getTitle() +
				// "》");
				title = sbxxyth.sbxxy.getTitle();
				novel_sbxxy_num.setText(sbxxyth.sbxxy.getTotalviews() + "");
				novel_sbxxy_type.setText(sbxxyth.sbxxy.getSortname());
				// if (sbxxyth.sbxxy.getFinishflag() == 1) {
				// novel_sbxxy_tag.setImageResource(R.drawable.sbxxy_ywj);
				// } else {
				// novel_sbxxy_tag.setImageResource(R.drawable.sbxxy_lzz);
				// }
				try {
					theme_list_img.setImageDrawable(Util.getDrawableFromCache(
							Novel_sbxxy.this, sbxxyth.sbxxy.getBook_logo()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				novel_sbxxy_zishu.setText(sbxxyth.sbxxy.getWordtotal() + "");
				novel_sbxxy_tuijian.setText(sbxxyth.sbxxy.getVoters() + "");
				sbxxy_zxzj.setText(sbxxyth.sbxxy.getChapter_title());
				novel_sbxxy_bos.setText(sbxxyth.sbxxy.getWordtotal() * 2 / 1024
						+ "KB");
				String str1 = "简介" + " ： " + sbxxyth.sbxxy.getDescription();
				novel_sbxxy_intro.setText(str1);
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novel__sbxxy);
		CloseActivity.add(this);

		dbAdapter = new DBAdapter(this);
		dbAdapter.open();

		// novel_sbxxy_splv = (LinearLayoutForListView)
		// findViewById(R.id.novel_sbxxy_splv);

		// LayoutInflater inflater = Novel_sbxxy.this.getLayoutInflater();
		// View view = inflater.inflate(R.layout.shuping_head, null);

		novel_sbxxy_title = (TextView) findViewById(R.id.novel_sbxxy_title);
		novel_sbxxy_author = (TextView) findViewById(R.id.novel_sbxxy_author);
		novel_sbxxy_bos = (TextView) findViewById(R.id.novel_sbxxy_bos);
		novel_sbxxy_num = (TextView) findViewById(R.id.novel_sbxxy_num);
		novel_sbxxy_type = (TextView) findViewById(R.id.novel_sbxxy_type);
		novel_sbxxy_zishu = (TextView) findViewById(R.id.novel_sbxxy_zishu);
		novel_sbxxy_tuijian = (TextView) findViewById(R.id.novel_sbxxy_tuijian);
		novel_sbxxy_zishu = (TextView) findViewById(R.id.novel_sbxxy_zishu);
		textView7 = (TextView) findViewById(R.id.textView7);
		sbxxy_zxzj = (TextView) findViewById(R.id.sbxxy_zxzj);
		sbxxy_gxri = (TextView) findViewById(R.id.sbxxy_gxri);
		// shuping_zong = (TextView) view.findViewById(R.id.shuping_zong);
		novel_sbxxy_intro = (TextView) findViewById(R.id.novel_sbxxy_intro);
		// sbxxy_mfsd = (Button) findViewById(R.id.sbxxy_mfsd);

		theme_list_img = (ImageView) findViewById(R.id.theme_list_img);
		// novel_sbxxy_tag = (ImageView) findViewById(R.id.novel_sbxxy_tag);
		sbxxy_jrsj = (Button) findViewById(R.id.sbxxy_jrsj);
		// sbxxy_mfsd = (Button) findViewById(R.id.sbxxy_mfsd);

		// novel_sbxxy_splv.addHeaderView(view);
		// novel_sbxxy_splv.addFooterView(showLayout());
		// // // novel_sbxxy_splv.setAdapter(null);
		// 获取相应的数据

		intent = getIntent();
		// final Bundle bundle = intent.getBundleExtra("newbook");
		// Articleid = bundle.getString("Articleid");
		Articleid = intent.getStringExtra("book_id");
		book_name = intent.getStringExtra("book_name");
		book_author = intent.getStringExtra("book_author");
		if (dbAdapter.exitBF1(Articleid, LocalStore.getLastUid(this))
				|| HCData.downingBook.containsKey(Articleid)) {
			sbxxy_jrsj.setText("马上阅读");
		}
		novel_sbxxy_title.setText(book_name);
		novel_sbxxy_author.setText(book_author);
		setTopBar();

		sbxxy_jrsj.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String aid = Articleid;
				if (dbAdapter.exitBF1(Articleid,
						LocalStore.getLastUid(Novel_sbxxy.this))
						|| HCData.downingBook.containsKey(Articleid)) {
					if (HCData.downingBook.containsKey(aid)) {
						DownFile f = HCData.downingBook.get(aid);
						if (f.isOK == 1) {
							HCData.downingBook.remove(aid);
						}
						if (f.isOK != 1) {
							mWaitDg2 = ProgressDialog.show(Novel_sbxxy.this,
									"正在下载中...", "请稍候...", true, true);
							mWaitDg2.show();
							dorun = true;
							mWaitDg2.setOnDismissListener(new ProgressDialog.OnDismissListener() {
								public void onDismiss(DialogInterface dialog) {
									dorun = false;
								}
							});
							new Thread() {
								public void run() {
									while (dorun) {
										if (HCData.downingBook
												.containsKey(Articleid)) {
											DownFile f = HCData.downingBook
													.get(Articleid);
											if (f.isOK == 1) {
												HCData.downingBook
														.remove(Articleid);
												HCData.downOK.put(Articleid,
														f.filelocation);
												handler.sendEmptyMessage(11);
												return;
											} else if (f.isOK == -1) {
												handler.sendEmptyMessage(12);
												return;
											}
											try {
												Thread.sleep(500);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
										} else {
											return;
										}
									}
								}
							}.start();
							return;
						}
					}
					// 从服务器更新下来的并没有下载的书籍操作
					if (dbAdapter.exitBF1(Articleid,
							LocalStore.getLastUid(Novel_sbxxy.this))
							&& (dbAdapter.queryOneBook(Articleid).get(0)
									.getBookFile() == null)
							|| dbAdapter.queryOneBook(Articleid).get(0)
									.getBookFile().equals("")) {

						DownFile downFile = new DownFile(Novel_sbxxy.this, aid,
								sbxxyth.sbxxy.getTitle());
						downFile.start();
						HCData.downingBook.put(aid, downFile);

						DownFile f = HCData.downingBook.get(aid);
						if (f.isOK == 1) {
							HCData.downingBook.remove(aid);
						}
						if (f.isOK != 1) {
							mWaitDg2 = ProgressDialog.show(Novel_sbxxy.this,
									"正在下载中...", "请稍候...", true, true);
							mWaitDg2.show();
							dorun = true;
							mWaitDg2.setOnDismissListener(new ProgressDialog.OnDismissListener() {

								public void onDismiss(DialogInterface dialog) {
									dorun = false;
								}
							});
							new Thread() {
								public void run() {
									while (dorun) {
										if (HCData.downingBook
												.containsKey(Articleid)) {
											DownFile f = HCData.downingBook
													.get(Articleid);
											if (f.isOK == 1) {
												HCData.downingBook
														.remove(Articleid);
												HCData.downOK.put(Articleid,
														f.filelocation);
												handler.sendEmptyMessage(11);
												return;
											} else if (f.isOK == -1) {
												handler.sendEmptyMessage(12);
												return;
											}
											try {
												Thread.sleep(500);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
										} else {
											return;
										}
									}
								}
							}.start();
							return;
						}
					}
					LastReadTable tb = new LastReadTable(Novel_sbxxy.this);
					tb.open();
					RDBook rd = tb.queryLastBook(aid, 2);
					tb.close();
					BFBook bk = dbAdapter.queryOneBook(aid).get(0);
					if (rd != null && rd.getIsVip() == 1) {
						Intent intent = new Intent(Novel_sbxxy.this,
								Readbook.class);
						intent.putExtra("textid", rd.getTextId());
						intent.putExtra("isVip", rd.getIsVip());
						intent.putExtra("aid", bk.getArticleid());
						intent.putExtra("beg", rd.getPosi());
						intent.putExtra("imgUrl", bk.getImgFile());
						startActivity(intent);
						return;
					} else {
						Intent intent = new Intent(Novel_sbxxy.this,
								ReadbookDown.class);
						intent.putExtra("aid", bk.getArticleid());
						intent.putExtra("bookFile", bk.getBookFile());
						if (rd != null) {
							intent.putExtra("beg", rd.getPosi());
							intent.putExtra("textid", rd.getTextId());
						}
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						return;
					}

				}
				String r = "加入书架成功";
				if (!dbAdapter.exitBookBF1(aid)) {
					BFBook bf = new BFBook();
					bf.setImgFile(Util.saveImgFile(Novel_sbxxy.this,
							sbxxyth.sbxxy.getBook_logo()));
					bf.setTitle(sbxxyth.sbxxy.getTitle());
					bf.setArticleid(Articleid);
					bf.setFinishFlag(sbxxyth.sbxxy.getFinishflag());
					if (dbAdapter.exitBF1(bf.getArticleid(),
							LocalStore.getLastUid(Novel_sbxxy.this))) {
						r = "该书已经在书架中";
					} else {
						r = dbAdapter.insertBook(bf);
					}

					DownFile downFile = new DownFile(Novel_sbxxy.this, aid,
							sbxxyth.sbxxy.getTitle());
					downFile.start();
					HCData.downingBook.put(aid, downFile);
				}

				String uidd = "-1";
				if (BookApp.getUser() != null
						&& BookApp.getUser().getUid() != null) {
					uidd = BookApp.getUser().getUid();
				}

				if (!dbAdapter.exitBookGx(aid, uidd)) {
					dbAdapter.insertGx(aid, uidd, 0);
				}

				Toast.makeText(Novel_sbxxy.this, r, Toast.LENGTH_SHORT).show();
				sbxxy_jrsj.setText("马上阅读");

				// 放入书架分享-当使用第三方登陆时
				LoginType loginType = LocalStore
						.getLastLoginType(Novel_sbxxy.this);
				if (LoginType.qq.equals(loginType)) {// 当QQ登陆时
					String shareContent = String.format(
							getString(R.string.bookshelf_share_content),
							sbxxyth.sbxxy.getTitle());
					CommonUtils.shareForQQ(Novel_sbxxy.this, shareContent,
							Articleid);
				} else if (LoginType.sina.equals(loginType)) {// 当sina登陆时
					String shareContent = String.format(
							getString(R.string.bookshelf_share_content),
							sbxxyth.sbxxy.getTitle());
					CommonUtils.shareForSina(Novel_sbxxy.this, shareContent,
							Articleid);
				}
			}
		});
		// sbxxy_mfsd.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// Intent intent = new Intent(Novel_sbxxy.this, Readbook.class);
		// LastReadTable db = new LastReadTable(Novel_sbxxy.this);
		// db.open();
		// RDBook rd = db.queryLastBook(Articleid, 1);
		// db.close();
		// if (rd != null) {
		// intent.putExtra("textid", rd.getTextId());
		// intent.putExtra("isVip", rd.getIsVip());
		// intent.putExtra("beg", rd.getPosi());
		// } else {
		// intent.putExtra("textid", first_chapter_id);
		// intent.putExtra("isVip", sbxxyth.sbxxy.getFirst_chapter_is_vip());
		// }
		// intent.putExtra("Tag", inetntTag);
		// intent.putExtra("imgUrl", sbxxyth.sbxxy.getBook_logo());
		// intent.putExtra("aid", Articleid);
		// intent.putExtra("title", sbxxyth.sbxxy.getTitle());
		// intent.putExtra("fcdVip",
		// sbxxyth.sbxxy.getFirst_vip_chapter_displayorder());
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(intent);
		// }
		// });

		sv = (ScrollView) findViewById(R.id.sbxxy_view1);
		sv.setVisibility(View.GONE);

		mWaitDg1 = ProgressDialog.show(Novel_sbxxy.this, "正在加载数据...", "请稍候...",
				true, true);
		mWaitDg1.show();

		sbxxyth = new ShubenxinxiyeThread(Novel_sbxxy.this, handler, Articleid);
		sbxxyth.start();

		// s = 1;
		// spth = new ShupingThread(Novel_sbxxy.this, mhandler, Articleid, s,
		// loadMoreBtn);
		// spth.start();

		// linearLayout5 = (View) findViewById(R.id.linearLayout5);
		// linearLayout5.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// intent.setClass(Novel_sbxxy.this, Novel_sbxxy_mulu.class);
		// intent.putExtra("imgUrl", sbxxyth.sbxxy.getBook_logo());
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(intent);
		// }
		// });

		ly_zxzj = (View) findViewById(R.id.ly_zxzj);
		// ly_zxzj.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		//
		// Intent intent = new Intent(Novel_sbxxy.this, Readbook.class);
		// intent.putExtra("Tag", inetntTag);
		// intent.putExtra("textid", lastUpdateid);
		// intent.putExtra("aid", Articleid);
		// intent.putExtra("imgUrl", sbxxyth.sbxxy.getBook_logo());
		// intent.putExtra("isVip", sbxxyth.sbxxy.getIs_vip());
		// intent.putExtra("title", sbxxyth.sbxxy.getTitle());
		// intent.putExtra("fcdVip",
		// sbxxyth.sbxxy.getFirst_vip_chapter_displayorder());
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(intent);
		// }
		//
		// });
	}

	private void setTopBar() {
		left2 = (Button) findViewById(R.id.title_btn_left2);
		left2.setText("返回");

		right1 = (Button) findViewById(R.id.title_btn_right1);
		right1.setText("书架");

		right2 = (Button) findViewById(R.id.title_btn_right2);
		right2.setText("分享");

		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText("");

		left2.setVisibility(View.VISIBLE);
		right1.setVisibility(View.VISIBLE);
		right2.setVisibility(View.VISIBLE);

		left2.setOnClickListener(this);
		right1.setOnClickListener(this);
		right2.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == left2.getId()) {
			finish();
		} else if (v.getId() == right1.getId()) {
			Intent intent = new Intent(Novel_sbxxy.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else if (v.getId() == right2.getId()) {
			String shareContent = String.format(
					getString(R.string.bookinfo_share_content),
					sbxxyth.sbxxy.getTitle());
			CommonUtils.customDialog(this, shareContent, Articleid).show();// 分享对话框
		}
	}

	// 以下为分页显示代码
	// private static LinearLayout searchLayout = null;
	// private static Button loadMoreBtn = null;
	//
	// public LinearLayout showLayout() {
	// searchLayout = new LinearLayout(this);
	// searchLayout.setOrientation(LinearLayout.HORIZONTAL);
	//
	// ProgressBar progressBar = new ProgressBar(this);
	// progressBar.setPadding(0, 0, 15, 0);
	// //
	// progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_medium));
	// searchLayout.addView(progressBar, new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.WRAP_CONTENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT));
	//
	// TextView textView = new TextView(this);
	// textView.setText("加载中...");
	// textView.setGravity(Gravity.CENTER_VERTICAL);
	// searchLayout.addView(textView, new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.FILL_PARENT,
	// LinearLayout.LayoutParams.FILL_PARENT));
	// searchLayout.setGravity(Gravity.CENTER);
	// loadMoreBtn = new Button(getApplicationContext());
	// loadMoreBtn.setText("更多评论...");
	// loadMoreBtn.setBackgroundColor(Color.WHITE);
	// loadMoreBtn.setTextColor(Color.rgb(0, 102, 184));
	// loadMoreBtn.setTextSize(16);
	//
	// loadMoreBtn.setOnClickListener(new Button.OnClickListener() {
	// public void onClick(View v) {
	// loadingLayout.removeAllViews();
	// loadingLayout.addView(searchLayout,
	// new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.WRAP_CONTENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT));
	// s++;
	// spth = new ShupingThread(Novel_sbxxy.this, mhandler, Articleid,
	// s, loadMoreBtn);
	// spth.start();
	// }
	// });
	//
	// loadingLayout = new LinearLayout(this);
	// loadingLayout.addView(loadMoreBtn, new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.WRAP_CONTENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT));
	// loadingLayout.setGravity(Gravity.CENTER);
	// return loadingLayout;
	// }

	// @Override
	// protected void onResume()
	// {
	// super.onResume();
	// textView7.setTextColor(Color.BLACK);
	// sbxxy_gxri.setTextColor(Color.rgb(102, 102, 102));
	// sbxxy_zxzj.setTextColor(Color.BLACK);
	// }

	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
		dbAdapter.close();
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

	protected void onStop() {
		super.onStop();

		// finish();
	}

	/**
	 * 支持作者(红包)
	 * 
	 * @param v
	 */
	// public void supportAuthor(View v) {
	// new SupportAuthorPageTask(this, Articleid, 1).execute();
	// }
}