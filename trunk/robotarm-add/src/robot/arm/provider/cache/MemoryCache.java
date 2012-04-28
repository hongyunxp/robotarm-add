/**
 * 
 */
package robot.arm.provider.cache;

import robot.arm.utils.StorageUtils;

/**
 * @author li.li
 * 
 *         Apr 28, 2012
 * 
 */
public class MemoryCache extends BaseCache {

	@Override
	public String getRootPath() {
		return StorageUtils.internalMemoryRootPath();
	}

	@Override
	public boolean available() {
		return true;
	}
	
	@Override
	public long getTotalExternalMemorySize() {
		return StorageUtils.getTotalInternalMemorySize();
	}

}
