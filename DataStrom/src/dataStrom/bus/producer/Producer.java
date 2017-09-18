/**
 * 
 */
package dataStrom.bus.producer;

import java.io.IOException;


import dataStrom.bus.config.BrokerConfig;
import dataStrom.bus.mq.MQCmd;
import dataStrom.bus.mq.MQMessage;
import dataStrom.bus.mq.Message;
import dataStrom.bus.mq.MqMode;
import dataStrom.bus.net.Sync.ResultCallback;

/**
 * @author jinyu
 * 生产者
 * 发布RPC
 * 一个对象锁定一类信息，但是不限制MQ类
 * MQ,Topic,RPC
 */
public class Producer {
    //private static final Logger log = Logger.getLogger(Producer.class.getName());
    private    BrokerConfig  brokerConfig;
   private     MqMode mqmodel;
    protected String accessToken = "";
    protected String registerToken = "";
    protected String masterMq = null;
    protected String masterToken = null;
    private   MQClient<Message, Message > client=null;
    private String mqName="";
    public Producer(BrokerConfig config,MqMode model)
    {
     this.brokerConfig=config;
     this.mqmodel=model;
      client=new  MQClient<Message, Message >(config, model);
     }
    private void fillCommonHeaders(Message msg){
        msg.cmd=MQCmd.MQMsg;
        msg.setMQ(brokerConfig.mqname);

    }
    public void sendAsync(Message msg, final ResultCallback<Message> callback) throws IOException {
        fillCommonHeaders(msg);
        invokeAsync(msg, callback);
    }
 
    private void invokeAsync(Message msg, ResultCallback<Message> callback) {
        MQMessage qmsg=new MQMessage(msg);
        if(mqName.isEmpty())
        {
           client.sendData(msg);
           if(mqmodel==MqMode.PubSub)
           {
               client.addName(qmsg.topicname());
           }
           else
           {
               client.addName(qmsg.mqname());
           }
        }
        else 
        {
            boolean isfind=false;
          if(mqmodel==MqMode.PubSub)
           {
               if(mqName.equalsIgnoreCase(qmsg.topicname()))
               {
                   isfind=true;
               }
           }
           else 
           {
               if(mqName.equalsIgnoreCase(qmsg.mqname()))
               {
                   isfind=true;
               }
           }
           if(isfind)
           {
               client.sendData(msg);
           }
        }
        
    }
    public void sendAsync(Message msg) throws IOException {
        sendAsync(msg, null);
    }
 
    public Message sendSync(Message msg, int timeout) throws IOException, InterruptedException {
        fillCommonHeaders(msg);
    return     invokeSync(msg, timeout);
    }
 

    private Message invokeSync(Message msg, int timeout) {
       return     client.read();
    }
    public Message sendSync(Message msg) throws IOException, InterruptedException {
        return sendSync(msg, 10);
    } 
    
    /**
     * 创建MQ
     * 调用该函数则固化MQ
     * 只为该MQ发送数据，其余数据将舍弃
     * @param name MQ名称
     */
    public void CreateMQ(String name)
    {
        if(name==null)
        {
            return;
        }
        mqName=name;
    }
}
