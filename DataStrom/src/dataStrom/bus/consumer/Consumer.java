/**
 * 
 */
package dataStrom.bus.consumer;

import java.io.IOException;
import java.util.logging.Logger;

import dataStrom.bus.config.BrokerConfig;
import dataStrom.bus.config.TrackBus;
import dataStrom.bus.config.TrackConfig;
import dataStrom.bus.mq.MQCmd;
import dataStrom.bus.mq.MQMessage;
import dataStrom.bus.mq.Message;
import dataStrom.bus.mq.MqMode;
import dataStrom.bus.proxy.ProxyClient;


/**
 * @author jinyu
 *订阅消费者
 */
public class Consumer {
    private static final Logger log = Logger.getLogger(Consumer.class.getName());
    private  BrokerConfig  brokerConfig=null;
     private  MqMode mqmodel;
    private Thread consumerThread;
    private volatile ConsumerHandler consumerHandler=null;
    private String address="";
    private String host="";
    private int port=0;
    private int index=0;
    private  Message msgack=null;
    private ProxyClient<Message,Message> proxyData=null;
    private Consumer consumer;
    public Consumer(BrokerConfig config,MqMode model)
{
    this.brokerConfig=config;
    this.mqmodel=model;
    msgack=new Message();
    msgack.setMQ("ACK");
    msgack.setBody("ACK");
    msgack.cmd=MQCmd.MQAck;
    TrackBus bus=new TrackBus();
 
    bus.busAddress(config.addressList.split(";"));
    TrackConfig.node.netType=config.netType;
}
    /**
     * 设置地址
     */
   private void resetHost()
   {
       if(index==TrackConfig.node.list.size())
       {
           index=0;
       }
      long id=TrackConfig.node.list.get(index);
      this.address=TrackConfig.node.hashID.get(id);
      String[] addrs=this.address.split(":");
      this.host=addrs[0];
      this.port=Integer.valueOf(addrs[1]);
      proxyData=new ProxyClient<Message,Message>();
      proxyData.netType=TrackConfig.node.netType;
      proxyData.setHost(host, port);
     
     index++;
   }
   
   /**
    * 扩展接口，暂时无用
    */
public void setTopic(String topic)
{
   
}
public synchronized void start(ConsumerHandler handler) throws IOException{
    onMessage(handler);
    start();
}
public void onMessage(final ConsumerHandler handler) throws IOException { 
    consumerHandler=handler;
}
public synchronized void start() throws IOException { 
    consumer=this;
    if (consumerThread == null) {
        consumerThread = new Thread(new Runnable() {
        
        private Message take()
          {
               return   proxyData.invokeResult();
          }
            @Override
            public void run() {
                //
                while(true)
                {
                   resetHost();
                try {
                 Message rsp=proxyData.invokeSync(msgack, brokerConfig.timeOut);
                 if(rsp!=null)
                 {
                   //发送订阅信息，等待返回
                     Message msgreq=new Message();
                    if(mqmodel==MqMode.MQ)
                    {
                        msgreq.setMQ(brokerConfig.mqname);
                        msgreq.cmd=MQCmd.MQSub;
                        msgreq.setBody("MQsub");
                    }
                    else if(mqmodel==MqMode.PubSub)
                    {
                        msgreq.setMQ(brokerConfig.mqname);
                        msgreq.setTopic(brokerConfig.mqname);
                        msgreq.cmd=MQCmd.TopicSub;
                        msgreq.setBody("TopicSub");
                    }
                    else if(mqmodel==MqMode.RPC)
                    {
                        msgreq.setMQ(brokerConfig.mqname);
                        msgreq.cmd=MQCmd.RequestRPC;
                        msgreq.setBody("RPC");
                    }
                    try {
                     rsp=proxyData.invokeSync(msgreq, brokerConfig.timeOut);
                     if(rsp!=null)
                     {
                         MQMessage qrsp=new MQMessage(rsp);
                         
                         if(qrsp.topicname().equalsIgnoreCase(brokerConfig.mqname)||qrsp.mqname().equalsIgnoreCase(brokerConfig.mqname))
                         {
                             break;
                         }
                     }
                 } catch (IOException ex) {
                     ex.printStackTrace();
                 } catch (InterruptedException ex) {
                     ex.printStackTrace();
                 }
                 }
                } catch (IOException e) {
                   
                    e.printStackTrace();
                } catch (InterruptedException e) {
                 
                    e.printStackTrace();
                }
                //
               
                }
                while(true)
                {
                    Message msg=take();
                    if(msg!=null&&consumerHandler!=null)
                    {
                        log.info("接收到数据！");
                        try {
                            consumerHandler.handle(msg, consumer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
               
            }
            
        });
        consumerThread.setName("ConsumerThread");
    }
    if (consumerThread.isAlive())
        return;
    consumerThread.start();
} 

}

