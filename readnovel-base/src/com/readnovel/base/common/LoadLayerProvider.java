package com.readnovel.base.common;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.readnovel.base.R;

/**
 * 加载层工具
 * @author li.li
 *
 * Nov 19, 2012
 */
public class LoadLayerProvider {
	private static volatile LoadLayerProvider instance = new LoadLayerProvider();
	private View load;

	private LoadLayerProvider() {
	}

	public static LoadLayerProvider getInstance() {
		return instance;
	}

	/**
	 * 打开
	 */
	public void open() {
		if (load == null) {
			//实例化数据加载层
			load = LayoutInflater.from(CommonApp.getInstance()).inflate(R.layout.loading, null);

			//数据加载层布局
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, LayoutParams.FLAG_SHOW_WHEN_LOCKED,
					LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
			params.gravity = Gravity.TOP;

			//加入到视图
			WindowManager vm = (WindowManager) CommonApp.getInstance().getSystemService(Context.WINDOW_SERVICE);
			vm.addView(load, params);
		}

		if (load != null)
			load.setVisibility(View.VISIBLE);
	}

	/**
	 * 关闭
	 */
	public void close() {
		if (load != null) {
			load.postDelayed(new Runnable() {

				@Override
				public void run() {
					load.setVisibility(View.GONE);
				}
			}, 500);
		}

	}

}
