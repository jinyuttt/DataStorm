/**
 * 
 */
package dataStrom.bus.proxy;
import java.util.concurrent.atomic.AtomicLong;

import dataStrom.bus.mq.MQMessage;
import dataStrom.bus.net.Client;
import dataStrom.bus.net.NetFactory;
import dataStrom.bus.net.Sync.ResultCallback;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * @author jinyu
 *没有使用
 */
public class Ticket<REQ, RES> {
    private static AtomicLong  sessionid=new AtomicLong(0);
    private long id=0;
    private int timeOut=Integer.MAX_VALUE;
    private MessageInvoker<REQ,RES> msgInvoker=new MessageInvoker<REQ,RES>();
    private LinkedBlockingQueue<RES> queue=new LinkedBlockingQueue<RES>();
    public String netType="";
    private Client client=null;
    public String host;
    public int port=0;
    ResultCallback<RES> callback=null;
    Ticket()
    {
        id=sessionid.getAndIncrement();
    }
     Ticket(int timeout)
    {
        id=sessionid.getAndIncrement();
        this.timeOut=timeout;
    }
     
     /**
      * 获取ID
      * @return
      */
     public String getid()
     {
         return String.valueOf(id);
     }
    public void add(REQ req)
    {
        MQMessage msg=msgInvoker.convertMsg(req);
        if(client==null)
        {
            client=NetFactory.createClient(netType);
            client.setHost(host, port);
            client.connect(timeOut);
        }
       msg.setId(String.valueOf(id));
       client.sendData(msg.convertToData());
    }
    public void read()
    {
  
        MQMessage msg=new MQMessage(client.read(),true);
        RES res=msgInvoker.convertResult(msg);
        if(res!=null)
        queue.offer(res);

        
    }
    public RES take()
    {
       try {
            RES res=   queue.poll(timeOut, TimeUnit.SECONDS);
            if(res==null&&timeOut!=Integer.MAX_VALUE)
            {
                client.close();
                client=null;
            }
            return res;
    } catch (InterruptedException e) {
        e.printStackTrace();
       return null;
    }
    }
    public void setCall( ResultCallback<RES> callback)
    {
        this.callback=callback;
    }
    public void close()
    {
        if(client!=null)
        {
            client.close();
        }
    }
}
