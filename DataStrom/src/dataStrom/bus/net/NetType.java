/**
 * 
 */
package dataStrom.bus.net;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
/**
 * @author jinyu
 * ����ͨ�����ʶ
 * TCP ����tcp_client, tcp_server
 * client��ʶ���ʹ��䣬server��ʾ���ն�
 */
public @interface NetType {
   public String name() default "";

public String value() default "";
}
