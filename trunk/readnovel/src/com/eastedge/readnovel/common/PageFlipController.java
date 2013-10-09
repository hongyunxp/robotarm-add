package com.eastedge.readnovel.common;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.eastedge.readnovel.ad.MoGoAdvert;
import com.eastedge.readnovel.beans.Chapterinfo;
import com.eastedge.readnovel.beans.Shubenmulu;
import com.eastedge.readnovel.beans.orm.OrderRecord;
import com.eastedge.readnovel.beans.orm.SupportAuthorRecord;
import com.eastedge.readnovel.db.orm.OrmDBHelper;
import com.eastedge.readnovel.utils.CommonUtils;
import com.eastedge.readnovel.view.BookPageFactory;
import com.eastedge.readnovel.view.PageWidget;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.readnovel.base.db.orm.DBHelper;
import com.readnovel.base.util.DateUtils;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.BookApp;
import com.xs.cn.activitys.LoginActivity;
import com.xs.cn.activitys.Readbook;
import com.xs.cn.activitys.ReadbookDown;
import com.xs.cn.activitys.RegistActivity;

/**
 * 翻页控制器
 * @author li.li
 *
 */
public class PageFlipController {
	private static PageFlipController instance = new PageFlipController();
	private MoGoAdvert mogo;

	private PageFlipController() {
	}

	public static PageFlipController getInstance() {
		return instance;
	}

	/**
	 * 翻页控制
	 * @param touchPageFlipContext 翻页上下文
	 * @param touchPageFlipListener 翻页监听器
	 */
	public void handler(final ReadbookDown touchPageFlipContext) {

		final PageWidget mPageWidget = touchPageFlipContext.getmPageWidget();
		final BookPageFactory pagefactory = touchPageFlipContext.getPagefactory();
		final Canvas mCurPageCanvas = touchPageFlipContext.getmCurPageCanvas();
		final Bitmap mCurPageBitmap = touchPageFlipContext.getmCurPageBitmap();
		final Bitmap mNextPageBitmap = touchPageFlipContext.getmNextPageBitmap();
		final Canvas mNextPageCanvas = touchPageFlipContext.getmNextPageCanvas();
		final Shubenmulu mul = touchPageFlipContext.getMul();
		final Activity act = touchPageFlipContext;

		//翻页事件
		mPageWidget.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent e) {
				boolean ret = false;
				if (v == mPageWidget) {
					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						//计算拖拽点对应的拖拽脚
						mPageWidget.calcCornerXY(e.getX(), e.getY());

						//绘制当前页
						pagefactory.onDraw(mCurPageCanvas);

						//执行翻页(上一页或下一页)
						if (mPageWidget.isDragToRight()) { //向右翻，上一页

							boolean isFirst = doPrePage(touchPageFlipContext);
							if (isFirst)
								return ret;

						} else { //向左翻，下一页
							boolean isEnd = doNextPage(touchPageFlipContext);
							if (isEnd)
								return ret;

						}

						//绘制下一页
						pagefactory.onDraw(mNextPageCanvas);

						//设置翻页(上一页或下一页)
						mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);

						//记录最后一次阅读的位置
						touchPageFlipContext.saveLastRead();

						//vip章节前10章提示登陆
						Chapterinfo curChapterinfo = touchPageFlipContext.getCurChapterinfo();
						if (mul != null && curChapterinfo != null) {
							if (mul.getFcvip() != 0) {
								if (curChapterinfo.getDisplayorder() != 0 && curChapterinfo.getDisplayorder() + 10 >= mul.getFcvip()) {
									if (BookApp.getUser() == null || "".equals(BookApp.getUser().getUid())) {
										showDL(act);
									}
								}
							}
						}

					}

					mPageWidget.abortAnimation();
					ret = mPageWidget.doTouchEvent(e);

