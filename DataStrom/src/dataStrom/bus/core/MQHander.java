/**
 * 
 */
package dataStrom.bus.core;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import dataStrom.bus.Track.dataStrombusTrack;
import dataStrom.bus.mq.DataStromConsumer;
import dataStrom.bus.mq.MQCmd;
import dataStrom.bus.mq.MQMessage;
import dataStrom.bus.mq.Message;
import dataStrom.bus.mq.MqAdmin;
import dataStrom.bus.mq.dataStrombusMQ;
import dataStrom.bus.net.Session;
import dataStrom.bus.net.http.MQDataInfo;
import dataStrom.bus.rpc.Heart;
import dataStrom.bus.rpc.MonitorSeq;
import dataStrom.bus.rpc.dataStrombusRPC;
import dataStrom.bus.topic.dataStrombusPubSub;

/**
 * @author jinyu
 * 处理MQ消息
 */
public class MQHander {
   
    private static MqAdmin mq=new dataStrombusMQ();
    private static MqAdmin topic=new dataStrombusPubSub();
    private static dataStrombusRPC   rpc=new dataStrombusRPC();//扩展了MQ方法，不使用接口
    private static dataStrombusTrack tracker=new dataStrombusTrack();
    private static AtomicLong rpcid=new AtomicLong(0);
    private static final Logger log = Logger.getLogger(MQServer.class.getName());
public void  process(MQMessage msg,Session session)
{
    if(msg.cmd==MQCmd.MQCopy)
    {
        DataStromConsumer consumer=new DataStromConsumer();
        if(msg.getMsg()!=null&&msg.mqname().equals("MQCopy"))
        {
            consumer.session=session;
        }
        consumer.mqname=msg.mqname();
        consumer.address=msg.getMsg();
        tracker.addCustomer(consumer.mqname, consumer);
        return;
    }
    if(msg.cmd==MQCmd.MQAck)
    {
        log.info("ACK信息");
        try
        {
        session.writeAndFlush(msg.convertToData());
        }
        catch(Exception ex)
        {
            log.warning(ex.getMessage());
        }
        return;
    }
    if(msg.cmd==MQCmd.Query)
    {
        //
        ArrayList<MQDataInfo> list=new  ArrayList<MQDataInfo>();
        String[]mqname=  mq.queryMQ();
        if(msg.mqname().equals("MQ"))
        {
        for(int i=0;i<mqname.length;i++)
          {
            String name=mqname[i];
            DataStromConsumer[]cons=  mq.queryClientCustomer(name);
          for(int j=0;j<cons.length;j++)
             {
              MQDataInfo info=new MQDataInfo();
               info.MQINFO= cons[j].mqname;
               info.MQConsumer=cons[j].session.getRemoteAddress();
               info.MQTime=cons[j].getDate(cons[j].createTime);
               list.add(info);
              }
           }
        }
        else if(msg.mqname().equals("TOPIC"))
        {
        //TOPIC
        mqname=  topic.queryMQ();
        for(int i=0;i<mqname.length;i++)
        {
            String name=mqname[i];
            DataStromConsumer[]cons=  topic.queryClientCustomer(name);
          for(int j=0;j<cons.length;j++)
          {
              MQDataInfo info=new MQDataInfo();
               info.TopicINFO= cons[j].topic;
               info.TopicConsumer=cons[j].session.getRemoteAddress();
               info.TopicTime=cons[j].getDate(cons[j].createTime);
               list.add(info);
          }
        }
        }
        else if(msg.mqname().equals("RPC"))
        {
        //RPC
        mqname=  rpc.queryMQ();
        for(int i=0;i<mqname.length;i++)
        {
            String name=mqname[i];
            DataStromConsumer[]cons=  rpc.queryClientCustomer(name);
            if(cons==null)
            {
                continue;
            }
          for(int j=0;j<cons.length;j++)
          {
              MQDataInfo info=new MQDataInfo();
               info.RPCINFO= cons[j].mqname;
               info.RPCTime=cons[j].getDate(cons[j].createTime);
               list.add(info);
          }
        }
        }
        //
       String resp=  HTTPRequest.getData(list);
       Message msgs=new Message();
       msgs.cmd=MQCmd.MQMsg;
       msgs.setBody(resp);
       MQMessage rmgs=new MQMessage(msgs);
       session.writeAndFlush(rmgs.convertToData());
       return;
    }
    if(!msg.mqname().isEmpty())
    {
        if(msg.cmd==MQCmd.MQSub)
        {
            DataStromConsumer consumer=new DataStromConsumer();
            if(msg.getMsg()!=null&&msg.mqname().equals("MQCopy"))
            {
                consumer.session=null;//说明是同步
            }
            else
            {
               consumer.session=session;
               MQMessage qrsp=new MQMessage(msg);
               qrsp.cmd=MQCmd.ResponseSub;
               session.writeAndFlush(qrsp.convertToData());
            }
            consumer.mqname=msg.mqname();
            consumer.address=session.getRemoteAddress();
            mq.addCustomer(consumer.mqname, consumer);
            tracker.addCustomer(consumer.mqname+","+"MQ", consumer);
            log.info("收到MQ订阅");
        }
        else if(msg.cmd==MQCmd.TopicSub)
        {
            DataStromConsumer consumer=new DataStromConsumer();
            if(msg.getMsg()!=null&&msg.mqname().equals("MQCopy"))
            {
                consumer.session=null;//说明是同步
            }
            else
            {
            consumer.session=session;
            MQMessage qrsp=new MQMessage(msg);
            qrsp.cmd=MQCmd.ResponseSub;
            session.writeAndFlush(qrsp.convertToData());
            }
            consumer.mqname=msg.mqname();
            consumer.topic=msg.topicname();
            consumer.address=session.getRemoteAddress();
            topic.addCustomer(consumer.topic, consumer);
            tracker.addCustomer(consumer.mqname+","+"TOPIC", consumer);
        }
        else  if(msg.cmd==MQCmd.Heart)
        {
            rpc.add(msg.mqname(), msg.getBody());
        }
        else if(msg.cmd==MQCmd.RPCMonitor)
        {  
            //RP监视
            MonitorSeq monitor=new MonitorSeq();
            monitor.name=msg.mqname();
            Heart heart=new Heart();
            heart.convertToModel(msg.getBody());
            monitor.id=heart.rpcID;
            monitor.taskid=heart.taskid;
            monitor.name=heart.name;
            monitor.address=heart.address;
            monitor.time=System.currentTimeMillis();
            rpc.addMonitor(msg.mqname(), monitor);
        }
        else if(msg.cmd==MQCmd.RPCCreate)
        {
            String rpcInfo=msg.getMsg();
            String name=msg.mqname();
            DataStromConsumer customer=new DataStromConsumer();
            customer.mqname=name;
            if(msg.getMsg()!=null&&msg.mqname().equals("MQCopy"))
            {
                customer.session=null;//说明是同步
            }
            else
            {
                customer.session=session;
            }
            String[]info=rpcInfo.split(" ");
            customer.address=info[0];//地址
            customer.keeplive=info[1];//类型
            customer.rpcid=rpcid.incrementAndGet();
            customer.RpcnetType=info[2];
            rpc.addCustomer(name, customer);
            rpc.addRPCType(name, info[3]);
            tracker.addCustomer(customer.mqname+","+"RPC", customer);
            log.info("创建RPC成功："+msg.mqname());
            session.writeAndFlush(msg.convertToData());
        }
        else if(msg.cmd==MQCmd.MQCreate)
        {
            log.info("创建MQ成功："+msg.mqname());
           mq.add(msg.mqname(), msg.getBody());
           tracker.add(msg.mqname(), "MQ".getBytes());
           session.writeAndFlush(msg.convertToData());
        }
        else if(msg.cmd==MQCmd.RequestRPC)
        {
            DataStromConsumer customer=  rpc.getRPCConsumers(msg.mqname());
            Message message=new Message();
            message.cmd=MQCmd.ResponseRPC;
            String rpcinf=customer.address+" "+customer.RpcnetType+" "+  customer.keeplive;
            message.setBody(rpcinf);
            message.setMQ(msg.mqname());
            MQMessage rsp=new MQMessage(message);
            session.writeAndFlush(rsp.convertToData());
            log.info("收到RPC请求，已回执："+session.getLocalAddress());
            //
            if(customer.keeplive.equals("udp"))
            {
            MonitorSeq monitor=new MonitorSeq();
            monitor.name=msg.mqname();
            Heart heart=new Heart();
            heart.convertToModel(msg.getBody());
            monitor.id=heart.rpcID;
            monitor.taskid=heart.taskid;
            monitor.name=heart.name;
            monitor.address=heart.address;
            monitor.time=System.currentTimeMillis();
            rpc.addMonitor(msg.mqname(), monitor);
            }
        }
        else if(msg.cmd==MQCmd.MQMsg)
        {
            mq.add(msg.mqname(),msg.convertToData());
        }
    }
    if(!msg.topicname().isEmpty())
    {
        if(msg.cmd==MQCmd.TopicCreate)
        {
            log.info("创建Topic成功："+msg.topicname());
            topic.add(msg.topicname(), msg.getBody()); 
            tracker.add(msg.topicname(), "TOPIC".getBytes());
            session.writeAndFlush(msg.convertToData());
        }
        else if(msg.cmd==MQCmd.TopicMsg)
        {
           topic.add(msg.topicname(), msg.convertToData()); 
        }
        else if(msg.cmd==MQCmd.TopicSub)
        {
            DataStromConsumer consumer=new DataStromConsumer();
            if(msg.getMsg()!=null&&msg.topicname().equals("MQCopy"))
            {
                consumer.session=null;//说明是同步
            }
            else
            {
            consumer.session=session;
            MQMessage qrsp=new MQMessage(msg);
            qrsp.cmd=MQCmd.ResponseSub;
            session.writeAndFlush(qrsp.convertToData());
            }
            consumer.mqname=msg.topicname();
            consumer.topic=msg.topicname();
            consumer.address=session.getRemoteAddress();
            topic.addCustomer(consumer.topic, consumer);
            tracker.addCustomer(consumer.mqname+","+"TOPIC", consumer);
        }
    }
}


}
