/**    
 * �ļ�����AckQueue.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��12��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
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
 * ��Ŀ���ƣ�NetProtocol    
 * �����ƣ�AckQueue    
 * �������� ��ʱ����ACK
 * �������session
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��12�� ����9:44:41    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��12�� ����9:44:41    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class AckQueue {
 private  static short maxNum=10;
 public static boolean  isRuning=true;
 private static LinkedBlockingQueue<AckCache> ackQueue=new LinkedBlockingQueue<AckCache>();
 private static LinkedBlockingQueue<AckCache>  freeQueue=new LinkedBlockingQueue<AckCache>();
 static LinkedTransferQueue<Long> handOffer=new LinkedTransferQueue<Long>();
 private static Thread ackReset=null;//����߳�
 private static ReentrantLock lock=new ReentrantLock();//�����߳�����
 
 /**
  * ������ɵ�sessonid
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
  * ���Ӷ���
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
              //ֱ�����1000�������ҷ��͹����
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
  * �����̷߳���ack
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
                                    Thread.sleep(100);//10w������
                                    count=0;
                                }
                                //
                                if(ackQueue.isEmpty())
                                {
                                    sessionid=handOffer.poll(100, TimeUnit.MILLISECONDS);
                                    //�Ѿ�Ϊ��
                                    Thread.sleep(1000);
                                    //����
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
                                   //�Ѿ�����Ҫ�����ˣ�
                                   continue;
                               }
                               if(!e.client.isClose())
                               {
                                  //����ⲿ�����ر�socket�򲻷�����
                                  //�߼����յ��˽��շ��Ľ��������Ϣ
                                byte[] ackbytes=  SubNetPackaget.createAckPackaget(e.ack);
                                e.client.sendData(e.srcIP, e.srcPort, ackbytes);
                                e.ackNum++;
                               if(e.ackNum<maxNum)
                               {
                                   //�ⲿû�йر�
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