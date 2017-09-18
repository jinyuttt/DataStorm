/**
 * 
 */
package dataStrom.bus.config;

/**
 * @author jinyu
 * 代理地址信息
 */
public class BrokerConfig {
public String addressList="127.0.0.1:6666";
public String netType="udp";
public String mqname="MQ";//mq名称（topic名称，rpc服务名称)
public int speed=1000;//每秒
public int timeOut=20;

/**
 * 是否进入队列
 * 进入队列的数据在udp时会实时发送连接探测包
 * 非队列直接发送，在udp时可能会丢
 * 只会连接一次
 */
public boolean isDirect=false;

/**
 * 是否等待连接包返回再次发送
 * 只有 isDirect=false有效
 */
public boolean iswaitAck=false;
}
