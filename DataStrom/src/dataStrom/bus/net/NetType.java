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
 * 网络通信类标识
 * TCP 创建tcp_client, tcp_server
 * client标识发送传输，server表示接收端
 */
public @interface NetType {
   public String name() default "";

public String value() default "";
}
