/**
 * 
 */
package dataStrom.bus.Track;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import dataStrom.bus.config.TrackConfig;
import dataStrom.bus.mq.DataStromConsumer;
import dataStrom.bus.mq.MQCmd;
import dataStrom.bus.mq.MQMessage;
import dataStrom.bus.mq.MqAdmin;
import dataStrom.bus.net.Client;
import dataStrom.bus.net.NetFactory;

/**
 * @author jinyu
 *管理Track
 */
public class dataStrombusTrack  implements MqAdmin{
    private final int waitTime=5;
    LinkedBlockingQueue<String> waitqueue=new LinkedBlockingQueue<String>();
    ConcurrentLinkedQueue<CopyModel> queue=new ConcurrentLinkedQueue<CopyModel>();
    private Thread trackThread;
    
  public  dataStrombusTrack()
  {
      if(TrackConfig.Istracker)
      {
           start();
      }
  }
    /**
     * 
     * @author jinyu
     * 执行类
     */
    public class CopyModel
    {
        String name;
        String mqType;
        DataStromConsumer consumer;
    }
    /**
     * 创建 MQ,Topic ,RPC
     */
    @Override
    public void add(String name, byte[] data) {
      //
     
        if(name.equalsIgnoreCase("MQCopyResponse"))
        {
            waitqueue.offer(new String(data));
        }
        else
        {
            CopyModel model=new CopyModel();
            model.name=name;
            model.mqType=new String(data);
            queue.offer(model);
        }
    }

    /**
     * 无用
     */
    @Override
    public byte[] getMQMsg(String name) {
        return null;
    }
    /**
     * 无用
     */
    @Override
    public int queryLen(String name) {
        // TODO Auto-generated method stub
        return 0;
    }
    /**
     * 无用
     */
    @Override
    public String[] queryMQ() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] queryCustomer(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MQ消费者
     * Topic订阅者
     * RPC服务信息
     * name=name+","+mqtype
     */
    @Override
    public void addCustomer(String name, DataStromConsumer customer) {
    
        if(name.equalsIgnoreCase("MQCopy"))
        {
            MQMessage mqmsg=new MQMessage();
            mqmsg.cmd=MQCmd.MQCopyResponse;
            mqmsg.setMQ("MQCopyResponse");
            mqmsg.setTopic("MQCopyResponse");
            mqmsg.setBody(TrackConfig.node.localAddress);
            Client socket=NetFactory.createClient(TrackConfig.node.netType);
            socket.sendData(mqmsg.convertToData());
            try
            {
                //支持网络6秒延时
            Thread.sleep(2000);
            socket.sendData(mqmsg.convertToData());
            Thread.sleep(2000);
            socket.sendData(mqmsg.convertToData());
            socket.close();
            }
            catch(Exception ex)
            {
                System.out.println("回执集群复制错误："+ex.getMessage());
            }
        }
        String[]mqname=name.split(",");
        CopyModel model=new CopyModel();
        model.name=mqname[0];
        model.mqType=mqname[1];
        model.consumer=customer;
        queue.offer(model);
    }
    /**
     * 无用
     */
    @Override
    public DataStromConsumer[] queryClientCustomer(String name) {
    
        return null;
    }
  private synchronized void start()
  {
  
    if(trackThread==null)
      {
          trackThread=new Thread(new Runnable() {

              @Override
              public void run() {
                  try {
                      TimeUnit.SECONDS.sleep(4);
                  } catch (InterruptedException e1) {
                      e1.printStackTrace();
                  }
                 int num=0;//执行次数
                 int index=0;//寻址
                 String address="";
                 boolean isfind=false;
                 Client socket=NetFactory.createClient(TrackConfig.node.netType);
                  while(true)
                  {
                      if(queue.isEmpty())
                      {
                          try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                          num=0;
                          index=0;
                          isfind=false;
                          continue;
                      }
                      //暂停
                      if(num%100==0)
                      {
                          try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                      }
                      if(num==0)
                      {
                          //第一次，发送请求，确认
                          if(index==0)
                          {
                              address=TrackConfig.node.nextAddress;
                          }
                          else
                          {
                              Long id=   TrackConfig.node.list.get(index);
                              address=TrackConfig.node.hashID.get(id);
                          }
                          String[]addr=address.split(":");
                          socket.setHost(addr[0], Integer.valueOf(addr[1]));
                          boolean r=  socket.connect();
                          if(!r)
                          {
                              if(index==0)
                              {
                                  //查找
                                index=   TrackConfig.node.list.indexOf(TrackConfig.node.localHashID);
                              }
                              index++;
                              continue;
                          }
                          else
                          {
                              MQMessage mqmsg=new MQMessage();
                              mqmsg.cmd=MQCmd.MQCopy;
                              mqmsg.setMQ("MQCopy");
                              mqmsg.setTopic("MQCopy");
                              mqmsg.setBody(TrackConfig.node.localAddress);
                              socket.sendData(mqmsg.convertToData());
                          }
                          String temp = null;
                        try {
                            temp = waitqueue.poll(waitTime, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                          if(temp==null)
                          {
                              if(num!=0)
                              {
                                  continue;//取出完了
                              }
                              if(index==0)
                              {
                                  //查找
                                index=TrackConfig.node.list.indexOf(TrackConfig.node.localHashID);
                              }
                              index++;
                          }
                          else
                          {
                              if(temp.equals(address))
                              {
                              //向该地址发送复制
                                 isfind=true;
                              }
                              else
                              {
                                 //抛弃
                                  continue;
                              }
                              
                          }
                      }
                      //
                      if(isfind)
                      {
                          num++;
                          CopyModel model=   queue.poll();
                          if(model==null)
                          {
                              continue;
                          }
                          MQMessage mqmsg=new MQMessage();
                          if(model.consumer==null)
                          {
                              if(model.mqType.equalsIgnoreCase("MQ"))
                              {
                                  mqmsg.cmd=MQCmd.MQCreate;
                              }
                              else if(model.mqType.equalsIgnoreCase("TOPIC"))
                              {
                                 mqmsg.cmd=MQCmd.TopicCreate;
                              }
                              else if(model.mqType.equalsIgnoreCase("RPC"))
                              {
                                  mqmsg.cmd=MQCmd.RPCCreate;
                              }
                              mqmsg.setMQ(model.name);
                              mqmsg.setTopic(model.name);
                              mqmsg.setBody("MQCopy");
                          }
                       
                      else
                      {
                          if(model.mqType.equalsIgnoreCase("MQ"))
                          {
                              mqmsg.cmd=MQCmd.MQSub;
                          }
                          else if(model.mqType.equalsIgnoreCase("TOPIC"))
                          {
                             mqmsg.cmd=MQCmd.TopicSub;
                          }
                          else if(model.mqType.equalsIgnoreCase("RPC"))
                          {
                              mqmsg.cmd=MQCmd.RPCCreate;
                          }
                          mqmsg.setMQ(model.name);
                          mqmsg.setTopic(model.name);
                          mqmsg.setBody(model.consumer.address+" "+model.consumer.keeplive+"  MQCopy");
                      }
                      
                  }
              }
              }
          });
          trackThread.setDaemon(true);
          trackThread.setName("trackThread");
          trackThread.start();
      }
  }
}
