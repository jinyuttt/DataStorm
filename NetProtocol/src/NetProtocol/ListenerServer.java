/**    
 * 文件名：ListenerServer.java    
 *    
 * 版本信息：    
 * 日期：2017年6月11日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package NetProtocol;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
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
 * 项目名称：NetProtocol    
 * 类名称：ListenerServer    
 * 类描述：    监听全部接收数据
 * 创建人：jinyu    
 * 创建时间：2017年6月11日 下午11:03:32    
 * 修改人：jinyu    
 * 修改时间：2017年6月11日 下午11:03:32    
 * 修改备注：    
 * @version     
 *     
 */
public class ListenerServer {
    ConcurrentHashMap<String,SessionMap<Long,Session>> hashOffer=new ConcurrentHashMap<String,SessionMap<Long,Session>>(); 
    private Lock lock = new ReentrantLock();// 锁对象
    private Lock locksession = new ReentrantLock();// 锁对象 
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    HashSet<String> set=new  HashSet<String> ();
    private int  outTime=20;//处理数据返回时超时接收
  //  ConcurrentHashMap<Integer,NetDataPackaget> recDataMap=new   ConcurrentHashMap<Integer,NetDataPackaget>();
    LinkedBlockingQueue<NetDataPackaget> recData=new   LinkedBlockingQueue<NetDataPackaget>();
    @AllowConcurrentEvents
public void  monitorServer(DataModel monitorData)
{
        ReturnCode returnCode=SubNetPackaget.AnalysisNetPackaget(monitorData.data);
        String key=monitorData.srcIP+monitorData.srcPort;
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
    /**
     * 检查接收完整
     */
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
             //recDataMap.put(session.localPort, netData);
             if(session.localPort==-1)
             {
                 try {
                     recData.clear();
                    recData.put(netData);
                } catch (InterruptedException e) {
             
                    e.printStackTrace();
                }
             }
              MessageBus.post(String.valueOf(session.localPort), netData);
            }
              
          });
    }

    /*
     * 直接获取数据
     * 单独使用此接口 port=-1
     */
    public NetDataPackaget getNetData(int port)
    {
        //NetDataPackaget data= recDataMap.remove(port);
        try {
            NetDataPackaget data= recData.poll(outTime, TimeUnit.SECONDS);
              recData.clear();
              return data;
        } catch (InterruptedException e) {
        }
        return null;
    }

}
 