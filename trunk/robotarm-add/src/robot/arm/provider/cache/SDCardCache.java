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
public class SDCardCache extends BaseCache {

	@Override
	public String getRootPath() {
		return StorageUtils.externalMemoryRootPath();
	}

	@Override
	public boolean available() {
		return StorageUtils.externalMemoryAvailable();
	}


	@Override
	public long getTotalExternalMemorySize() {
		return StorageUtils.getTotalExternalMemorySize();
	}

}
