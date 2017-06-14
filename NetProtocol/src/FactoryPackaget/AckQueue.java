/**    
 * 文件名：AckQueue.java    
 *    
 * 版本信息：    
 * 日期：2017年6月12日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package FactoryPackaget;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import JNetSocket.UDPClient;
import RecviceData.AckCache;
import RecviceData.AckPackaget;

/**    
 *     
 * 项目名称：NetProtocol    
 * 类名称：AckQueue    
 * 类描述： 定时发送ACK
 * 接收清除session
 * 创建人：jinyu    
 * 创建时间：2017年6月12日 下午9:44:41    
 * 修改人：jinyu    
 * 修改时间：2017年6月12日 下午9:44:41    
 * 修改备注：    
 * @version     
 *     
 */
public class AckQueue {
 private  static short maxNum=10;
 public static boolean  isRuning=true;
 private static LinkedBlockingQueue<AckCache> ackQueue=new LinkedBlockingQueue<AckCache>();
 private static LinkedBlockingQueue<AckCache>  freeQueue=new LinkedBlockingQueue<AckCache>();
 static LinkedTransferQueue<Long> handOffer=new LinkedTransferQueue<Long>();
 private static Thread ackReset=null;//检查线程
 private static ReentrantLock lock=new ReentrantLock();//控制线程启动
 
 /**
  * 添加完成的sessonid
  */
 public   static boolean addEndSession(long id)
 {
  try {
    return    handOffer.tryTransfer(id, 10, TimeUnit.MILLISECONDS);
} catch (InterruptedException e) {
    e.printStackTrace();
}
return false;
 }
 
 /*
  * 添加对象
  */
 public static void put(AckPackaget ack, UDPClient client)
 {
     AckCache cache=new AckCache();
     cache.ack=ack;
     cache.client=client;
     startThread();
     try {
      boolean r=ackQueue.offer(cache, 100,TimeUnit.MILLISECONDS );
      if(!r)
      {
          for(int i=0;i<2000;i++)
          {
              //直接清除1000个，并且发送过多次
              AckCache e=ackQueue.poll();
              if(e.ackNum<maxNum/2)
              {
                  ackQueue.offer(e);
              }
          }
          ackQueue.offer(cache);
      }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
 }

 /*
  * 启动线程发送ack
  */
 private static void startThread()
 {
     if(ackReset==null)
     {
         lock.lock();
         try
         {
         if(ackReset==null)
         {
             ackReset=new   Thread(new Runnable()
                     {
                        @Override
                        public void run() {
                            int count=0;
                            Long  sessionid=null;
                           while(isRuning)
                           {
                            try {
                                if(count>10000)
                                {
                                    Thread.sleep(100);//10w次运算
                                    count=0;
                                }
                                //
                                if(ackQueue.isEmpty())
                                {
                                    sessionid=handOffer.poll(100, TimeUnit.MILLISECONDS);
                                    //已经为空
                                    Thread.sleep(1000);
                                    //互换
                                    if(!freeQueue.isEmpty())
                                    {
                                    LinkedBlockingQueue<AckCache> tmp=ackQueue;
                                    ackQueue= freeQueue;
                                    ackQueue=tmp;
                                    }
                                }
                               AckCache e=ackQueue.take();
                               if(sessionid!=null&&e.ack.sessionid==sessionid.longValue())
                               {
                                   //已经不需要发送了；
                                   continue;
                               }
                               if(!e.client.isClose())
                               {
                                  //如果外部真正关闭socket则不发送了
                                  //逻辑上收到了接收方的接收完成信息
                                byte[] ackbytes=  SubNetPackaget.createAckPackaget(e.ack);
                                e.client.sendData(e.srcIP, e.srcPort, ackbytes);
                                e.ackNum++;
                               if(e.ackNum<maxNum)
                               {
                                   //外部没有关闭
                                  // ackQueue.offer(e);
                                   freeQueue.offer(e);
                               }
                               count++;
                               }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            
                           }
                        }
                 
                     });
             ackReset.setDaemon(true);
             ackReset.setName("sendAckThread");
             ackReset.start();
                 
             }
         }
         finally
         {
             lock.unlock();
         }
     }
     ackQueue.clear();
     freeQueue.clear();
 }

}
