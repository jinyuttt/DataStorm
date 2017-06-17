/**    
 * �ļ�����ListenerServer.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��11��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package NetProtocol;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.google.common.eventbus.AllowConcurrentEvents;

import EventBus.MessageBus;
import FactoryPackaget.NetDataPackaget;
import FactoryPackaget.ReturnCode;
import FactoryPackaget.SessionMap;
import FactoryPackaget.SubNetPackaget;
import NetModel.DataModel;
import RecviceData.Session;


/**    
 *     
 * ��Ŀ���ƣ�NetProtocol    
 * �����ƣ�ListenerServer    
 * ��������    ����ȫ����������
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��11�� ����11:03:32    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��11�� ����11:03:32    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class ListenerServer {
    ConcurrentHashMap<String,SessionMap<Long,Session>> hashOffer=new ConcurrentHashMap<String,SessionMap<Long,Session>>(); 
    private Lock lock = new ReentrantLock();// ������
    private Lock locksession = new ReentrantLock();// ������ 
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    HashSet<String> set=new  HashSet<String> ();
    @AllowConcurrentEvents
public void  monitorServer(DataModel monitorData)
{
        ReturnCode returnCode=SubNetPackaget.AnalysisNetPackaget(monitorData.data);
        String key=monitorData.srcIP+monitorData.localPort;
        SessionMap<Long,Session> single=hashOffer.getOrDefault(key, null);
   
        if(single==null)
        {
         
            lock.lock();
            try
            {
            if(single==null)
            {
                single=new SessionMap<Long,Session>();
                Session session=new Session(returnCode.packagetNum);
                single.put(returnCode.SessionID, session);
                hashOffer.put(key, single);
                session.id=returnCode.SessionID;
                session.netType=monitorData.netType;
                session.socket=monitorData.socket;
                session.srcIP=monitorData.srcIP;
                session.srcPort=monitorData.srcPort;
                session.localIP=monitorData.localIP;
                session.localPort=monitorData.localPort;
                session.addData(returnCode);
                checkRead(session);
            }
            lock.unlock();
            }
            finally
            {
                lock.unlock();
            }
             
        }
        else
        {
            Session session=single.get(returnCode.SessionID);
            if(session==null)
            {
                try
                {
                    locksession.lock();
                    if(session==null)
                    {
                     session=new Session(returnCode.packagetNum);
                     single.put(returnCode.SessionID, session);
                     session.id=returnCode.SessionID;
                     session.netType=monitorData.netType;
                     session.socket=monitorData.socket;
                     session.srcIP=monitorData.srcIP;
                    session.srcPort=monitorData.srcPort;
                    session.localIP=monitorData.localIP;
                    session.localPort=monitorData.localPort;
                    session.addData(returnCode);
                    checkRead(session);
                    }
                    locksession.unlock();
                }
                finally
                {
                    locksession.unlock();
                }
            }
            else
            {
                session.addData(returnCode);
            }
        }
        
        
        
}
    private void checkRead( Session session)
    {
        cachedThreadPool.execute(new Runnable(){
            @Override
            public void run() {
             byte[]  realData=session.read();
             NetDataPackaget  netData=new NetDataPackaget();
             netData.localIP=session.localIP;
             netData.localPort=session.localPort;
             netData.netPackaget=realData;
             netData.netType=session.netType;
             netData.socket=session.socket;
             netData.srcIP=session.srcIP;
             netData.srcPort=session.srcPort;
              MessageBus.post(String.valueOf(session.localPort), netData);
            }
              
          });
    }
}
 