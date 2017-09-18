/**
 * 
 */
package dataStrom.bus.config;

/**
 * @author jinyu
 *
 */
public class ServerConfig {
public String address="127.0.0.1";
public int port=1111;
public String netType="";
public String keepalive="UDP";//udp类型，不会连接
public String name="";
public String rpcType="0";//0是负载均衡
public int waitHeart=3;//心跳频率(秒)
}
