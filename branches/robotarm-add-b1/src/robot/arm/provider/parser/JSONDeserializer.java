
package robot.arm.provider.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import robot.arm.provider.parser.annotation.ItemType;
import robot.arm.provider.parser.annotation.SerializedName;
import android.util.Log;

/**
 * JSON解析---对象映射工具类
 *
 * @author wanglin(lin3.wang@changhong.com)
 * 2012-5-14下午3:01:32
 */
public final class JSONDeserializer {

	/**
	 * Logger tag for json deserializer
	 */
	public final static String TAG = "JSONUtils -> JSONDeserializer";

	/**
	 * default collection size , meaning user need whole json array
	 */
	public final static int DEFAULT_ITEM_COLLECTION_SIZE = -100;

	private static JSONDeserializer jsonDeserializer = new JSONDeserializer();

	private JSONDeserializer() {

	}

	public static JSONDeserializer getInstance() {
		return jsonDeserializer;
	}

	
	public <T> T deserialize(final Class<T> objType, final InputStream jsonContentStream) throws JSONException,
			IOException {
		return deserialize(objType, new JSONObject(convertStreamToString(jsonContentStream)));
	}


	public <T> T deserialize(final Class<T> objType, final String jsonContent) throws JSONException {
		return deserialize(objType, new JSONObject(jsonContent));
	}


	public <T> T deserialize(final Class<T> objType, final JSONObject jsonObject) throws JSONException {

		try{

			Stack<Class<?>> stack = new Stack<Class<?>>();
			stack.push(objType);

			T resultObject = objType.newInstance();

			deserialize(resultObject, jsonObject, stack);

			return resultObject;
		} catch(IllegalAccessException e){
			Log.e(TAG, e.getMessage());
		} catch(InstantiationException e){
			Log.e(TAG, e.getMessage());
		}

		return null;
	}


	private void deserialize(Object obj, JSONObject jsonObject, Stack<Class<?>> stack) throws JSONException {

		Iterator<?> iterator = jsonObject.keys();
		Class<?> userClass = stack.peek();

		while(iterator.hasNext()){
			Object jsonKey = iterator.next();

			if(jsonKey instanceof String){ 
				String key = (String) jsonKey;
				Object jsonElement = jsonObject.get(key);

				try{

					Field field = getField(userClass, key);
					String fieldName = field.getName();
					Class<?> classType = field.getType();

					if(jsonElement instanceof JSONObject){
						if(!Converter.isPseudoPrimitive(classType)){

							String setMethodName = getSetMethodName(fieldName, classType);
							Method setMethod = userClass.getDeclaredMethod(setMethodName, classType);

							JSONObject fieldObject = (JSONObject) jsonElement;

							stack.push(classType);
							Object itemObj = classType.newInstance();
							deserialize(itemObj, fieldObject, stack);

							setMethod.invoke(obj, itemObj);
						} else{
							Log.e(TAG, "Expecting composite type for " + fieldName);
						}
					} else if(jsonElement instanceof JSONArray){
						if(Converter.isCollectionType(classType)){
							if(field.isAnnotationPresent(ItemType.class)){
								ItemType itemType = field.getAnnotation(ItemType.class);
								Class<?> itemValueType = itemType.value();
								int size = itemType.size();

								JSONArray fieldArrayObject = (JSONArray) jsonElement;

								if(size == JSONDeserializer.DEFAULT_ITEM_COLLECTION_SIZE
										|| size > fieldArrayObject.length()){
									size = fieldArrayObject.length();
								}

								for(int index = 0; index < size; index++){
									Object value = fieldArrayObject.get(index);
									if(value instanceof JSONObject){
										stack.push(itemValueType);
										Object itemObj = itemValueType.newInstance();
										deserialize(itemObj, (JSONObject) value, stack);

										String addMethodName = getAddMethodName(fieldName);
										Method addMethod = userClass.getDeclaredMethod(addMethodName, itemValueType);
										addMethod.invoke(obj, itemObj);
									}
								}
							}
						} else{
							Log.e(TAG, "Expecting collection type for " + fieldName);
						}
					} else if(Converter.isPseudoPrimitive(classType)){

						Object value = Converter.convertTo(jsonObject, key, classType, field);

						String setMethodName = getSetMethodName(fieldName, classType);
						Method setMethod = userClass.getDeclaredMethod(setMethodName, classType);
						setMethod.invoke(obj, value);
					} else{
						Log.e(TAG, "Unknown datatype");
					}

				} catch(NoSuchFieldException e){
					Log.e(TAG, e.getMessage());
				} catch(NoSuchMethodException e){
					Log.e(TAG, e.getMessage());
				} catch(IllegalAccessException e){
					Log.e(TAG, e.getMessage());
				} catch(InvocationTargetException e){
					Log.e(TAG, e.getMessage());
				} catch(InstantiationException e){
					Log.e(TAG, e.getMessage());
				}
			}
		}
	}

	private Field getField(final Class<?> userClass, final String jsonKey) throws NoSuchFieldException {

		Field targetField = null;
		for(Field field : userClass.getDeclaredFields()){
			if(field.getName().equals(jsonKey)){
				targetField = field;
				break;
			} else if(field.isAnnotationPresent(SerializedName.class)){
				SerializedName serializedNameObj = field.getAnnotation(SerializedName.class);
				if(serializedNameObj.value().equals(jsonKey)){
					targetField = field;
					break;
				}
			}
		}

		if(targetField == null)
			throw new NoSuchFieldException("NoSuchFieldException : " + jsonKey);

		return targetField;
	}

	private String getSetMethodName(final String fieldName, final Class<?> classType) {
		String methodName = null;
		if(Converter.isBoolean(classType) && fieldName.startsWith("is")){
			methodName = "set" + Character.toUpperCase(fieldName.charAt(2)) + fieldName.substring(3);
		} else{
			methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		}
		return methodName;
	}

	private String getAddMethodName(String fieldName) {
		return "add" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}

	private String convertStreamToString(final InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while((line = reader.readLine()) != null){
			sb.append(line + "\n");
		}
		is.close();
		return sb.toString();
	}
}
