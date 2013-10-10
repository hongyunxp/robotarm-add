package com.readnovel.book.base.utils;

import com.readnovel.book.base.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
public class ViewUtils {
	/**
	 * confirm对话框
	 * @param context
	 * @param title
	 * @param msg
	 * @param positiveMsg
	 * @param negativeMsg
	 * @param pl
	 * @param nl
	 */
	public static void confirm(final Context context, String title, String msg, String positiveMsg, String negativeMsg, OnClickListener pl,
			OnClickListener nl) {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setTitle(title);
		tDialog.setMessage(msg);
		tDialog.setPositiveButton(positiveMsg, pl);
		tDialog.setNegativeButton(negativeMsg, nl);
		tDialog.setInverseBackgroundForced(false);
		tDialog.setCancelable(true);
		tDialog.show();
	}

	/**
	 * confirm对话框
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @param positiveMsg
	 * @param negativeMsg
	 * @param pl
	 * @param nl
	 */
	public static void confirm(final Context context, String title, String msg, OnClickListener pl, OnClickListener nl) {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setTitle(title);
		tDialog.setMessage(msg);
		tDialog.setPositiveButton("确定", pl);
		tDialog.setNegativeButton("取消", nl);
		tDialog.setInverseBackgroundForced(false);
		tDialog.setCancelable(true);
		tDialog.show();
	}

	/**
	 * 数据加载
	 * @param act
	 * @return
	 */
	public static ProgressDialog progressLoading(Activity act) {
		ProgressDialog pd = new ProgressDialog(act, R.style.DialogStyle);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("加载中，请稍等.....");
		pd.setIndeterminate(false);
		pd.setInverseBackgroundForced(false);
		pd.setCancelable(true);
		pd.show();

		return pd;
	}

	/**
	 * 对话框
	 * @param context
	 * @param strTitle
	 * @param strText
	 * @param icon
	 * @param pl
	 */
	public static void showDialog(Context context, String msg, int icon, OnClickListener ensureListener) {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setIcon(icon);
		tDialog.setTitle("温馨提示");
		tDialog.setMessage(msg);
		tDialog.setPositiveButton("确定", ensureListener);
		tDialog.show();
	}

	public static void showDialog(Context context, String strTitle, String strText, OnClickListener pl) {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setTitle(strTitle);
		tDialog.setMessage(strText);
		tDialog.setPositiveButton("确定", pl);
		tDialog.show();
	}

}
