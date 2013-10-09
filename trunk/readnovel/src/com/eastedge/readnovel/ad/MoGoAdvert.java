package com.eastedge.readnovel.ad;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.adsmogo.adapters.AdsMogoCustomEventPlatformEnum;
import com.adsmogo.adview.AdsMogoLayout;
import com.adsmogo.controller.listener.AdsMogoListener;
import com.adsmogo.util.AdsMogoUtil;
import com.eastedge.readnovel.utils.CommonUtils;
import com.eastedge.readnovel.view.BookPageFactory;
import com.eastedge.readnovel.view.PageWidget;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.PhoneUtils;
import com.xs.cn.R;

/**
 * 芒果广告
 * @author li.li
 *
 */
public class MoGoAdvert implements AdsMogoListener {

	private ViewGroup adLayout;
	private AdsMogoLayout adsMogoLayoutCode;
	private String adId;
	private int[] wh;
	private int adHeight;
	private BookPageFactory pagefactory;
	private PageWidget mPageWidget;
	private ViewGroup rl;
	private View ad;
	private boolean adIsReady;
	private boolean isShwoAd = true;

	public MoGoAdvert(Activity act, BookPageFactory pagefactory, RelativeLayout r1, PageWidget mPageWidget) {
		this.isShwoAd = isShwoAd && CommonUtils.isShowAd();
		LogUtils.info("加载广告=================================" + isShwoAd);

		if (!isShwoAd)
			return;//不加载广告

		//mogo debug
		//		com.adsmogo.util.L.debug = true;

		this.pagefactory = pagefactory;
		this.rl = r1;
		this.mPageWidget = mPageWidget;
		this.wh = PhoneUtils.getScreenPix(act);//屏幕分辨率
		this.ad = LayoutInflater.from(act).inflate(R.layout.ad, rl, false);
		this.adHeight = (int) act.getResources().getDimension(R.dimen.ad_height);//广告高度
		this.adLayout = (ViewGroup) ad.findViewById(R.id.ad_layout);
		this.adId = act.getString(R.string.ad_id);

		//加入广告位
		LayoutParams lpAd = new LayoutParams(wh[0], adHeight);
		lpAd.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rl.addView(ad, lpAd);

		//创建mogo广告
		adsMogoLayoutCode = new AdsMogoLayout(act, adId, false);
		adsMogoLayoutCode.setAdsMogoListener(this);
		adsMogoLayoutCode.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT));

		//将广告加入到广告位（重要，不加入不请求广告）
		adLayout.removeView(adsMogoLayoutCode);
		adLayout.addView(adsMogoLayoutCode);

		//		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
		//				WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_TOAST, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
		//				PixelFormat.TRANSLUCENT);
		//		lp.gravity = Gravity.BOTTOM;
		//
		//		vm.addView(adsMogoLayoutCode, lp);

	}

	/**
	 * 显示广告
	 * 当广告准备好时显示加载的广告，没有准备好时不显示
	 */
	public void show() {
		if (!isShwoAd)
			return;//不加载广告

		if (adIsReady) {
			adLayout.setVisibility(View.VISIBLE);

			//缩小文本高度
			pagefactory.resetHeight(wh[1] - adHeight);

			//改变阅读页面布局，留出广告位
			//			LayoutParams lpCon = new LayoutParams(wh[0], wh[1] - adHeight);
			//			rl.removeView(mPageWidget);
			//			rl.addView(mPageWidget, lpCon);
			//			mPageWidget.postInvalidate();
		}

	}

	/**
	 * 广告销毁
	 */
	public void destory() {
		// 清除 adsMogoLayout 实例 所产生用于多线程缓冲机制的线程池
		if (adsMogoLayoutCode != null) {
			adsMogoLayoutCode.clearThread();
		}
	}

	@Override
	public void onClickAd(String arg0) {
		LogUtils.debug(AdsMogoUtil.ADMOGO + "-=onClickAd=-");

	}

	@Override
	public boolean onCloseAd() {
		LogUtils.debug(AdsMogoUtil.ADMOGO + "-=onCloseAd=-");
		return true;
	}

	@Override
	public void onCloseMogoDialog() {
		LogUtils.debug(AdsMogoUtil.ADMOGO + "-=onCloseMogoDialog=-");
	}

	@Override
	public void onFailedReceiveAd() {
		LogUtils.debug(AdsMogoUtil.ADMOGO + "-=onFailedReceiveAd=-");

	}

	@Override
	public void onRealClickAd() {
		LogUtils.debug(AdsMogoUtil.ADMOGO + "-=onRealClickAd=-");

	}

	@Override
	public void onReceiveAd(ViewGroup arg0, String arg1) {
		LogUtils.debug(AdsMogoUtil.ADMOGO + "-=onReceiveAd=-");
		//广告已准备好
		adIsReady = true;

	}

	@Override
	public void onRequestAd(String arg0) {
		LogUtils.debug(AdsMogoUtil.ADMOGO + "-=onRequestAd=-");

	}

	@Override
	public Class getCustomEvemtPlatformAdapterClass(AdsMogoCustomEventPlatformEnum arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
