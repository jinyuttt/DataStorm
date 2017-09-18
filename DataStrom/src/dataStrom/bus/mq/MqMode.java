/**
 * 
 */
package dataStrom.bus.mq;

/**
 * @author jinyu
 *
 */
public enum MqMode {
    MQ,       //MQ 服务
    PubSub,   //Topic服务
    Memory,   //内存服务
    RPC;
}
