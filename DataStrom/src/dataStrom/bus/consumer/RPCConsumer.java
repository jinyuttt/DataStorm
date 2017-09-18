/**
 * 
 */
package dataStrom.bus.consumer;





import dataStrom.bus.config.BrokerConfig;
import dataStrom.bus.config.ServerConfig;
import dataStrom.bus.config.TrackBus;
import dataStrom.bus.config.TrackConfig;
import dataStrom.bus.mq.MQCmd;
import dataStrom.bus.mq.MQMessage;
import dataStrom.bus.mq.Message;
import dataStrom.bus.proxy.ProxyClient;
import dataStrom.bus.rpc.ProxyFactory;

/**
 * @author jinyu
 *
 */
public class RPCConsumer {
    
    /**
     * 返回连接代理
     * @param config 代理配置
     * @param cls
     * @return
     */
public ProxyFactory rpcClient(BrokerConfig config,Class<?> cls)
{
    TrackBus bus=new TrackBus();
    bus.busAddress(config.addressList.split(";"));
    ProxyClient<Message, Message> client=new ProxyClient<Message, Message>();
    //
   //查找 
    Message msg=new Message();
    msg.cmd=MQCmd.RequestRPC;
    msg.setMQ(cls.getSimpleName());
    msg.setBody("RPC");
    client.netType=config.netType;
    for(Long id:TrackConfig.node.list)
    {
        try {
       String mqaddr= TrackConfig.node.hashID.get(id);
       if(mqaddr!=null)
       {
           String[]addrs=mqaddr.split(":");
           String host=addrs[0];
           int port=Integer.valueOf(addrs[1]);
           client.setHost(host, port);
           Message resp=  client.invokeSync(msg, config.timeOut);
          if(resp!=null&&resp.cmd==MQCmd.ResponseRPC)
          {
              MQMessage rsp=new MQMessage(resp);
              String info=rsp.getBodyString(rsp.getEncoding());
              String[]rpcInfo=info.split(" ");//三部分 地址，通讯类型，存活类型
              String[] rpcHost=rpcInfo[0].split(":");
              String rpcIP=rpcHost[0];
              int rpcPort=Integer.valueOf(rpcHost[1]);
              ProxyFactory factory =null;
              factory =new ProxyFactory();
              factory.setRPCHost(rpcIP ,rpcPort, rpcInfo[1]);
              return factory;
          }
       }
        
    } catch (Exception e1) {
    } 
}
    return null;
}

/**
 * 返回代理
 * @param srvConfig 服务地址
 * @return
 */
public ProxyFactory rpcClient(ServerConfig srvConfig)
{
    ProxyFactory factory =null;
    factory =new ProxyFactory();
    String[]rpcHost=srvConfig.address.split(":");
    String host=rpcHost[0];
    int port=Integer.valueOf(rpcHost[1]);
    factory.setRPCHost(host, port, srvConfig.netType);
   return factory;
}

}
