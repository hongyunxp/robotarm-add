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

	String getRootPath();

	boolean available();

	void put(String imageUrl,Bitmap bm);

	Bitmap get(String imageUrl);
	
	long getTotalExternalMemorySize();

}
