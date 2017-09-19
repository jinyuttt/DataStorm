/**
 * 
 */
package dataStrom.bus.topic;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import dataStrom.bus.mq.DataStromConsumer;
import dataStrom.bus.mq.MqAdmin;
import dataStrom.bus.net.udp.udpSession;

/**
 * @author jinyu
 *订阅发布管理
 */
public class dataStrombusPubSub  implements MqAdmin{
    private AtomicInteger  topiccount=new AtomicInteger (0);
    private   ConcurrentHashMap<String,ArrayList<DataStromConsumer>> hashTopic=new ConcurrentHashMap<String,ArrayList<DataStromConsumer>>();
    public ConcurrentHashMap<String,PubSubData> hashMQ=new ConcurrentHashMap<String,PubSubData>();
    private Thread mqThread;
    private TopicSingle single=new TopicSingle();
public dataStrombusPubSub()
{
    start();
}
public void add(String name,byte[]data)
{
	PubSubData  datamq=	hashMQ.get(name);
	if(datamq==null)
	{
	    String create=new String(data);
        if(create.equals("CREATE"))
        {
            datamq=new PubSubData();
            hashMQ.put(name, datamq);
            return;
        }
        else
        {
            //无创建无数据
            return;
        }
	}
	datamq.add(data);
}
public int queryLen(String name)
{
	PubSubData  datamq=	hashMQ.get(name);
	if(datamq==null)
	{
		return 0;
	}
	return datamq.getSize();
}
public byte[] getMQMdg(String name)
{
	PubSubData  datamq=	hashMQ.get(name);
	if(datamq==null)
	{
		return null;
	}
	
	return datamq.remove();
}
@Override
public String[] queryMQ() {
    int size=hashMQ.size();
    int index=0;
    String[] names=new String[size];
    for(String name:hashMQ.keySet())
    {
        names[index]=name;
        index++;
        if(index==size)
        {
            break;
        }
    }
    return names;
   
}

@Override
public byte[] getMQMsg(String name) {
    
    PubSubData  datamq=  hashMQ.get(name);
    if(datamq==null)
    {
        return null;
    }
    
    return datamq.remove();
}
@Override
public String[] queryCustomer(String name) {
  
    ArrayList<DataStromConsumer> list=  hashTopic.get(name);
    if(list!=null)
    {
        DataStromConsumer[] consumers=new DataStromConsumer[topiccount.get()];
        list.toArray(consumers);
        String[] consumerinfo=new String[consumers.length];
        for(int i=0;i<consumers.length;i++)
        {
            consumerinfo[i]=consumers[i].session.getRemoteAddress();
        }
        return consumerinfo;
    }
    return null;
}
@Override
public void addCustomer(String name, DataStromConsumer customer) {
    ArrayList<DataStromConsumer>list=  hashTopic.get(name);
    if(list==null)
    {
       list=new ArrayList<DataStromConsumer>();
       hashTopic.put(name, list);
    }
    if(!list.contains(customer))
    {
    list.add(customer);
    topiccount.incrementAndGet();
    //
    String info=name+" "+customer.session.getRemoteAddress()+" "+customer.session.getRemotePort();
   //
      single.write(info);
    }
}
private  DataStromConsumer[] getTopicConsumers(String name)
{
    ArrayList<DataStromConsumer> list=  hashTopic.get(name);
    if(list!=null)
    {
        DataStromConsumer[] consumers=new DataStromConsumer[topiccount.get()];
        list.toArray(consumers);
        return consumers;
    }
    return null;
}
/*
 * 启动分发
 */
private synchronized void start()
{
    if(mqThread==null)
    {
        mqThread=new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    readSingle() ;
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                int num=0;
                while(true)
                {
                    num=0;
                    String[] mqs=queryMQ();
                    for(int i=0;i<mqs.length;i++)
                    {
                        byte[] data=  getMQMsg(mqs[i]);
                        if(data==null)
                        {
                            num++;
                        }
                        else {
                            DataStromConsumer[] consumer=getTopicConsumers(mqs[i]);
                            if(consumer!=null)
                              {
                                for(int j=0;j<consumer.length;j++)
                                {
                                    consumer[j].session.writeAndFlush(data);
                                }
                             }
                        }
                    }
                    if(num==mqs.length)
                    {
                        //说明没有数据
                        try {
                         TimeUnit.SECONDS.sleep(5);
                        
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                    }
                }
            }
            
        });
        mqThread.setDaemon(true);
        mqThread.setName("mqdatasend");
        mqThread.start();
    }
}
@Override
public DataStromConsumer[] queryClientCustomer(String name) {
    ArrayList<DataStromConsumer> list=  hashTopic.get(name);
    if(list!=null)
    {
        DataStromConsumer[] all=new DataStromConsumer[list.size()];
        list.toArray(all);
        return all;
    }
    return null;
}
@Override
public void readSingle() {
  
    String[] cs=  single.read();
    if(cs==null)
    {
        return;
    }

      DatagramSocket srvsocket = null;
      try {
          srvsocket = new DatagramSocket();
      } catch (SocketException e) {
          e.printStackTrace();
      }
    for(int i=0;i<cs.length;i++)
    {
        String line="";
        try
        {
         line=cs[i].substring(2);
        String[] info=line.split(" ");
        DataStromConsumer c=new DataStromConsumer();
        c.mqname=info[0];
        c.topic=info[0];
        InetAddress host=InetAddress.getByName(info[1]);
        int port=Integer.valueOf(info[2]);
        c.session=new udpSession(srvsocket, host, port);
        //只有在非连接的网络才有效；
       //连接的网络需要消费者重连接，所有直接创建udpSession发送
        }
        catch(Exception ex)
        {
           // log.warning("读取消费者失败："+line);
        }
    }
    
}
}
