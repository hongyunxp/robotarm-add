package robot.arm.common;

import android.app.Application;

/**
 * 全局 Application
 */
public class BaseApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("@@@@@@@@@@@######################################################");
	}
}
