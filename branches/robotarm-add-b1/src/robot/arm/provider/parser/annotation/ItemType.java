package robot.arm.provider.parser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import robot.arm.provider.parser.JSONDeserializer;

/**
 * 聚合对象映射
 * 
 * @author wanglin(lin3.wang@changhong.com) 2012-5-14下午4:30:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ItemType {

	Class<?> value();

	int size() default JSONDeserializer.DEFAULT_ITEM_COLLECTION_SIZE;

	boolean canEmpty() default true;
}
