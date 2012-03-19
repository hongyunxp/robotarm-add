package robot.arm.utils;

import robot.arm.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
/**
 * 
 * @author li.li
 *
 * Mar 15, 2012
 *
 */
public class BaseUtils {
	private static volatile DisplayMetrics display;

	public static void confirm(final Context context, int message, OnClickListener pl, OnClickListener nl) {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setTitle(R.string.confirm_title);
		tDialog.setMessage(message);
		tDialog.setPositiveButton(R.string.Ensure, pl);
		tDialog.setNegativeButton(R.string.Cancel, nl);
		tDialog.show();
	}

	public static DisplayMetrics getScreen(Activity act) {
		if (display == null) {
			synchronized (BaseUtils.class) {
				if (display == null) {
					DisplayMetrics metrics = new DisplayMetrics();
					act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
					display = metrics;
				}
			}
		}

		return display;
	}

	public static int setListViewHeight(ListView lv) {
		ViewGroup.LayoutParams lp = lv.getLayoutParams();
		lp.height = getListViewHeight(lv);
		lv.setLayoutParams(lp);

		return lp.height;
	}

	public static void setViewGroupHeight(ViewGroup vg, int height) {
		ViewGroup.LayoutParams lp = vg.getLayoutParams();
		lp.height = height;
		vg.setLayoutParams(lp);
	}

	public static int getListViewHeight(ListView lv) {
		ListAdapter la = lv.getAdapter();
		if (null == la)
			return 0;
		int h = 0;
		int cnt = la.getCount();
		View item = null;
		for (int i = 0; i < cnt; i++) {
			item = la.getView(i, null, lv);
			item.measure(0, 0);
			h += item.getMeasuredHeight();
		}

		return h + (lv.getDividerHeight() * (cnt - 1));
	}

}