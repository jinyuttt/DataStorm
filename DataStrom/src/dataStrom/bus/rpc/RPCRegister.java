/**
 * 
 */
package dataStrom.bus.rpc;

import java.util.ArrayList;
import java.util.HashSet;


import dataStrom.bus.mq.DataStromConsumer;

/**
 * @author jinyu
 * 保存注册信息；管理注册RPC数据
 */
public class RPCRegister {
    private volatile int   rpccount=0;
    private   ArrayList<DataStromConsumer> list=new ArrayList<DataStromConsumer>();
    private HashSet<String> hash=new HashSet<String>();
 
 /**
  * 保存RPC注册信息
  * RPC信息
  * @param consumer
  */
public synchronized void add(DataStromConsumer consumer)
{
    if(consumer==null)
    {
       return;
    }
   if(hash.add(consumer.address))
   {
       list.add(consumer);
       rpccount++;
   }
  
}

/**
 * 返回所有的信息
 * 原注册信息
 * @return
 */
public  ArrayList<DataStromConsumer> getAllConsumer()
{
    return list;
}

/**
 * 获取注册信息
 * 复制，不影响原数据
 * @return
 */
public DataStromConsumer[] getArrayConsumer()
{
    DataStromConsumer[] consumers=new DataStromConsumer[rpccount];
    list.toArray(consumers);
    return consumers;
}
/**
 * 获取注册信息地址
 * 复制，不影响原数据
 * @return
 */
public String[] getConsumers()
{
    DataStromConsumer[] consumers=this.getArrayConsumer();
    String[] registerAddr=new String[consumers.length];
    for(int i=0;i<consumers.length;i++)
    {
        registerAddr[i]=consumers[i].address;
    }
    return registerAddr;
}
public void updatekeepalive(String address)
{
    if(list!=null)
    {
        //也可以充当注册
       for(int j=0;j<list.size();j++)
       {
           if(list.get(j).address.equals(address))
           {
               //找到服务
               list.get(j).lifecycle=true;
               list.get(j).time=System.currentTimeMillis();
               break;
           }
       }
    }
}
}
