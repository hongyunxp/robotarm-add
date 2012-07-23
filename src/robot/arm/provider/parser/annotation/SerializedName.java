
package robot.arm.provider.parser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于注解JSON对象属性 别名映射
 *
 * @author wanglin(lin3.wang@changhong.com)
 * 2012-5-14下午3:57:06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface SerializedName {

	/**
	 * Represent the json key name
	 * 
	 * @return json key name
	 */
	String value();
}
