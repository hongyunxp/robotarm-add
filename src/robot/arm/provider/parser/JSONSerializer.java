
package robot.arm.provider.parser;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import robot.arm.provider.parser.annotation.ItemType;
import robot.arm.provider.parser.annotation.SerializedName;
import android.util.Log;

/**
 * 将对象映射生成JSONObject 工具类
 * 
 * @author wanglin(lin3.wang@changhong.com)
 *         2012-5-14上午09:11:08
 */
public class JSONSerializer {

	/**
	 * Logger tag for json serializer
	 */
	private final String TAG = "JSONUtils -> JSONSerializer";

	private static JSONSerializer jsonSerializer = new JSONSerializer();

	private JSONSerializer() {

	}

	public static JSONSerializer getInstance() {
		return jsonSerializer;
	}

	public String serializer(final Object objType) throws JSONException {

		Stack<Object> stack = new Stack<Object>();
		stack.push(objType);

		JSONObject jsonObject = new JSONObject();

		serializer(jsonObject, stack);

		return jsonObject.toString();
	}

	private void serializer(JSONObject jsonObject, final Stack<Object> stack) throws JSONException {

		Object userObject = stack.peek();
		Class<?> userClass = userObject.getClass();
		Field[] fields = userClass.getDeclaredFields();

		for(Field field : fields){

			String fieldName = field.getName();
			Class<?> classType = field.getType();
			String jsonKeyName = getKeyName(field);

			try{

				String getMethodName = getGetMethodName(fieldName, classType);
				Method getMethod = userClass.getDeclaredMethod(getMethodName);
				Object returnValue = getMethod.invoke(userObject, new Object[] {});

				if(Converter.isPseudoPrimitive(classType)){
					Converter.storeValue(jsonObject, jsonKeyName, returnValue, field);
				} else if(Converter.isCollectionType(classType)){

					JSONArray jsonArray = new JSONArray();
					boolean canAdd = true;

					if(returnValue instanceof Collection){
						Collection<?> userCollectionObj = (Collection<?>) returnValue;

						if(userCollectionObj.size() != 0){

							Iterator<?> iterator = userCollectionObj.iterator();

							while(iterator.hasNext()){
								Object itemObject = iterator.next();
								JSONObject object = new JSONObject();
								stack.push(itemObject);
								serializer(object, stack);
								jsonArray.put(object);
							}
						} else if(field.isAnnotationPresent(ItemType.class)){
							ItemType itemType = field.getAnnotation(ItemType.class);
							canAdd = itemType.canEmpty();
						}

						if(canAdd)
							jsonObject.put(jsonKeyName, jsonArray);
					} else if(returnValue instanceof Map){
						Map<?, ?> userMapObj = (Map<?, ?>) returnValue;
						JSONObject object = new JSONObject(userMapObj);
						jsonArray.put(object);
						jsonObject.put(jsonKeyName, jsonArray);
					}
				} else{
					stack.push(returnValue);
					JSONObject object = new JSONObject();
					serializer(object, stack);
					jsonObject.put(jsonKeyName, object);
				}

			} catch(NoSuchMethodException e){
				Log.e(TAG, e.getMessage());
			} catch(IllegalAccessException e){
				Log.e(TAG, e.getMessage());
			} catch(InvocationTargetException e){
				Log.e(TAG, e.getMessage());
			}
		}
	}

	private String getKeyName(final Field field) {
		if(field.isAnnotationPresent(SerializedName.class)){
			SerializedName serializedName = field.getAnnotation(SerializedName.class);
			return serializedName.value();
		} else{
			return field.getName();
		}
	}

	private String getGetMethodName(String fieldName, final Class<?> classType) {
		String methodName = "";
		if(Converter.isBoolean(classType)){
			if(fieldName.startsWith("is")){
				methodName = fieldName;
			} else{
				methodName = "is" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
			}
		} else{
			methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		}
		return methodName;
	}
}
