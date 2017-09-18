/**
 * 
 */
package dataStrom.bus.rpc;

import java.util.ArrayList;
import java.util.HashSet;


import dataStrom.bus.mq.DataStromConsumer;

/**
 * @author jinyu
 * ����ע����Ϣ������ע��RPC����
 */
public class RPCRegister {
    private volatile int   rpccount=0;
    private   ArrayList<DataStromConsumer> list=new ArrayList<DataStromConsumer>();
    private HashSet<String> hash=new HashSet<String>();
 
 /**
  * ����RPCע����Ϣ
  * RPC��Ϣ
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
 * �������е���Ϣ
 * ԭע����Ϣ
 * @return
 */
public  ArrayList<DataStromConsumer> getAllConsumer()
{
    return list;
}

/**
 * ��ȡע����Ϣ
 * ���ƣ���Ӱ��ԭ����
 * @return
 */
public DataStromConsumer[] getArrayConsumer()
{
    DataStromConsumer[] consumers=new DataStromConsumer[rpccount];
    list.toArray(consumers);
    return consumers;
}
/**
 * ��ȡע����Ϣ��ַ
 * ���ƣ���Ӱ��ԭ����
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
        //Ҳ���Գ䵱ע��
       for(int j=0;j<list.size();j++)
       {
           if(list.get(j).address.equals(address))
           {
               //�ҵ�����
               list.get(j).lifecycle=true;
               list.get(j).time=System.currentTimeMillis();
               break;
           }
       }
    }
}
}
