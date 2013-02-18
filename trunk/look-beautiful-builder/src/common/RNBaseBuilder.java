package common;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.Project;

/**
 * readnovel自动构建
 * 
 * @author li.li
 *
 * Aug 10, 2012
 */
public class RNBaseBuilder {
	private final String filePath;
	private final String build;

	private String work_location;//android主工程目�?
	private String dist_location;//apk目录目录
	private String manifest;
	private String strings;
	private String versionName;
	private String versionCode;
	private String today;
	private String[] channelList;//19个渠道\
	private Properties props;
	private String appName;

	public RNBaseBuilder(String filePath, String build, String appName) {
		this.filePath = filePath;
		this.build = build;
		this.appName = appName;

		init();
	}

	private void init() {
		props = PropetiesUtils.getPropeties(filePath);
		work_location = props.getProperty("mainproject.abs");
		dist_location = props.getProperty("dist_location");
		String channels = props.getProperty("channels");
		channelList = StringUtils.split(channels, ",");
		manifest = work_location + "\\AndroidManifest.xml";
		strings = work_location + "\\res\\values\\strings.xml";
		versionName = ApkBuildUtils.getVersionName(manifest);
		versionCode = ApkBuildUtils.getVersionCode(manifest);
		today = ApkBuildUtils.getToday();
	}

	public void build() {

		long start = System.currentTimeMillis();
		try {
			boolean result = false;

			for (String channel : channelList) {
				System.out.println("====================================================================");
				System.out.println("构建" + channel);

				//更改渠道
				result = ApkBuildUtils.changeChannel(strings, channel);
				//构建工程
				result = ApkBuildUtils.doBuild(build, Project.MSG_INFO);

				if (!result) {
					System.out.println("构建失败|" + channel);
					break;
				} else {
					//移动apk包到指定目录
					copyApk(channel, work_location, dist_location);
				}
			}

			//打包apk
			zipApks();

		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("构建失败|");
		} finally {
			System.out.println("总共花费时间" + (System.currentTimeMillis() - start));
		}
	}

	/**
	 * 移动apk包到指定目录
	 * @param channel
	 */
	public void copyApk(String channel, String workLocation, String distLocation) {
		//移动apk包到指定目录
		String from = workLocation + "\\bin\\" + appName + "-final.apk";
		String to = distLocation + "\\dist\\" + appName + "_" + channel + "_" + versionName + "_" + versionCode + "_" + today + ".apk";
		ApkBuildUtils.copy(from, to);
		System.out.println("构建" + channel + "成功|" + from + "|" + to);
	}

	/**
	 * 打包apk
	 */
	public void zipApks() {
		//打包apk
		String fromDir = dist_location + "\\dist";
		String toFileName = dist_location + "\\dist\\" + appName + "_" + versionName + "_" + versionCode + "_" + today + ".zip";
		ZipUtils.compressApks(fromDir, toFileName);
	}

	public String getWork_location() {
		return work_location;
	}

	public void setWork_location(String work_location) {
		this.work_location = work_location;
	}

	public String getDist_location() {
		return dist_location;
	}

	public void setDist_location(String dist_location) {
		this.dist_location = dist_location;
	}

	public String getManifest() {
		return manifest;
	}

	public void setManifest(String manifest) {
		this.manifest = manifest;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	public String[] getChannelList() {
		return channelList;
	}

	public void setChannelList(String[] channelList) {
		this.channelList = channelList;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getBuild() {
		return build;
	}

}
