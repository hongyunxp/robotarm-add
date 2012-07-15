package robot.arm.utils;

import robot.arm.R;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
/**
 * 
 * @author li.li
 *
 * Mar 15, 2012
 *
 */
public class AppExit {
	private static final AppExit instance = new AppExit();

	private AppExit() {

	}

	public static AppExit getInstance() {
		return instance;
	}

	private OnClickListener ensureListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			doExit();
		}
	};

	// double close
	public void exit(Activity curAct) {
		BaseUtils.confirm(curAct, R.string.confirm_exit_msg, ensureListener, null);
	}

	private void doExit() {
		// 保证完全退出，或System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid()); // 获取PID，
	}

}