					return ret;
				}
				return false;
			}
		});
	}

	/**
	 * 加载广告
	 * @param touchPageFlipContext
	 * @return
	 */
	public void loadAd(ReadbookDown touchPageFlipContext) {
		//销毁当前广告平
		destoryAd();

		mogo = new MoGoAdvert(touchPageFlipContext, touchPageFlipContext.getPagefactory(), touchPageFlipContext.getRl(),
				touchPageFlipContext.getmPageWidget());

	}

	/**
	 * 卸载广告
	 * @param touchPageFlipContext
	 * @return
	 */
	public void destoryAd() {
		//销毁当前广告平
		if (mogo != null)
			mogo.destory();
	}

	/**
	 * 显示登录的提示框
	 */
	private void showDL(final Activity act) {
		new AlertDialog.Builder(act).setTitle("温馨提示").setMessage("以下为收费章节,必须登录才能阅读！").setPositiveButton("登录", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//跳转到登录页面
				Intent intent = new Intent(act, LoginActivity.class);
				intent.putExtra("Tag", "readbook");
				act.startActivity(intent);
				dialog.dismiss();
			}
		}).setNegativeButton("快速注册", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//跳转到注册页面
				Intent intent = new Intent(act, RegistActivity.class);
				intent.putExtra("Tag", "readbook");
				act.startActivity(intent);
				dialog.dismiss();
			}
		}).show();
	}

	public void initPage(ReadbookDown touchPageFlipContext) throws IOException {
		//加载广告
		loadAd(touchPageFlipContext);

		BookPageFactory pagefactory = touchPageFlipContext.getPagefactory();
		Chapterinfo curChapterinfo = touchPageFlipContext.getCurChapterinfo();
		File file = touchPageFlipContext.getFile();
		Canvas mNextPageCanvas = touchPageFlipContext.getmNextPageCanvas();
		Canvas mCurPageCanvas = touchPageFlipContext.getmCurPageCanvas();

		pagefactory.openbook(file, touchPageFlipContext.getBeg(), curChapterinfo.getPosi(), curChapterinfo.getLen());
		pagefactory.setTitle(curChapterinfo.getSubhead());
		pagefactory.onDraw(mCurPageCanvas);
		pagefactory.onDraw(mNextPageCanvas);

		//设置分享内容-当前章节名
		touchPageFlipContext.getReadBookShareListener().setChapterName(curChapterinfo.getSubhead());

		//显示红包
		showSupportAuthor(touchPageFlipContext);

	}

	private boolean doNextPage(ReadbookDown touchPageFlipContext) {
		if (mogo != null)
			mogo.show();

		BookPageFactory pagefactory = touchPageFlipContext.getPagefactory();
		Canvas mNextPageCanvas = touchPageFlipContext.getmNextPageCanvas();
		//		Chapterinfo curChapterinfo = touchPageFlipContext.getCurChapterinfo();
		HashMap<String, Chapterinfo> txMap = touchPageFlipContext.getTxMap();
		ImageView addMark = touchPageFlipContext.getAddMark();
		AlphaAnimation animationout3 = touchPageFlipContext.getAnimationout3();
		Animation animationin2 = touchPageFlipContext.getAnimationin2();
		//		HashMap<String, Long> sqmap = touchPageFlipContext.getSqmap();
		File file = touchPageFlipContext.getFile();
		Activity act = touchPageFlipContext;
		String aid = touchPageFlipContext.getAid();

		int finishFlag = touchPageFlipContext.getFinishFlag();

		try {
			pagefactory.nextPage();
			// 获取当前章节 设置章节头
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (pagefactory.islastPage()) { //判断是否为最后一页
			Chapterinfo curChapterinfo = touchPageFlipContext.getCurChapterinfo();
			if (curChapterinfo != null && curChapterinfo.getNextvip() == 1) { //判断下一章是不是vip
				if (BookApp.getUser() == null || "".equals(BookApp.getUser().getUid())) { //判断当前有没登陆用户
					showDL(act); //提示登陆
				} else {
					//跳转到在线阅读
					Intent intent = new Intent(act, Readbook.class);
					intent.putExtra("aid", aid);
					intent.putExtra("textid", curChapterinfo.getNextid());
					intent.putExtra("isVip", curChapterinfo.getNextvip());
					act.startActivity(intent);
					act.finish();
				}
				return true;
			}
			if (curChapterinfo != null && curChapterinfo.getNextid() != null) { //判断有没有下一章

				if (txMap.containsKey(curChapterinfo.getNextid())) { //判断有没下一章的信息
					Chapterinfo cinfo = txMap.get(curChapterinfo.getNextid());
					if (cinfo != null) { //判断是否为空	
						//						curChapterinfo = cinfo; //替换当前章节
						touchPageFlipContext.setCurChapterinfo(cinfo);//替换当前章节
						pagefactory.setTitle(cinfo.getSubhead()); //设置章节名
						try {
							pagefactory.openbook(file, 0, cinfo.getPosi(), cinfo.getLen()); //打开章节
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						//最后一页处理
						CommonUtils.doReadLastPage(act, finishFlag, aid, curChapterinfo.getSubhead());
						return true;
					}
				}
			} else {
				//最后一页处理
				CommonUtils.doReadLastPage(act, finishFlag, touchPageFlipContext.getMul().getTitle(), curChapterinfo.getSubhead());
				return true;
			}
		}
		hasBookMark(touchPageFlipContext.getCurChapterinfo(), pagefactory, addMark, animationin2, animationout3, touchPageFlipContext.getSqmap());

		//设置分享内容-当前章节名
		touchPageFlipContext.getReadBookShareListener().setChapterName(touchPageFlipContext.getCurChapterinfo().getSubhead());

		//显示红包
		showSupportAuthor(touchPageFlipContext);

		return false;
	}

	public boolean doPrePage(ReadbookDown touchPageFlipContext) {//向右翻
		if (mogo != null)
			mogo.show();

		BookPageFactory pagefactory = touchPageFlipContext.getPagefactory();
		Canvas mNextPageCanvas = touchPageFlipContext.getmNextPageCanvas();
		//		Chapterinfo curChapterinfo = touchPageFlipContext.getCurChapterinfo();
		HashMap<String, Chapterinfo> txMap = touchPageFlipContext.getTxMap();
		ImageView addMark = touchPageFlipContext.getAddMark();
		AlphaAnimation animationout3 = touchPageFlipContext.getAnimationout3();
		Animation animationin2 = touchPageFlipContext.getAnimationin2();
		//		HashMap<String, Long> sqmap = touchPageFlipContext.getSqmap();
		File file = touchPageFlipContext.getFile();

		try {
			//向前翻
			pagefactory.prePage();
		} catch (IOException e1) {
			LogUtils.error(e1.getMessage(), e1);
		}
		if (pagefactory.isfirstPage()) { //判断是否为第一页
			Chapterinfo curChapterinfo = touchPageFlipContext.getCurChapterinfo();
			if (curChapterinfo != null && curChapterinfo.getPreid() != null) { //判断当前章节是否有前一章
				if (txMap.containsKey(curChapterinfo.getPreid())) { //判断有没上一章的信息
					Chapterinfo cinfo = txMap.get(curChapterinfo.getPreid()); //获取上一章
					if (cinfo != null) { //判断是否为空
						//						curChapterinfo = cinfo; //替换当前章节
						touchPageFlipContext.setCurChapterinfo(cinfo);//替换当前章节
						pagefactory.setTitle(cinfo.getSubhead()); //设置章节头
						try {
							pagefactory.openbook(file, 0, cinfo.getPosi(), cinfo.getLen()); //打开章节
							pagefactory.last(); //显示最后一页
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			} else { //第一页 不做操作
				return true;
			}
		}
		hasBookMark(touchPageFlipContext.getCurChapterinfo(), pagefactory, addMark, animationin2, animationout3, touchPageFlipContext.getSqmap()); //判断有没有书签

		//显示红包
		showSupportAuthor(touchPageFlipContext);

		return false;

	}

	/**
	 * 判断是否有书签 
	 */
	private void hasBookMark(Chapterinfo curChapterinfo, BookPageFactory pagefactory, ImageView addMark, Animation animationin2,
			AlphaAnimation animationout3, HashMap<String, Long> sqmap) {
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
	 * 翻页监听器接口
	 */
	public interface TouchPageFlipListener {
		boolean nextPage();

		boolean prePage();
	}

	/**
	 * 显示支持作者
	 */
	private void showSupportAuthor(ReadbookDown touchPageFlipContext) {
		try {

			if (BookApp.getUser() != null) {
				OrmDBHelper dbHelper = DBHelper.getHelper(OrmDBHelper.class);

				Dao<SupportAuthorRecord, Integer> supportAuthorDAO = dbHelper.getDao(SupportAuthorRecord.class);

				QueryBuilder<SupportAuthorRecord, Integer> queryBuilder = supportAuthorDAO.queryBuilder();
				Date date = new SimpleDateFormat(SupportAuthorRecord.DATE_FORMAT).parse(DateUtils.now());

				List<SupportAuthorRecord> supportAuthorList = queryBuilder.where().eq(SupportAuthorRecord.ADD_TIME, date).query();

				Dao<OrderRecord, Integer> orderRecordDAO = dbHelper.getDao(OrderRecord.class);
				QueryBuilder<OrderRecord, Integer> orderRecordQueryBuilder = orderRecordDAO.queryBuilder();

				List<OrderRecord> orderRecordList = orderRecordQueryBuilder.where().eq(OrderRecord.ADD_TIME, date).and()
						.eq(OrderRecord.BOOK_ID, touchPageFlipContext.getAid()).and().eq(OrderRecord.USER_ID, BookApp.getUser().getUid()).query();

				//				LogUtils.info("$$$$$$$$$$$$$$$$$$$" + (orderRecordList == null ? 0 : orderRecordList.size()) + "|"
				//						+ (supportAuthorList == null ? 0 : supportAuthorList.size()) + "||" + date + "|" + touchPageFlipContext.getAid() + "|"
				//						+ BookApp.getUser().getUid());

				//决定是否显示红包
				if (orderRecordList != null && !orderRecordList.isEmpty() && orderRecordList.size() > 5) {//今天订阅当前书大于5章节，并且未给过红包显示红包按钮
					if (supportAuthorList == null || supportAuthorList.isEmpty()) {
						ImageView supportAuthor = (ImageView) touchPageFlipContext.findViewById(R.id.support_author);//红包
						supportAuthor.setVisibility(View.VISIBLE);
					}
				}
			}

		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
	}

}
