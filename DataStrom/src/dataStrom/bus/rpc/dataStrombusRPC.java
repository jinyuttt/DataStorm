/**
 * 
 */
package dataStrom.bus.rpc;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import dataStrom.bus.mq.DataStromConsumer;
import dataStrom.bus.mq.MQCmd;
import dataStrom.bus.mq.Message;
import dataStrom.bus.mq.MqAdmin;
import dataStrom.bus.mq.RPCCode;
import dataStrom.bus.mq.Shared;

/**
 * @author jinyu
 *RPC����
 *�����������
 */
public class dataStrombusRPC  implements MqAdmin{

    private Thread mqThread;//���������߳�
    private Thread monitorThread;//��������߳�
    private final long timeout=30*1000;//30��
    private  ConcurrentHashMap<String,String>  hashRPCType=new ConcurrentHashMap<String,String>();
    /*
     * RPC��ַ��Ϣ
     */
    private   ConcurrentHashMap<String,RPCRegister> hashRPC=new ConcurrentHashMap<String,RPCRegister>();

/*
 * ����
 */
    private ConcurrentHashMap<String,RPCData> hashMQ=new ConcurrentHashMap<String,RPCData>();

/*
 * ���ü���
 */
    private ConcurrentHashMap<String,MonitorRPC> hashMonitor=new ConcurrentHashMap<String,MonitorRPC>();
    public dataStrombusRPC()
    {
    start();
    startMonitor();
    }

