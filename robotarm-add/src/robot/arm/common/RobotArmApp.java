package robot.arm.common;

import android.app.Application;

/**
 * 
 * @author li.li
 * 
 *         Mar 15, 2012
 * 
 */
public abstract class RobotArmApp extends Application {

	private static volatile BaseAppContext baseAppContext;

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	// 初始化程序
	private void init() {
		if (baseAppContext == null) {// double check
			synchronized (RobotArmApp.class) {
				if (baseAppContext == null) {
					baseAppContext = new BaseAppContext(this);
				}
			}
		}
	}

	public static Application getApp() {
		return baseAppContext.getApp();
	}

	public static final class BaseAppContext {
		private Application app;

		private BaseAppContext(Application app) {
			this.app = app;
		}

		public Application getApp() {
			return app;
		}
	}

}
