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

	void savePicToSd(Bitmap bm, String imageUrl);

	Bitmap getPicToSd(String imageUrl);

}
