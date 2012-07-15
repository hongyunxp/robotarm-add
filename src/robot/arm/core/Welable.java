/**
 * 
 */
package robot.arm.core;

import android.app.Activity;

/**
 * @author li.li
 * 
 *         Apr 24, 2012
 * 
 */
public interface Welable {
	/**
	 * 返回欢迎界面clazz
	 * 
	 * @return
	 */
	public abstract Class<? extends Activity> welcomeClazz();
}
