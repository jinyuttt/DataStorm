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
 * 发布生产者代理
 */
public class ProxyProducer {
 private   BrokerConfig config=null;
 private    Producer p=null;
 private  PublishRPC rpc=null;
 private  boolean error=false;
 ProxyException  ex=new ProxyException("初始化错误,非RPC初始化必须有BrokerConfig配置");
 
 /**
  * 创建生产者代理
  * @param brokerconfig MQ代理配置,RPC可以为空，调用时采用直连服务方式
  * @param model  代理类型默认MQ
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
           //RPC可以直连,不是RPC就必须有代理配置
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
     * 发布数据（只能是MQ或者Topic)
     * @param name MQ或者Topic名称
     * @param data 数据
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
 * 发布数据（只能是MQ或者Topic)
 * @param msg  数据
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
 * 发布RPC
 * @param processor  发布信息
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
 * 发布RPC
 * @param obj  服务对象
 * @param srvConfig  服务配置
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
 * 发布RPC
 * @param srvConfig  RPC配置
 * @param mcls  服务对象类型
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
 * 发布RPC 
 * @param srvConfig  RPC配置
 * @param mcls  服务对象
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
 * 发布RPC
 * @param srvConfig  RPC配置
 * @param srvname  RPC类名称
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
