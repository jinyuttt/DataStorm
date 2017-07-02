/**    
 * �ļ�����ListenerReq.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��11��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package Client;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

import NetProtocol.judpClient;
import Util.FactoryPackaget;
import Util.IDataPackaget;
import Util.ReqPackaget;
import Util.RspPackaget;

/**    
 *     
 * ��Ŀ���ƣ�DataStromClient    
 * �����ƣ�ListenerReq    
 * ��������    
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��11�� ����4:07:45    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��11�� ����4:07:45    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class ListenerReq {
     ExecutorService  cachePool= Executors.newCachedThreadPool();
     FactoryPackaget f=new FactoryPackaget();
     LinkedBlockingQueue<RspPackaget> data=new LinkedBlockingQueue<RspPackaget>();
     @Subscribe
    @AllowConcurrentEvents
public void  listenerClient(IDataPackaget packaget)
{
        judpClient client=new judpClient();
        byte[] data=f.unDataModel(packaget);
        client.sendData(ClientMaster.masterIP, ClientMaster.port, data);
        if(ClientMaster.isBack)
        {
             startListern(client);
             ClientMaster.isBack=false;
        }
        if(ClientMaster.random.getWeightCategory().equals("wc1"))
        {
            ClientMaster.isBack=true;
        }
        if(packaget.packagetType==2)
        {
            ReqPackaget req=(ReqPackaget)packaget;
            if(req.reqType==1)
            {
                waitCallData(client);
            }
        }
}
    /*
     * �ȴ����ݷ���
     */
private void  waitCallData(judpClient client)
{
     byte[]callData=client.getCallBackData();//ֱ�ӽ��յ����ݣ�����װ��
     if(callData!=null)
     {
     IDataPackaget packaget= f.unPackaget(callData);
     RspPackaget resp=(RspPackaget)packaget;
     data.clear();
     data.offer(resp);
     }
     else
     {
         RspPackaget resp=new RspPackaget();
         resp.serverName="";
         data.clear();
         data.offer(resp);
     }
}

/**
 * ��������
 */
public  RspPackaget  getCall()
{
    try {
        return data.take();
    } catch (InterruptedException e) {
      return null;
    }
}
    
    /**
     * ���ջ�ִ��master��ַ
     */
 private void startListern( judpClient client)
 {
     cachePool.execute(new Runnable(){

        @Override
        public void run() {
        // client..
            byte[] data=client.getCallBackData();
            if(data!=null&&data.length<10)
            {
                //�Ż�
                try
                {
                Method method = client.getClass().getDeclaredMethod("add", byte[].class);
                method.setAccessible(true); //û�����þͻᱨ��
                //���ø÷���
                method.invoke(client, data);
                }
                catch(Exception ex)
                {
                    
                }
               
            }
            else
            {
               if(data==null)
               {
                   //�Ż�
                   try
                   {
                   Method method = client.getClass().getDeclaredMethod("add", byte[].class);
                   method.setAccessible(true); //û�����þͻᱨ��
                   //���ø÷���
                   method.invoke(client, data);
                   }
                   catch(Exception ex)
                   {
                       
                   }
                   return;
               }
            ByteBuffer buf=ByteBuffer.wrap(data);
            //������master��ַ��masteraddr;
            byte[] head=new byte[10];
            buf.get(head);
            String strhead=new String(head);
            if(strhead.equalsIgnoreCase("masteraddr"))
            {
              short len=  buf.getShort();//IP
              byte[]ip=new byte[len];
              buf.get(ip);
              int masterPort= buf.getInt();
              String masterIP=new String(ip);
              
              ClientMaster.masterIP=masterIP;
              ClientMaster.port=masterPort;
            }
            else
            {
                //�Ż�
                try
                {
                Method method = client.getClass().getDeclaredMethod("add", byte[].class);
                method.setAccessible(true); //û�����þͻᱨ��
                //���ø÷���
                method.invoke(client, data);
                }
                catch(Exception ex)
                {
                    
                }
               // client.add(data);
            }
            }
              
        }
         
     });
 }
}
