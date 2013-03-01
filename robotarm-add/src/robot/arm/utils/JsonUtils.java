package robot.arm.utils;

import com.google.gson.Gson;

/**
 * JSON转换工具
 * @author li.li
 *
 */
public class JsonUtils {

	/**
	 * JSON转对象
	 * @param json JSON字符串
	 * @param clazz 对象类型
	 * @return 对象
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		Gson gson = new Gson();
		T obj = gson.fromJson(json, clazz);

		return obj;
	}

	/**
	 * 对象对JSON
	 * @param obj 对象
	 * @return json字符串
	 */
	public static <T> String toJson(T obj) {
		Gson gson = new Gson();
		String json = gson.toJson(obj);

		return json;
	}

}
