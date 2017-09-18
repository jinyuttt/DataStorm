/**
 * 
 */
package dataStrom.bus.proxy;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import dataStrom.bus.mq.MQCmd;
import dataStrom.bus.mq.MQMessage;
import dataStrom.bus.net.Client;
import dataStrom.bus.net.Sync.ResultCallback;

/**
 * @author jinyu
 * ��������
 * ����ͨѶ�ӿ�
 */
public class Tickets<REQ, RES> {
    private static AtomicLong  sessionid=new AtomicLong(0);
    private long id=0;
    private int timeOut=Integer.MAX_VALUE;
    private MessageInvoker<REQ,RES> msgInvoker=new MessageInvoker<REQ,RES>();
    private LinkedBlockingQueue<RES> queue=new LinkedBlockingQueue<RES>();
    private Client client=null;
 //   public String host;
  //  public int port=0;
    ResultCallback<RES> callback=null;
    public Tickets(Client conClient)
    {
         this.client=conClient;
         id=sessionid.getAndIncrement();
    }
    Tickets(Client conClient,int timeout)
    {
        this.client=conClient;
        id=sessionid.getAndIncrement();
        this.timeOut=timeout;
    }
     
     /**
      * ��ȡID
      * @return
      */
     public String getid()
     {
         return String.valueOf(id);
     }
     
     /**
      * ��������
      * @param req
      */
    public void add(REQ req)
    {
        MQMessage msg=msgInvoker.convertMsg(req);
       msg.setId(String.valueOf(id));
       client.sendData(msg.convertToData());
    }
    public void add(REQ req,MQCmd cmd)
    {
        MQMessage msg=msgInvoker.convertMsg(req);
       msg.setId(String.valueOf(id));
       if(cmd!=null)
       msg.cmd=cmd;
       client.sendData(msg.convertToData());
    }
    
    /*
     * ��ȡ����
     */
    public void read()
    {
        MQMessage msg=new MQMessage(client.read());
        RES res=msgInvoker.convertResult(msg);
        if(callback==null)
        {
        queue.offer(res);
        }
        else
        {
            callback.onReturn(res);
        }
    }
    
    /**
     * ��ȡ��������
     * @return
     */
    public RES take()
    {
       try {
            return   queue.poll(timeOut, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
        e.printStackTrace();
       return null;
    }
    }
    
    /**
     * ���÷���
     * @param callback
     */
    public void setCall( ResultCallback<RES> callback)
    {
        this.callback=callback;
    }
    
    /**
     * �ر�ͨѶ
     */
    public void close()
    {
        if(client!=null)
        {
            client.close();
        }
    }
}
