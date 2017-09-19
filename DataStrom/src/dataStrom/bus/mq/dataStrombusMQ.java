/**
 * 
 */
package dataStrom.bus.mq;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import dataStrom.bus.net.udp.udpSession;


/**
 * @author jinyu
 *MQ�������
 */
public class dataStrombusMQ  implements MqAdmin{
    private static final Logger log = Logger.getLogger(dataStrombusMQ.class.getName());
    private AtomicInteger mqcount=new AtomicInteger (0);
    private AtomicInteger  mqindex=new AtomicInteger (0);
    private ConcurrentHashMap<String,MQData> hashMQ=new ConcurrentHashMap<String,MQData>();
    private  Thread mqThread=null;
    private  SingleStrom single=new SingleStrom();
    private   ConcurrentHashMap<String,ArrayList<DataStromConsumer>> hash=new ConcurrentHashMap<String,ArrayList<DataStromConsumer>>();
public dataStrombusMQ()
{
    
    start();
}
 public void readSingle()
{
  String[] customers=  single.read();
  if(customers==null)
  {
      return;
  }

    DatagramSocket srvsocket = null;
    try {
        srvsocket = new DatagramSocket();
    } catch (SocketException e) {
        e.printStackTrace();
    }
  for(int i=0;i<customers.length;i++)
  {
      String line="";
      try
      {
       line=customers[i].substring(2);
      String[] info=line.split(" ");
      DataStromConsumer c=new DataStromConsumer();
      c.mqname=info[0];
      InetAddress host=InetAddress.getByName(info[1]);
      int port=Integer.valueOf(info[2]);
      c.session=new udpSession(srvsocket, host, port);
      this.addCustomer(info[0], c);
      //ֻ���ڷ����ӵ��������Ч��
     //���ӵ�������Ҫ�����������ӣ�����ֱ�Ӵ���udpSession����
      }
      catch(Exception ex)
      {
          log.warning("��ȡ������ʧ�ܣ�"+line);
      }
  }
}
/**
 * �������
 */
public void add(String name,byte[]data)
{
	MQData  datamq=	hashMQ.get(name);
	if(datamq==null)
	{
		//
		String create=new String(data);
		if(create.equals("CREATE"))
		{
		    datamq=new MQData();
	        hashMQ.put(name, datamq);
	        return;
		}
		else
		{
		    //�޴���������
		    return;
		}
	}
	if(data!=null&&data.length>0)
	{
	    log.info("MQ����");
	    if(data.length==6)
	    {
	        log.info("5�ֽ�");
	    }
	   datamq.add(data);
	}
}
/*
 * (non-Javadoc)
 * ��ѯ����������
 */
public int queryLen(String name)
{
	MQData  datamq=	hashMQ.get(name);
	if(datamq==null)
	{
		return 0;
	}
	return datamq.getSize();
}

/**
 * ��ȡ����
 */
public byte[] getMQMsg(String name)
{
	MQData  datamq=	hashMQ.get(name);
	if(datamq==null)
	{
		return null;
	}
	
	return datamq.remove();
}

/**
 * ��ѯMQ����
 */
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
public String[] queryCustomer(String name) {
    ArrayList<DataStromConsumer> list=  hash.get(name);
    if(list!=null)
    {
        DataStromConsumer[] consumers=new DataStromConsumer[mqcount.get()];
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

    ArrayList<DataStromConsumer>list=  hash.get(name);
    if(list==null)
    {
       list=new ArrayList<DataStromConsumer>();
       hash.put(name, list);
    }
    if(!list.contains(customer))
    {
       list.add(customer);
       customer.rpcid=mqcount.incrementAndGet();
       String info=name+" "+customer.session.getRemoteAddress()+" "+customer.session.getRemotePort();
    //
       single.write(info);
    }
}
private  DataStromConsumer getMQConsumers(String name)
{
    ArrayList<DataStromConsumer> list=  hash.get(name);
    if(list!=null)
    {
        if(mqindex.get()>=mqcount.get())
        {
            mqindex.set(0);
        }
        DataStromConsumer consumer = list.get(mqindex.getAndIncrement());
        return consumer;
    }
    return null;
}

/*
 * �����ַ�
 */
private synchronized void start()
{
    //����ǵ��ڵ����ȡ
    if(mqThread==null)
    {
        mqThread=new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    readSingle();
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
                           DataStromConsumer consumer=getMQConsumers(mqs[i]);
                            if(consumer!=null)
                            {
                                log.info("���͸������ߣ�"+consumer.session.getLocalAddress());
                                if(data.length==6)
                                {
                                    log.info("5�ֽڷ���");
                                }
                                 consumer.session.writeAndFlush(data);
                           }
                        }
                    }
                    if(num==mqs.length)
                    {
                        //˵��û������
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
    ArrayList<DataStromConsumer> list=  hash.get(name);
    if(list!=null)
    {
        DataStromConsumer[] all=new DataStromConsumer[list.size()];
        list.toArray(all);
        return all;
    }
    return null;
}

}
