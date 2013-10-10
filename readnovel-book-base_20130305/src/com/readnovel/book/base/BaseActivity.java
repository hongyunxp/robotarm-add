package com.readnovel.book.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.mobclick.android.MobclickAgent;
import com.readnovel.book.base.sp.StyleSaveUtil;
import com.readnovel.book.base.utils.BookInfoUtils;
import com.readnovel.book.base.utils.CloseAll;

public class BaseActivity extends Activity {

	private AlertDialog.Builder builderExit;
	private Dialog dialogExit;
	protected boolean isFree;//是否是免费书
	StyleSaveUtil sst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CloseAll.getInstance().add(this);// 加入关闭管理
		sst = new StyleSaveUtil(this);
		builderExit = new AlertDialog.Builder(this);
		dialogExit = new Dialog(this);
		isFree = BookInfoUtils.isFree(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 关闭程序
	 */
	protected void close() {
		builderExit.setTitle("提示").setMessage("确定要退出吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				sst.setLastThirdTipState(false);
				CloseAll.getInstance().close();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialogExit.cancel();
			}
		});
		dialogExit = builderExit.create();
		dialogExit.show();
	}

	public boolean isFree() {
		return isFree;
	}

}
