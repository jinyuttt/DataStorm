/**
 * 
 */
package dataStrom.bus.mq;

/**
 * @author jinyu
 *所有通讯指令
 *创建指令只有生产者有
 */
public enum MQCmd {
 MQCreate,
 TopicCreate,
  RPCCreate,//就是RPC发布，产生Consumer
 MQMsg,//生产者数据
 TopicMsg,//生产者数据
 Query,//网页查询
 Heart,//RPC心跳
 MQSub,
 TopicSub,
 ResponseSub,//订阅信息的返回，表示已经订阅成功
 RPCMonitor,//
 RequestRPC,//请求RPC
 ResponseRPC,//RPC返回
 MQCopy,//集群复制
 MQCopyResponse,//集群复制回复
 MQAck;//探测包
}
