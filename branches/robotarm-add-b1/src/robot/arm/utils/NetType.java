package robot.arm.utils;

public enum NetType {
	WIFI("WIFI网络", true), //
	GPRS_WEB("GPRS WEB网络", true), //
	GPRS_WAP("GPRS WAP网络", true), //
	NONE("网络不可用", false), //
	;

	public String desc;// 网络连接描述
	public boolean available;

	private NetType(String desc, boolean available) {
		this.desc = desc;
		this.available = available;
	}

	public String getDesc() {
		return desc;
	}

	public boolean isAvailable() {
		return available;
	}
}
