package robot.arm.common;

import android.app.Application;

/**
 * 
 * @author li.li
 * 
 *         Mar 15, 2012
 * 
 */
public abstract class CommonApp extends Application {

	private static Application instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public static Application getInstance() {
		return instance;
	}

}
