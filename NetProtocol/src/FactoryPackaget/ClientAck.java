/**    
 * �ļ�����ClientAck.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��12��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package FactoryPackaget;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import CacheDataReset.CacheModel;
import CacheDataReset.DataCacheBus;
import DataBus.CacheData;
import JNetSocket.UDPClient;
import NetPackaget.PackagetRandom;
import NetProtocol.judpClient;

/**    
 *     
 * ��Ŀ���ƣ�NetProtocol    
 * �����ƣ�ClientAck    
 * ����������ȡ���ص�ACk
 * �����ر�socket;
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��12�� ����10:45:52    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��12�� ����10:45:52    
 * �޸ı�ע��    
 * @version     
 *     
 */
 public class ClientAck {
 private static   ExecutorService cachedThreadPool = Executors.newCachedThreadPool();  
 /*
  * Ψһ���
  */
 private static  HashSet<UDPClient> ackClient = new HashSet<UDPClient>();
 /*
  * ���Ӷ���
  */
private static ReferenceQueue<judpClient>  freQueue = new ReferenceQueue<judpClient>();  

/*
 * �ж�ʹ�����
 */
private static ConcurrentHashMap<WeakReference<judpClient>,UDPClient> listenerClient=new ConcurrentHashMap< WeakReference<judpClient>,UDPClient>();
private  volatile static boolean isStart=true;

//�����߼��رյ�socket;
private static  CacheData<Long,UDPClient> closeCache=new CacheData<Long,UDPClient>(100, 60, false);

 /**
   * ���ӻ�ȡack
   */
  public static void put(judpClient curClient,UDPClient client) 
{
      
    if(ackClient.add(client))
    {
        //������freQueue��������
        WeakReference<judpClient> wr = new WeakReference<judpClient>(curClient,freQueue);
        listenerClient.put(wr, client);
        if(isStart)
        {
            isStart=false;
            startThread();
            CacheClientListenter listener=new CacheClientListenter();
            closeCache.setListenter(listener);
        }
        cachedThreadPool.execute(new Runnable(){

            @Override
            public void run() {
                while(true)
                {
                try
                {
               byte[] ack=client.getCallBackData();
               ReturnCode code=SubNetPackaget.AnalysisNetPackaget(ack);
               if(code.isAck)
                {
                   int num=code.packagetNum;
                   String key=String.valueOf(code.ackPackaget.sessionid)+String.valueOf(code.ackPackaget.packagetID);
                   CacheModel model=DataCacheBus.getInstance().get(key);
                  if(code.ackPackaget.ackType==1)
                  {
                      //��session���а����
                      AckQueue.addEndSession(code.ackPackaget.sessionid);
                      DataCacheBus.getInstance().remove(key);
                      //�����Ժ��ж��Ƿ��߼��ر���
                      for(int i=0;i<num;i++)
                      {
                          key=String.valueOf(code.ackPackaget.sessionid)+String.valueOf(i);
                          DataCacheBus.getInstance().remove(key);
                      }
                     
                  }
                  else if(code.ackPackaget.ackType==2)
                  {
                      model.client.sendData(model.remoteHost, model.remotePort, model.data);
                  }
                }
               if(client.isClose())
               {
                   //�Ѿ��߼��ر�
                   ackClient.remove(client);
               }
                }
                catch(Exception ex)
                {
                    if(client.isClose())
                    {
                        ackClient.remove(client);
                    }
                    return;
                }
                }
            }
            
        });
    }
}

  /**
   * ���ͻ��˻���
   */
  private static  void startThread()
  {
      Thread  jclient=new Thread(new Runnable(){

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
      while(true)
      {
          try {
              //�����յ�����
            WeakReference<judpClient> wr =(WeakReference<judpClient>) freQueue.remove();
            judpClient cl=wr.get();
            if(cl!=null)
            {
                //�����û�л���Ҳ�߼��رգ���������)
                wr.get().close();
            }
            //  ������judpClient������رջ���
            UDPClient tmp=listenerClient.remove(wr);
            long id=PackagetRandom.getInstanceID(this);
            closeCache.put(id, tmp);
            ackClient.remove(tmp);
//            else
//            {
//                UDPClient tmp=listenerClient.remove(wr);
//                long id=PackagetRandom.getInstanceID(this);
//                closeCache.put(id, tmp);
//                ackClient.remove(tmp);
//               
//            }
        } catch (InterruptedException e) {
        
            e.printStackTrace();
        }
          
      }
        }
          
      }
      );
      jclient.setDaemon(true);
      jclient.setName("judpClientGC");
      jclient.start();
      //
      Thread  jprotocol=new Thread(new Runnable(){

          @Override
          public void run() {
        while(true)
        {
          
                       //����Ѿ��߼��رյ��ǻ�������
                      //�߼��رյĶ�����������
                     // ��������еط������õ���Ҳû�йرգ���ʱ����������)
                        for(Map.Entry<WeakReference<judpClient>,UDPClient> e: listenerClient.entrySet() ){
                            WeakReference<judpClient> wr=e.getKey();
                            judpClient client=wr.get();
                            if(client!=null)
                            {
                              if(client.isClose())
                              {
                                  //�߼��ر��ˣ�����رջ��滺��
                                  long id=PackagetRandom.getInstanceID(this);
                                  closeCache.put(id, e.getValue());
                                  ackClient.remove(e.getValue());
                              }
                            }
                        }
       
            
        }
          }
            
        }
        );
      jprotocol.setDaemon(true);
      jprotocol.setName("judpClientClose");
      jprotocol.start();
  }
 }