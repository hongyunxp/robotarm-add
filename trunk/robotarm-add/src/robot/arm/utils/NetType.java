package robot.arm.utils;

public enum NetType {
	WIFI("WIFI", true), //
	GPRS_WEB("GPRS WEB", true), //
	GPRS_WAP("GPRS WAP", true), //
	NONE("无连接", false), //
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
