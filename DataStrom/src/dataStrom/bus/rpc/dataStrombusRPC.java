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
 *RPC心跳
 *另外的是数据
 */
public class dataStrombusRPC  implements MqAdmin{

    private Thread mqThread;//心跳处理线程
    private Thread monitorThread;//任务监视线程
    private final long timeout=30*1000;//30秒
    private  ConcurrentHashMap<String,String>  hashRPCType=new ConcurrentHashMap<String,String>();
    /*
     * RPC地址信息
     */
    private   ConcurrentHashMap<String,RPCRegister> hashRPC=new ConcurrentHashMap<String,RPCRegister>();

/*
 * 心跳
 */
    private ConcurrentHashMap<String,RPCData> hashMQ=new ConcurrentHashMap<String,RPCData>();

/*
 * 调用监视
 */
    private ConcurrentHashMap<String,MonitorRPC> hashMonitor=new ConcurrentHashMap<String,MonitorRPC>();
    public dataStrombusRPC()
    {
    start();
    startMonitor();
    }

    /**
     * RPC的数据是心跳数据
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
//2类数据，1是心跳，2 是调用监视回执
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
        //监视
       MonitorRPC monitorRPC=hashMonitor.get(heart.name);
       if(monitorRPC==null)
       {
           //没有建立起监视状态
           return ;
       }
       MonitorSeq  monitor=  monitorRPC.find(heart.taskid);
       if(monitor==null)
       {
           //没有建立起监视状态
           return;
       }
       else
       {
           //更新
           monitor.result=heart.result;
           monitor.time=System.currentTimeMillis();
       }
    }
}
/**
 * 获取存在的服务名称
 */

@Override
public String[] queryMQ() {
    int size=hashRPC.size();//服务名称来自注册
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
 * 没有使用
 * @
 */
@Override
public byte[] getMQMsg(String name) {
    //没有使用
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
 * 获取心跳
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
 * 查询注册服务
 */
@Override
public String[] queryCustomer(String name) {
   //发布地址,
    RPCRegister info=  hashRPC.get(name);
    if(info!=null)
    {
      return   info.getConsumers();
    }
    return null;
}
@Override
public synchronized void addCustomer(String name, DataStromConsumer customer) {
    //发布服务
    
    RPCRegister info=  hashRPC.get(name);
    if(info==null)
    {
        info=new RPCRegister();
        hashRPC.put(name, info);
    }
    info.add(customer);
}

/**
 * 获取RPC
 */
public  DataStromConsumer getRPCConsumers(String name)
{
    RPCRegister info=hashRPC.get(name); 
   String  rpcType=  hashRPCType.get(name);
   if("0".equals(rpcType))
   {
       //负载均衡
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
       //主从复制
       
   }
    return null;
}

/**
 * 添加任务监视
 * @param name 服务名称
 * @param monitor 监视对象
 */
public void addMonitor(String name, MonitorSeq monitor)
{
    //监视
    MonitorRPC monitorRPC=   hashMonitor.get(name);
   if(monitorRPC==null)
   {
       monitorRPC=new MonitorRPC();
       hashMonitor.put(name, monitorRPC);
   }
    monitorRPC.add(monitor);
}
/**
 * 添加RPC类
 * @param name 服务名称
 * @param monitor 监视对象
 */
public void addRPCType(String name, String type)
{
    //监视
    hashRPCType.put(name, type);
    
}
/*
 * 启动任务监视
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
                    String[] mqs=queryMQ();//获取到服务名称
                    for(int i=0;i<mqs.length;i++)
                    {
                       
                            MonitorRPC list=  hashMonitor.get(mqs[i]);//获取到监视
                            if(list!=null)
                            {
                               //检查超时，异常
                               //每类服务取出一个检查
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
                                        //检查服务状态
                                        ArrayList<DataStromConsumer> listRPC=  hashRPC.get(mqs[i]).getAllConsumer();
                                        for(int j=0;j<listRPC.size();j++)
                                        {
                                            if(listRPC.get(j).rpcid==seq.id)
                                            {
                                                if(listRPC.get(j).lifecycle)
                                                {
                                                    //服务存活则修改监视状态
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
                        //说明没有数据
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
 * 启动心跳检查
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
                        //说明没有数据
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
