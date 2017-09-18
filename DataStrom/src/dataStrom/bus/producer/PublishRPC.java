/**
 * 
 */
package dataStrom.bus.producer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import dataStrom.bus.config.BrokerConfig;
import dataStrom.bus.config.ServerConfig;
import dataStrom.bus.config.TrackBus;
import dataStrom.bus.config.TrackConfig;
import dataStrom.bus.core.TrackNode;
import dataStrom.bus.mq.MQCmd;
import dataStrom.bus.mq.MQMessage;
import dataStrom.bus.mq.Message;
import dataStrom.bus.mq.RPCCode;
import dataStrom.bus.net.NetFactory;
import dataStrom.bus.net.Server;
import dataStrom.bus.net.Session;
import dataStrom.bus.proxy.ProxyClient;
import dataStrom.bus.rpc.Heart;
import dataStrom.bus.rpc.RpcProcessor;

/**
 * @author jinyu
 *����RPC
 */
public class PublishRPC {
  private  BrokerConfig config=null;
  private   RpcProcessor processor=null;
  private  ServerConfig srvConfig=null;
  private  Thread srvThread=null;
private int index;
private String address;
private String host;
private Integer port;
private   ProxyClient<Message, Message>   proxyMonitor=null;//����
private  AtomicLong taskid=new AtomicLong(0);
private static final java.util.logging.Logger log = Logger.getLogger(RpcProcessor.class.getName());
public  boolean publish(RpcProcessor processor)
{
    this.processor=processor;
    config=processor.getBroker();
    TrackBus bus=new TrackBus();
    if(config==null)
    {
        //û�д���
        TrackConfig.node=new TrackNode();
    }
    else
    {
        bus.busAddress(config.addressList.split(";"));
    }
    this.srvConfig=processor.getSrvConfig();
    proxyMonitor=new  ProxyClient<Message, Message>();
    
    resetHost();
    return  start();
    
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
  proxyMonitor.netType=this.config.netType;
  index++;
}
/**
 * ��������
 * @return
 */
  private synchronized boolean start()
{
    if(srvThread==null)
    {
        srvThread=new Thread(new Runnable() {

            @Override
            public void run() {
                if(srvConfig!=null)
                {
                    Server s=NetFactory.createServer(srvConfig.netType);
                    if(srvConfig.address==null||srvConfig.address.isEmpty())
                    {
                        try {
                            s.start(srvConfig.port);
                            log.info("��������");
                        } catch (Exception e) {
                            log.warning("��������ʧ��");
                        }
                    }
                    else
                    {
                    try {
                        s.start(srvConfig.address, srvConfig.port);
                        log.info("��������");
                    } catch (Exception e) {
                   
                        log.warning("��������ʧ��");
                    }
                    }
                   //
                    while(true)
                    {
                       boolean r=   startTest();
                       if(r)
                       {
                           break;
                       }
                    }
                    //
                    boolean isBoker=true;
                    if(TrackConfig.node.list.isEmpty())
                    {
                        isBoker=false;
                    }
                    if(isBoker)
                    {
                      startRegister(s.getlocahost(),s.getLocalPort());
                 //monitor
                   
                  Message msgheart=new Message();
                  msgheart.cmd=MQCmd.Heart;
                  Heart heart=new Heart();
                  heart.address=host+":"+port;
                  heart.rpcType=Byte.valueOf(srvConfig.rpcType);
                  heart.taskid=taskid.getAndIncrement();
                  heart.result=RPCCode.busMonitor;
                  try {
                    proxyMonitor.invokeAsync(msgheart, null);
                } catch (IOException e) {
                      log.warning("����RPCMonitorʧ�ܣ�RPC,taskid:"+taskid.get());
                }
                    }
                      while(true)
                      {
                          try
                          {
                            
                      Session session=s.getSession();
                      byte[]data= session.handleReadData();
                       MQMessage msg=new MQMessage(data,true);
                       Message rsp= processor.process(msg);
                     
                          if(rsp==null)
                          {
                              if(isBoker)
                              {
                          
                              Message msgheart=new Message();
                              msgheart.cmd=MQCmd.Heart;
                              Heart heart=new Heart();
                              heart.address=host+":"+port;
                              heart.rpcType=Byte.valueOf(srvConfig.rpcType);
                              heart.taskid=taskid.getAndIncrement();
                              heart.result=RPCCode.busMonitor;
                              heart.result=RPCCode.error;
                          try {
                              proxyMonitor.invokeAsync(msgheart, null);
                          } catch (IOException e) {
                                log.warning("����RPCMonitorʧ�ܣ�RPC,taskid:"+taskid.get());
                          }
                          
                           }
                              continue;
                       }
                      MQMessage qrsp=new MQMessage(rsp);
                      session.writeAndFlush(qrsp.convertToData());
                      //
                      }
                      
                  catch(Exception ex)
                  {
                      if(isBoker)
                      {
                      Message msgheart=new Message();
                      msgheart.cmd=MQCmd.Heart;
                      Heart heart=new Heart();
                      heart.address=host+":"+port;
                      heart.rpcType=Byte.valueOf(srvConfig.rpcType);
                      heart.taskid=taskid.getAndIncrement();
                      heart.result=RPCCode.error;
                      try {
                          proxyMonitor.invokeAsync(msgheart, null);
                      } catch (IOException e) {
                            log.warning("����RPCMonitorʧ�ܣ�RPC,taskid:"+taskid.get());
                      }
                      }
                  }
                
                 //
                  if(isBoker)
                  {
                  Message msgheart=new Message();
                  msgheart.cmd=MQCmd.Heart;
                  Heart heart=new Heart();
                  heart.address=host+":"+port;
                  heart.rpcType=Byte.valueOf(srvConfig.rpcType);
                  heart.taskid=taskid.getAndIncrement();
                  heart.result=RPCCode.error;
                 heart.result=RPCCode.sucess;
                 try {
                     proxyMonitor.invokeAsync(msgheart, null);
                 } catch (IOException e) {
                       log.warning("����RPCMonitorʧ�ܣ�RPC,taskid:"+taskid.get());
                 }
                  }
                  }
                //
            }
            }
            
        });
    }
    if(srvThread.isAlive())
    {
        return true;
    }
    srvThread.setName("srv");
    srvThread.setDaemon(true);
    srvThread.start();
    return true;
    
}

