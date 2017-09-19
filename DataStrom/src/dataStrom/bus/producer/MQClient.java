/**
 * 
 */
package dataStrom.bus.producer;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import dataStrom.bus.config.BrokerConfig;
import dataStrom.bus.config.TrackBus;
import dataStrom.bus.config.TrackConfig;
import dataStrom.bus.mq.MQCmd;
import dataStrom.bus.mq.Message;
import dataStrom.bus.mq.MqMode;
import dataStrom.bus.net.Client;
import dataStrom.bus.net.NetFactory;
import dataStrom.bus.net.Sync.ResultCallback;
import dataStrom.bus.proxy.ProxyClient;
import dataStrom.bus.proxy.Tickets;

/**
 * @author jinyu
 *���з��ͣ������߷�װ
 *���
 */
public class MQClient<REQ, RES> {
    private   BrokerConfig  brokerConfig;//����
    private   MqMode mqmodel;//��ǰ���
    private   ProxyClient<Message, Message>   proxyMonitor=null;//����
    private  Tickets<REQ, RES> proxyData=null;//���ݷ���
    private  Client clientData=null;//���ݷ���
    /**
     * ���ݶ���
     */
    private   ConcurrentLinkedQueue<REQ> queue=new ConcurrentLinkedQueue<REQ>();
   
    /**
     * ��������
     */
    private   ConcurrentLinkedQueue<String> mqnames=new  ConcurrentLinkedQueue<String>();
    private   volatile boolean isRuning=false;//
    private  ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private String address="";
    private String host="";
    private int port=0;
    private int index=0;
    private  Message msgack=null;//ack
    private  Message msgcreate=null;//����ָ��
    private  MQCmd msgCmd=null;
    private long ackTime=System.currentTimeMillis();
    private HashSet<String> setName=new HashSet<String>();
    MQClient(BrokerConfig config,MqMode model)
     {
     TrackBus busConfig=new TrackBus();
     String[] addrs=config.addressList.split(";");
     busConfig.busAddress(addrs);
     this.brokerConfig=config;
     this.mqmodel=model;
     //
      proxyMonitor=new ProxyClient<Message, Message>();
      proxyMonitor.netType=this.brokerConfig.netType;
      msgack=new Message();
      msgack.cmd=MQCmd.MQAck;
      msgack.setMQ("ACK");
      msgack.setBody("ACK");
      //
      msgcreate=new Message();
      msgcreate.setMQ("111");
      msgcreate.setTopic("111");
      msgcreate.setBody("CREATE");
      if(model==MqMode.MQ)
      {
          msgcreate.cmd=MQCmd.MQCreate;
          msgCmd=MQCmd.MQMsg;
      }
      else  if(model==MqMode.PubSub)
      {
          msgcreate.cmd=MQCmd.TopicCreate;
          msgCmd=MQCmd.TopicMsg;
      }
      else  if(model==MqMode.RPC)
      {
          msgcreate.cmd=MQCmd.RPCCreate;
          msgCmd=MQCmd.RPCCreate;
      }
      
      resetHost();
}
 
