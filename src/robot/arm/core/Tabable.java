/**
 * 
 */
package robot.arm.core;

import java.util.Map;

import android.app.Activity;
import android.view.MenuItem;

/**
 * @author li.li
 * 
 *         Mar 15, 2012
 * 
 */
public interface Tabable {
	
	/**
	 * 创建tabs
	 * @return
	 */
	public abstract Map<Integer, Class<? extends Activity>> newTabs();
	
	/**
	 * 处理option选择事件
	 * @param item
	 * @return
	 */
	public boolean optionsItemSelected(MenuItem item);
}