    /**
     * RPC����������������
     */
public int queryLen(String name)
{
	RPCData  datamq=	hashMQ.get(name);
	if(datamq==null)
	{
		return 0;
	}
	return datamq.getSize();
}

@Override
public void add(String name, byte[] data) {
//2�����ݣ�1��������2 �ǵ��ü��ӻ�ִ
    Heart heart=new Heart();
    heart.convertToModel(data);
 //
    if(heart.taskid<0)
    {
    RPCData  datamq= hashMQ.get(name);
    if(datamq==null)
    {
        datamq=new RPCData();
        hashMQ.put(name, datamq);
    }
     PRCAddress packaget=new PRCAddress();
     packaget.address=heart.address;
     packaget.name=heart.name;
     packaget.time=System.currentTimeMillis();
     datamq.add(packaget);
    }
    else
    {
        //����
       MonitorRPC monitorRPC=hashMonitor.get(heart.name);
       if(monitorRPC==null)
       {
           //û�н��������״̬
           return ;
       }
       MonitorSeq  monitor=  monitorRPC.find(heart.taskid);
       if(monitor==null)
       {
           //û�н��������״̬
           return;
       }
       else
       {
           //����
           monitor.result=heart.result;
           monitor.time=System.currentTimeMillis();
       }
    }
}
/**
 * ��ȡ���ڵķ�������
 */

@Override
public String[] queryMQ() {
    int size=hashRPC.size();//������������ע��
    int index=0;
    String[] names=new String[size];
    for(String name:hashRPC.keySet())
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

/**
 * û��ʹ��
 * @
 */
@Override
public byte[] getMQMsg(String name) {
    //û��ʹ��
//    RPCData  datamq=  hashMQ.get(name);
//    if(datamq==null)
//    {
//        return null;
//    }
//    
//    return datamq.remove();
    return null;
}

/*
 * ��ȡ����
 */
private PRCAddress  getMQHeart(String name)
{
  RPCData  datamq=  hashMQ.get(name);
  if(datamq==null)
  {
      return null;
  }
  
  return datamq.remove();
}

/**
 * ��ѯע�����
 */
@Override
public String[] queryCustomer(String name) {
   //������ַ,
    RPCRegister info=  hashRPC.get(name);
    if(info!=null)
    {
      return   info.getConsumers();
    }
    return null;
}
@Override
public synchronized void addCustomer(String name, DataStromConsumer customer) {
    //��������
    
    RPCRegister info=  hashRPC.get(name);
    if(info==null)
    {
        info=new RPCRegister();
        hashRPC.put(name, info);
    }
    info.add(customer);
}

/**
 * ��ȡRPC
 */
public  DataStromConsumer getRPCConsumers(String name)
{
    RPCRegister info=hashRPC.get(name); 
   String  rpcType=  hashRPCType.get(name);
   if("0".equals(rpcType))
   {
       //���ؾ���
      List<DataStromConsumer> list= info.getAllConsumer();
       Shared<DataStromConsumer> share=new Shared<DataStromConsumer>(list);
       DataStromConsumer consumer= share.getOne();
       if(consumer.lifecycle)
       {
           return consumer;
       }
       else
       {
           list.remove(consumer);
       }
   }
   else
   {
       //���Ӹ���
       
   }
    return null;
}

/**
 * ����������
 * @param name ��������
 * @param monitor ���Ӷ���
 */
public void addMonitor(String name, MonitorSeq monitor)
{
    //����
    MonitorRPC monitorRPC=   hashMonitor.get(name);
   if(monitorRPC==null)
   {
       monitorRPC=new MonitorRPC();
       hashMonitor.put(name, monitorRPC);
   }
    monitorRPC.add(monitor);
}
/**
 * ���RPC��
 * @param name ��������
 * @param monitor ���Ӷ���
 */
public void addRPCType(String name, String type)
{
    //����
    hashRPCType.put(name, type);
    
}
/*
 * �����������
 */
private synchronized void startMonitor()
{
    if(monitorThread==null)
    {
        monitorThread=new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                int num=0;
                while(true)
                {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e1) {
                      
                        e1.printStackTrace();
                    }//
                    num=0;
                    String[] mqs=queryMQ();//��ȡ����������
                    for(int i=0;i<mqs.length;i++)
                    {
                       
                            MonitorRPC list=  hashMonitor.get(mqs[i]);//��ȡ������
                            if(list!=null)
                            {
                               //��鳬ʱ���쳣
                               //ÿ�����ȡ��һ�����
                                MonitorSeq seq=  list.remove();
                                if(seq.result==RPCCode.sucess)
                                {
                                    Heart heart=new Heart();
                                    heart.address=seq.address;
                                    heart.name=mqs[i];
                                    heart.result=RPCCode.sucess;
                                    heart.taskid=seq.taskid;
                                    heart.rpcID=seq.id;
                                    Message msg=new Message();
                                    msg.setMQ(mqs[i]);
                                    msg.cmd=MQCmd.RPCMonitor;
                                    msg.setBody(heart.convertToData());
                                    seq.session.writeAndFlush(msg);
                                }
                                else
                                {
                                    if(System.currentTimeMillis()-seq.time>timeout)
                                    {
                                        //������״̬
                                        ArrayList<DataStromConsumer> listRPC=  hashRPC.get(mqs[i]).getAllConsumer();
                                        for(int j=0;j<listRPC.size();j++)
                                        {
                                            if(listRPC.get(j).rpcid==seq.id)
                                            {
                                                if(listRPC.get(j).lifecycle)
                                                {
                                                    //���������޸ļ���״̬
                                                    seq.time=System.currentTimeMillis();
                                                    list.add(seq);
                                                }
                                                else
                                                {
                                                    Heart heart=new Heart();
                                                    heart.address=seq.address;
                                                    heart.name=mqs[i];
                                                    heart.result=RPCCode.outTime;
                                                    heart.taskid=seq.taskid;
                                                    heart.rpcID=seq.id;
                                                    Message msg=new Message();
                                                    msg.setMQ(mqs[i]);
                                                    msg.cmd=MQCmd.RPCMonitor;
                                                    msg.setBody(heart.convertToData());
                                                    seq.session.writeAndFlush(msg);
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        list.add(seq);
                                    }
                                }
                                //
                                if(list.isEmpty())
                                {
                                    num++;
                                }
                            }
                            else
                            {
                                num++;
                            }
                        
                    }
                    if(num==mqs.length)
                    {
                        //˵��û������
                        try {
                         TimeUnit.SECONDS.sleep(10);
                        
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                    }
                }
            }
            
        });
        monitorThread.setDaemon(true);
        monitorThread.setName("MonitorTask");
        monitorThread.start();
    }
}

/*
 * �����������
 */
private synchronized void start()
{
    if(mqThread==null)
    {
        mqThread=new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                int num=0;
                while(true)
                {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e1) {
                      
                        e1.printStackTrace();
                    }
                    num=0;
                    String[] mqs=queryMQ();
                    for(int i=0;i<mqs.length;i++)
                    {
                        PRCAddress data=  getMQHeart(mqs[i]);
                        if(data==null)
                        {
                            num++;
                        }
                        else {
                            RPCRegister info=  hashRPC.get(mqs[i]);
                            if(info!=null)
                            {
                                info.updatekeepalive(data.address);
                            }
                          
                        }
                    }
                    if(num==mqs.length)
                    {
                        //˵��û������
                        try {
                         TimeUnit.SECONDS.sleep(10);
                        
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                    }
                }
            }
            
        });
        mqThread.setDaemon(true);
        mqThread.setName("rpcheart");
        mqThread.start();
    }
}
@Override
public DataStromConsumer[] queryClientCustomer(String name) {
    RPCRegister list=  hashRPC.get(name);
    if(list!=null)
    {
       return list.getArrayConsumer();
    }
    return null;
}

}
