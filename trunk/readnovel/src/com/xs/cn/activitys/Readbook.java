package com.xs.cn.activitys;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.ad.MoGoAdvert;
import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.beans.BookMark;
import com.eastedge.readnovel.beans.OrderAllMsg;
import com.eastedge.readnovel.beans.RDBook;
import com.eastedge.readnovel.beans.Shubenmulu;
import com.eastedge.readnovel.beans.Shubenxinxiye;
import com.eastedge.readnovel.beans.SubResultBean;
import com.eastedge.readnovel.beans.orm.OrderRecord;
import com.eastedge.readnovel.beans.orm.SupportAuthorRecord;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.Constants.LoginType;
import com.eastedge.readnovel.common.HCData;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.common.ReadBookShareListener;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.db.AutoOrderTable;
import com.eastedge.readnovel.db.Bookmuluadb;
import com.eastedge.readnovel.db.DBAdapter;
import com.eastedge.readnovel.db.LastReadTable;
import com.eastedge.readnovel.db.orm.OrmDBHelper;
import com.eastedge.readnovel.task.BatchSubTextTask;
import com.eastedge.readnovel.task.SupportAuthorPageTask;
import com.eastedge.readnovel.utils.CommonUtils;
import com.eastedge.readnovel.view.BookPageFactory;
import com.eastedge.readnovel.view.DialogView;
import com.eastedge.readnovel.view.PageWidget;
import com.eastedge.readnovel.view.PageWidgetFactory;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.mobclick.android.MobclickAgent;
import com.readnovel.base.common.NetType;
import com.readnovel.base.db.orm.DBHelper;
import com.readnovel.base.openapi.QZoneAble;
import com.readnovel.base.util.DateUtils;
import com.readnovel.base.util.FileUtils;
import com.readnovel.base.util.JsonUtils;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.NetUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;
import com.xs.cn.http.DownFile;
import com.xs.cn.http.HttpImpl;
import com.xs.cn.http.ReadAheadLoader;

/**
 * 在线阅读
 * 
 * @author li.li
 * 
 *         Jul 25, 2012
 */
@SuppressLint("ParserError")
public class Readbook extends QZoneAble implements OnClickListener {
	private static final ReadAheadLoader readAheadLoader = ReadAheadLoader.getInstance();
	/** Called when the activity is first created. */
	private PageWidget mPageWidget;
	Bitmap mCurPageBitmap, mNextPageBitmap;
	Canvas mCurPageCanvas, mNextPageCanvas;
	BookPageFactory pagefactory;
	private RelativeLayout yy, yy2, yy3, menuRl, rl, topRl, showly3, showly1, showly2;// r1书本阅读页面
	private int sw, sh;
	private SeekBar readszseek, readjpseek, readltseek;
	private int nowbgid = 0;
	private RadioGroup group, rdbgRG;
	private int nowChecked;
	private CheckBox checkBox;
	private int beg = 0, isV = 0;
	private Button btnBack, btnML, btnBM, btnReadjp1, btnReadjp2, btnReadsize1, btnReadsize2, btnReadlight1, btnReadlight2, btnReadjps1, btnReadjps2;
	private ProgressDialog mWaitDg;
	private ProgressDialog progress;
	private ImageView addMark;
	private TextView jpTex;
	private RadioButton rbtn1, rbtn2, rbtn3, rbtn4, rbtn5, rdbg1, rdbg2, rdbg3, rdbg4;
	private LinearLayout rly1, rly2, rly3, rly5, nowly;
	private File file;
	private TextView titleTx;
	private String textid, aid, title, imgUrl, imgFile;
	private String intenttag;
	private RDBook rd;
	private int lastseek, lastjp, fcdVip;
	private OrderAllMsg allMsg;
	private DialogView dia = new DialogView(this);
	private Animation animationout, animationin, animationins, animationouts, animationin2, animationInShare, animationOutShare;
	private int flagFlash = 0;
	private int time = 0;
	private boolean exist, ts;
	private RelativeLayout help;
	private HashMap<String, Long> sqmap;
	private String r;
	private int p = 0;
	private boolean autoOrder;
	private AlphaAnimation animationout3;
	private int admk = 0;
	private int admko = 0;
	private int i = 0;
	private String a;
	private DBAdapter dbAdapter;
	private MoGoAdvert mogo;

	private OrmDBHelper dbHelper;

	private LinearLayout shareTooks;// 分享工具栏
	private ReadBookShareListener readBookShareListener;// 工具栏监听器
	private ImageView supportAuthor;//红包

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 111:
				if (mWaitDg != null && mWaitDg.isShowing()) {
					mWaitDg.dismiss();
				}
				a = "以下为收费章节,必须登录才能阅读！";
				showDL();
				break;
			case 11:
				if (mWaitDg != null && mWaitDg.isShowing()) {
					mWaitDg.dismiss();
				}
				mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
				rl.removeAllViews();
				rl.addView(mPageWidget);
				// 加载广告
				loadAd();

				//显示红包
				showSupportAuthor();

				try {
					if (rd != null)
						pagefactory.setTitle(rd.getTextTitle());
					sqmap = dbAdapter.queryAllBookP(aid, 1);
					pagefactory.openbook(file, beg);
					pagefactory.last();
					setJumpLis();
					dbAdapter.updateSetBookLT(aid);
					if (rd != null && sqmap.containsKey(rd.getTextId() + pagefactory.m_mbBufBegin)) {
						addMark.setVisibility(View.VISIBLE);
						addMark.startAnimation(animationin2);
					} else {
						addMark.setVisibility(View.GONE);
					}
					pagefactory.onDraw(mCurPageCanvas);
					pagefactory.onDraw(mNextPageCanvas);

					// if (mogo != null)
					// mogo.show();

				} catch (IOException e1) {
					Toast.makeText(Readbook.this, "获取电子书有错", Toast.LENGTH_LONG).show();
				}
				break;
			case 12:
				if (mWaitDg != null && mWaitDg.isShowing()) {
					mWaitDg.dismiss();
				}

				mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
				rl.removeAllViews();
				rl.addView(mPageWidget);

				// 加载广告
				loadAd();

				//显示红包
				showSupportAuthor();

				try {
					if (rd != null)
						pagefactory.setTitle(rd.getTextTitle());
					sqmap = dbAdapter.queryAllBookP(aid, 1);

					pagefactory.openbook(file, beg);
					setJumpLis();
					beg = 0;
					dbAdapter.updateSetBookLT(aid);
					if (rd != null && sqmap.containsKey(rd.getTextId() + pagefactory.m_mbBufBegin)) {
						addMark.setVisibility(View.VISIBLE);
						addMark.startAnimation(animationin2);
					} else {
						addMark.setVisibility(View.GONE);
					}
					pagefactory.onDraw(mCurPageCanvas);
					pagefactory.onDraw(mNextPageCanvas);

					//					// 解决三星note2跨章节翻页卡住问题
					//					mPageWidget.startAnimation(1200);
					//					mPageWidget.postInvalidate();

					// 设置分享内容-当前章节名
					readBookShareListener.setChapterName(rd.getTextTitle());

				} catch (IOException e1) {
					Toast.makeText(Readbook.this, "获取电子书有错", Toast.LENGTH_LONG).show();
					LogUtils.error(e1.getMessage(), e1);
				}
				break;
			case 13:
				if (mWaitDg != null && mWaitDg.isShowing()) {
					mWaitDg.dismiss();
				}
				Toast.makeText(Readbook.this, "获取不到信息", Toast.LENGTH_SHORT).show();
				break;
			case 14:
				if (mWaitDg != null && mWaitDg.isShowing()) {
					mWaitDg.dismiss();
				}
				Toast.makeText(Readbook.this, getString(R.string.network_err), Toast.LENGTH_SHORT).show();
				break;
			case 20:
				if (mWaitDg != null && mWaitDg.isShowing()) {
					mWaitDg.dismiss();
				}
				doOrder();
				break;
			// 判断完成之后，vip直接调用这个代码，严格区分全本订阅+折扣订阅 NIMEIDE
			case 21:
				if (mWaitDg != null && mWaitDg.isShowing()) {
					mWaitDg.dismiss();
				}
				rl.removeAllViews();
				yy3.setVisibility(View.VISIBLE);
				showly1.setVisibility(View.VISIBLE);
				topRl.setVisibility(View.VISIBLE);
				titleTx.setVisibility(View.VISIBLE);
				btnBM.setVisibility(View.GONE);
				btnML.setVisibility(View.GONE);
				titleTx.setText(title);
				showly1.removeAllViews();

