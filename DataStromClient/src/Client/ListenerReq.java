/**    
 * 文件名：ListenerReq.java    
 *    
 * 版本信息：    
 * 日期：2017年6月11日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
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
 * 项目名称：DataStromClient    
 * 类名称：ListenerReq    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2017年6月11日 下午4:07:45    
 * 修改人：jinyu    
 * 修改时间：2017年6月11日 下午4:07:45    
 * 修改备注：    
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
     * 等待数据返回
     */
private void  waitCallData(judpClient client)
{
     byte[]callData=client.getCallBackData();//直接接收的数据，带封装；
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
 * 返回数据
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
     * 接收回执的master地址
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
                //放回
                try
                {
                Method method = client.getClass().getDeclaredMethod("add", byte[].class);
                method.setAccessible(true); //没有设置就会报错
                //调用该方法
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
                   //放回
                   try
                   {
                   Method method = client.getClass().getDeclaredMethod("add", byte[].class);
                   method.setAccessible(true); //没有设置就会报错
                   //调用该方法
                   method.invoke(client, data);
                   }
                   catch(Exception ex)
                   {
                       
                   }
                   return;
               }
            ByteBuffer buf=ByteBuffer.wrap(data);
            //可能是master地址；masteraddr;
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
                //放回
                try
                {
                Method method = client.getClass().getDeclaredMethod("add", byte[].class);
                method.setAccessible(true); //没有设置就会报错
                //调用该方法
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
