package robot.arm.provider.asyc;

import android.util.Log;
/**
 * 
 * @author li.li
 *
 * Mar 15, 2012
 *
 */
public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {
	private static final String TAG = DefaultExceptionHandler.class.getSimpleName();

	@Override
	public void uncaughtException(Thread t, Throwable e) {

		Log.e(TAG, e.getMessage());//子线程的异常

	}

}