 /**
  * ���õ�ַ
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
   proxyMonitor.setHost(host, port);
   proxyMonitor.netType=this.brokerConfig.netType;
  if(clientData!=null)
  {
      clientData.close();
  }
   clientData=NetFactory.createClient(this.brokerConfig.netType);
   clientData.setHost(host, port);
   clientData.connect();
   proxyData=new Tickets<REQ, RES>(clientData);
 
   index++;
}

/**
 * ��������
 * @param req
 */
 private  void addData(REQ req)
 {
   
     //���Է������
     queue.offer(req);
     if(!isRuning)
     {
         isRuning=true;
         cachedThreadPool.execute(new Runnable() {
             @Override
             public void run() {
               int speed=brokerConfig.speed;
               int num=0;
               while(true)
               {
                   if(queue.isEmpty())
                   {
                       break;
                   }
                   if(num++%speed==0)
                   {
                       try {
                         TimeUnit.SECONDS.sleep(1);
                         if(brokerConfig.iswaitAck)
                         {
                             //�ȴ�����
                              int numaddr=TrackConfig.node.list.size();
                              while(numaddr>0)
                              {
                                 Message rsp = null;
                                 try {
                                rsp = proxyMonitor.invokeSync(msgack,1000);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                                if(rsp==null)
                               {
                                 resetHost();
                                 numaddr--;
                                }
                                else
                                {
                                    int num1=0;
                                    boolean isSucess=true;
                                    do
                                    {
                                       if(mqnames.isEmpty()||num1>1000)
                                      {
                                       break;
                                      }
                                      String name=   mqnames.poll();
                                      if(name==null)
                                      {
                                          break;
                                      }
                                      num1++;
                                      try {
                                          msgcreate.setMQ(name);
                                          msgcreate.setTopic(name);
                                          rsp = proxyMonitor.invokeSync(msgcreate,20);
                                        if(rsp==null)
                                        {
                                            //�Ѿ�����ʧ��
                                           
                                            isSucess=false;
                                            mqnames.offer(name);
                                            break;
                                            
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    }while(true);
                                //
                                    if(isSucess)
                                    {
                                        break;//�˳�
                                    }
                                }
                              }
                         }
                         else
                         {
                             try {
                              //
                                 Message rsp=null;
                                 int num1=0;
                                // boolean isSucess=true;
                                 do
                                 {
                                    if(mqnames.isEmpty()||num1>1000)
                                   {
                                    break;
                                   }
                                   String name=   mqnames.poll();
                                   if(name==null)
                                   {
                                       break;
                                   }
                                   num1++;
                                   try {
                                       msgcreate.setMQ(name);
                                       msgcreate.setTopic(name);
                                       rsp = proxyMonitor.invokeSync(msgcreate,20);
                                     if(rsp==null)
                                     {
                                         //�Ѿ�����ʧ
                                         //isSucess=false;
                                         mqnames.offer(name);
                                         break;
                                         
                                     }
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                                 }while(true);
                                 //�ᶪʧ���ݣ�����ҵ���ж�
                                proxyMonitor.invokeAsync(msgack, new ResultCallback<Message>() {
                                    @Override
                                    public void onReturn(Message result) {
                                        ackTime=System.currentTimeMillis();
                                    }
                                     
                                 });
                                //
                                //�����̼߳�����
                                if(System.currentTimeMillis()-ackTime>20000)
                                {
                                    boolean issucess=true;
                                    resetHost();//�޶�һ�κ�Ҫ����һ�Σ���ֹʧ��û���л������ڵ㣩
                                    for (String str : setName) {
                                        msgcreate.setMQ(str);
                                        msgcreate.setTopic(str);
                                        rsp = proxyMonitor.invokeSync(msgcreate,20);
                                        if(rsp==null)
                                        {
                                            issucess=false;
                                            break;
                                        }
                                  }
                                   if(!issucess)
                                   {
                                       continue;
                                   }
                                    
                                }
                            } catch (IOException e) {
                              
                                e.printStackTrace();
                            }
                         }
                        
                     } catch (InterruptedException e) {
                      
                         e.printStackTrace();
                     }
                   }
                   //
                   REQ req=queue.poll();
                   if(req==null)
                   {
                        break;
                   }
                  
                   proxyData.add(req,msgCmd);
               }
               isRuning=false;
             }
             
         });
     }
 
 }
 
 /**
  * ��������
  * @param req
  */
public void sendData(REQ req)
{
    if(mqmodel!=MqMode.RPC&&!brokerConfig.isDirect)
    {
        addData(req);
    }
    else
    {
        proxyData.add(req);
    }
}

/**
 * ����
 * @return
 */
public RES read()
{
   return   proxyData.take();
}

/*
 * ��������ʱ����������
 */
public  void addName(String name)
{
    if(setName.add(name))
    {
        mqnames.offer(name);
    }
}
}