 private  boolean startTest()
 {
     boolean isSucess=true;
     Message msgack = new Message();
     msgack.cmd=MQCmd.MQAck;
     msgack.setMQ("ACK");
     msgack.setBody("ACK");
     int numaddr=TrackConfig.node.list.size();
     if(numaddr==0)
     {
         return true;//˵��û�д���
     }
     while(numaddr>0)
     {
        Message rsp = null;
        try {
            rsp = proxyMonitor.invokeSync(msgack,1000);
        } catch (IOException e) {
            log.warning("����ע��ʧ��");
        } catch (InterruptedException e) {
            log.warning("����ע���߳�ʧ��");
        }
       if(rsp==null)
      {
        resetHost();
        numaddr--;
        isSucess=false;
      }
       else
       {
           isSucess=true;
           break;
       }
     }
     return isSucess;
 }
 
 /**
  * ����ע�������
  * @param host
  * @param port
  */
  private void startRegister(String host,int port)
  {
      Thread heartThread=new Thread(new Runnable() {

        @Override
        public void run() {
        
      if(config!=null)
      {
          try {
              Message msgcrate=new Message();
              msgcrate.cmd=MQCmd.RPCCreate;
              List<String>  list=  processor.getModules();
              for(int i=0;i<list.size();i++)
              {
                  msgcrate.setMQ(list.get(0));
                  String info=host+":"+port+" "+srvConfig.keepalive+" " +   srvConfig.netType +" "+srvConfig.rpcType;
                  //��Ӧ ��ַ ͨѶ���� �������ͣ����ӻ��Ǹ��ؾ���)
                  msgcrate.setBody(info);
                  Message rsp = proxyMonitor.invokeSync(msgcrate,1000);
                  if(rsp!=null)
                  {
                      
                  }
              }
              //ע����������
              if(TrackConfig.node.list.isEmpty())
              {
                  return;
              }
              while(true)
              {
                  Message msgheart=new Message();
                  msgheart.cmd=MQCmd.Heart;
                   list=  processor.getModules();
                   Heart heart=new Heart();
                   heart.address=host+":"+port;
                   heart.rpcType=Byte.valueOf(srvConfig.rpcType);
                  for(int i=0;i<list.size();i++)
                  {
                      msgheart.setMQ(list.get(i));
                      //String info=host+":"+port+" "+srvConfig.protocolType+" " +   srvConfig.netType +" "+srvConfig.rpcType;
                      //��Ӧ ��ַ ͨѶ���� �������ͣ����ӻ��Ǹ��ؾ���)
                  
                      heart.name=list.get(i);
                      msgheart.setBody(heart.convertToData());
                      proxyMonitor.invokeAsync(msgheart, null);
                  }
                  TimeUnit.SECONDS.sleep(srvConfig.waitHeart);
              }
              
          
          } catch (IOException e) {
              log.warning("����ע��ʧ��");
          } catch (InterruptedException e) {
              log.warning("����ע���߳�ʧ��");
          }
      }
        }
        
      });
      heartThread.setDaemon(true);
      heartThread.setName("heart");
      heartThread.start();
  }
}
