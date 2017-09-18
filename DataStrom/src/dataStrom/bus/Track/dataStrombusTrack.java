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
 *����Track
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
     * ִ����
     */
    public class CopyModel
    {
        String name;
        String mqType;
        DataStromConsumer consumer;
    }
    /**
     * ���� MQ,Topic ,RPC
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
     * ����
     */
    @Override
    public byte[] getMQMsg(String name) {
        return null;
    }
    /**
     * ����
     */
    @Override
    public int queryLen(String name) {
        // TODO Auto-generated method stub
        return 0;
    }
    /**
     * ����
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
     * MQ������
     * Topic������
     * RPC������Ϣ
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
                //֧������6����ʱ
            Thread.sleep(2000);
            socket.sendData(mqmsg.convertToData());
            Thread.sleep(2000);
            socket.sendData(mqmsg.convertToData());
            socket.close();
            }
            catch(Exception ex)
            {
                System.out.println("��ִ��Ⱥ���ƴ���"+ex.getMessage());
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
     * ����
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
                 int num=0;//ִ�д���
                 int index=0;//Ѱַ
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
                      //��ͣ
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
                          //��һ�Σ���������ȷ��
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
                                  //����
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
                                  continue;//ȡ������
                              }
                              if(index==0)
                              {
                                  //����
                                index=TrackConfig.node.list.indexOf(TrackConfig.node.localHashID);
                              }
                              index++;
                          }
                          else
                          {
                              if(temp.equals(address))
                              {
                              //��õ�ַ���͸���
                                 isfind=true;
                              }
                              else
                              {
                                 //����
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
