package com.eastedge.readnovel.task;

import java.io.File;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.common.LocalStore;
import com.eastedge.readnovel.common.PageFlipController;
import com.eastedge.readnovel.common.Util;
import com.eastedge.readnovel.db.DBAdapter;
import com.eastedge.readnovel.view.BookPageFactory;
import com.eastedge.readnovel.view.PageWidgetFactory;
import com.readnovel.base.sync.EasyTask;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.PhoneUtils;
import com.readnovel.base.util.ViewUtils;
import com.xs.cn.R;
import com.xs.cn.activitys.ReadbookDown;

/**
 * 本地书阅读初始化异步任务
 * 
 * @author li.li
 *
 */
public class ReadBookInitTask extends EasyTask<ReadbookDown, Void, Void, Void> {
	private ProgressDialog pd;

	public ReadBookInitTask(ReadbookDown caller) {
		super(caller);
	}

	@Override
	public void onPreExecute() {
		//打加载对话框
		pd = ViewUtils.progressLoading(caller);
	}

	@Override
	public void onPostExecute(Void result) {
		//关闭加载对话框
		pd.cancel();
	}

	@Override
	public Void doInBackground(Void... params) {
		try {
			Thread.sleep(500);//睡眠一会，缩短响应时间
		} catch (InterruptedException e) {
			LogUtils.error(e.getMessage(), e);
		}

		int[] wh = PhoneUtils.getScreenPix(caller);
		int sw = wh[0];
		int sh = wh[1];

		caller.setDbAdapter(new DBAdapter(caller));
		caller.getDbAdapter().open();

		// 第一次阅读显示帮助
		HANDLER.post(new Runnable() {

			@Override
			public void run() {

				int flag = LocalStore.getFirstRead(caller);
				if (flag == 0) {
					LocalStore.setFirstRead(caller, 1);
					caller.setHelp((RelativeLayout) caller.findViewById(R.id.read_help));
					caller.getHelp().setVisibility(View.VISIBLE);
					caller.getHelp().setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							caller.getHelp().setVisibility(View.GONE);
						}
					});
				}

			}
		});

		//获取屏幕的宽sw 高sh
		caller.setAnimationin(AnimationUtils.loadAnimation(caller, R.anim.menu_in));
		caller.setAnimationout(AnimationUtils.loadAnimation(caller, R.anim.menu_out));
		caller.setAnimationins(AnimationUtils.loadAnimation(caller, R.anim.menu_ins));
		caller.setAnimationouts(AnimationUtils.loadAnimation(caller, R.anim.menu_outs));
		caller.setAnimationInShare(AnimationUtils.loadAnimation(caller, R.anim.menu_in_share));
		caller.setAnimationOutShare(AnimationUtils.loadAnimation(caller, R.anim.menu_out_share));
		caller.setAnimationin2(new AlphaAnimation(0f, 1.0f));
		caller.getAnimationin2().setDuration(200);
		caller.setAnimationout3(new AlphaAnimation(1.0f, 0));
		caller.getAnimationout3().setDuration(200);

		//接收参数
		caller.setBeg(caller.getIntent().getIntExtra("beg", 0));//开始位置
		caller.setIsV(caller.getIntent().getIntExtra("isVip", 0));//是否是vip
		caller.setFinishFlag(caller.getIntent().getIntExtra("finishFlag", 1));//是否完结
		caller.setAid(caller.getIntent().getStringExtra("aid"));//书本aid
		caller.setBookFile(caller.getIntent().getStringExtra("bookFile"));//书所在路径
		caller.setTxid(caller.getIntent().getStringExtra("textid"));//章节id

		if (caller.getBookFile() == null || "".equals(caller.getBookFile())) {
			if (caller.getAid() != null && !"".equals(caller.getAid())) {
				//书本数据所在路径 
				caller.setBookFile(new File(Constants.READNOVEL_BOOK, "book_text_" + caller.getAid() + ".txt").getPath());
			}
		}

		//初始化控件
		caller.find();
		caller.getReadltseek().setProgress(LocalStore.getMrld(caller)); //设置亮度 为上次退出时候的值
		caller.getReadszseek().setProgress(LocalStore.getFontsize(caller)); //设置字体大小 为上次退出时候的值

		HANDLER.post(new Runnable() {

			@Override
			public void run() {
				int v = LocalStore.getMrld(caller);
				if (v < 20) {
					v = 20;
					LocalStore.setMrld(caller, 20);
				}
				Util.setBrightness(caller, v); //改变亮度
			}
		});

		//点击阴影部分 隐藏底部跟顶部
		caller.getYy().setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				caller.doGone();
			}
		});

		//点击中间区域 显示菜单栏跟头部
		caller.getYy2().setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				caller.doShow();
				caller.getAnimationins().setAnimationListener(new AnimationListener() {
					public void onAnimationStart(Animation animation) {
						if (caller.getAddMark().isShown()) { //动画开始时候 如果与书签 先隐藏
							caller.getAddMark().setVisibility(View.GONE);
							caller.setAdmk(1);
						}
					}

					public void onAnimationRepeat(Animation animation) {
					}

					public void onAnimationEnd(Animation animation) {
						if (caller.getAdmk() == 1) { //动画结束后   如果之前有书签 显示
							caller.getAddMark().setVisibility(View.VISIBLE);
							caller.setAdmk(0);
						}
					}
				});
			}
		});

		//初始化 翻页控件(caller, sw, sh)
		caller.setmPageWidget(PageWidgetFactory.createPageWidget(caller, sw, sh));
		caller.setmCurPageBitmap(Bitmap.createBitmap(sw, sh, Bitmap.Config.ARGB_8888));//当前页Bitmap
		caller.setmNextPageBitmap(Bitmap.createBitmap(sw, sh, Bitmap.Config.ARGB_8888));//下一页Bitmap
		caller.setmCurPageCanvas(new Canvas(caller.getmCurPageBitmap()));//当前页画笔
		caller.setmNextPageCanvas(new Canvas(caller.getmNextPageBitmap()));//下一页画笔
		caller.setPagefactory(new BookPageFactory(caller, sw, sh, 32, caller.getReadjpseek(), caller.getJpTex()));//实例化 绘制文字类
		caller.getPagefactory().setFontSize2(LocalStore.getFontsize(caller)); //设置上次字体大小
		caller.getPagefactory().cgbg(LocalStore.getMrbg(caller)); //设置上次背景
		caller.getmPageWidget().cgbg(LocalStore.getMrbg(caller));

		//初始化滑动条
		caller.setSeekInint();
		//初始化radiogroup
		caller.setRadioInit();

		//判断 上一次是白天 还是黑夜
		if (LocalStore.getMrnt(caller) == 1) {
			caller.getRbtn4().setCompoundDrawablesWithIntrinsicBounds(null, caller.getResources().getDrawable(R.drawable.menu_read42), null, null);
			caller.getRbtn4().setText("白天");
			caller.getPagefactory().cgbg(4);
			caller.getmPageWidget().cgbg(4);
			caller.setNowbgid(LocalStore.getMrbg(caller));
		} else {
			caller.getPagefactory().cgbg(LocalStore.getMrbg(caller));
			caller.getmPageWidget().cgbg(LocalStore.getMrbg(caller));
		}

		//判断书本是否存在， 读取书本目录
		if (caller.getBookFile() != null && !"".equals(caller.getBookFile()) && caller.getAid() != null && !"".equals(caller.getAid())) {
			caller.setFile(new File(caller.getBookFile()));
			caller.setMul(Util.read(caller.getAid()));//读取目录
			caller.getDbAdapter().updateSetBookLT(caller.getAid()); //更新最后次打开时间
			if (caller.getMul() == null) {
				Toast.makeText(caller, "获取章节信息失败", Toast.LENGTH_SHORT).show();
			} else {
				caller.setTxMap(); //解析数据

				HANDLER.post(new Runnable() {

					@Override
					public void run() {
						caller.doinit(); //初始化 显示首页
					}
				});
			}

		}

		//查询书签的集合
		caller.setSqmap(caller.getDbAdapter().queryAllBookP(caller.getAid(), 0));
		if (caller.getCurChapterinfo() != null
				&& caller.getSqmap().containsKey(caller.getCurChapterinfo().getId() + caller.getPagefactory().m_mbBufBegin)) {
			if (caller.getAddMark() != null) {

			}
		} else {
			if (caller.getAddMark() != null) {
				HANDLER.post(new Runnable() {

					@Override
					public void run() {
						caller.getAddMark().setVisibility(View.GONE);
					}
				});
			}

		}

		//翻页控制
		PageFlipController.getInstance().handler(caller);

		return null;
	}
}
