package com.readnovel.book.base;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.readnovel.book.base.ad.Advert;
import com.readnovel.book.base.db.MyDataBase;
import com.readnovel.book.base.entity.Book;
import com.readnovel.book.base.entity.Chapter;
import com.readnovel.book.base.reader.BookPageFactory;
import com.readnovel.book.base.reader.PageWidget1;
import com.readnovel.book.base.sp.StyleSaveUtil;
import com.readnovel.book.base.utils.BookInfoUtils;
import com.readnovel.book.base.utils.BookListProvider;
import com.readnovel.book.base.utils.CommonUtils;
import com.readnovel.book.base.utils.DisplayUtil;
import com.readnovel.book.base.utils.LogUtils;
import com.readnovel.book.base.utils.NetUtils;
import com.readnovel.book.base.utils.NetUtils.NetType;
import com.readnovel.book.lru.BMemCache;

public class PageFlipActivity extends ToolsActivity implements OnTouchListener, OnGestureListener {

	public int intLevel;
	public int intScale;
	// 添加电量
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			/* 如果捕捉到的action是ACTION_BATTERY_CHANGED，
			 * 就执行onBatteryInfoReceiver() */
			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				if (sst != null) {
					sst.setlight(intent.getIntExtra("level", 0));
				}
			}
		}
	};

	/* 拦截到ACTION_BATTERY_CHANGED时要执行的method */
	public void onBatteryInfoReceiver(int intLevel, int intScale) {
		unregisterReceiver(mBatInfoReceiver);
	}

	private LinearLayout left_right_txt;// 阅读背景,网站链接
	private RelativeLayout bookTagLayout;// 书签背景
	private PageWidget1 mPageWidget;
	private Bitmap mCurPageBitmap, mNextPageBitmap; // 当前和下页图片
	public Canvas mCurPageCanvas; // 当前和下页的画布
	public Canvas mNextPageCanvas;
	public BookPageFactory pagefactory; // 执行在画布上显示什么的类
	public StyleSaveUtil util; // 保存若干信息的对象
	private WindowManager.LayoutParams lp; // 管理屏幕亮度相关
	private float lightNum; // 屏幕亮度值
	public int step = 1; // step 步长，每次增减的数量，比如：字体大小
	private PopupWindow lastThirdpopup, popBookTagPic, popBookTagPicBlank;// 弹出浮窗
	private String number, pre_tag, next_tag;// 临时变量
	public String tag;// 所显示的文件名
	private int end;// 临时变量
	private int mstart;// 临时变量
	private String mchapterName;// 章节名
	private int mLastOffset = 0; // 上次阅读到的位置
	private int bookTagLastRead = 0;// 书签记录的阅读位置
	private int sw, sh; //屏幕的sw 宽  sw 高
	private String pageNum;// 书签百分比
	private List<Chapter> mlist;// 存储解析存储章节字数位置的XML集合
	private MyDataBase mydata;// SQlite数据库对象
	private String strPercent;// 百分比字符串
	private String bookTagTag;// 从书签传过来的文件名
	private boolean bookTagShow = false;// 记录书签是否显示
	private int prePageFlag = 0;// 代表向前一章翻页
	private Cursor c;
	public int mChapterNum = 0;// 章节数
	private TextView left_tv;// 左下角文字
	private TextView right_tv;// 右下角文字
	BMemCache bm; // ，章节为粗体
	private int turnPageNum = 0;// 设置翻多少页跳章文字消失
	public int titleFlag = 0;// 大分辨率是的章节文字大小标记
	private String foretext = null;// 开头的文字
	private int deleteReadPosition = 0;// 需要删除书签的点
	private String filePath;
	private View adView;
	private Advert advert;//广告
	private GestureDetector mGestureDetector;
	private AudioManager am;//上下键监听的时候，屏幕按下音量键的出现的声音。
	StyleSaveUtil sst;
	private Button bookbgBtn;
	private Boolean fanyeflag = false;
	private Boolean fanyeBack, fanyeForword;
	private View dialogMengcengForLastThird;
	private static final Handler handler = new Handler();

	@Override
	public void goChapter(View view) {
		super.goChapter(view);
		this.finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(this);
		sst = new StyleSaveUtil(this);
		bm = BMemCache.getInstance();
		//获取屏幕的宽sw 高sh,这个数据实际上并不需要每次进入阅读界面，都要获取。
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		sw = display.getWidth();
		sh = display.getHeight();
		getAndroidSDKVersion();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏显示
		filePath = CommonUtils.getAssetsPath(this);
		util = new StyleSaveUtil(this);
		// 获取TextView
		if (displaysMetrics.widthPixels > 640) {
			titleFlag = 1;
		}
		//注册电量
		registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 从章节进入阅读页
		if (util.writeReadPath() == 0) {
			tag = bundle.getString("filename");
		}
		end = tag.indexOf(".");
		//		mstart = tag.indexOf("_") + 1;
		mstart = tag.indexOf("_", 6) + 1;

		number = tag.substring(mstart, end);
		// 文件名对应的数字
		mChapterNum = Integer.valueOf(number);
		// 章节名
		mchapterName = bundle.getString("chaptername");
		// 从书签进入阅读页记录的所读的断点
		bookTagLastRead = bundle.getInt("lastread");
		// 百分比
		pageNum = bundle.getString("pagenum");
		// 书签存储的所读到的文字
		foretext = bundle.getString("foretext");
		// 初始化
		init();
	}

	@Override
	protected void onStart() {
		super.onStart();

		// 获取屏幕当前的亮度
		if (util.writeLight() != 0) {
			lp = getWindow().getAttributes();
			lp.screenBrightness = util.writeLight();
			getWindow().setAttributes(lp);
		}
		// 章节跳转
		stepChapter(mLastOffset);
	}

	private void init() {
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		turnPageNum = 0;
		mydata = new MyDataBase(this);
		pagefactory = new BookPageFactory(this, wide, height);
		pagefactory.setBg(sst.getreadbg());
		end = tag.indexOf(".");
		number = tag.substring(mstart, end);
		left_right_txt = (LinearLayout) this.findViewById(R.id.txt_left_right);// 文本布局		
		mCurPageBitmap = Bitmap.createBitmap(wide, height, Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap.createBitmap(wide, height, Bitmap.Config.ARGB_8888);
		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		left_tv = (TextView) this.findViewById(R.id.left_tv);
		right_tv = (TextView) this.findViewById(R.id.right_tv);
		mPageWidget = new PageWidget1(this, wide, height);
		mPageWidget.setOnTouchListener(this);
		dialogMengcengForLastThird = this.findViewById(R.id.dialogmengceng);
		// 从章节进入
		if (util.writeReadPath() == 0) {
			// 没读过从开头开始显示
			if (util.writeChapterName() == null) {
				mLastOffset = 0;
			} else {
				// 如果存储的信息与此时的章节明相同证明此章读过读出存储的信息
				if (util.writeChapterName().equals(tag)) {
					mLastOffset = util.writeLastOffSet();
					pagefactory.setPageNum(util.writeReadPage());
				}
			}
			pagefactory.setFontSize(util.getFontsize() == 0 ? DisplayUtil.getFontMiddle(this) : util.getFontsize());
			showChapterFirstNum();
		}

		left_right_txt.addView(mPageWidget, wide, height);

		int chapterId = mChapterNum;

		if (isFree) {//免费

		} else {//收费
				//是否跳转到其它付费逻辑页面
			if (CommonUtils.goToPay(this, chapterId))
				return;
			//得到当前章节信息
//			Chapter curChapter = BookInfoUtils.getByChapterId(this, mChapterNum);
		}
			if (prePageFlag == 0)
				showChapterFirstNum();
			pagefactory.openBook(filePath + "/" + tag, mLastOffset);
			pagefactory.setFontSize(util.getFontsize() == 0 ? DisplayUtil.getFontMiddle(this) : util.getFontsize());
			pagefactory.setChapterName(mchapterName);
			//渲染当前页内容到画板
			pagefactory.onDraw(mCurPageCanvas);
			//渲染下一页内容到画板
			pagefactory.onDraw(mNextPageCanvas);
			//将当前和下一页内容设置到翻页动画实例中
			mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
	}

	@Override
	protected void onStop() {
		super.onStop();
		closeCursor();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		saveLastRead();
		sst.setLastThirdTipState(false);
		//取消电量注册
		onBatteryInfoReceiver(intLevel, intScale);

		if (mCurPageBitmap != null) {
			mCurPageBitmap.recycle();
		}
		if (mNextPageBitmap != null) {
			mNextPageBitmap.recycle();
		}
		mPageWidget = null;
		mCurPageCanvas = null;
		mNextPageCanvas = null;
		closeCursor();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (popMenu == null || !popMenu.isShowing()) {
				show();
			} else {
				hidePopMenu();
				hide();
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (popMenu != null && popMenu.isShowing()) {
				hide();
				hidePopMenu();
				return true;
			} else {
				saveHistoryInfo();
				util.saveFirstStep(1);
				util.saveFirstReadChapter(culNum(tag));
				saveLastRead();
				close();
				//				finish();
			}
		}
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			saveLastRead();
			// 不是第一次阅读则不比较跳章
			util.saveFirstReadChapter(culNum(tag));
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			// 解决音量键在按下的时候出现声音的问题。用户在上下键翻页的时候每次都出现系统提示音会让用户烦躁。
			if (popMenu != null && popMenu.isShowing()) {
				hide();
				hidePopMenu();
			}
			am.setRingerMode(0);
			//绘制当前页
			pagefactory.onDraw(mNextPageCanvas);
			try {
				//向前翻
				pagefactory.prePage();
			} catch (IOException e1) {
				LogUtils.error(e1.getMessage(), e1);
			}
			// 如果是本章第一页则不进行翻页
			if (pagefactory.isfirstPage()) {
				turnPageNum++;
				//				if (turnPageNum == 11) {
				//					left_tv.setVisibility(View.GONE);
				//					right_tv.setVisibility(View.GONE);
				//				}
				prePageFlag = 1;
				//判断是第一章节
				String firstchapter = BookInfoUtils.getChapterFirst(this);
				if (tag.equals(firstchapter)) {
					Toast.makeText(PageFlipActivity.this, "当前为第一页", Toast.LENGTH_SHORT).show();
					return false;
				} else {
					culpreNum();
					if (util.getFontsize() == 0)
						showChapterFirstNum();
					else
						showChapterFirstNum();
					//章节跳转
					stepChapter(turnLastPage(tag));
				}
			}
			pagefactory.onDraw(mCurPageCanvas);
			mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
			mPageWidget.abortAnimation();
			mPageWidget.doVoiceEvent(false);
			if (getAndroidSDKVersion() < 8) {
				pagefactory.onDraw(mCurPageCanvas);
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			if (popMenu != null && popMenu.isShowing()) {
				hide();
				hidePopMenu();
			}
			am.setRingerMode(0);
			pagefactory.onDraw(mCurPageCanvas);
			try {
				//向前翻
				pagefactory.nextPage();
			} catch (IOException e1) {
				LogUtils.error(e1.getMessage(), e1);
			}
			// 如果是最后一页则不进行翻页
			if (pagefactory.islastPage()) {
				// 判断读到整个文章最后的时候
				turnPageNum++;
				//				if (turnPageNum == 10) {
				//					left_tv.setVisibility(View.GONE);
				//					right_tv.setVisibility(View.GONE);
				//				}
				// 判断此章是最后一张的最后一页进行页面的跳转
				String endchapter = BookInfoUtils.getChapterEnd(this);
				if (tag.equals(endchapter)) {
					//					Intent intent = new Intent(PageFlipActivity.this, MoreActivity.class);
					//					startActivity(intent);
					return false;
				} else {
					culnextNum();
					mLastOffset = 0;
					//章节跳转
					stepChapter(mLastOffset);
				}
			}
			//绘制下一页
			pagefactory.onDraw(mNextPageCanvas);
			//将当前和下一页内容设置到翻页动画实例中
			mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
			mPageWidget.abortAnimation();
			mPageWidget.doVoiceEvent(true);
			if (getAndroidSDKVersion() < 8) {
				pagefactory.onDraw(mCurPageCanvas);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 每次翻页时的一些基本操作 ,前翻
	public boolean fanYeFlipingOpreationForBack() {
		try {
			pagefactory.onDraw(mNextPageCanvas);
			turnPageNum++;
			if (popMenu != null && popMenu.isShowing()) {
				hide();
				hidePopMenu();
			}
			pagefactory.prePage();
			prePageFlag = 0;
			// 判断是否显示书签
		} catch (IOException e11) {
			LogUtils.error(e11.getMessage(), e11);
		}
		// 如果是本章第一页则不进行翻页
		if (pagefactory.isfirstPage()) {
			turnPageNum++;
			prePageFlag = 1;
			//判断是第一章节
			String firstchapter = BookInfoUtils.getChapterFirst(this);
			if (tag.equals(firstchapter)) {
				Toast.makeText(PageFlipActivity.this, "当前为第一页", Toast.LENGTH_SHORT).show();
				return false;
			} else {
				culpreNum();
				if (util.getFontsize() == 0)
					showChapterFirstNum();
				else
					showChapterFirstNum();
				stepChapter(turnLastPage(tag));
			}
		} else {
			pagefactory.onDraw(mCurPageCanvas);
		}
		mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
		return true;
	}

	// 翻页基本操作之后翻
	public boolean fanYeFlipingOpreationForForword() {
		try {

			pagefactory.onDraw(mCurPageCanvas);
			// 如果翻页则隐藏
			turnPageNum++;
			if (popMenu != null && popMenu.isShowing()) {
				hide();
				hidePopMenu();
			}
			pagefactory.nextPage();
			prePageFlag = 0;
		} catch (IOException e11) {
			LogUtils.error(e11.getMessage(), e11);
		}
		// 如果是最后一页则不进行翻页
		if (pagefactory.islastPage()) {
			// 判断读到整个文章最后的时候
			turnPageNum++;
			// 判断此章是最后一章的最后一页进行页面的跳转
			String endchapter = BookInfoUtils.getChapterEnd(this);
			if (tag.equals(endchapter)) {
				//				Intent intent = new Intent(PageFlipActivity.this, MoreActivity.class);
				//				startActivity(intent);
				return false;
			} else {
				culnextNum();
				mLastOffset = 0;
				//章节跳转
				stepChapter(mLastOffset);
			}
		} else {
			//在画板上渲染下一页内容
			pagefactory.onDraw(mNextPageCanvas);
		}
		//将当前和下一页内容设置到翻页动画实例中
		mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
		return true;
	}

	// 字体/亮读调节
	private void resizeLight(View view) {
		LayoutInflater inflater = LayoutInflater.from(PageFlipActivity.this);
		View lightPop = inflater.inflate(R.layout.lightpop, null);
		final Button smallBtn = (Button) lightPop.findViewById(R.id.smallbtn);
		final Button bigBtn = (Button) lightPop.findViewById(R.id.bigbtn);
		if (util.getFontsize() == 0) {
		}
		OnClickListener btnSizeClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				if (util.getFontsize() == 0) {
					util.setFontsize(DisplayUtil.getFontMiddle(PageFlipActivity.this));
				}
				if (id == R.id.smallbtn) {
					util.setFontsize(util.getFontsize() - 2);
					pagefactory.setFontSize(util.getFontsize());
					showChapterFirstNum();
						pagefactory.openBook(filePath + "/" + tag, mLastOffset);
					// 调节字号之后需要重新对Canvas进行画图
					// 画出当前页和下一页
					pagefactory.onDraw(mCurPageCanvas);
					pagefactory.onDraw(mNextPageCanvas);
					mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					// 更新画布
					mPageWidget.reflash();
				} else if (id == R.id.bigbtn) {
					util.setFontsize(util.getFontsize() + 2);
					pagefactory.setFontSize(util.getFontsize());
					showChapterFirstNum();
						pagefactory.openBook(filePath + "/" + tag, mLastOffset);
					// 调节字号之后需要重新对Canvas进行画图
					// 画出当前页和下一页
					pagefactory.onDraw(mCurPageCanvas);
					pagefactory.onDraw(mNextPageCanvas);
					mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					// 更新画布
					mPageWidget.reflash();
				}
			}
		};
		smallBtn.setOnClickListener(btnSizeClickListener);
		bigBtn.setOnClickListener(btnSizeClickListener);
		final SeekBar seekbar = (SeekBar) lightPop.findViewById(R.id.screen_light_seekbar);
		seekbar.setMax(255);
		if (util.writeLight() == 0) {
			seekbar.setProgress(getScreenBrightness(PageFlipActivity.this));
		} else {
			seekbar.setProgress(util.writeSeekbar());
		}
		ImageView dark = (ImageView) lightPop.findViewById(R.id.imageView_light_down);
		ImageView light = (ImageView) lightPop.findViewById(R.id.imageView_light_up);
		dark.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lp = getWindow().getAttributes();
				if (seekbar.getProgress() >= 22 && seekbar.getProgress() <= 255) {
					lightNum = Float.valueOf(seekbar.getProgress() - 20) * (1f / 255f);
					seekbar.setProgress(seekbar.getProgress() - 20);
					util.saveLight(lightNum);
					util.saveSeekbar(seekbar.getProgress() - 20);
					lp.screenBrightness = lightNum;
					getWindow().setAttributes(lp);
				}
			}
		});
		light.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lp = getWindow().getAttributes();

				if (seekbar.getProgress() >= 2 && seekbar.getProgress() <= 235) {
					lightNum = Float.valueOf(seekbar.getProgress() + 20) * (1f / 255f);
					seekbar.setProgress(seekbar.getProgress() + 20);
					util.saveLight(lightNum);
					util.saveSeekbar(seekbar.getProgress() + 20);
					lp.screenBrightness = lightNum;
					getWindow().setAttributes(lp);
				}
			}
		});

		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// 通过改变拖动条来调节屏幕的亮度
				lp = getWindow().getAttributes();
				if (seekbar.getProgress() >= 2 && seekbar.getProgress() <= 255) {
					lightNum = Float.valueOf(seekbar.getProgress()) * (1f / 255f);
					util.saveLight(lightNum);
					util.saveSeekbar(seekbar.getProgress());
					lp.screenBrightness = lightNum;
					getWindow().setAttributes(lp);
				}
			}
		});
		popSize = new PopupWindow(lightPop, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popSize.setAnimationStyle(android.R.style.Animation_Toast);
		popSize.showAtLocation(findViewById(R.id.bg_ll), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
		// 刷新popupwindows
		popSize.update();
		pagefactory.onDraw(mCurPageCanvas);
	}

	// 處理背景設置  + 网页跳转
	@Override
	public void resumeBackground(View view) {
		super.resumeBackground(view);
		//		LayoutInflater inflater = LayoutInflater.from(PageFlipActivity.this);
		//		View gPop = inflater.inflate(R.layout.backgroundgallery, null);
		//		final Gallery gallery = (Gallery)gPop.findViewById(R.id.bg_gallery);
		//		gallery.setAdapter(new ImageGalleryAdapter(PageFlipActivity.this));
		//		gallery.setOnItemClickListener(new OnItemClickListener() {
		//			@Override
		//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		//		    // 更换背景颜色
		//				pagefactory.setBg(arg2+2);
		//				mPageWidget.cgbg(arg2+2);
		//				refresh();
		//			}
		//		});
		//		popSize = new PopupWindow(gPop, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		//		popSize.setAnimationStyle(android.R.style.Animation_Toast);
		//		popSize.showAtLocation(findViewById(R.id.bg_ll), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
		//		// 刷新popupwindows
		//		popSize.update();
		//		Intent intent = new Intent(ToolsActivity.this, MoreforWebViewActivity.class);
		//		startActivity(intent);
	}

	// 得到屏幕的当前亮度
	public static int getScreenBrightness(Activity activity) {
		int nowBrightnessValue = 0;
		ContentResolver resolver = activity.getContentResolver();
		try {
			nowBrightnessValue = android.provider.Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return nowBrightnessValue;
	}

	// 显示书签pop
	public void bookTag() {
	}

	// 获得当前的时间
	public String getCurrentTime() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		String time = year + "-" + month + "-" + day + " " + hour + ":" + minute;
		return time;
	}

	// 读取XML 显示每章开头的页码  
	public void showChapterFirstNum() {
		Book book = BookListProvider.getInstance(this).getBook();
		mlist = book.getChapters();
		//		mlist = ParserBySAX.parseXMLChapterNum(input);
		int totalfontnum = BookInfoUtils.getTotalnum(PageFlipActivity.this);
		for (int i = 0; i < mlist.size(); i++) {
			if (mlist.get(i).getFileName().equals(tag)) {
				float fPercent = (float) (Float.valueOf(mlist.get(i).getNum()) * 1.0 / totalfontnum);
				DecimalFormat df = new DecimalFormat("#0.0");
				strPercent = df.format(fPercent * 100) + "%";// 计算页码值
				System.out.println("页码：" + mlist.get(i).getNum() + "|" + totalfontnum);
				pagefactory.setPageNum(strPercent);// 显示页码
				LogUtils.info("strPercent=" + strPercent);
				break;
			}
		}
		// 跳章的时候改变此时的章节名
		for (int i = 0; i < mlist.size(); i++) {
			if (mlist.get(i).getFileName().equals(tag)) {
				mchapterName = mlist.get(i).getTitle();
			}
		}
	}

	// 翻到上一章最后的位置
	public int turnLastPage(String tag) {
		try {
			pagefactory.openBook(filePath + "/" + tag);
		} catch (IOException e1) {
			LogUtils.error(e1.getMessage(), e1);
		}
		while (true) {
			try {
				pagefactory.nextPage();
			} catch (IOException e) {
				LogUtils.error(e.getMessage(), e);
			}
			if (pagefactory.islastPage()) {
				return pagefactory.getBufBegin();
			}
		}
	}

	// 计算上一章的章节号
	public void culpreNum() {
		end = tag.indexOf(".");
		number = tag.substring(mstart, end);
		mChapterNum = Integer.valueOf(number);
		if (mChapterNum > 1) {
			mChapterNum = Integer.valueOf(number) - 1;
		}
		number = mChapterNum + "";
		pre_tag = tag.substring(0, mstart) + mChapterNum + ".txt";
		tag = pre_tag;
		util.savescollery(util.writescollery() - 1);
	}

	// 计算下一章的章节号
	public void culnextNum() {
		end = tag.indexOf(".");
		number = tag.substring(mstart, end);
		mChapterNum = Integer.valueOf(number) + 1;
		number = mChapterNum + "";
		next_tag = tag.substring(0, mstart) + mChapterNum + ".txt";
		tag = next_tag;
		util.savescollery(util.writescollery() + 1);
	}

	/**
	 * 章节跳转
	 * @param flag
	 * @param mLastOffset
	 */
	public void stepChapter(int mLastOffset) {
		int chapterId = mChapterNum;
		//是否跳转到其它付费逻辑页面
		if (CommonUtils.goToPay(this, chapterId))
			return;
		//得到当前章节信息
//		Chapter curChapter = BookInfoUtils.getByChapterId(this, mChapterNum);
			if (prePageFlag == 0)
				showChapterFirstNum();
			pagefactory.openBook(filePath + "/" + tag, mLastOffset);
			pagefactory.setFontSize(util.getFontsize() == 0 ? DisplayUtil.getFontMiddle(this) : util.getFontsize());
			pagefactory.setChapterName(mchapterName);

			if (mLastOffset != 0) {
				//渲染当前页内容到画板
				pagefactory.onDraw(mCurPageCanvas);
			} else {
				//渲染下一页内容到画板
				pagefactory.onDraw(mNextPageCanvas);
			}
			//将当前和下一页内容设置到翻页动画实例中
			mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
	}

	public void flush() {
		for (int i = 0; i < 400; i++) {
			mPageWidget.reflash();
		}
	}

	public void saveLastRead() {
		mLastOffset = pagefactory.getBufBegin();// 记录读到的位置
		util.saveLastOffSet(mLastOffset);// 存储读到的位置
		util.saveChapterName(tag);// 存储读到的章节名
		util.saveReadPage(pagefactory.getPageNum());// 读到的百分比
	}

	// 插入书签
	public void insertData() {
		SQLiteDatabase db = mydata.getWritableDatabase();
		ContentValues values = new ContentValues();
		if (util.getFontsize() == 0) {
			values.put(MyDataBase.FontSize, DisplayUtil.getFontMiddle(this));
			strPercent = pagefactory.getPageNum();
		} else {
			values.put(MyDataBase.FontSize, util.getFontsize());
			strPercent = pagefactory.getPageNum();
		}
		values.put(MyDataBase.BookTitle, mchapterName);
		values.put(MyDataBase.ForeText, pagefactory.getForeText());
		values.put(MyDataBase.Time, getCurrentTime());
		values.put(MyDataBase.PerCent, strPercent);
		values.put(MyDataBase.LastOffset, mLastOffset);
		values.put(MyDataBase.FileName, tag);
		values.put(MyDataBase.PAGENUM, pagefactory.getPageNum());
		db.insert(MyDataBase.TABLE_NAME_BOOKTAG, MyDataBase.ID, values);
		closeCursor();
	}

	// 查出从书签进来的点
	public void queryData1() {
		closeCursor();
		bookTagShow = false;
		String select;
		select = "(" + MyDataBase.FileName + "=" + "'" + tag + "'" + "and" + MyDataBase.ForeText + "=" + "'" + foretext + "'" + ")";
		SQLiteDatabase db = mydata.getReadableDatabase();
		String[] columns = new String[] { MyDataBase.BookTitle, MyDataBase.ForeText, MyDataBase.Time, MyDataBase.PerCent, MyDataBase.FontSize,
				MyDataBase.LastOffset, MyDataBase.FileName, MyDataBase.PAGENUM };
		try {
			c = db.query(MyDataBase.TABLE_NAME_BOOKTAG, columns, select, null, null, null, null);
			while (c.moveToNext()) {
				mLastOffset = c.getInt(c.getColumnIndex(MyDataBase.LastOffset));
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		closeCursor();
	}

	// 查出此章节所有书签记录的断点
	public boolean queryData2() {
		closeCursor();
		bookTagShow = false;
		String select;
		select = "(" + MyDataBase.FileName + "=" + "'" + tag + "'" + ")";
		SQLiteDatabase db = mydata.getReadableDatabase();
		String[] columns = new String[] { MyDataBase.BookTitle, MyDataBase.ForeText, MyDataBase.Time, MyDataBase.PerCent, MyDataBase.FontSize,
				MyDataBase.LastOffset, MyDataBase.FileName, MyDataBase.PAGENUM };
		try {
			c = db.query(MyDataBase.TABLE_NAME_BOOKTAG, columns, select, null, null, null, null);
			while (c.moveToNext()) {
				int mLastOffset = c.getInt(c.getColumnIndex(MyDataBase.LastOffset));
				if (mLastOffset + 2 >= pagefactory.getBufBegin() && mLastOffset < pagefactory.getBufEnd()) {
					deleteReadPosition = mLastOffset;
					bookTagShow = true;
				}
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		} finally {
			db.close();
		}
		return bookTagShow;
	}

	public void deleteBookTag() {
		SQLiteDatabase db = mydata.getWritableDatabase();
		String select = "(" + MyDataBase.FileName + "=" + "'" + tag + "'" + " and " + MyDataBase.LastOffset + "=" + deleteReadPosition + " )";
		try {
			db.delete(MyDataBase.TABLE_NAME_BOOKTAG, select, null);
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		closeCursor();
	}

	private void closeCursor() {
		try {
			if (c != null && c.isClosed() != true) {
				c.close();
				c = null;
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
	}

	// 记录跳章之前的历史信息
	private void saveHistoryInfo() {
		if (util.getFontsize() == 0) {
			util.saveFirstReadChapterFontSize(DisplayUtil.getFontMiddle(this));
			strPercent = pagefactory.getPageNum();
		} else {
			util.saveFirstReadChapterFontSize(util.getFontsize());
			strPercent = pagefactory.getPageNum();
		}
		util.saveFirstReadChapterName(mchapterName);
		util.saveFirstReadChapterTime(getCurrentTime());
		util.saveFirstReadChapterPer(strPercent);
		util.saveFirstReadChapterLast(mLastOffset);
		util.saveFirstReadChapterFileName(tag);
		util.saveFirstReadChapterPageNum(pagefactory.getPageNum());
	}

	// 计算章节的int型，即数字形式
	private int culNum(String name) {
		int end = name.indexOf(".");
		String number = name.substring(mstart, end);
		int a = Integer.valueOf(number);
		return a;
	}

	View reshowdayornight;

	// 显示底部弹出菜单
	private void show() {

		if (popSize != null) {
			popSize.dismiss();
		}
		LayoutInflater inflater = LayoutInflater.from(PageFlipActivity.this);
		View view = inflater.inflate(R.layout.tabs, null);
		View resize = view.findViewById(R.id.tab_resize_font);
		// 显示字大小工具
		resize.setVisibility(View.VISIBLE);
		View reshowmodel = view.findViewById(R.id.tab_ll3);
		reshowmodel.setVisibility(View.VISIBLE);
		reshowdayornight = view.findViewById(R.id.tab_btn3);
		if (sst.getreadbg() == 0) {
			reshowdayornight.setBackgroundResource(R.drawable.day);
		} else {
			reshowdayornight.setBackgroundResource(R.drawable.day_1);
		}
		popMenu = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popMenu.setAnimationStyle(android.R.style.Animation_InputMethod);
		popMenu.showAtLocation(findViewById(R.id.bg_ll), Gravity.NO_GRAVITY, 0, height - view.getHeight());
		popMenu.update();// 显示

	}

	/**
	 * 点击改变字大小
	 * 
	 * @param view
	 */
	@Override
	public void goResizeFont(View view) {
		boolean notNeedShow = popSize != null && popSize.isShowing();
		hidePopMenu();// 隐藏
		if (!notNeedShow) {
			resizeLight(view);
			pagefactory.onDraw(mCurPageCanvas);
		}
	}

	public void refresh() {
		pagefactory.onDraw(mCurPageCanvas);
		pagefactory.onDraw(mNextPageCanvas);
		mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
		mPageWidget.postInvalidate();
	}

	/**
	 * 点击书签,借用显示书签的按钮，来显示需要白天黑夜模式
	 */
	@Override
	public void goBookTag(View view) {
		if (sst.getreadbg() == 0) {
			sst.setreadbg(1);// 设置成黑夜模式
			reshowdayornight.setBackgroundResource(R.drawable.day_1);
			pagefactory.setBg(1);
			mPageWidget.cgbg(1);
			refresh();
		} else {
			sst.setreadbg(0);// 设置成白天模式
			reshowdayornight.setBackgroundResource(R.drawable.day);
			pagefactory.setBg(0);
			mPageWidget.cgbg(0);
			refresh();
		}
	}

	private void hide() {
		if (popMenu != null) {
			popMenu.dismiss();// 隐藏
		}
		if (popSize != null) {
			popSize.dismiss();
		}
		popMenu = null;
	}

	// 判断系统版本
	private static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			LogUtils.error(e.getMessage(), e);
		}
		return version;
	}

	// PageWidget的触摸事件处理
	@Override
	public boolean onTouch(View v, MotionEvent e) {
		boolean ret = false;
		if (pagefactory.getBufBegin() != 0) {
			mLastOffset = pagefactory.getBufBegin();
		}
		// 最 关 键 的 一 笔
		if (e.getAction() == MotionEvent.ACTION_UP) {
			fanyeflag = false;
			mPageWidget.doTouchUp();
			fanyeBack = false;
			fanyeForword = false;
		}
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			fanyeBack = false;
			fanyeForword = false;
		}

		return mGestureDetector.onTouchEvent(e);
	}

	@Override
	protected int getContentView() {
		return R.layout.read;
	}

	// 加载广告
	private void load_Ad() {
		NetType netType = NetUtils.checkNet(this);
		if (!NetType.TYPE_NONE.equals(netType) && advert.getAd()) {
			advert.show();
			//重置pagefactory留出广告位
			int adHeight = (int) getResources().getDimension(R.dimen.ad_height);//广告高度
			int conHeight = height - adHeight;
			if (pagefactory.getmHeight() != conHeight) {
				pagefactory.setmHeight(conHeight);//重新设置高度
				pagefactory.resetHeight();//初始化
			}
		}
	}

	//卸载广告
	private void unL_oadAd() {
		//重置pagefactory取消广告位
		int conHeight = height;
		if (pagefactory.getmHeight() != conHeight) {
			pagefactory.setmHeight(conHeight);//重新设置高度
			//            mPageWidget 
			pagefactory.init();//初始化
		}
		//隐藏广告
		ViewGroup adLayout = (ViewGroup) findViewById(R.id.ad_layout);
		adLayout.setVisibility(View.GONE);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		mPageWidget.abortAnimation();
		// 传入触摸点的坐标
		float xdistance = e2.getX() - e1.getX();
		// 向右滑动 
		if (xdistance > 0 && fanyeForword == false) {
			try {
				// 主要是考虑到 onscroll是连续不断的调用，但又必须在这个过程中绘制界面内容
				// 因此需要在每次翻页的第一次onscoll 方法进行相应的配置
				if (fanyeflag == false) {
					fanyeBack = true;
					pagefactory.onDraw(mNextPageCanvas);
					turnPageNum++;
					if (popMenu != null && popMenu.isShowing()) {
						hide();
						hidePopMenu();
					}
					fanyeflag = true;
					pagefactory.prePage();
					// 如果是本章第一页则不进行翻页
					if (pagefactory.isfirstPage()) {
						turnPageNum++;
						prePageFlag = 1;
						//判断是第一章节
						String firstchapter = BookInfoUtils.getChapterFirst(this);

						if (tag.equals(firstchapter)) {
							Toast.makeText(PageFlipActivity.this, "已经是第一页", Toast.LENGTH_SHORT).show();
							return false;
						} else {
							culpreNum();
							showChapterFirstNum();
							//章节跳转
							stepChapter(turnLastPage(tag));
						}
						// 判断是否显示书签
					} else {
						//渲染下一页内容到画板
						pagefactory.onDraw(mCurPageCanvas);
					}
					//将当前和下一页内容设置到翻页动画实例中
					mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
				}
				prePageFlag = 0;
				// 判断是否显示书签
			} catch (IOException e11) {
				LogUtils.error(e11.getMessage(), e11);
			}
			//执行翻页动画
			mPageWidget.doTouchEvent(e1, e2);
		} else if (xdistance < 0 && fanyeBack == false) {
			// 后翻
			try {
				if (fanyeflag == false) {
					// 如果是最后一页则不进行翻页
					if (pagefactory.islastPage()) {
						pagefactory.onDraw(mCurPageCanvas);
						// 如果翻页则隐藏
						turnPageNum++;
						fanyeForword = true;
						if (popMenu != null && popMenu.isShowing()) {
							hide();
							hidePopMenu();
						}
						fanyeflag = true;
						// 判断此章是最后一章的最后一页进行页面的跳转
						String endchapter = BookInfoUtils.getChapterEnd(this);
						if (tag.equals(endchapter)) {
							Intent intent = new Intent(PageFlipActivity.this, MoreforWebViewActivity.class);
							startActivity(intent);
							return false;
						} else {
							culnextNum();
							mLastOffset = 0;
							//章节跳转
							stepChapter(mLastOffset);
						}
						//将当前和下一页内容设置到翻页动画实例中
						mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					} else {
						pagefactory.onDraw(mCurPageCanvas);
						// 如果翻页则隐藏
						turnPageNum++;
						fanyeForword = true;
						if (popMenu != null && popMenu.isShowing()) {
							hide();
							hidePopMenu();
						}
						fanyeflag = true;
						pagefactory.nextPage();
						//在画板上渲染下一页内容
						pagefactory.onDraw(mNextPageCanvas);
						//将当前和下一页内容设置到翻页动画实例中
						mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					}
				}
				prePageFlag = 0;
			} catch (IOException e11) {
				LogUtils.error(e11.getMessage(), e11);
			}
			mPageWidget.doTouchEvent(e1, e2);
		}
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// 点击中间区域
		if (e.getRawX() >= wide / 3 && e.getRawX() <= 2 * wide / 3 && e.getRawY() >= height / 3 && e.getRawY() <= 2 * height / 3) {
			if (popMenu == null || !popMenu.isShowing()) {
				if (popSize == null || !popSize.isShowing()) {
					show();
				} else {
					popSize.dismiss();
				}
			} else {
				hide();
				hidePopMenu();
			}
		}
		if (e.getX() > 2 * sw / 3) {
			fanYeFlipingOpreationForForword();
			mPageWidget.startAnimation2(true);
		} else if (e.getX() < sw / 3) {
			fanYeFlipingOpreationForBack();
			mPageWidget.startAnimation2(false);
		}
		if (e.getX() < sw / 2 && e.getX() >= sw / 3) {
			if (e.getY() < sh / 3 || e.getY() > 2 * sh / 3) {
				fanYeFlipingOpreationForBack();
				mPageWidget.startAnimation2(false);
			}
		}
		// 避免点击区域重合
		if (e.getX() > sw / 2 && e.getX() < 2 * sw / 3) {
			if (e.getY() < sh / 3 || e.getY() > 2 * sh / 3) {
				fanYeFlipingOpreationForForword();
				mPageWidget.startAnimation2(true);
			}
		}
		return true;
	}

	public int getwhat(int i) {
		return i;
	}

}