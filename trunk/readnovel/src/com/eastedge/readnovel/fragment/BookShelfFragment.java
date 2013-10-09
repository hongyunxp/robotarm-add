package com.eastedge.readnovel.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.adapters.ListViewAdapt;
import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.beans.RDBook;
import com.eastedge.readnovel.beans.User;
import com.eastedge.readnovel.common.HCData;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.db.DBAdapter;
import com.eastedge.readnovel.db.LastReadTable;
import com.eastedge.readnovel.task.DelBookTask;
import com.eastedge.readnovel.task.NoticeCheckTask;
import com.eastedge.readnovel.threads.BanbenxinThread;
import com.eastedge.readnovel.threads.CheckUpdateBookThread;
import com.eastedge.readnovel.threads.UpdateBookThread;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.util.DateUtils;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.AboutweActivity;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.activitys.MainActivity;
import com.xs.cn.activitys.Readbook;
import com.xs.cn.activitys.ReadbookDown;
import com.xs.cn.activitys.SettingActivity;
import com.xs.cn.activitys.UseHelpActivity;
import com.xs.cn.http.DownFile;
import com.xs.cn.http.MyAutoUpdate;

/**
 * 书架
 */

public class BookShelfFragment extends Fragment implements
		OnItemSelectedListener {
	private ListView listview;
	private LinearLayout ly; // 书架下面 点所对应的layout ，动态的往里面添加 "点"
	private RelativeLayout rl; // 阴影背景
	private int lastP, isvip; // lastP：最后次点在的位置 isvip：当前选择的是否为vip书架
	private Vector<BFBook> list = new Vector<BFBook>(); // 当前用于在adapter 显示的集合
	private Button btnRn, lastRead; // right1 书城 btnRn 左下角 rn按钮
	// lastRead最后次阅读
	private ListViewAdapt adapt; // 书本列表的 adapt
	private DBAdapter dbAdapter; // 操作数据库
	private PopupWindow curpop = null; // 书本封面pop
	private boolean finish; // 结束线程标识
	private User user; // 当前登录用户
	private ImageView help; // 第一次进入显示帮助页面
	private TextView tv1, tv2; // tv1 tv2 没有书的时候 提示书架为空的文字
	private BanbenxinThread bbxxth; // 获取版本信息的线程 检测是否更新
	private int sw, sh, upP; // sw sh 屏幕的宽高 upP需要更新章节的位置
	private ProgressDialog mWaitDg1; // 等待框
	private boolean isBook = true;
	private boolean isVip = false;
	private LastReadTable tb;
	private Button topBarButton;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 333:
				// 书本点击事件 获取当前点击的书
				BFBook bf = list.get(msg.arg1);
				// 查询这本书最后次阅读的信息
				RDBook rd = tb.queryLastBook(bf.getArticleid(), 2);

				if (rd != null)
					LogUtils.info("最后阅读信息" + rd.getArticleId() + "|"
							+ rd.getTextId() + "|" + rd.getBookFile() + "|"
							+ rd.getIsVip() + "|" + rd.getPosi());

				// 判断最后一次阅读是不是vip章节
				if (rd != null && rd.getIsVip() == 1) {

					// 跳转到在线阅读
					Intent intent = new Intent(getActivity(), Readbook.class);
					intent.putExtra("aid", bf.getArticleid()); // 书本id

					if (rd != null) {
						intent.putExtra("isVip", rd.getIsVip()); // 是否是vip
						intent.putExtra("textid", rd.getTextId()); // 章节id
						intent.putExtra("beg", rd.getPosi()); // 开始位置
					}

					intent.putExtra("imgFile", bf.getImgFile()); // 封面图片路径
					intent.putExtra("title", bf.getTitle()); // 标题
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

				} else {
					// 本地书本阅读
					Intent intent = new Intent(getActivity(),
							ReadbookDown.class);
					intent.putExtra("bookFile", bf.getBookFile()); // 书本文件路径
					intent.putExtra("finishFlag", bf.getFinishFlag()); // 是否完结标识
					intent.putExtra("aid", bf.getArticleid()); // 书本id
					if (rd != null) {
						intent.putExtra("beg", rd.getPosi()); // 开始位置
						intent.putExtra("textid", rd.getTextId()); // 章节id
					}

					intent.putExtra("imgFile", bf.getImgFile()); // 封面图片路径
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

				}
				break;
			case 334:
				// 点击有更新标识的书本
				final int p334 = msg.arg1;
				new AlertDialog.Builder(getActivity())
						.setTitle("当前作品有最新章节")
						.setMessage("是否更新？")
						.setPositiveButton("更新",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// 点击更新时 弹出等待框
										mWaitDg1 = ProgressDialog.show(
												getActivity(), "正在更新章节...",
												"请稍候...", true, true);
										mWaitDg1.show();
										BFBook bf2 = list.get(p334);
										upP = p334;

										// 启动更新线程
										UpdateBookThread up = new UpdateBookThread(
												getActivity(), bf2
														.getArticleid(), bf2
														.getTitle(), handler);
										up.start();
									}
								})
						.setNegativeButton("进入阅读",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										Message msg = new Message();
										msg.arg1 = p334;
										msg.what = 333;
										handler.sendMessage(msg);
									}
								}).show();
				break;
			case 335:
				// 更新失败
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}
				Toast.makeText(getActivity(),
						BookShelfFragment.this.getString(R.string.network_err),
						Toast.LENGTH_SHORT).show();
				break;
			case 336:
				// 更新成功
				if (mWaitDg1 != null && mWaitDg1.isShowing()) {
					mWaitDg1.dismiss();
				}

				BFBook bf3 = list.get(upP);

				// 更新成功后清除掉相应书的相应缓存(清除书的明细页面缓存)
				CommonUtils.delCacheAfterUpbook(bf3);

				// 去掉更新标识
				bf3.setIsUp(0);
				Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT)
						.show();
				adapt.notifyDataSetChanged();
				break;
			case 12:
				// 点击删除pop框后
				final int p = msg.arg1;
				new AlertDialog.Builder(getActivity())
						.setTitle("温馨提示")
						.setMessage("是否删除该小说？")
						.setPositiveButton(
								getActivity().getString(R.string.ensure),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										BFBook b = list.remove(p);

										// 执行删除异步任务
										new DelBookTask(BookShelfFragment.this,
												b).execute();
									}
								})
						.setNegativeButton(
								getActivity().getString(R.string.cacel),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
				break;
			case 13:
				// 长按显示删除pop框
				// 显示阴影
				rl.setVisibility(View.VISIBLE);
				String str = msg.obj.toString();
				// 初始化pop
				PopupWindow pop = initBookPop(str);
				pop.showAtLocation(listview, Gravity.NO_GRAVITY, msg.arg1 - 15,
						msg.arg2 - 15);
				pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
					public void onDismiss() {
						rl.setVisibility(View.GONE);
					}
				});
				curpop = pop;
				break;
			case 15:
				// 点击书下载
				String a = list.get(msg.arg1).getArticleid();
				String title = list.get(msg.arg1).getTitle();
				// 启动下载线程
				DownFile downFile = new DownFile(getActivity(), a, title);
				downFile.start();
				// 加入缓存的下载集合
				HCData.downingBook.put(a, downFile);
				adapt.notifyDataSetChanged();
				break;
			case 16:
				// 下载完成后 隐藏下载进度条
				BFBook bookd2 = list.get(msg.arg1);
				bookd2.setIsonDown(View.GONE);
				adapt.notifyDataSetChanged();
				break;
			case 17:
				// 刷新
				adapt.notifyDataSetChanged();
				break;
			case 18:
				// 下载失败
				Object n = msg.obj;
				String nn = "";
				if (n != null) {
					nn = n.toString();
				}
				Toast.makeText(getActivity(),
						getString(R.string.network_err) + "，" + nn + " 下载失败",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				int code = bbxxth.bbxx.getVersionCode();
				boolean isforce = bbxxth.bbxx.getIsforce();
				int[] wrongversion = bbxxth.bbxx.getWrongversion();
				String apkurl = bbxxth.bbxx.getAppurl();
				String arr[] = bbxxth.bbxx.getFeatures();
				StringBuffer buffer = new StringBuffer();
				if (arr != null && arr.length > 0) {
					for (int i = 0; i < arr.length; i++) {
						if (arr[i] != null && !"".equals(arr[i])) {
							buffer.append(i + 1 + "、" + arr[i] + "。");
						}
					}
				}
				// 检测更新
				MyAutoUpdate update = new MyAutoUpdate(getActivity(), code,
						apkurl, false, buffer.toString(), isforce, wrongversion);
				update.check();
				break;
			case 2:
				LogUtils.info("通知书架更新");
				dbAdapter.open();
				Vector<BFBook> bflist = dbAdapter.queryMyBFData(LocalStore
						.getLastUid(BookApp.getInstance()));
				list.clear();// 清空
				list.addAll(bflist);
				adapt.notifyDataSetChanged();
				lastP = listview.getSelectedItemPosition();
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.gallerylist, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 获取当前屏幕的宽高
		WindowManager windowManager = getActivity().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		sw = display.getWidth();
		sh = display.getHeight();

		tb = new LastReadTable(getActivity());
		// 打开数据库
		tb.open();

		// 是否是第一次进入书架 显示帮助
		int tag = LocalStore.getFirstbf(getActivity());
		if (tag == 0) {
			LocalStore.setFirstbf(getActivity(), 1);
			help = (ImageView) getActivity().findViewById(R.id.bf_help);
			help.setVisibility(View.VISIBLE);
			help.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					help.setVisibility(View.GONE);
				}
			});
		}

		// 打开数据库
		dbAdapter = new DBAdapter(getActivity());
		dbAdapter.open();

		// 检查书章节更新
		CheckUpdateBookThread ck = new CheckUpdateBookThread(getActivity());
		ck.start();

		tv1 = (TextView) getActivity().findViewById(R.id.bookshelf_nobook_tv);
		tv2 = (TextView) getActivity().findViewById(R.id.bookshelf_noVIP_tv);

		// gallery = (MyGallery) findViewById(R.id.gallery);
		listview = (ListView) getActivity().findViewById(R.id.listview);

		ly = (LinearLayout) getActivity().findViewById(R.id.ly);
		rl = (RelativeLayout) getActivity().findViewById(R.id.bf_yy);
		btnRn = (Button) getActivity().findViewById(R.id.bf_rn);
		lastRead = (Button) getActivity().findViewById(R.id.bf_lastRead);
		topBarButton = (Button) getActivity().findViewById(R.id.top_bar_button);

		if (!DateUtils.isSameDay(System.currentTimeMillis(),
				LocalStore.getSlidingMenuTime(getActivity()))) {
			topBarButton
					.setBackgroundResource(R.drawable.shortcut_centre_with_popup_button_bg);
		}
		// 最后次阅读 点击事件
		lastRead.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LastReadTable db = new LastReadTable(getActivity());
				db.open();
				// 查询阅读记录
				RDBook rd = db.queryLastBook(null, -1);
				db.close();
				if (rd != null) {
					if (rd.getIsOL() == 1) {
						// 跳转到在线阅读
						Intent intent = new Intent(getActivity(),
								Readbook.class);
						intent.putExtra("textid", rd.getTextId());
						intent.putExtra("isVip", rd.getIsVip());
						intent.putExtra("beg", rd.getPosi());
						intent.putExtra("aid", rd.getArticleId());
						intent.putExtra("title", rd.getBookName());
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					} else {
						// 本地阅读
						Intent intent = new Intent(getActivity(),
								ReadbookDown.class);
						intent.putExtra("bookFile", rd.getBookFile());
						intent.putExtra("finishFlag", rd.getFinishflag());
						intent.putExtra("aid", rd.getArticleId());
						intent.putExtra("textid", rd.getTextId());
						intent.putExtra("beg", rd.getPosi());
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				} else {
					Toast.makeText(getActivity(), "没有最后次阅读记录",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		NoticeCheckTask noticeCheckTask = new NoticeCheckTask(getActivity());
		noticeCheckTask.execute();
	}

	@Override
	public void onStart() {
		super.onStart();
		// //初始化书架
		// if (!BookApp.isInit) {
		// //书架更新
		// User user = BookApp.getUser();
		// if (user != null) {
		// SyncBFBookThread syncT = new SyncBFBookThread(user.getUid(),
		// user.getToken(), handler, true);
		// syncT.start();
		// BookApp.isInit = true;
		// }
		// }
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		lastP = position;
	}

	/**
	 * 设置标题栏
	 */
	private void setTopBar() {

	}

	/**
	 * 设置菜单
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, Menu.FIRST, 0, "用户中心").setIcon(R.drawable.menu_1);
		menu.add(0, Menu.FIRST + 1, 1, "图书管理").setIcon(R.drawable.menu_2);
		menu.add(0, Menu.FIRST + 2, 2, "设置").setIcon(R.drawable.menu_3);
		menu.add(1, Menu.FIRST + 3, 3, "使用帮助").setIcon(R.drawable.menu_4);
		menu.add(1, Menu.FIRST + 4, 4, "关于").setIcon(R.drawable.menu_5);
		menu.add(1, Menu.FIRST + 5, 5, "退出").setIcon(R.drawable.menu_6);
		return true;
	}

	/**
	 * 菜单项选择事件
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			// 跳转到用户中心
			((MainActivity) getActivity()).goToUserCenter();
			break;
		case Menu.FIRST + 1:
			// 显示删除按钮
			for (BFBook book : list) {
				book.setIsdelete(View.VISIBLE);
			}
			adapt.notifyDataSetChanged();
			break;
		case Menu.FIRST + 2:
			// 跳转到设置界面
			Intent intent2 = new Intent(getActivity(), SettingActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent2);
			break;
		case Menu.FIRST + 3:
			// 跳转到帮助界面
			Intent intent3 = new Intent(getActivity(), UseHelpActivity.class);
			intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent3);
			break;
		case Menu.FIRST + 4:
			// 跳转到关于界面
			Intent intent4 = new Intent(getActivity(), AboutweActivity.class);
			intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent4);
			break;
		case Menu.FIRST + 5:
			BookApp.exitApp(getActivity());
			break;
		}
		return false;
	}

	/**
	 * 初始化 书本删除的pop框
	 * 
	 * @param str
	 *            位置下标
	 * @return
	 */
	public PopupWindow initBookPop(String str) {
		final Integer pi = Integer.valueOf(str);
		View popview = LayoutInflater.from(getActivity()).inflate(
				R.layout.book_pop, null, false);
		final PopupWindow popwin = new PopupWindow(popview,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		BFBook book = list.get(pi);
		Button btn1 = (Button) popview.findViewById(R.id.book_pop);
		ImageView delete = (ImageView) popview.findViewById(R.id.bfitem_d7);
		// 设置弹出的封面图片
		if (book.getBookDrawable() != null) {
			btn1.setBackgroundDrawable(book.getBookDrawable());
		} else {
			btn1.setBackgroundResource(R.drawable.imgtest);
		}
		// 设置删除点击事件
		btn1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Message msg = new Message();
				msg.arg1 = pi;
				msg.what = 12;
				handler.sendMessage(msg);
			}
		});
		// 设置删除点击事件
		delete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Message msg = new Message();
				msg.arg1 = pi;
				msg.what = 12;
				handler.sendMessage(msg);
			}
		});
		popwin.setBackgroundDrawable(new BitmapDrawable());
		return popwin;
	}

	/**
	 * 设置 Rn 按钮的pop框
	 * 
	 * @return
	 */
	public PopupWindow initPopuptWindow() {
		View popview = LayoutInflater.from(getActivity()).inflate(
				R.layout.rnpop, null, false);
		final PopupWindow popwin = new PopupWindow(popview,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		Button btn1 = (Button) popview.findViewById(R.id.popbtn_1);
		Button btn2 = (Button) popview.findViewById(R.id.popbtn_2);
		Button btn3 = (Button) popview.findViewById(R.id.popbtn_3);
		Button btn4 = (Button) popview.findViewById(R.id.popbtn_4);
		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((MainActivity) getActivity()).goToUserCenter();
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SettingActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				popwin.dismiss();
			}
		});
		btn3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), UseHelpActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				popwin.dismiss();
			}
		});
		btn4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AboutweActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				popwin.dismiss();
			}
		});
		popwin.setBackgroundDrawable(new BitmapDrawable());
		return popwin;
	}

	public void onNothingSelected(AdapterView<?> parent) {
	}

	/**
	 * 把set里面的内容在转为 string 以，分开
	 * 
	 * @param set
	 * @return
	 */
	private String setToString(Set<String> set) {
		if (set != null && set.size() > 0) {
			String st = "";
			StringBuffer buff = new StringBuffer();
			for (String string : set) {
				buff.append(st + string);
				if ("".equals(st)) {
					st = ",";
				}
			}
			return buff.toString();
		}
		return "";
	}

	public void onDestroy() {
		super.onDestroy();
		dbAdapter.close();
	}

	public void onPause() {
		super.onPause();
		finish = true;
	}

	public void onResume() {
		super.onResume();
		// 判断是否执行检查更新
		switch (LocalStore.getUpdate(getActivity())) {
		case 0:
		case 1:
			if (LocalStore.getIsFullStart(getActivity())) {
				doUpdata();
				LocalStore.setIsFullStart(getActivity(), false);
			}
			break;
		case 2:
			if (getNowTime(1) >= Integer.parseInt(LocalStore
					.getUptime(getActivity()))) {
				doUpdata();
			}
			break;
		case 3:
			if (getNowTime(7) >= Integer.parseInt(LocalStore
					.getUptime(getActivity()))) {
				doUpdata();
			}
			break;
		}
		finish = false;
		setTopBar();
		// 查询书架书的集合
		dbAdapter.open();
		Vector<BFBook> bflist = dbAdapter.queryMyBFData(LocalStore
				.getLastUid(getActivity()));
		list = bflist;
		adapt = new ListViewAdapt(handler, getActivity(), list);
		// gallery.setAdapter(adapt);
		listview.setAdapter(adapt);
		// gallery.setOnItemSelectedListener(this);
		listview.setOnItemSelectedListener(this);

		if (list.size() == 0 || list == null) {
			tv1.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
		} else {
			tv1.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
		}
		/**
		 * 检测时候有下载线程，如果有下载 刷新进度条
		 */
		new Thread() {
			public void run() {
				// 判断是否结束线程
				while (!finish) {
					Hashtable<String, DownFile> tb = HCData.downingBook;
					if (HCData.downingBook != null
							&& HCData.downingBook.size() > 0) {
						ArrayList<String> dl = new ArrayList<String>();
						for (String key : tb.keySet()) {
							DownFile df = tb.get(key);
							if (df.isOK == 1) { // 下载完成
								HCData.downOK.put(key, df.filelocation);
								dl.add(key); // 下载完成的列表
								LogUtils.info("下载完成");
							} else if (df.isOK == -1) { // 下载出错
								dl.add(key);
								Message msMessage = new Message();
								msMessage.what = 18;
								msMessage.obj = df.title;
								handler.sendMessage(msMessage);
							}
						}
						for (String string : dl) {
							tb.remove(string); // 出去下载完成的数据
						}
						dl.clear(); // 清空集合
						handler.sendEmptyMessage(17);
						try {
							Thread.sleep(1000); // 休息1秒
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
		}.start();
	}

	// 执行更新操作并修改上次更新时间
	private void doUpdata() {
		bbxxth = new BanbenxinThread(getActivity(), handler);
		bbxxth.start();
		LocalStore.setUptime(getActivity(), "" + getNowTime(0));
	}

	// 得到 当前时间/判定时间
	private int getNowTime(int flag) {
		Date now = new Date();
		if (flag == 1) {
			now.setHours(now.getHours() - 24);
		} else if (flag == 7) {
			now.setDate(now.getDay() - 7);
		}
		return Integer.parseInt(new SimpleDateFormat("yyyyMMddHH").format(now));
	}

	float ex;

	public int getLastP() {
		return lastP;
	}

	public void setLastP(int lastP) {
		this.lastP = lastP;
	}

	public ListView getListView() {
		return listview;
	}

	public int getIsvip() {
		return isvip;
	}

	public void setIsvip(int isvip) {
		this.isvip = isvip;
	}

	public ListViewAdapt getAdapt() {
		return adapt;
	}

	public void setAdapt(ListViewAdapt adapt) {
		this.adapt = adapt;
	}

	public PopupWindow getCurpop() {
		return curpop;
	}

	public void setCurpop(PopupWindow curpop) {
		this.curpop = curpop;
	}

}
