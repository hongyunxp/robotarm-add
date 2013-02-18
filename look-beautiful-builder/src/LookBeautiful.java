import common.RNBaseBuilder;

public class LookBeautiful {
	public static void main(String[] args) {
		String appName = "look-beautiful";
		String filePath = "auto-builder-look-beautiful.properties";
		String build = "build_for_look-beautiful.xml";

		RNBaseBuilder ba = new RNBaseBuilder(filePath, build, appName);
		ba.build();
	}
}
