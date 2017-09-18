/**
 * 
 */
package dataStrom.bus.proxyFactory;

import java.io.IOException;

import dataStrom.bus.config.BrokerConfig;
import dataStrom.bus.config.ServerConfig;
import dataStrom.bus.consumer.Consumer;
import dataStrom.bus.consumer.ConsumerHandler;
import dataStrom.bus.consumer.RPCConsumer;
import dataStrom.bus.mq.MqMode;
import dataStrom.bus.rpc.ProxyFactory;

/**
 * @author jinyu
 * 代理订阅端以及RPC调用端
 */
public class ProxyConsumer {
    
    /**
     * MQ
     * @param config
     * @param handler
     * @return
     */
public boolean invokeMQ(BrokerConfig config,ConsumerHandler handler)
{
    Consumer c=new Consumer(config,MqMode.MQ);     ;
    try {
       c.start(handler);
       return true;
    } catch (IOException e) {
        e.printStackTrace();
    }
    return false;
    
}

/**
 * Topic
 * @param config
 * @param handler
 * @return
 */
public boolean invokeTopic(BrokerConfig config,ConsumerHandler handler)
{
    Consumer c=new Consumer(config,MqMode.PubSub);     ;
    try {
       c.start(handler);
       return true;
    } catch (IOException e) {
        e.printStackTrace();
    }
    return false;
    
}

/**
 *代理连接
 * @param config 代理地址
 * @param cls
 * @return
 */
public  <T> T invokeRPC(BrokerConfig config,Class< T> cls)
{
    RPCConsumer c=new RPCConsumer();
    ProxyFactory f= c.rpcClient(config, cls);
    if(f!=null)
    {
        try {
         return    f.getService(cls, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return null;
    
}

/**
 * 服务直连
 * @param config  服务配置 主要是地址和通讯类型，其余设置无效
 * @param cls
 * @return
 */
public  <T> T invokeSrvRPC(ServerConfig config,Class< T> cls)
{
    RPCConsumer c=new RPCConsumer();
    ProxyFactory f= c.rpcClient(config);
    if(f!=null)
    {
        try {
              return   f.getService(cls, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return null;
    
}
}