				if (dia == null || rd == null)
					return;

				View v = dia.getDialogVip(rd.getOrderMsg());
				String tt = rd.getOrderMsg().getText();
				if (tt != null) {
					try {
						String title = "";
						if (rd.getTextTitle() != null)
							title = rd.getTextTitle() + "\r\n";
						if (tt.length() > 50)
							tt = tt.substring(0, 40) + "……";
						tt = "\n\n\n\n\n" + title + tt;
						File ff = new File(getCacheDir(), "temp");
						if (ff.exists())
							ff.delete();
						ff.createNewFile();
						DataOutputStream writer = new DataOutputStream(new FileOutputStream(ff));
						writer.write(tt.getBytes("utf-8"));
						writer.flush();
						writer.close();
						pagefactory.openbook(ff, beg, true);
						setJumpLis();
						beg = 0;
						refresh();
						rl.removeAllViews();
						rl.addView(mPageWidget);

					} catch (Exception e) {
					}
				}
				// 如果是vip章节，还没付费，调用付费逻辑 
				vipView(v);
				showly1.addView(v);
				break;
			case 22:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				Toast.makeText(Readbook.this, "订阅出错", Toast.LENGTH_SHORT).show();
				break;
			case 23:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}

				Toast.makeText(Readbook.this, "订阅成功", Toast.LENGTH_SHORT).show();

				// 第一次订阅时分享-当使用第三方登陆时
				if (LocalStore.getFirstConsume(Readbook.this)) {

					LoginType loginType = LocalStore.getLastLoginType(Readbook.this);
					if (!LoginType.def.equals(loginType)) {

						String bookName = null;
						Shubenmulu mulu = Util.read(aid);
						if (mulu != null) // 本地阅读有目录
							bookName = mulu.getTitle();
						else {// 在线阅读无目录，得到书的明细，从而得到书名
							Shubenxinxiye bookDetail = HttpImpl.Shubenxinxiye(aid);
							if (bookDetail != null)
								bookName = bookDetail.getTitle();
						}
						// 分享内容
						String shareContent = String.format(getString(R.string.book_vip_share_content), bookName);

						if (LoginType.qq.equals(loginType)) // 当QQ登陆时
							CommonUtils.shareForQQ(Readbook.this, shareContent, aid);
						else if (LoginType.sina.equals(loginType)) // 当sina登陆时
							CommonUtils.shareForSina(Readbook.this, shareContent, aid);

						LocalStore.setFirstConsume(Readbook.this, false);
					}
				}

				if (checkBox != null && checkBox.isChecked()) {
					AutoOrderTable autoOrderTable = new AutoOrderTable(Readbook.this);
					autoOrderTable.open();
					autoOrderTable.insertAutoOrder(aid);
					autoOrderTable.close();
					autoOrder = true;
				}

				if (!dbAdapter.exitBookBF1(aid)) {
					BFBook book = new BFBook();
					if (imgUrl != null) {
						book.setImgFile(Util.saveImgFile(Readbook.this, imgUrl));
					} else {
						book.setImgFile(imgFile);
					}
					book.setTitle(title);
					book.setArticleid(aid);
					book.setFinishFlag(rd.getFinishflag());
					dbAdapter.insertBook(book);
					DownFile downFile = new DownFile(Readbook.this, aid, title);
					downFile.start();
					HCData.downingBook.put(aid, downFile);
				}

				String uidd = "-1";
				if (BookApp.getUser() != null && BookApp.getUser().getUid() != null) {
					uidd = BookApp.getUser().getUid();
				}
				// 添加到关系表
				if (!dbAdapter.exitBookGxVip(aid, uidd)) {
					dbAdapter.insertGx(aid, uidd, 1);
				}

				if (!dbAdapter.exitBookGx(aid, uidd)) {
					dbAdapter.insertGx(aid, uidd, 0);
				}

				titleTx.setVisibility(View.GONE);
				btnBM.setVisibility(View.VISIBLE);
				btnML.setVisibility(View.VISIBLE);
				topRl.setVisibility(View.GONE);
				showly1.setVisibility(View.GONE);
				showly2.setVisibility(View.GONE);
				yy3.setVisibility(View.GONE);
				mWaitDg = ViewUtils.progressLoading(Readbook.this);
				GetTextThread t = new GetTextThread(textid, 12, isV);
				t.start();
				break;
			case 31:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				showly1.setVisibility(View.GONE);
				showly2.setVisibility(View.VISIBLE);
				yy3.setVisibility(View.VISIBLE);
				View v2 = dia.getDialogAll(allMsg);
				setView2(v2);
				showly2.setGravity(Gravity.CENTER);
				showly2.addView(v2, new LayoutParams(sw, LayoutParams.WRAP_CONTENT));
				break;
			// 弹出折扣订阅的界面信息，根据则中给出的数据，进行不同的展示 NIMEIDE
			case 33:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				showly1.setVisibility(View.GONE);
				showly2.setVisibility(View.VISIBLE);
				yy3.setVisibility(View.VISIBLE);
				// 全本订阅的接口，这里需要调用其他的不同的界面
				View v11 = dia.getDiscountDialogAll(allMsg);
				setViewALLDiscount(v11);
				showly2.setGravity(Gravity.CENTER);
				showly2.addView(v11, new LayoutParams(sw, LayoutParams.WRAP_CONTENT));
				break;
			case 32:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				Toast.makeText(Readbook.this, "获取章节信息出错", Toast.LENGTH_SHORT).show();
				break;
			case 41://阅读币不足1
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				if (mWaitDg != null && mWaitDg.isShowing()) {
					mWaitDg.dismiss();
				}
				topRl.setVisibility(View.VISIBLE);
				titleTx.setVisibility(View.VISIBLE);
				btnBM.setVisibility(View.GONE);
				btnML.setVisibility(View.GONE);
				yy3.setVisibility(View.VISIBLE);
				titleTx.setText(title);

				LoginType logInType = LocalStore.getLastLoginType(Readbook.this);
				if (logInType != null && LoginType.alipay.equals(logInType)) {
					ViewUtils.toastLong(Readbook.this, "余额不足");
					startActivity(new Intent(Readbook.this, ConsumeOnlyZfb.class));
				} else {
					View v3 = dia.getDialogView3(BookApp.getUser().getUid());
					showly3.removeAllViews();
					showly3.setVisibility(View.VISIBLE);
					showly3.addView(v3);
				}

				break;

			case 42:// 阅读币不足2，弹出充值对话框
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}

				ViewUtils.confirm(Readbook.this, "温馨提示", "您的阅读币不足，请充值阅读币。", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						LoginType logInType = LocalStore.getLastLoginType(Readbook.this);
						if (logInType != null && LoginType.alipay.equals(logInType)) {
							ViewUtils.toastLong(Readbook.this, "余额不足");
							startActivity(new Intent(Readbook.this, ConsumeOnlyZfb.class));
						} else {
							goZfb();
						}
					}

				}, null);
				break;
			case 43:// 已订购
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				Toast.makeText(Readbook.this, "已订阅", Toast.LENGTH_SHORT).show();
				break;
			case 44://订阅出错
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				Toast.makeText(Readbook.this, "订阅出错，非法结果", Toast.LENGTH_SHORT).show();
				break;
			case 45://订阅出错
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				Toast.makeText(Readbook.this, "订阅出错，其它错误", Toast.LENGTH_SHORT).show();
				break;
			}

		};
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readbook);
		CloseActivity.add(this);
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();

		dbHelper = DBHelper.getHelper(OrmDBHelper.class);

		int flag = LocalStore.getFirstRead(Readbook.this);
		if (flag == 0) {
			LocalStore.setFirstRead(Readbook.this, 1);
			help = (RelativeLayout) findViewById(R.id.read_help);
			help.setVisibility(View.VISIBLE);
			help.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					help.setVisibility(View.GONE);
				}
			});
		}

		animationin = AnimationUtils.loadAnimation(this, R.anim.menu_in);
		animationout = AnimationUtils.loadAnimation(this, R.anim.menu_out);
		animationins = AnimationUtils.loadAnimation(this, R.anim.menu_ins);
		animationouts = AnimationUtils.loadAnimation(this, R.anim.menu_outs);

		animationInShare = AnimationUtils.loadAnimation(this, R.anim.menu_in_share);
		animationOutShare = AnimationUtils.loadAnimation(this, R.anim.menu_out_share);

		animationin2 = new AlphaAnimation(0f, 1.0f);
		animationin2.setDuration(300);

		animationout3 = new AlphaAnimation(1.0f, 0);
		animationout3.setDuration(300);

		textid = getIntent().getStringExtra("textid");
		intenttag = getIntent().getStringExtra("Tag");
		p = getIntent().getIntExtra("p", 0);

		fcdVip = getIntent().getIntExtra("fcdVip", 0);
		beg = getIntent().getIntExtra("beg", 0);
		isV = getIntent().getIntExtra("isVip", 0);
		aid = getIntent().getStringExtra("aid");
		title = getIntent().getStringExtra("title");
		imgUrl = getIntent().getStringExtra("imgUrl");
		imgFile = getIntent().getStringExtra("imgFile");
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		sw = display.getWidth();
		sh = display.getHeight();
		readszseek = (SeekBar) findViewById(R.id.readszseek);
		readjpseek = (SeekBar) findViewById(R.id.readjpseek);
		readltseek = (SeekBar) findViewById(R.id.readltseek);
		mPageWidget = PageWidgetFactory.createPageWidget(this, sw, sh);
		rl = (RelativeLayout) findViewById(R.id.read_txrl);

		shareTooks = (LinearLayout) findViewById(R.id.read_book_share_tools);
		supportAuthor = (ImageView) findViewById(R.id.support_author);//红包

		readltseek.setProgress(LocalStore.getMrld(this));
		readszseek.setProgress(LocalStore.getFontsize(this));

		int v = LocalStore.getMrld(this);
		if (v < 20) {
			v = 20;
			LocalStore.setMrld(Readbook.this, 20);
		}
		Util.setBrightness(Readbook.this, v);

		yy = (RelativeLayout) findViewById(R.id.read_yy);
		yy2 = (RelativeLayout) findViewById(R.id.read_yy2);
		yy3 = (RelativeLayout) findViewById(R.id.read_yy3);
		showly1 = (RelativeLayout) findViewById(R.id.showly1);
		showly2 = (RelativeLayout) findViewById(R.id.showly2);
		showly3 = (RelativeLayout) findViewById(R.id.showly3);
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

		// 分享tools
		readBookShareListener = new ReadBookShareListener(Readbook.this);
		shareTooks.setOnClickListener(readBookShareListener);// 工具栏注入监听器

		addMark = (ImageView) findViewById(R.id.addmark);
		titleTx = (TextView) findViewById(R.id.title_tv);

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

		/**
		 * 1. 当前为QQ登陆时，并且session未过期只显示QQ分享按钮 2.
		 * 当前为Sina登陆时，并且session未过期只显示sina分享按钮 3. 当都普通登陆时，qq分享和sina分享按钮都显示
		 */
		View shareQQ = findViewById(R.id.read_book_share_qq_button);
		View shareSina = findViewById(R.id.read_book_share_sina_button);
		CommonUtils.controlShareButton(this, shareQQ, shareSina);

		if ("BookMarkActivity".equals(intenttag)) {
			addMark.setVisibility(View.VISIBLE);
		}

		yy3.setOnClickListener(this);

		yy.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// 隐藏菜单
				nowly.setVisibility(View.GONE);
				group.clearCheck();

				yy.setVisibility(View.GONE);

				menuRl.setVisibility(View.GONE);
				menuRl.startAnimation(animationout);

				topRl.setVisibility(View.GONE);
				topRl.startAnimation(animationouts);

				shareTooks.setVisibility(View.GONE);
				shareTooks.startAnimation(animationOutShare);

				supportAuthor.setVisibility(View.GONE);
				supportAuthor.startAnimation(animationOutShare);

			}
		});

		yy2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// 显示菜单
				yy.setVisibility(View.VISIBLE);

				menuRl.setVisibility(View.VISIBLE);
				menuRl.startAnimation(animationin);

				topRl.setVisibility(View.VISIBLE);
				topRl.startAnimation(animationins);

				shareTooks.setVisibility(View.VISIBLE);
				shareTooks.startAnimation(animationInShare);

				supportAuthor.setVisibility(View.VISIBLE);
				supportAuthor.startAnimation(animationInShare);

				animationins.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {
						if (addMark.getVisibility() == View.VISIBLE) {
							addMark.setVisibility(View.GONE);
							admk = 1;
						}
					}

					public void onAnimationRepeat(Animation animation) {
					}

					public void onAnimationEnd(Animation animation) {
						if (admk == 1) {
							addMark.setVisibility(View.VISIBLE);
							// addMark.startAnimation(animationin2);k
							admk = 0;
						}
						if (admko == 1) {
							addMark.setVisibility(View.VISIBLE);
						}
					}
				});
			}
		});

		setSeekInint();
		setRadioInit();

		mCurPageBitmap = Bitmap.createBitmap(sw, sh, Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap.createBitmap(sw, sh, Bitmap.Config.ARGB_8888);
		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		pagefactory = new BookPageFactory(this, sw, sh, 32, readjpseek, jpTex);
		pagefactory.setFontSize2(LocalStore.getFontsize(this));

		if (LocalStore.getMrnt(this) == 1) {
			rbtn4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.menu_read42), null, null);
			rbtn4.setText("白天");
			pagefactory.cgbg(4);
			mPageWidget.cgbg(4);
			nowbgid = LocalStore.getMrbg(this);
		} else {
			pagefactory.cgbg(LocalStore.getMrbg(this));
			mPageWidget.cgbg(LocalStore.getMrbg(this));
		}

		mPageWidget.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent e) {
				boolean ret = false;
				if (v == mPageWidget) {
					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						mPageWidget.calcCornerXY(e.getX(), e.getY());
						// 画当前页
						pagefactory.onDraw(mCurPageCanvas);
						// 执行翻页(上一页或下一页)
						if (mPageWidget.isDragToRight()) {
							time++;
							if (mogo != null)
								mogo.show();
							try {
								pagefactory.prePage();

								if (rd != null && sqmap.containsKey(rd.getTextId() + pagefactory.m_mbBufBegin)) {
									addMark.setVisibility(View.VISIBLE);
									addMark.startAnimation(animationin2);
								} else {
									addMark.setVisibility(View.GONE);
								}
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							if (pagefactory.isfirstPage()) {
								if (rd != null && rd.getPreId() != null) {
									i--;
									if (!existHC(rd.getPreId(), 11)) {
										mWaitDg = ViewUtils.progressLoading(Readbook.this);
										addMark.setVisibility(View.GONE);
										textid = rd.getPreId();
										GetTextThread t = new GetTextThread(textid, 11, rd.getPreVip());
										t.start();
									}
									return true;
								}
								return false;
							}

						} else {
							// 下一页进行
							time++;
							if (mogo != null)
								mogo.show();
							try {
								// 向左翻
								pagefactory.nextPage();

								if (rd != null && sqmap.containsKey(rd.getTextId() + pagefactory.m_mbBufBegin)) {
									addMark.setVisibility(View.VISIBLE);
									addMark.startAnimation(animationin2);
								} else {
									addMark.setVisibility(View.GONE);
								}
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							if (pagefactory.islastPage()) {
								if (rd == null)
									return false;
								if (rd.getNextId() != null) {
									i++;
									// 这个方法只是为了获得相关的数据
									if (!existHC(rd.getNextId(), 12)) {
										mWaitDg = ViewUtils.progressLoading(Readbook.this, "正在加载,请稍候...");
										addMark.setVisibility(View.GONE);
										textid = rd.getNextId();
										// 启动线程去下载相关的信息，会回传相关的信息
										// 这个是第一步，也要进行解耦，避免必要的耦合

										GetTextThread t = new GetTextThread(textid, 12, rd.getNextVip());
										t.start();
									}
									return true;
								}

								// 最后一页处理
								if (rd != null) {
									CommonUtils.doReadLastPage(Readbook.this, rd.getFinishflag(), rd.getArticleId(), title);
								}

								return false;
							}
						}
						// 画下一页
						pagefactory.onDraw(mNextPageCanvas);
						// 设置翻页(上一页或下一页)
						mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);

						// 记录最后一次阅读的位置
						saveLastRead();

						if (!ts && time >= 5 && !exist && aid != null) {
							ts = true;
							showAddBF();
						}
						if (rd != null && fcdVip != 0) {
							if (rd.getOrder() != 0 && rd.getOrder() + 10 >= fcdVip) {
								if (BookApp.getUser() == null || "".equals(BookApp.getUser().getUid())) {
									if (rd.getOrder() + 10 >= fcdVip) {
										a = "以下为免费章节,必须登录才能阅读！";
									}
									showDL();
								}
							}
						}
					}

					mPageWidget.abortAnimation();
					ret = mPageWidget.doTouchEvent(e);
					if (Util.getAndroidSDKVersion() < 8) {
						pagefactory.onDraw(mCurPageCanvas);
					}
					return ret;
				}

				return false;
			}

		});

		if (!existHC(textid, 12)) {
			mWaitDg = ViewUtils.progressLoading(Readbook.this);
			GetTextThread t = new GetTextThread(textid, 12, isV);
			t.start();

		}

	}

	private boolean existHC(String bkid, int code) {
		if (HCData.bookMap.containsKey(bkid)) {
			RDBook hbk = HCData.bookMap.get(bkid);
			File cacheFile = new File(Constants.READNOVEL_CACHE);
			cacheFile.mkdirs();
			file = new File(cacheFile, hbk.getArticleId() + "_" + bkid + ".txt");
			if (file.exists()) {
				rd = hbk;
				handler.sendEmptyMessage(code);
				return true;
			}
		}
		return false;
	}

	private void setSeekInint() {
		readszseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (lastseek != readszseek.getProgress()) {
					pagefactory.setFontSize(readszseek.getProgress());
					refresh();
					LocalStore.setFontsize(Readbook.this, readszseek.getProgress());
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
				Util.setBrightness(Readbook.this, readltseek.getProgress() + 20);
				LocalStore.setMrld(Readbook.this, readltseek.getProgress());
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}
		});

	}

	public void setJumpLis() {
		if (readjpseek != null) {
			readjpseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				}

				public void onStartTrackingTouch(SeekBar seekBar) {
					lastjp = seekBar.getProgress();
				}

				public void onStopTrackingTouch(SeekBar seekBar) {
					if (lastjp != readjpseek.getProgress()) {
						pagefactory.jump(readjpseek.getProgress());
						if (rd != null && sqmap.containsKey(rd.getTextId() + pagefactory.m_mbBufBegin) && addMark.getVisibility() == View.GONE) {
							addMark.setVisibility(View.VISIBLE);
							addMark.startAnimation(animationin2);
						} else {
							addMark.setVisibility(View.GONE);
						}
						refresh();
					}
				}
			});
		}
		if (btnReadjp1 != null) {
			btnReadjp1.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					pagefactory.jumpleft();
					if (rd != null && sqmap.containsKey(rd.getTextId() + pagefactory.m_mbBufBegin) && addMark.getVisibility() == View.GONE) {
						addMark.setVisibility(View.VISIBLE);
						addMark.startAnimation(animationin2);
					} else {
						addMark.setVisibility(View.GONE);
					}
					refresh();
				}
			});
		}
		if (btnReadjp2 != null) {
			btnReadjp2.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					pagefactory.jumpRight();
					if (rd != null && sqmap.containsKey(rd.getTextId() + pagefactory.m_mbBufBegin) && addMark.getVisibility() == View.GONE) {
						addMark.setVisibility(View.VISIBLE);
						addMark.startAnimation(animationin2);
					} else {
						addMark.setVisibility(View.GONE);
					}
					refresh();
				}
			});
		}
	}

	private void setRadioInit() {
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
					if (rbtn1.isChecked()) {
						nowChecked = checkedId;
						nowly.setVisibility(View.GONE);
						rly1.setVisibility(View.VISIBLE);
						rly1.startAnimation(animationin2);
						nowly = rly1;
						refresh();
					}
				} else if (checkedId == rbtn2.getId()) {
					if (rbtn2.isChecked()) {
						nowChecked = checkedId;
						nowly.setVisibility(View.GONE);
						rly2.setVisibility(View.VISIBLE);
						rly2.startAnimation(animationin2);
						nowly = rly2;
						refresh();
					}
				} else if (checkedId == rbtn3.getId()) {
					if (rbtn3.isChecked()) {
						nowChecked = checkedId;
						nowly.setVisibility(View.GONE);
						rly3.setVisibility(View.VISIBLE);
						rly3.startAnimation(animationin2);
						nowly = rly3;
						refresh();
					}
				} else if (checkedId == rbtn4.getId()) {
					nowly.setVisibility(View.GONE);
					group.clearCheck();
					if ("夜晚".equals(rbtn4.getText().toString())) {
						rbtn4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.menu_read42), null, null);
						rbtn4.setText("白天");
						pagefactory.cgbg(4);
						mPageWidget.cgbg(4);
						refresh();
						LocalStore.setMrnt(Readbook.this, 1);
					}

					else {
						rbtn4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.menu_read41), null, null);
						rbtn4.setText("夜晚");
						pagefactory.cgbg(nowbgid);
						mPageWidget.cgbg(nowbgid);
						refresh();
						LocalStore.setMrnt(Readbook.this, 0);
					}

				} else if (checkedId == rbtn5.getId()) {
					if (rbtn5.isChecked()) {
						nowChecked = checkedId;
						nowly.setVisibility(View.GONE);
						rly5.setVisibility(View.VISIBLE);
						rly5.startAnimation(animationin2);
						nowly = rly5;
						refresh();
					}
				}

			}
		});

		rdbgRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == rdbg1.getId()) {
					if (rdbg1.isChecked()) {
						nowbgid = 0;
						pagefactory.cgbg(0);
						mPageWidget.cgbg(0);
						LocalStore.setMrbg(Readbook.this, 0);
						doChangeBg();
						refresh();
					}

				} else if (checkedId == rdbg2.getId()) {
					if (rdbg2.isChecked()) {
						nowbgid = 1;
						pagefactory.cgbg(1);
						mPageWidget.cgbg(1);
						LocalStore.setMrbg(Readbook.this, 1);
						doChangeBg();
						refresh();
					}
				} else if (checkedId == rdbg3.getId()) {
					if (rdbg3.isChecked()) {
						nowbgid = 2;
						pagefactory.cgbg(2);
						mPageWidget.cgbg(2);
						LocalStore.setMrbg(Readbook.this, 2);
						doChangeBg();
						refresh();
					}
				} else if (checkedId == rdbg4.getId()) {
					if (rdbg4.isChecked()) {
						nowbgid = 3;
						pagefactory.cgbg(3);
						mPageWidget.cgbg(3);
						LocalStore.setMrbg(Readbook.this, 3);
						doChangeBg();
						refresh();
					}
				}

			}
		});
	}

	private void doChangeBg() {
		if ("白天".equals(rbtn4.getText().toString())) {
			rbtn4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.menu_read41), null, null);
			rbtn4.setText("夜晚");
		}
	}

	private void refresh() {
		pagefactory.onDraw(mCurPageCanvas);
		pagefactory.onDraw(mNextPageCanvas);
		mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
		mPageWidget.postInvalidate();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (menuRl.getVisibility() == View.VISIBLE) {
				nowly.setVisibility(View.GONE);
				group.clearCheck();
				yy.setVisibility(View.GONE);

				menuRl.setVisibility(View.GONE);
				menuRl.startAnimation(animationout);

				topRl.setVisibility(View.GONE);
				topRl.startAnimation(animationouts);

				shareTooks.setVisibility(View.GONE);
				shareTooks.startAnimation(animationOutShare);

				animationouts.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {
						if (addMark.getVisibility() == View.VISIBLE) {
							addMark.setVisibility(View.GONE);
							admko = 1;
						}
					}

					public void onAnimationRepeat(Animation animation) {

					}

					public void onAnimationEnd(Animation animation) {
						if (admko == 1) {
							addMark.setVisibility(View.VISIBLE);
							admko = 0;
							return;
						}
						if (admk == 1) {
							addMark.setVisibility(View.VISIBLE);
							addMark.startAnimation(animationin2);
						}
					}
				});
			} else {
				yy.setVisibility(View.VISIBLE);

				topRl.setVisibility(View.VISIBLE);
				menuRl.setVisibility(View.VISIBLE);

				topRl.startAnimation(animationins);
				menuRl.startAnimation(animationin);

				shareTooks.setVisibility(View.VISIBLE);
				shareTooks.startAnimation(animationInShare);

				animationins.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {
						if (addMark.getVisibility() == View.VISIBLE) {
							addMark.setVisibility(View.GONE);
							admk = 1;
						}
					}

					public void onAnimationRepeat(Animation animation) {

					}

					public void onAnimationEnd(Animation animation) {
						if (admk == 1) {
							addMark.setVisibility(View.VISIBLE);
							addMark.startAnimation(animationin2);
							admk = 0;
						}
					}
				});
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {// 后退按钮
			if (showly2.getVisibility() == View.VISIBLE) {
				showly1.setVisibility(View.VISIBLE);
				showly2.setVisibility(View.GONE);
				return true;
			} else if (yy.getVisibility() == View.VISIBLE) {
				nowly.setVisibility(View.GONE);
				group.clearCheck();
				yy.setVisibility(View.GONE);

				topRl.setVisibility(View.GONE);
				topRl.startAnimation(animationouts);

				menuRl.setVisibility(View.GONE);
				menuRl.startAnimation(animationout);

				shareTooks.setVisibility(View.GONE);
				shareTooks.startAnimation(animationOutShare);

				shareTooks.setVisibility(View.GONE);
				shareTooks.startAnimation(animationOutShare);

				return true;
			} else if ("Novel_sbxxy".equals(intenttag)) {

				Intent intent = new Intent(Readbook.this, Novel_sbxxy.class);
				Bundle bundle = new Bundle();
				bundle.putString("Articleid", aid);
				intent.putExtra("newbook", bundle);
				startActivity(intent);
				finish();

				return false;
			} else if ("Novel_sbxxy_mulu".equals(intenttag)) {
				Intent intent = new Intent(Readbook.this, Novel_sbxxy_mulu.class);
				Bundle bundle = new Bundle();
				bundle.putString("Articleid", aid);
				bundle.putInt("beg", pagefactory.m_mbBufBegin);
				bundle.putString("nowtextid", textid);
				if (p != 0) {
					bundle.putInt("p", (p + i));
				}
				intent.putExtra("newbook", bundle);
				intent.putExtra("imgFile", imgFile);
				Bookmuluadb bookmuluadb = new Bookmuluadb(Readbook.this);
				bookmuluadb.open();
				bookmuluadb.insertBook(aid, textid, p + i, pagefactory.m_mbBufBegin);
				bookmuluadb.close();

				startActivity(intent);
				finish();

				return false;
			}
			// finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onClick(View v) {
		if (v.getId() == btnReadjps1.getId()) {
			if (rd.getPreId() != null) {
				i--;
				if (!existHC(rd.getPreId(), 12)) {
					mWaitDg = ViewUtils.progressLoading(Readbook.this);
					textid = rd.getPreId();
					GetTextThread t = new GetTextThread(textid, 12, rd.getPreVip());
					t.start();
				}
				return;
			} else {
				Toast.makeText(Readbook.this, "没有上一章节", Toast.LENGTH_SHORT).show();
				return;
			}
		} else if (v.getId() == btnReadjps2.getId()) {
			if (rd.getNextId() != null) {
				i++;
				if (!existHC(rd.getNextId(), 12)) {
					mWaitDg = ViewUtils.progressLoading(Readbook.this);
					textid = rd.getNextId();
					GetTextThread t = new GetTextThread(textid, 12, rd.getNextVip());
					t.start();
				}
				return;
			} else {
				Toast.makeText(Readbook.this, "没有下一章节", Toast.LENGTH_SHORT).show();
				return;
			}
		} else if (v.getId() == btnReadlight1.getId()) {
			int v1 = readltseek.getProgress();
			if (v1 == 0) {
				return;
			}
			readltseek.setProgress(v1 - 10);
			LocalStore.setMrld(Readbook.this, v1 - 10);
			Util.setBrightness(Readbook.this, v1 + 10);
		} else if (v.getId() == btnReadlight2.getId()) {
			int v1 = readltseek.getProgress();
			if (v1 == 235) {
				return;
			}
			readltseek.setProgress(v1 + 10);
			LocalStore.setMrld(Readbook.this, v1 + 10);
			Util.setBrightness(Readbook.this, v1 + 30);
		}

		else if (v.getId() == btnReadsize1.getId()) {
			int v1 = readszseek.getProgress();
			if (v1 == 0) {
				return;
			}
			readszseek.setProgress(v1 - 1);
			LocalStore.setFontsize(Readbook.this, v1 - 1);
			pagefactory.setFontSize(v1 - 1);
			refresh();
		} else if (v.getId() == btnReadsize2.getId()) {
			int v1 = readszseek.getProgress();
			if (v1 == 4) {
				return;
			}
			readszseek.setProgress(v1 + 1);
			LocalStore.setFontsize(Readbook.this, v1 + 1);
			pagefactory.setFontSize(v1 + 1);
			refresh();
		} else if (v.getId() == btnBack.getId()) {
			if (showly2.getVisibility() == View.VISIBLE) {
				showly1.setVisibility(View.VISIBLE);
				showly2.setVisibility(View.GONE);
				return;
			}

			if ("Novel_sbxxy".equals(intenttag)) {
				if (rd != null && aid != null && textid != null) {
					HCData.readtextidmap.put(aid, textid);
				}
				Intent intent = new Intent(Readbook.this, Novel_sbxxy.class);
				Bundle bundle = new Bundle();
				bundle.putString("Articleid", aid);
				intent.putExtra("newbook", bundle);
				startActivity(intent);
				finish();

				return;
			}
			if ("Novel_sbxxy_mulu".equals(intenttag)) {
				Intent intent = new Intent(Readbook.this, Novel_sbxxy_mulu.class);
				Bundle bundle = new Bundle();
				bundle.putString("Articleid", aid);
				bundle.putString("nowtextid", textid);
				bundle.putInt("beg", pagefactory.m_mbBufBegin);
				if (p != 0) {
					bundle.putInt("p", (p + i));
				}
				intent.putExtra("newbook", bundle);
				intent.putExtra("imgFile", imgFile);
				Bookmuluadb bookmuluadb = new Bookmuluadb(Readbook.this);
				bookmuluadb.open();
				bookmuluadb.insertBook(aid, textid, p + i, pagefactory.m_mbBufBegin);
				bookmuluadb.close();

				startActivity(intent);
				finish();
				return;
			}
			finish();

		} else if (v.getId() == btnML.getId()) {
			Intent intent = new Intent(Readbook.this, Novel_sbxxy_mulu.class);
			Bundle bundle = new Bundle();
			bundle.putString("Articleid", aid);
			bundle.putString("nowtextid", textid);
			bundle.putInt("beg", pagefactory.m_mbBufBegin);
			if (p != 0) {
				bundle.putInt("p", (p + i));
			}
			intent.putExtra("newbook", bundle);
			intent.putExtra("imgFile", imgFile);
			Bookmuluadb bookmuluadb = new Bookmuluadb(Readbook.this);
			bookmuluadb.open();
			bookmuluadb.insertBook(aid, textid, p + i, pagefactory.m_mbBufBegin);
			bookmuluadb.close();

			startActivity(intent);
			finish();
		} else if (v.getId() == addMark.getId()) {
			dbAdapter.deleteOneMarkaid(rd.getArticleId(), rd.getTextId(), pagefactory.m_mbBufBegin);
			sqmap = dbAdapter.queryAllBookP(aid, 0);
			addMark.setVisibility(View.GONE);
			addMark.startAnimation(animationout3);
			refresh();
		} else if (v.getId() == btnBM.getId()) {
			if (addMark.getVisibility() == View.GONE) {
				addMk();
				sqmap = dbAdapter.queryAllBookP(aid, 0);
				addMark.setVisibility(View.VISIBLE);
				addMark.startAnimation(animationin2);
			} else {
				dbAdapter.deleteOneMarkaid(rd.getArticleId(), rd.getTextId(), pagefactory.m_mbBufBegin);
				sqmap = dbAdapter.queryAllBookP(aid, 0);
				addMark.setVisibility(View.GONE);
				addMark.startAnimation(animationout3);
			}
			refresh();
		}
	}

	private void addMk() {
		BookMark bf = new BookMark();
		bf.setArticleid(rd.getArticleId());
		bf.setTextid(rd.getTextId());
		bf.setTexttitle(rd.getTextTitle());
		bf.setTextjj(pagefactory.getJJ());
		bf.setTime(System.currentTimeMillis());
		bf.setLocation(pagefactory.m_mbBufBegin);
		bf.setLength(pagefactory.m_mbBufLen);
		bf.setIsV(rd.getIsVip());
		dbAdapter.insertBookMark(bf, 1);
	}

	private final class GetTextThread extends Thread {

		public String tid;
		public int code;
		public int isVip;

		// 具体的参数，根据则中那边可能需要修改
		public GetTextThread(String id, int code1, int vip) {
			tid = id;
			code = code1;
			isVip = vip;
		}

		// 避免耦合 ，原有的验证逻辑也是走这个代码
		public void run() {
			NetType netType = NetUtils.checkNet(Readbook.this);
			if (NetType.TYPE_NONE.equals(netType))
				handler.sendEmptyMessage(14);
			/**
			 * 读本章节内容，预读下一章内容 当无权限读时强制读
			 */
			if (isVip == 0) {

				// rd = HttpImpl.downText(tid);
				rd = readAheadLoader.execute(tid, false);
			} else {
				// rd = HttpImpl.downVipText(tid);
				rd = readAheadLoader.execute(tid, true);
			}

			/**
			 * 未登陆用户空指针
			 */
			if (rd != null)
				readBookShareListener.setAId(rd.getArticleId());

			if (rd == null) {

				if (BookApp.getUser() == null || "".equals(BookApp.getUser().getUid())) {
					handler.sendEmptyMessage(111);// 未登陆，提示登陆
					return;
				}
				return;
			} else if (rd.getOrderMsg() != null) {
				if (autoOrder == true) {
					handler.sendEmptyMessage(20);
				} else {
					// 这个也要避免耦合
					handler.sendEmptyMessage(21);
				}
				return;
			} else if (!"1".equals(rd.getCode())) {
				if ("2".equals(rd.getCode()) && isVip == 1) {

					handler.sendEmptyMessage(41);
					return;
				} else {
					handler.sendEmptyMessage(13);
					return;
				}

			}

			if (isVip == 0) {
				HCData.bookMap.put(tid, rd);
			}
			File cacheFile = new File(Constants.READNOVEL_CACHE);

			FileUtils.deleteDir(cacheFile);
			cacheFile.mkdirs();

			file = new File(cacheFile, rd.getArticleId() + "_" + tid + ".txt");
			try {
				if (file.exists()) {
					file.delete();
					file.createNewFile();
				}
				String title = "";
				if (rd.getTextTitle() != null)
					title = rd.getTextTitle() + "\r\n";
				DataOutputStream writer = new DataOutputStream(new FileOutputStream(file));
				writer.write((title + rd.getText()).getBytes("utf-8"));// 写入在线阅读数据
				writer.flush();
				writer.close();
				handler.sendEmptyMessage(code);
				return;
			} catch (Exception e2) {
				LogUtils.error(e2.getMessage(), e2);
				handler.sendEmptyMessage(13);
			}

		};
	};

	// 这里面用按钮进行解耦，区分开来 NIMEIDE

	private void vipView(View v) {
		LinearLayout l7 = (LinearLayout) v.findViewById(R.id.linearLayout7);
		LinearLayout l8 = (LinearLayout) v.findViewById(R.id.linearLayout8);

		View discount_sub_layout = v.findViewById(R.id.discount_sub_layout);//折扣区

		//订阅按钮
		Button bt_dy = (Button) v.findViewById(R.id.bt_dy);//订阅按钮
		Button bt_dyqbzj = (Button) v.findViewById(R.id.bt_dyqbzj);//订阅全部按钮
		Button bt_zqdyqbzj = (Button) v.findViewById(R.id.bt_zqdyqbzj);//折扣订阅全部按钮
		Button bt_dy_5_chapters = (Button) v.findViewById(R.id.bt_dy_5_chapters);//订阅5章按钮

		TextView tv60 = (TextView) v.findViewById(R.id.tv6);
		TextView textView6 = (TextView) v.findViewById(R.id.textView111);
		TextView mscz = (TextView) v.findViewById(R.id.textView2);
		checkBox = (CheckBox) v.findViewById(R.id.checkBox);

		// 判断是否是折扣书
		if (rd.getIs_discount_book() == 1) {
			int i = (int) rd.getTotalprice();
			if (i == 0 || rd.getDiscount() == 0) {

			} else {
				bt_zqdyqbzj.setText("" + rd.getDiscount() + "折订阅全本立省" + i * (10 - rd.getDiscount()) / 10 + "阅读币");
			}

			//折扣区显示
			discount_sub_layout.setVisibility(View.VISIBLE);
			//批量订阅不显示
			bt_dyqbzj.setVisibility(View.GONE);//全部订阅按钮不显示
			bt_dy_5_chapters.setVisibility(View.GONE);
			tv60.setVisibility(View.GONE);
			l8.setVisibility(View.GONE);
			l7.setVisibility(View.GONE);
		} else {
			//折扣区不显示
			discount_sub_layout.setVisibility(View.GONE);
		}

		mscz.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (BookApp.getUser() != null && BookApp.getUser().getUid() != null) {
					goZfb();
				} else {
					Toast.makeText(Readbook.this, "您尚未登录，请先登录！", Toast.LENGTH_SHORT).show();
					Intent i = new Intent(Readbook.this, LoginActivity.class);
					i.putExtra("Tag", "readbook");
					startActivity(i);
				}
			}
		});
		bt_dy.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doOrder();
			}
		});
		// 以后可能要去掉全本订阅模式
		bt_dyqbzj.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				progress = ViewUtils.progressLoading(Readbook.this, "正在获取所有章节信息...");
				new Thread() {
					public void run() {
						allMsg = HttpImpl.subTextMsg(aid);
						if (allMsg != null) {
							handler.sendEmptyMessage(31);
							return;
						}
						handler.sendEmptyMessage(32);
					};
				}.start();
			}
		});

		bt_dy_5_chapters.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//				ViewUtils.toastLong(Readbook.this, "批量订阅5章");
				progress = ViewUtils.progressLoading(Readbook.this, "温馨提示，正在订阅中...");
				new BatchSubTextTask(Readbook.this, aid, textid, rd.getOrderMsg().getCurtime(), 5, handler, progress).execute();
			}
		});

		// 订阅折扣区的书进行的相关操作，不和原有逻辑耦合。NIMEIDE
		bt_zqdyqbzj.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				progress = ViewUtils.progressLoading(Readbook.this, "正在获取所有章节信息...");
				new Thread() {
					public void run() {
						allMsg = HttpImpl.subTextDiscountMsg(aid);
						if (allMsg != null) {
							// 这个也要区分开来，调的是不同的代码接口
							handler.sendEmptyMessage(33);
							return;
						}
						handler.sendEmptyMessage(32);
					};
				}.start();
			}
		});
	}

	private void doOrder() {
		progress = ViewUtils.progressLoading(Readbook.this, "温馨提示，正在订阅中...");
		new Thread() {
			public void run() {
				JSONObject json = HttpImpl.subText(textid, rd.getOrderMsg().getCurtime());
				if (json == null) {
					handler.sendEmptyMessage(22);
					return;
				}

				try {
					SubResultBean subResultBean = JsonUtils.fromJson(json.toString(), SubResultBean.class);

					if (subResultBean == null)
						return;// balk

					if (SubResultBean.SUCCESS.equals(subResultBean.getCode())) {// 订阅成功
						// 强制预读章节重命名
						if (BookApp.getUser() != null)
							CommonUtils.renameTempVip(textid, BookApp.getUser().getUid(), BookApp.getUser().getToken());

						try {//订阅成功后添加记录

							Dao<OrderRecord, Integer> orderRecordDAO = dbHelper.getDao(OrderRecord.class);

							OrderRecord or = new OrderRecord();
							or.setBookId(aid);
							or.setUserId(BookApp.getUser().getUid());
							or.setAddTime(new Date(System.currentTimeMillis()));

							orderRecordDAO.createIfNotExists(or);

						} catch (SQLException e) {
							LogUtils.error(e.getMessage(), e);
						}

						handler.sendEmptyMessage(23);
						return;

					} else if (SubResultBean.FAILS.equals(subResultBean.getCode()) && "remain is not enough for pay".equals(subResultBean.getInfo())) {// 订阅失败,阅读币不足
						handler.sendEmptyMessage(42);
						return;

					} else if (SubResultBean.FAILS.equals(subResultBean.getCode()) && "this textid is paid before!".equals(subResultBean.getInfo())) {// 订阅失败,已订阅
						handler.sendEmptyMessage(43);
						return;
					}

				} catch (Exception e) {
					LogUtils.error(e.getMessage(), e);
				}

				// /其它情况，返回订阅失败
				handler.sendEmptyMessage(22);
			};
		}.start();
	}

	@Override
	protected void onStart() {
		super.onStart();

		//是否屏幕常亮
		if (LocalStore.getKeepScreenOn(this))
			CommonUtils.keepScreenOn(this, true);
		else
			CommonUtils.keepScreenOn(this, false);

	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		sqmap = dbAdapter.queryAllBookP(aid, 0);
		if (aid != null && !"".equals(aid)) {
			AutoOrderTable autoOrderTable = new AutoOrderTable(this);
			autoOrderTable.open();
			autoOrder = autoOrderTable.exist(aid);
			autoOrderTable.close();
		}

		CloseActivity.curActivity = this;
		if (flagFlash == 1) {
			if (!existHC(textid, 12)) {
				mWaitDg = ViewUtils.progressLoading(Readbook.this);
				GetTextThread t = new GetTextThread(textid, 12, isV);
				t.start();
				flagFlash = 0;
			}
		}
		if (aid != null && !"".equals(aid)) {
			exist = dbAdapter.exitBF1(aid, LocalStore.getLastUid(this));
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
	private void saveLastRead() {
		// 记录最后一次阅读的位置
		if (rd != null && rd.getTextId() != null) {
			LastReadTable db = new LastReadTable(this);
			db.open();
			RDBook lastBook = new RDBook();
			lastBook.setTextId(rd.getTextId());
			lastBook.setArticleId(aid);
			lastBook.setBookName(title);
			lastBook.setPosi(pagefactory.m_mbBufBegin);
			lastBook.setIsVip(isV);
			lastBook.setIsOL(1);
			db.insertLastRead(lastBook);
			db.close();
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		if (mCurPageBitmap != null && !mCurPageBitmap.isRecycled()) {
			mCurPageBitmap.recycle();
			mCurPageBitmap = null;
		}

		if (mNextPageBitmap != null && !mNextPageBitmap.isRecycled()) {
			mNextPageBitmap.recycle();
			mNextPageBitmap = null;
		}
		mNextPageCanvas = null;
		mCurPageCanvas = null;
		pagefactory.destry();
		System.gc();

		if (dbAdapter != null)
			dbAdapter.close();
		CloseActivity.remove(this);
		destoryAd();
	}

	private void showDL() {
		new AlertDialog.Builder(Readbook.this).setTitle("温馨提示").setMessage(a).setPositiveButton("登录", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Readbook.this, LoginActivity.class);
				intent.putExtra("Tag", "readbook");
				startActivity(intent);
				dialog.dismiss();
				flagFlash = 1;
			}
		}).setNegativeButton("快速注册", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Readbook.this, RegistActivity.class);
				intent.putExtra("Tag", "readbook");
				startActivity(intent);
				dialog.dismiss();
				flagFlash = 1;
			}
		}).show();
	}

	private void showAddBF() {
		new AlertDialog.Builder(Readbook.this).setTitle("温馨提示").setMessage("请把此书放入书架，\n便于您的下次阅读！")
				.setPositiveButton(Readbook.this.getString(R.string.ensure), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (dbAdapter.exitBF1(aid, LocalStore.getLastUid(Readbook.this))) {
							// Toast.makeText(Readbook.this,"该书已经在书架中",
							// Toast.LENGTH_SHORT).show();
							exist = true;
							return;
						}
						if (!dbAdapter.exitBookBF1(aid)) {
							BFBook bf = new BFBook();
							if (imgUrl != null) {
								bf.setImgFile(Util.saveImgFile(Readbook.this, imgUrl));
							} else {
								bf.setImgFile(imgFile);
							}
							bf.setTitle(title);
							bf.setArticleid(aid);
							bf.setFinishFlag(rd.getFinishflag());
							String r = dbAdapter.insertBook(bf);

							DownFile downFile = new DownFile(Readbook.this, aid, title);
							downFile.start();
							HCData.downingBook.put(aid, downFile);
						}

						String uidd = "-1";
						if (BookApp.getUser() != null && BookApp.getUser().getUid() != null) {
							uidd = BookApp.getUser().getUid();
						}
						if (!dbAdapter.exitBookGx(aid, uidd)) {
							dbAdapter.insertGx(aid, uidd, 0);
						}
					}
				}).setNegativeButton(Readbook.this.getString(R.string.cacel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	private void setView2(View view) {
		Button bt_dyqb = (Button) view.findViewById(R.id.bt_dyqb);
		bt_dyqb.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				double v1 = Double.parseDouble(allMsg.getTotal_price());
				double v2 = Double.parseDouble(allMsg.getRemain());

				if (v2 < v1) {
					handler.sendEmptyMessage(42);
					return;
				}

				//订阅全部
				doSubAll();

			}
		});
	}

	public float getDiscount(float d) {
		if (d < 100)
			return d;
		int m = (int) d / 100;
		return m * 100;
	}

	private void setViewALLDiscount(View view) {
		Button bt_dyqb = (Button) view.findViewById(R.id.bt_dyqb);
		bt_dyqb.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// NIMEIDE
				float v1 = getDiscount(Float.parseFloat(allMsg.getPrice()) * (allMsg.getDiscountCount()) / 10);
				float v2 = Float.parseFloat(allMsg.getRemain());

				if (v2 < v1) {
					handler.sendEmptyMessage(42);
					return;
				}

				//折扣书订阅
				doSubDiscountAll();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 最后一页提示
	 * 
	 * @param str
	 *            提示内容
	 */
	public void showBack(String str) {
		new AlertDialog.Builder(Readbook.this).setTitle("温馨提示").setMessage(str).setPositiveButton("返回书城", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Readbook.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
	}

	/**
	 * 加载广告
	 * 
	 * @param touchPageFlipContext
	 * @return
	 */
	private void loadAd() {
		destoryAd();
		mogo = new MoGoAdvert(this, pagefactory, rl, mPageWidget);
	}

	private void destoryAd() {
		if (mogo != null)
			mogo.destory();
	}

	private void goZfb() {
		CommonUtils.goToConsume(this);
	}

	/**
	 * 订阅全部
	 */
	private void doSubAll() {
		progress = ViewUtils.progressLoading(Readbook.this, "温馨提示，正在订阅中...");
		new Thread() {
			public void run() {
				JSONObject json = HttpImpl.subTextAll(aid, allMsg.getCurtime());
				if (json == null) {
					handler.sendEmptyMessage(22);
					return;
				}
				try {
					Thread.sleep(2000);
					if (!json.isNull("code")) {
						int code = json.getInt("code");
						if (code == 1) {
							handler.sendEmptyMessage(23);
							return;

						}
					}
				} catch (Exception e) {
				}
				handler.sendEmptyMessage(22);
			};
		}.start();
	}

	/**
	 * 折扣书订阅
	 */
	private void doSubDiscountAll() {
		progress = ViewUtils.progressLoading(Readbook.this, "温馨提示，正在订阅中...");
		new Thread() {
			public void run() {
				JSONObject json = HttpImpl.subDiscountTextAll(aid, allMsg.getCurtime());
				if (json == null) {
					handler.sendEmptyMessage(22);
					return;
				}
				try {
					Thread.sleep(2000);
					if (!json.isNull("code")) {
						int code = json.getInt("code");
						if (code == 1) {
							handler.sendEmptyMessage(23);
							return;
						}
					}
				} catch (Exception e) {
				}
				handler.sendEmptyMessage(22);
			};
		}.start();
	}

	/**
	 * 充值
	 */
	public void charge(View view) {
		if (BookApp.getUser() != null && BookApp.getUser().getUid() != null) {
			goZfb();
		}
	}

	/**
	 * 支持作者
	 * @param v
	 */
	public void supportAuthor(View v) {
		new SupportAuthorPageTask(this, aid, 1).execute();
	}

	/**
	 * 显示支持作者
	 */
	private void showSupportAuthor() {
		try {

			if (BookApp.getUser() != null) {
				Dao<SupportAuthorRecord, Integer> supportAuthorDAO = dbHelper.getDao(SupportAuthorRecord.class);

				QueryBuilder<SupportAuthorRecord, Integer> queryBuilder = supportAuthorDAO.queryBuilder();
				Date date = new SimpleDateFormat(SupportAuthorRecord.DATE_FORMAT).parse(DateUtils.now());

				List<SupportAuthorRecord> supportAuthorList = queryBuilder.where().eq(SupportAuthorRecord.ADD_TIME, date).query();

				Dao<OrderRecord, Integer> orderRecordDAO = dbHelper.getDao(OrderRecord.class);
				QueryBuilder<OrderRecord, Integer> orderRecordQueryBuilder = orderRecordDAO.queryBuilder();

				List<OrderRecord> orderRecordList = orderRecordQueryBuilder.where().eq(OrderRecord.ADD_TIME, date).and().eq(OrderRecord.BOOK_ID, aid)
						.and().eq(OrderRecord.USER_ID, BookApp.getUser().getUid()).query();

				//				LogUtils.info("$$$$$$$$$$$$$$$$$$$" + (orderRecordList == null ? 0 : orderRecordList.size()) + "|"
				//						+ (supportAuthorList == null ? 0 : supportAuthorList.size()));

				//决定是否显示红包
				if (orderRecordList != null && !orderRecordList.isEmpty() && orderRecordList.size() > 5) {//今天订阅当前书大于5章节，并且未给过红包显示红包按钮
					if (supportAuthorList == null || supportAuthorList.isEmpty()) {
						supportAuthor.setVisibility(View.VISIBLE);
					}
				}
			}

		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
	}
}