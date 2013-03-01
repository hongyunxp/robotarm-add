package robot.arm.common;

/**
 * 全局 Application
 */
public class BaseApp extends CommonApp {
	public static final String UPPDATE_URL = "http://192.168.0.104:8080/version";

	@Override
	public void onCreate() {
		super.onCreate();
	}
}
