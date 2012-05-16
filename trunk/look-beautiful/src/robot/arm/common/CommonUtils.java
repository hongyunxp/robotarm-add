/**
 * 
 */
package robot.arm.common;

import robot.arm.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

/**
 * @author li.li
 * 
 *         May 16, 2012
 * 
 */
public class CommonUtils {
	public static Builder dialogAbout(final Context context) {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setTitle(context.getString(R.string.menu_about_title));
		tDialog.setMessage(context.getString(R.string.menu_about_content) + context.getString(R.string.menu_about_content2));
		tDialog.setPositiveButton(context.getString(R.string.menu_about_button), null);
		tDialog.show();

		return tDialog;

	}
	
	public static Builder dialogFeedback(final Context context) {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
		tDialog.setTitle(context.getString(R.string.menu_feedback_title));
		tDialog.setMessage(context.getString(R.string.menu_feedback_content));
		tDialog.setPositiveButton(context.getString(R.string.menu_feedback_button), null);
		tDialog.show();

		return tDialog;

	}
}
