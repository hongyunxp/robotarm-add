/**
 * 
 */
package robot.arm.provider.cache;

import android.graphics.Bitmap;

/**
 * @author li.li
 * 
 *         Apr 28, 2012
 * 
 */
public interface Cache {

	void put(String key, Bitmap value);

	Bitmap get(String key);

	String getRootPath();

	boolean available();

	long getTotalExternalMemorySize();

}
