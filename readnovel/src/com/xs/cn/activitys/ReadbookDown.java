package com.xs.cn.activitys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.beans.BookMark;
import com.eastedge.readnovel.beans.Chapterinfo;
import com.eastedge.readnovel.beans.RDBook;
import com.eastedge.readnovel.beans.Shubenmulu;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.common.PageFlipController;
import com.eastedge.readnovel.common.ReadBookShareListener;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.db.DBAdapter;
import com.eastedge.readnovel.db.LastReadTable;
import com.eastedge.readnovel.task.ReadBookInitTask;
import com.eastedge.readnovel.task.SupportAuthorPageTask;
import com.eastedge.readnovel.utils.CommonUtils;
import com.eastedge.readnovel.view.BookPageFactory;
import com.eastedge.readnovel.view.PageWidget;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.openapi.QZoneAble;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.R;

/**
 * 本地阅读
 * 
 * @author li.li
 *
 * Jul 25, 2012
 */
public class ReadbookDown extends QZoneAble implements OnClickListener {
	private PageWidget mPageWidget; //翻页组建		
	private Bitmap mCurPageBitmap, mNextPageBitmap; //mCurPageBitmap 当前页bitmap  mNextPageBitmap下一页的bitmap
	private Canvas mCurPageCanvas, mNextPageCanvas; //mCurPageCanvas 当前页画笔	   mNextPageCanvas 下一页画笔
	BookPageFactory pagefactory; //绘制文字
	private RelativeLayout yy, yy2, menuRl, rl, topRl; //yy 整个的阴影背景   yy2中间触摸的部分  menuRl底部菜单栏 rl放书本阅读的布局 topRl 顶部标题栏
	private SeekBar readszseek, readjpseek, readltseek; //readszseek 字体滑动条  readjpseek 进度跳转滑动条  readltseek亮度改变 滑动条
	private int nowbgid = 0; //当前背景id
	private RadioGroup group, rdbgRG; //group  底部菜单栏RadioGroup  rdbgRG为选择背景的RadioGroup
	private int beg = 0, isV = 0; //beg 开始位置   isV 是否为vip
	private Button btnBack, btnML, btnBM, btnReadjp1, btnReadjp2, btnReadsize1, //btnBack 返回按钮	btnML 目录按钮  btnBM书签按钮	btnReadjp1 向前跳0.1%  btnReadjp2 向后跳0.1%
			btnReadsize2, btnReadlight1, btnReadlight2, btnReadjps1, //btnReadsize1 上个字体 btnReadsize2 下个字体  btnReadlight1 上个亮度  btnReadlight2 下个亮度
			btnReadjps2; //btnReadjps1 上一章   btnReadjps2 下一章
	private ImageView addMark; // 书签的图标
	private RadioButton rbtn1, rbtn2, rbtn3, rbtn4, rbtn5, //底部从左到右的5个按钮
			rdbg1, rdbg2, rdbg3, rdbg4; //选择背景的时候4个背景
	private LinearLayout rly1, rly2, rly3, rly5, nowly; //5个按钮对应的出现的布局。  因为 白天黑夜没有布局 所有没有rly4 nowly当前显示的2级布局
	private File file; //阅读对应的文件
	private TextView jpTex;; //进度跳转时  显示当前进度的文本
	private Chapterinfo curChapterinfo; //当前章节对象
	private Shubenmulu mul; //当前书的 书本目录对象
	private HashMap<String, Chapterinfo> txMap = new HashMap<String, Chapterinfo>();//章节的id   对应的章节对象
	private String aid, txid; //txid当前章节的id
	private int lastseek; // 记录章节进度的 最后次进度
	private String bookFile; //文件所在的路径
	private Animation animationout, animationin, animationins, animationouts, //隐藏  显示动画
			animationin2, animationInShare, animationOutShare;
	private HashMap<String, Long> sqmap = null; //书签 key为 书的id加上记录的位置 value为对应 书签记录自动增长的id（删除用）
	private DBAdapter dbAdapter = null; //操作用的数据库
	private AlphaAnimation animationout3;
	private RelativeLayout help; //第一次进入 引导页的布局
	private int admk = 0; //顶部消失 动画操作 记录当前是否有书签 有为1 没有为0
	private int finishFlag; //判断是否完结

