
package robot.arm.provider.parser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日期类型--属性映射
 *
 * @author wanglin(lin3.wang@changhong.com)
 * 2012-5-14下午4:42:03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface DateFormat {

	String format();
}
