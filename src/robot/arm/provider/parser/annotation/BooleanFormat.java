
package robot.arm.provider.parser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 布尔类型--属性映射
 *
 * @author wanglin(lin3.wang@changhong.com)
 * 2012-5-14下午4:32:03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface BooleanFormat {

	/**
	 * Represent the true format
	 * 
	 * @return true format
	 */
	String trueFormat();

	/**
	 * Represent the false format
	 * 
	 * @return false format , default false
	 */
	String falseFormat() default "false";
}