	private LinearLayout shareTooks;//分享工具栏
	private ReadBookShareListener readBookShareListener;//工具栏监听器
	private ImageView supportAuthor;//红包

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//打开数据库
		setContentView(R.layout.readbook);
		CloseActivity.add(this);

	}

	@Override
	protected void onStart() {
		super.onStart();

		//是否屏幕常亮
		if (LocalStore.getKeepScreenOn(this))
			CommonUtils.keepScreenOn(this, true);
		else
			CommonUtils.keepScreenOn(this, false);

		//异步初始化
		new ReadBookInitTask(this).execute();

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		CloseActivity.curActivity = this;

		//刚进入的时候 隐藏布局
		if (nowly != null)
			nowly.setVisibility(View.GONE);
		if (group != null)
			group.clearCheck();
		if (yy != null)
			yy.setVisibility(View.GONE);
		if (menuRl != null)
			menuRl.setVisibility(View.GONE);
		if (topRl != null)
			topRl.setVisibility(View.GONE);
	}

	/**
	 * 判断是否有书签 
	 */
	private void hasBookMark() {
		if (curChapterinfo != null && sqmap.containsKey(curChapterinfo.getId() + pagefactory.m_mbBufBegin)) {
			addMark.setVisibility(View.VISIBLE);
			addMark.startAnimation(animationin2);
		} else {
			if (addMark.getVisibility() == View.VISIBLE) {
				addMark.setVisibility(View.GONE);
				addMark.startAnimation(animationout3);
			}
		}
	}

	/**
	 * 初始化 打开书 显示首页
	 */
	public void doinit() {
		try {
			//获取当前章节
			if (txid != null && !"".equals(txid)) {
				curChapterinfo = txMap.get(txid);
			} else if (curChapterinfo != null && curChapterinfo.getLen() == 0) {
				curChapterinfo = mul.getMulist().get(0);
			} else if (mul.getMulist() != null)
				curChapterinfo = mul.getMulist().get(0);

			if (curChapterinfo != null) {
				//添加书本控件
				rl.removeAllViews();
				rl.addView(mPageWidget);
				setJumpLis();
				mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);

				//分享监听器初始化
				if (readBookShareListener == null)
					readBookShareListener = new ReadBookShareListener(this);
				readBookShareListener.setAId(aid);
				shareTooks.setOnClickListener(readBookShareListener);//工具栏注入监听器

				PageFlipController.getInstance().initPage(this);

			}

		} catch (IOException e1) {
			Toast.makeText(ReadbookDown.this, "获取电子书有错", Toast.LENGTH_LONG).show();
			LogUtils.error(e1.getMessage(), e1);
		}

	}

	/**
	 * 设置滑动条
	 */
	public void setSeekInint() {
		readszseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				//判断跟上次改变的一样不一样
				if (lastseek != readszseek.getProgress()) {
					pagefactory.setFontSize(readszseek.getProgress()); //改变字体大小
					refresh();
					LocalStore.setFontsize(ReadbookDown.this, readszseek.getProgress()); //保存本地
				}
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				lastseek = seekBar.getProgress();
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}
		});

		readltseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				Util.setBrightness(ReadbookDown.this, readltseek.getProgress() + 20); //改变亮度
				LocalStore.setMrld(ReadbookDown.this, readltseek.getProgress()); //保存本地
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}
		});
	}

	/**
	 * 判断是否是白天 如果是 改成晚上 并图标
	 */
	private void doChangeBg() {
		if ("白天".equals(rbtn4.getText().toString())) {
			rbtn4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.menu_read41), null, null);
			rbtn4.setText("夜晚");
		}
	}

	/**
	 * 刷新
	 */
	private void refresh() {
		pagefactory.onDraw(mCurPageCanvas);
		pagefactory.onDraw(mNextPageCanvas);
		mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
		mPageWidget.postInvalidate();
	}

	/**
	 * 按键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			//判断当前才菜单是否显示	 显示则隐藏， 隐藏则显示
			if (menuRl.getVisibility() == View.VISIBLE)
				doGone();
			else
				doShow();

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {

			//然后阴影显示 则隐藏阴影 
			if (yy.getVisibility() == View.VISIBLE) {
				doGone();
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 隐藏
	 */
	public void doGone() {
		nowly.setVisibility(View.GONE); //2级布局隐藏
		group.clearCheck(); //清除选中状态
		yy.setVisibility(View.GONE); //阴影隐藏

		menuRl.setVisibility(View.GONE); //底部隐藏
		topRl.setVisibility(View.GONE); //头部隐藏

		menuRl.startAnimation(animationout); //隐藏动画
		topRl.startAnimation(animationouts);//隐藏动画

		shareTooks.setVisibility(View.GONE);
		shareTooks.startAnimation(animationOutShare);

		supportAuthor.setVisibility(View.GONE);
		supportAuthor.startAnimation(animationOutShare);
	}

	/**
	 * 显示
	 */
	public void doShow() {
		yy.setVisibility(View.VISIBLE); //显示阴影

		menuRl.setVisibility(View.VISIBLE); //显示导航栏
		topRl.setVisibility(View.VISIBLE); //显示顶部

		menuRl.startAnimation(animationin); //出现动画
		topRl.startAnimation(animationins);//出现动画

		shareTooks.setVisibility(View.VISIBLE);
		shareTooks.startAnimation(animationInShare);

		supportAuthor.setVisibility(View.VISIBLE);
		supportAuthor.startAnimation(animationInShare);
	}

	/**
	 * 点击事件
	 */
	public void onClick(View v) {
		// 跳上一章
		if (v.getId() == btnReadjps1.getId()) {
			if (curChapterinfo != null && curChapterinfo.getPreid() != null) {
				Chapterinfo c = txMap.get(curChapterinfo.getPreid());
				if (c != null) {
					curChapterinfo = c;
					hasBookMark();
					//跳到下一章的完整操作，包括，更换章节名，读取内容，画出当前界面。
					// 获取当前章节 设置章节头
					pagefactory.setTitle(curChapterinfo.getSubhead());
					try {
						pagefactory.openbook(file, 0, curChapterinfo.getPosi(), curChapterinfo.getLen());
					} catch (IOException e) {
						e.printStackTrace();
					}
					refresh();
					return;
				}

			}
			Toast.makeText(ReadbookDown.this, "没有上一章节", Toast.LENGTH_SHORT).show();
			return;
		}
		// 跳下一章
		else if (v.getId() == btnReadjps2.getId()) {
			if (curChapterinfo != null && curChapterinfo.getNextvip() == 1) {
				Toast.makeText(ReadbookDown.this, "下一章为vip章节 请在线进行阅读", Toast.LENGTH_SHORT).show();
				return;
			}
			if (curChapterinfo != null && curChapterinfo.getNextid() != null) {
				Chapterinfo c = txMap.get(curChapterinfo.getNextid());
				if (c != null) {
					curChapterinfo = c;
					hasBookMark();
					// 获取当前章节 设置章节头
					pagefactory.setTitle(curChapterinfo.getSubhead());
					try {
						pagefactory.openbook(file, 0, curChapterinfo.getPosi(), curChapterinfo.getLen());
					} catch (IOException e) {
						e.printStackTrace();
					}
					refresh();
					return;
				}

			}
			Toast.makeText(ReadbookDown.this, "没有下一章节", Toast.LENGTH_SHORT).show();
			return;
		} else if (v.getId() == btnReadlight1.getId()) {
			//向左调亮度
			int v1 = readltseek.getProgress();
			if (v1 == 0) {
				return;
			}
			readltseek.setProgress(v1 - 10);
			LocalStore.setMrld(ReadbookDown.this, v1 - 10);
			Util.setBrightness(ReadbookDown.this, v1 + 10);
		} else if (v.getId() == btnReadlight2.getId()) {
			//向右调亮度
			int v1 = readltseek.getProgress();
			if (v1 == 235) {
				return;
			}
			readltseek.setProgress(v1 + 10);
			LocalStore.setMrld(ReadbookDown.this, v1 + 10);
			Util.setBrightness(ReadbookDown.this, v1 + 30);
		}

		else if (v.getId() == btnReadsize1.getId()) {
			//字体小一号
			int v1 = readszseek.getProgress();
			if (v1 == 0) {
				return;
			}
			readszseek.setProgress(v1 - 1);
			LocalStore.setFontsize(ReadbookDown.this, v1 - 1);//把设置的字保存起来
			pagefactory.setFontSize(v1 - 1);//进行更新，重新计算显示多少行，显示什么内容。
			refresh();
		} else if (v.getId() == btnReadsize2.getId()) {
			//字体大一号
			int v1 = readszseek.getProgress();
			if (v1 == 4) {
				return;
			}
			readszseek.setProgress(v1 + 1);
			LocalStore.setFontsize(ReadbookDown.this, v1 + 1);
			pagefactory.setFontSize(v1 + 1);
			refresh();
		} else if (v.getId() == btnBack.getId()) {
			//返回按钮
			finish();
			return;
		} else if (v.getId() == btnML.getId()) {
			//跳转到目录页码
			Intent intent = new Intent(ReadbookDown.this, BfMLActivity.class);
			intent.putExtra("aid", aid);
			intent.putExtra("nowtxid", curChapterinfo.getId());
			intent.putExtra("curf", curChapterinfo.getCurF());
			intent.putExtra("imgFile", getIntent().getStringExtra("imgFile"));
			startActivityForResult(intent, 222);
		} else if (v.getId() == addMark.getId()) {
			//点击书签的图标删除书签
			dbAdapter.deleteOneMark(aid, pagefactory.m_mbBufBegin);
			sqmap = dbAdapter.queryAllBookP(aid, 0);
			addMark.setVisibility(View.GONE);
			addMark.startAnimation(animationout3);
			refresh();
		} else if (v.getId() == btnBM.getId()) {
			//判断当前书签 是否存在 
			if (addMark.getVisibility() == View.GONE) {
				//添加书签
				addMk();
				sqmap = dbAdapter.queryAllBookP(aid, 0);
				addMark.setVisibility(View.VISIBLE);
				addMark.startAnimation(animationin2);
			} else {
				//删除书签
				dbAdapter.deleteOneMark(aid, pagefactory.m_mbBufBegin);
				sqmap = dbAdapter.queryAllBookP(aid, 0);
				addMark.setVisibility(View.GONE);
				addMark.startAnimation(animationout3);
			}
			refresh();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		/**
		 * 目录页返回
		 */
		shareTooks.setVisibility(View.GONE);

		if (requestCode == 222) {
			//跳转到相应的章节 相应的位置
			String tid = data.getStringExtra("txid");
			int b = data.getIntExtra("beg", 0);
			curChapterinfo = txMap.get(tid);
			if (curChapterinfo != null) {
				pagefactory.setTitle(curChapterinfo.getSubhead());
				try {
					pagefactory.openbook(file, b, curChapterinfo.getPosi(), curChapterinfo.getLen());
				} catch (IOException e) {
					e.printStackTrace();
				}
				refresh();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 添加书签
	 */
	private void addMk() {
		BookMark bf = new BookMark();
		bf.setArticleid(aid);
		bf.setTextid(curChapterinfo.getId());
		bf.setTexttitle(curChapterinfo.getSubhead());
		bf.setTextjj(pagefactory.getJJ());
		bf.setTime(System.currentTimeMillis());
		bf.setLocation(pagefactory.m_mbBufBegin);
		bf.setLength(pagefactory.m_mbBufLen);
		dbAdapter.insertBookMark(bf, 0);
	}

	/**
	 * 进度条 设置
	 */
	public void setJumpLis() {
		if (readjpseek != null) {
			readjpseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				}

				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				public void onStopTrackingTouch(SeekBar seekBar) {
					pagefactory.jump(readjpseek.getProgress()); //跳到相应的位置
					hasBookMark(); //判断该位置有没书签
					refresh();
				}
			});
		}
		if (btnReadjp1 != null) {
			btnReadjp1.setOnClickListener(new OnClickListener() {
				public void onClick(View v1) {
					pagefactory.jumpleft(); //跳转到上个章节
					hasBookMark(); //判断该位置有没书签
					refresh();
				}
			});
		}
		if (btnReadjp2 != null) {
			btnReadjp2.setOnClickListener(new OnClickListener() {
				public void onClick(View v1) {
					pagefactory.jumpRight(); //跳转到下个章节
					hasBookMark(); //判断该位置有没书签
					refresh();
				}
			});
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		CommonUtils.keepScreenOn(this, false);
	}

	/**
	 * 记录最后一次阅读的位置
	 */
	public void saveLastRead() {
		if (file != null && file.exists()) {
			LastReadTable db = new LastReadTable(this);
			db.open();
			RDBook lastBook = new RDBook();
			lastBook.setArticleId(aid);
			lastBook.setPosi(pagefactory.m_mbBufBegin);
			lastBook.setIsVip(isV);
			lastBook.setTextId(curChapterinfo.getId());
			lastBook.setBookFile(file.getPath());
			lastBook.setFinishflag(finishFlag);
			db.insertLastRead(lastBook);
			db.close();
		}
	}

	/**
	 * 清空缓存
	 */
	protected void onDestroy() {
		super.onDestroy();

		mNextPageCanvas = null;
		mCurPageCanvas = null;
		//回收 mCurPageBitmap
		if (mCurPageBitmap != null && !mCurPageBitmap.isRecycled()) {
			mCurPageBitmap.recycle();
			mCurPageBitmap = null;
		}
		//回收 mCurPageBitmap
		if (mNextPageBitmap != null && !mNextPageBitmap.isRecycled()) {
			mNextPageBitmap.recycle();
			mNextPageBitmap = null;
		}
		if (pagefactory != null)
			pagefactory.destry();
		mul = null;
		System.gc();
		CloseActivity.remove(this);

		PageFlipController.getInstance().destoryAd();

	}

	/**
	 * 把章节list列表转为txMap
	 */
	public void setTxMap() {
		ArrayList<Chapterinfo> clist = mul.getMulist();
		if (clist != null) {
			int i = 0;
			for (Chapterinfo cinfo : clist) {
				cinfo.setCurF(i);
				i++;
				txMap.put(cinfo.getId(), cinfo);
			}
		}
	}

	/**
	 * 初始化
	 */
	public void find() {
		yy = (RelativeLayout) findViewById(R.id.read_yy);
		yy2 = (RelativeLayout) findViewById(R.id.read_yy2);
		menuRl = (RelativeLayout) findViewById(R.id.menu_button);
		topRl = (RelativeLayout) findViewById(R.id.read_top);
		nowly = rly1 = (LinearLayout) findViewById(R.id.do_readsize);
		rly2 = (LinearLayout) findViewById(R.id.do_readbg);
		rly3 = (LinearLayout) findViewById(R.id.do_readlight);
		rly5 = (LinearLayout) findViewById(R.id.do_readjp);
		jpTex = (TextView) findViewById(R.id.readjp_txt);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnML = (Button) findViewById(R.id.btn_readmkl);
		btnBM = (Button) findViewById(R.id.btn_readmkr);
		btnReadjp1 = (Button) findViewById(R.id.btn_readjp1);
		btnReadjp2 = (Button) findViewById(R.id.btn_readjp2);
		btnReadjps1 = (Button) findViewById(R.id.btn_readjps1);
		btnReadjps2 = (Button) findViewById(R.id.btn_readjps2);
		btnReadsize1 = (Button) findViewById(R.id.btn_readsize1);
		btnReadsize2 = (Button) findViewById(R.id.btn_readsize2);
		btnReadlight1 = (Button) findViewById(R.id.btn_readlight1);
		btnReadlight2 = (Button) findViewById(R.id.btn_readlight2);
		rl = (RelativeLayout) findViewById(R.id.read_txrl);
		readszseek = (SeekBar) findViewById(R.id.readszseek);
		readjpseek = (SeekBar) findViewById(R.id.readjpseek);
		readltseek = (SeekBar) findViewById(R.id.readltseek);
		addMark = (ImageView) findViewById(R.id.addmark);
		shareTooks = (LinearLayout) findViewById(R.id.read_book_share_tools);

		supportAuthor = (ImageView) findViewById(R.id.support_author);//红包

		/**
		 * 1. 当前为QQ登陆时，并且session未过期只显示QQ分享按钮
		 * 2. 当前为Sina登陆时，并且session未过期只显示sina分享按钮
		 * 3. 当都普通登陆时，qq分享和sina分享按钮都显示 
		 */
		View shareQQ = findViewById(R.id.read_book_share_qq_button);
		View shareSina = findViewById(R.id.read_book_share_sina_button);
		CommonUtils.controlShareButton(this, shareQQ, shareSina);

		rly1.setOnClickListener(this);
		rly2.setOnClickListener(this);
		rly3.setOnClickListener(this);
		rly5.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnML.setOnClickListener(this);
		addMark.setOnClickListener(this);
		btnBM.setOnClickListener(this);
		topRl.setOnClickListener(this);
		btnReadsize1.setOnClickListener(this);
		btnReadsize2.setOnClickListener(this);
		btnReadlight1.setOnClickListener(this);
		btnReadlight2.setOnClickListener(this);
		btnReadjps1.setOnClickListener(this);
		btnReadjps2.setOnClickListener(this);

	}

	/**
	 * 初始化 RadioGroup
	 */
	public void setRadioInit() {
		group = (RadioGroup) findViewById(R.id.radioGroup);
		rdbgRG = (RadioGroup) findViewById(R.id.rdbgRG);
		rdbg1 = (RadioButton) findViewById(R.id.rdbg1);
		rdbg2 = (RadioButton) findViewById(R.id.rdbg2);
		rdbg3 = (RadioButton) findViewById(R.id.rdbg3);
		rdbg4 = (RadioButton) findViewById(R.id.rdbg4);
		rbtn1 = (RadioButton) findViewById(R.id.menu_btn1);
		rbtn2 = (RadioButton) findViewById(R.id.menu_btn2);
		rbtn3 = (RadioButton) findViewById(R.id.menu_btn3);
		rbtn4 = (RadioButton) findViewById(R.id.menu_btn4);
		rbtn5 = (RadioButton) findViewById(R.id.menu_btn5);
		group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == rbtn1.getId()) {
					//字体大小
					if (rbtn1.isChecked()) {
						nowly.setVisibility(View.GONE);
						rly1.setVisibility(View.VISIBLE);
						rly1.startAnimation(animationin2);
						nowly = rly1;
						refresh();
					}
				} else if (checkedId == rbtn2.getId()) {
					//背景
					if (rbtn2.isChecked()) {
						nowly.setVisibility(View.GONE);
						rly2.setVisibility(View.VISIBLE);
						rly2.startAnimation(animationin2);
						nowly = rly2;
						refresh();
					}
				} else if (checkedId == rbtn3.getId()) {
					//亮度
					if (rbtn3.isChecked()) {
						nowly.setVisibility(View.GONE);
						rly3.setVisibility(View.VISIBLE);
						rly3.startAnimation(animationin2);
						nowly = rly3;
						refresh();
					}
				} else if (checkedId == rbtn4.getId()) {
					//夜晚
					nowly.setVisibility(View.GONE);
					group.clearCheck();
					if ("夜晚".equals(rbtn4.getText().toString())) {
						rbtn4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.menu_read42), null, null);
						rbtn4.setText("白天");
						pagefactory.cgbg(4);
						mPageWidget.cgbg(4);
						refresh();
						LocalStore.setMrnt(ReadbookDown.this, 1);
					} else {
						rbtn4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.menu_read41), null, null);
						rbtn4.setText("夜晚");
						pagefactory.cgbg(nowbgid);
						mPageWidget.cgbg(nowbgid);
						refresh();
						LocalStore.setMrnt(ReadbookDown.this, 0);
					}
				} else if (checkedId == rbtn5.getId()) {
					//跳转
					if (rbtn5.isChecked()) {
						nowly.setVisibility(View.GONE);
						rly5.setVisibility(View.VISIBLE);
						rly5.startAnimation(animationin2);
						nowly = rly5;
						refresh();
					}
				}
			}
		});

		//背景选择
		rdbgRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == rdbg1.getId()) {
					//默认背景
					if (rdbg1.isChecked()) {
						nowbgid = 0;
						pagefactory.cgbg(0);
						mPageWidget.cgbg(0);
						LocalStore.setMrbg(ReadbookDown.this, 0);
						doChangeBg();
						refresh();
					}
				} else if (checkedId == rdbg2.getId()) {
					if (rdbg2.isChecked()) {
						nowbgid = 1;
						pagefactory.cgbg(1);
						mPageWidget.cgbg(1);
						LocalStore.setMrbg(ReadbookDown.this, 1);
						doChangeBg();
						refresh();
					}
				} else if (checkedId == rdbg3.getId()) {
					if (rdbg3.isChecked()) {
						nowbgid = 2;
						pagefactory.cgbg(2);
						mPageWidget.cgbg(2);
						LocalStore.setMrbg(ReadbookDown.this, 2);
						doChangeBg();
						refresh();
					}
				} else if (checkedId == rdbg4.getId()) {
					if (rdbg4.isChecked()) {
						nowbgid = 3;
						pagefactory.cgbg(3);
						mPageWidget.cgbg(3);
						LocalStore.setMrbg(ReadbookDown.this, 3);
						doChangeBg();
						refresh();
					}
				}
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public int getFinishFlag() {
		return finishFlag;
	}

	public void setFinishFlag(int finishFlag) {
		this.finishFlag = finishFlag;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public HashMap<String, Long> getSqmap() {
		return sqmap;
	}

	public void setSqmap(HashMap<String, Long> sqmap) {
		this.sqmap = sqmap;
	}

	public AlphaAnimation getAnimationout3() {
		return animationout3;
	}

	public void setAnimationout3(AlphaAnimation animationout3) {
		this.animationout3 = animationout3;
	}

	public Animation getAnimationin2() {
		return animationin2;
	}

	public void setAnimationin2(Animation animationin2) {
		this.animationin2 = animationin2;
	}

	public PageWidget getmPageWidget() {
		return mPageWidget;
	}

	public void setmPageWidget(PageWidget mPageWidget) {
		this.mPageWidget = mPageWidget;
	}

	public Canvas getmCurPageCanvas() {
		return mCurPageCanvas;
	}

	public void setmCurPageCanvas(Canvas mCurPageCanvas) {
		this.mCurPageCanvas = mCurPageCanvas;
	}

	public Bitmap getmCurPageBitmap() {
		return mCurPageBitmap;
	}

	public void setmCurPageBitmap(Bitmap mCurPageBitmap) {
		this.mCurPageBitmap = mCurPageBitmap;
	}

	public Bitmap getmNextPageBitmap() {
		return mNextPageBitmap;
	}

	public void setmNextPageBitmap(Bitmap mNextPageBitmap) {
		this.mNextPageBitmap = mNextPageBitmap;
	}

	public Chapterinfo getCurChapterinfo() {
		return curChapterinfo;
	}

	public void setCurChapterinfo(Chapterinfo curChapterinfo) {
		this.curChapterinfo = curChapterinfo;
	}

	public BookPageFactory getPagefactory() {
		return pagefactory;
	}

	public void setPagefactory(BookPageFactory pagefactory) {
		this.pagefactory = pagefactory;
	}

	public Shubenmulu getMul() {
		return mul;
	}

	public void setMul(Shubenmulu mul) {
		this.mul = mul;
	}

	public HashMap<String, Chapterinfo> getTxMap() {
		return txMap;
	}

	public void setTxMap(HashMap<String, Chapterinfo> txMap) {
		this.txMap = txMap;
	}

	public Canvas getmNextPageCanvas() {
		return mNextPageCanvas;
	}

	public void setmNextPageCanvas(Canvas mNextPageCanvas) {
		this.mNextPageCanvas = mNextPageCanvas;
	}

	public ImageView getAddMark() {
		return addMark;
	}

	public void setAddMark(ImageView addMark) {
		this.addMark = addMark;
	}

	public RelativeLayout getRl() {
		return rl;
	}

	public void setRl(RelativeLayout rl) {
		this.rl = rl;
	}

	public int getBeg() {
		return beg;
	}

	public void setBeg(int beg) {
		this.beg = beg;
	}

	public String getBookFile() {
		return bookFile;
	}

	public void setBookFile(String bookFile) {
		this.bookFile = bookFile;
	}

	public DBAdapter getDbAdapter() {
		return dbAdapter;
	}

	public void setDbAdapter(DBAdapter dbAdapter) {
		this.dbAdapter = dbAdapter;
	}

	public int getNowbgid() {
		return nowbgid;
	}

	public void setNowbgid(int nowbgid) {
		this.nowbgid = nowbgid;
	}

	public RadioButton getRbtn4() {
		return rbtn4;
	}

	public void setRbtn4(RadioButton rbtn4) {
		this.rbtn4 = rbtn4;
	}

	public TextView getJpTex() {
		return jpTex;
	}

	public void setJpTex(TextView jpTex) {
		this.jpTex = jpTex;
	}

	public SeekBar getReadjpseek() {
		return readjpseek;
	}

	public void setReadjpseek(SeekBar readjpseek) {
		this.readjpseek = readjpseek;
	}

	public RelativeLayout getYy() {
		return yy;
	}

	public void setYy(RelativeLayout yy) {
		this.yy = yy;
	}

	public RelativeLayout getYy2() {
		return yy2;
	}

	public void setYy2(RelativeLayout yy2) {
		this.yy2 = yy2;
	}

	public Animation getAnimationins() {
		return animationins;
	}

	public void setAnimationins(Animation animationins) {
		this.animationins = animationins;
	}

	public int getAdmk() {
		return admk;
	}

	public void setAdmk(int admk) {
		this.admk = admk;
	}

	public SeekBar getReadszseek() {
		return readszseek;
	}

	public void setReadszseek(SeekBar readszseek) {
		this.readszseek = readszseek;
	}

	public SeekBar getReadltseek() {
		return readltseek;
	}

	public void setReadltseek(SeekBar readltseek) {
		this.readltseek = readltseek;
	}

	public int getIsV() {
		return isV;
	}

	public void setIsV(int isV) {
		this.isV = isV;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public Animation getAnimationout() {
		return animationout;
	}

	public void setAnimationout(Animation animationout) {
		this.animationout = animationout;
	}

	public Animation getAnimationin() {
		return animationin;
	}

	public void setAnimationin(Animation animationin) {
		this.animationin = animationin;
	}

	public Animation getAnimationouts() {
		return animationouts;
	}

	public void setAnimationouts(Animation animationouts) {
		this.animationouts = animationouts;
	}

	public RelativeLayout getHelp() {
		return help;
	}

	public void setHelp(RelativeLayout help) {
		this.help = help;
	}

	public LinearLayout getShareTooks() {
		return shareTooks;
	}

	public void setShareTooks(LinearLayout shareTooks) {
		this.shareTooks = shareTooks;
	}

	public ReadBookShareListener getReadBookShareListener() {
		return readBookShareListener;
	}

	public void setReadBookShareListener(ReadBookShareListener readBookShareListener) {
		this.readBookShareListener = readBookShareListener;
	}

	public Animation getAnimationInShare() {
		return animationInShare;
	}

	public void setAnimationInShare(Animation animationInShare) {
		this.animationInShare = animationInShare;
	}

	public Animation getAnimationOutShare() {
		return animationOutShare;
	}

	public void setAnimationOutShare(Animation animationOutShare) {
		this.animationOutShare = animationOutShare;
	}

	/**
	 * 支持作者
	 * @param v
	 */
	public void supportAuthor(View v) {
		new SupportAuthorPageTask(this, aid, 1).execute();
	}

}