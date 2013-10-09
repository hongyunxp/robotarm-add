package com.readnovel.base.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.readnovel.base.R;

public class ViewUtils {

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
		tDialog.setPositiveButton(context.getString(R.string.ensure), pl);
		tDialog.setNegativeButton(context.getString(R.string.cacel), nl);
		tDialog.setInverseBackgroundForced(false);
		tDialog.setCancelable(true);
		tDialog.show();
	}

	/**
	 * confirm对话框
	 * 
	 */
	public static void confirm(final Context context, String msg, String ensure, OnClickListener pl) {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setTitle("温馨提示");
		tDialog.setMessage(msg);
		tDialog.setPositiveButton(ensure, pl);
		tDialog.setNegativeButton(context.getString(R.string.cacel), null);
		tDialog.setInverseBackgroundForced(false);
		tDialog.setCancelable(true);
		tDialog.show();
	}

	/**
	 * confirm对话框
	 * 
	 */
	public static void confirm(final Context context, String msg, String ensure, String cacel, OnClickListener pl, OnClickListener nl) {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setTitle("温馨提示");
		tDialog.setMessage(msg);
		tDialog.setPositiveButton(ensure, pl);
		tDialog.setNegativeButton(cacel, nl);
		tDialog.setInverseBackgroundForced(false);
		tDialog.setCancelable(true);
		tDialog.show();
	}

	public static void confirmOnUi(final Activity act, final String msg, final String ensure, final String cacel, final OnClickListener pl,
			final OnClickListener nl) {

		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				confirm(act, msg, ensure, cacel, pl, nl);
			}
		});
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
	public static void confirm2(final Context context, String title, String msg, OnClickListener pl, OnClickListener nl) {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setTitle(title);
		tDialog.setMessage(msg);
		tDialog.setPositiveButton(context.getString(R.string.ensure), pl);
		tDialog.setNegativeButton(context.getString(R.string.cacel), nl);
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

		return progressLoading(act, act.getString(R.string.common_loading_msg));
	}

	/**
	 * 数据加载
	 * @param act
	 * @return
	 */
	public static ProgressDialog progressLoading(Activity act, String msg) {

		return progressLoading(act, msg, true);
	}

	/**
	 * 数据加载
	 * @param act
	 * @return
	 */
	public static ProgressDialog progressLoading(Activity act, String msg, final boolean canCacel) {
		final ProgressDialog pd = new ProgressDialog(act, R.style.DialogStyle);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage(msg);
		pd.setIndeterminate(false);
		pd.setInverseBackgroundForced(false);
		pd.setCancelable(false);
		pd.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialoginterface, int i, KeyEvent keyevent) {
				if (i == KeyEvent.KEYCODE_BACK) {

					//取消加载对话框
					if (canCacel)
						pd.dismiss();

					return true;
				}
				return false;
			}

		});
		pd.show();

		return pd;
	}

	/**
	 * toast提示
	 */
	public static void toastShart(Activity act, String msg) {
		toastDialog(act, msg, Toast.LENGTH_SHORT);
	}

	/**
	 * toast提示
	 */
	public static void toastLong(Activity act, String msg) {
		toastDialog(act, msg, Toast.LENGTH_LONG);
	}

	/**
	 * toast提示
	 */
	public static void toastDialog(Activity act, String msg, int duration) {
		Toast.makeText(act, msg, duration).show();
	}

	/**
	 * toast提示
	 */
	public static void toastOnUI(final Activity act, final String msg, final int duration) {
		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(act, msg, duration).show();
			}
		});
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
		tDialog.setPositiveButton(context.getString(R.string.ensure), ensureListener);
		tDialog.show();
	}

	public static void showDialog(Context context, String strTitle, String strText, OnClickListener pl) {
		if (context == null)
			return;
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setTitle(strTitle);
		tDialog.setMessage(strText);
		tDialog.setPositiveButton("确定", pl);
		tDialog.show();
	}

	public static void showDialogOnUi(final Activity act, final String strTitle, final String strText, final OnClickListener pl) {

		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showDialog(act, strTitle, strText, pl);
			}
		});
	}

	public static Dialog custom(Context ctx, int layoutResId) {
		Dialog dialog = new Dialog(ctx, R.style.Theme_FullHeightDialog);
		dialog.setContentView(layoutResId);

		return dialog;
	}

	/**
	 * 自定义确认对话框
	 * @param ctx
	 * @param layoutResId
	 * @param title
	 * @param msg
	 * @param pl
	 * @param nl
	 * @return
	 */
	public static Dialog customDialogCfm(Context ctx, int layoutResId, String title, String msg, View.OnClickListener pl, View.OnClickListener nl) {

		final Dialog dialog = custom(ctx, layoutResId);

		View ensure = dialog.findViewById(R.id.dialog_confirm_ensure);
		View cacel = dialog.findViewById(R.id.dialog_confirm_cacel);

		if (ensure != null)
			ensure.setOnClickListener(pl);

		if (cacel != null)
			cacel.setOnClickListener(nl);

		View titleV = dialog.findViewById(R.id.dialog_confirm_titel);
		View msgV = dialog.findViewById(R.id.dialog_confirm_msg);

		if (title != null) {
			TextView titleTV = (TextView) titleV;
			titleTV.setText(title);
		}

		if (msg != null) {
			TextView msgTV = (TextView) msgV;
			msgTV.setText(msg);
		}

		if (ensure != null && pl == null)
			cacel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dialog.dismiss();

				}
			});

		if (cacel != null && nl == null)
			cacel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dialog.dismiss();

				}
			});

		dialog.show();

		return dialog;
	}

	/**
	 * 自宝义提示对话框
	 * @param ctx
	 * @param layoutResId
	 * @param title
	 * @param msg
	 * @param pl
	 * @return
	 */
	public static Dialog customDialogInfo(Activity act, int layoutResId, String title, String msg, final View.OnClickListener pl) {

		final Dialog dialog = custom(act, layoutResId);

		View ensure = dialog.findViewById(R.id.dialog_confirm_ensure);

		if (ensure != null)
			ensure.setOnClickListener(pl);

		View titleV = dialog.findViewById(R.id.dialog_confirm_titel);
		View msgV = dialog.findViewById(R.id.dialog_confirm_msg);

		if (title != null) {
			TextView titleTV = (TextView) titleV;
			titleTV.setText(title);
		}

		if (msg != null) {
			TextView msgTV = (TextView) msgV;
			msgTV.setText(msg);
		}

		if (ensure != null && pl == null)
			ensure.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dialog.dismiss();

				}
			});
		dialog.show();

		return dialog;
	}

	public static void customDialogInfoOnUI(final Activity act, final int layoutResId, final String title, final String msg,
			final View.OnClickListener pl) {

		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Dialog dialog = customDialogInfo(act, layoutResId, title, msg, pl);
				dialog.show();

			}
		});

	}

	public static void customDialogCfmOnUI(final Activity act, final int layoutResId, final String title, final String msg,
			final View.OnClickListener pl, final View.OnClickListener nl) {

		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Dialog dialog = customDialogCfm(act, layoutResId, title, msg, pl, nl);
				dialog.show();

			}
		});

	}
}
