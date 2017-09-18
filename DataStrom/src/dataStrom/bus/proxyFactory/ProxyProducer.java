/**
 * 
 */
package dataStrom.bus.proxyFactory;

import java.io.IOException;


import dataStrom.bus.config.BrokerConfig;
import dataStrom.bus.config.ServerConfig;
import dataStrom.bus.mq.Message;
import dataStrom.bus.mq.MqMode;
import dataStrom.bus.producer.Producer;
import dataStrom.bus.producer.PublishRPC;
import dataStrom.bus.rpc.RpcProcessor;

/**
 * @author jinyu
 * ���������ߴ���
 */
public class ProxyProducer {
 private   BrokerConfig config=null;
 private    Producer p=null;
 private  PublishRPC rpc=null;
 private  boolean error=false;
 ProxyException  ex=new ProxyException("��ʼ������,��RPC��ʼ��������BrokerConfig����");
 
 /**
  * ���������ߴ���
  * @param brokerconfig MQ��������,RPC����Ϊ�գ�����ʱ����ֱ������ʽ
  * @param model  ��������Ĭ��MQ
  */
    public ProxyProducer(BrokerConfig brokerconfig,MqMode model)
    {
   
        this.config=brokerconfig;
       if(model==null)
       {
           model=MqMode.MQ;
       }
       if(brokerconfig==null&&model!=MqMode.RPC)
       {
           error=true;
           //RPC����ֱ��,����RPC�ͱ����д�������
           return;
       }
        if(model!=MqMode.RPC)
        {
           p=new  Producer( brokerconfig, model);
        }
        else
        {
            rpc=new PublishRPC();
        }
    }
    
    /**
     * �������ݣ�ֻ����MQ����Topic)
     * @param name MQ����Topic����
     * @param data ����
     * @return
     */
public boolean publish(String name,byte[]data)
{
   if(error)
   {
       return false;
   }
    if(p!=null)
    {
        Message msg=new Message();
        msg.setMQ(name);
        msg.setTopic(name);
        msg.setBody(data);
        try {
            p.sendAsync(msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return false;
}

/**
 * �������ݣ�ֻ����MQ����Topic)
 * @param msg  ����
 * @return
 */
public boolean publish(Message msg)
{
    if(error)
    {
        return false;
    }
    if(p!=null)
    {
      
        try {
            p.sendAsync(msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return false;
}

/**
 * ����RPC
 * @param processor  ������Ϣ
 * @return
 */
public boolean publishRPC(RpcProcessor processor)
{
    if(error)
    {
        return false;
    }
    if(rpc==null||processor==null)
    {
        return false;
    }
    if(this.config!=null)
    {
        processor.setBrokerConfig(config);
    }
  return  rpc.publish(processor);
    
}

/**
 * ����RPC
 * @param obj  �������
 * @param srvConfig  ��������
 * @return
 */
public <T>  boolean  publishRPC(T  obj,ServerConfig srvConfig)
{
    if(error)
    {
        return false;
    }
    if(rpc==null||obj==null)
    {
        return false;
    }
    RpcProcessor processor=new RpcProcessor(config, srvConfig);
    processor.addModule(obj);
    return  rpc.publish(processor);
    
}

/**
 * ����RPC
 * @param srvConfig  RPC����
 * @param mcls  �����������
 * @return
 */
public   boolean  publishRPC(ServerConfig srvConfig,Class<?>...  mcls)
{
    if(error)
    {
        return false;
    }
    if(rpc==null||mcls==null)
    {
        return false;
    }
    RpcProcessor processor=new RpcProcessor(config, srvConfig);
        for(Class<?> cls:mcls)
        {
            if(!cls.isInterface())
            {
             Object obj;
            try {
                obj = cls.newInstance();
                processor.addModule(obj);
            } catch (InstantiationException e) {
            
                e.printStackTrace();
            } catch (IllegalAccessException e) {
            
                e.printStackTrace();
            }
           
            }
        }
        return  rpc.publish(processor);
}

/**
 * ����RPC 
 * @param srvConfig  RPC����
 * @param mcls  �������
 * @return
 */
public   boolean  publishRPC(ServerConfig srvConfig,Object ...  mcls)
{
    if(error)
    {
        return false;
    }
    if(rpc==null||mcls==null)
    {
        return false;
    }
    RpcProcessor processor=new RpcProcessor(config, srvConfig);
    processor.addModule(mcls);
        return  rpc.publish(processor);
}

/**
 * ����RPC
 * @param srvConfig  RPC����
 * @param srvname  RPC������
 * @return
 */
public   boolean  publishRPC(ServerConfig srvConfig,String[] srvname)
{
    if(error)
    {
        return false;
    }
    if(rpc==null||srvname==null)
    {
        return false;
    }
    RpcProcessor processor=new RpcProcessor(config, srvConfig);
     for(int i=0;i<srvname.length;i++)
     {
     
         try {
            Object obj=   Class.forName(srvname[i]).newInstance();
            processor.addModule(obj);
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
     }
        return  rpc.publish(processor);
}
public String getErrorMessage()
{
    if(error)
    {
       return ex.getMessage();
    }
    return null;
}
}
