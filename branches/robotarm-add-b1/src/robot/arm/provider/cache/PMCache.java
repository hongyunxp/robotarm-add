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
public class PMCache extends BaseCache {

	@Override
	public String getRootPath() {
		return StorageUtils.internalMemoryRootPath();
	}

	@Override
	public boolean available() {
		return true;
	}
	
	@Override
	public long getTotalMemorySize() {
		return StorageUtils.getTotalInternalMemorySize();
	}

	@Override
	public long getAvailableMemorySize() {
		return StorageUtils.getAvailableInternalMemorySize();
	}

}
