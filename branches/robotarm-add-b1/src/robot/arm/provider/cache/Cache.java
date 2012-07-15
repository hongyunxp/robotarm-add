/**
 * 
 */
package robot.arm.provider.cache;


/**
 * @author li.li
 * 
 *         Apr 28, 2012
 * 
 */
public interface Cache {

	String put(String key);

	String get(String key);

	String getRootPath();

	boolean available();

	long getTotalMemorySize();
	
	long getAvailableMemorySize();

}
