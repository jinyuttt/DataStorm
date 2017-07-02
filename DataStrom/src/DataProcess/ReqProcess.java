/**    
 * �ļ�����ReqProcess.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��10��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package DataProcess;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

import Config.CenterConfig;
import DataStrom.ServerBus;
import EventBus.MessageBus;
import NetModel.NetAddress;
import NetPackaget.PackagetRandom;
import NetProtocol.judpClient;
import StromModel.LogMsg;
import StromModel.ServerModel;
import Util.DataPackaget;
import Util.FactoryPackaget;
import Util.ReqPackaget;
import Util.RspPackaget;
import Util.ServerInfo;

/**    
 *     
 * ��Ŀ���ƣ�DataStrom    
 * �����ƣ�ReqProcess    
 * ��������    ע�����Ľ��յ��ͻ��˵�����
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����3:06:31    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����3:06:31    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class ReqProcess {
    private FactoryPackaget f=new FactoryPackaget();
    private StromCenterBiil  biil=new StromCenterBiil();
    private   ConcurrentLinkedQueue<ReqPackaget> cache=new ConcurrentLinkedQueue<ReqPackaget>();
  private  ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
  private  volatile boolean isRuning=false;
  @Subscribe
    @AllowConcurrentEvents
public void recRequest(ReqPackaget req)
{
     NetAddress addr= null;
     try
     {
         addr=ServerBus.objSocket.getByKey(String.valueOf(req.sessionid));
     }
     catch(Exception ex)
     {
         System.out.println("û�л�ȡ�������ַ");
     }
    if(CenterConfig.localCenter.centerByte==1)
    {
        //����Լ���master,����
     
        if(req.reqType==1)
        {
            //��ȡ�����ַ
           //
            ServerModel model= biil.getServerAddr(req.serverName);
            if(addr==null)
            {
                return ;
            }
            judpClient clientl=new judpClient();
            if(model!=null)
            {
                //ת��������
                    //
                    ServerInfo info=new ServerInfo();
                    info.IP=model.IP;
                    info.port=model.port;
                    info.netType=model.netType;
                    info.serverName=model.name;
                    byte[] tmp=f.unDataModel(info);//������Ϣ���
                    RspPackaget rsp=new RspPackaget();
                    rsp.result=tmp;
                    rsp.serverName=model.name;
                    byte[]data=f.unDataModel(rsp);//��ִ��Ϣ���
                    clientl.sendData(addr.srcIP, addr.srcPort, data);
                    clientl.close();
//                    LogMsg msg=new LogMsg();
//                    msg.level=0;
//                    msg.msg="�յ��ͻ�������";
                 //   MessageBus.post("LogInfo", msg);
                    LogMsg log=new LogMsg();
                    log.level=0;
                    log.msg="���յ������ַ����:"+model.name+"��ַ��"+addr.srcIP+","+ addr.srcPort;
                    MessageBus.getBus("LogInfo").post(log);
            }
            else
            {
              
                ServerInfo info=new ServerInfo();
                info.IP="";
                info.port=0;
                info.netType=0;
                info.serverName="";
                byte[] tmp=f.unDataModel(info);//������Ϣ���
                RspPackaget rsp=new RspPackaget();
                rsp.result=tmp;
                byte[]data=f.unDataModel(rsp);//��ִ��Ϣ���
                clientl.sendData(addr.srcIP, addr.srcPort, data);
                clientl.close();
                LogMsg log=new LogMsg();
                log.level=0;
                log.msg="û���ҵ�������Ϣ���ش���ַ��"+addr.srcIP+","+ addr.srcPort;
                MessageBus.getBus("LogInfo").post(log);
            }
        }
        else
        {
            //ֱ��ת������
            ServerModel model= biil.getServerAddr(req.serverName);
            judpClient clientl=new judpClient();
            if(model!=null)
            {
                  //ת��������
                    DataPackaget data=new DataPackaget();
                    data.data=req.args;
                    data.sessionid=PackagetRandom.getSequeueID();
                   ServerBus.objSocket.remove(String.valueOf(req.sessionid));//ԭ�������
                   ServerBus.objSocket.put(String.valueOf(data.sessionid),addr);
                    byte[] tmp=f.unDataModel(data);//������Ϣ���
                    clientl.sendData(model.IP, model.port, tmp);
                    clientl.close();
                    LogMsg log=new LogMsg();
                    log.msg="�������ݵ�����:"+model.name;
                    MessageBus.getBus("LogInfo").post(log);
            }
        }
    }
    else
    {
        //ת��master;
        req.srcAddr=addr.srcIP+"#"+addr.srcPort;
        if(CenterConfig.masterCenter!=null&&CenterConfig.masterCenter.action)
        {
            byte[] data=f.unDataModel(req);
            judpClient client=new judpClient();
           client.sendData(CenterConfig.masterCenter.IP, CenterConfig.masterCenter.port, data);
           client.close();
           LogMsg log=new LogMsg();
           log.msg="ת�����ݸ�master";
           log.level=1;
           MessageBus.getBus("LogInfo").post(log);
        }
        else
        {
            cache.add(req);
            //�洢һ��ʱ��
            startThread();
        }
        //
        if(req.rsp==1)
        {
            judpClient clientl=new judpClient();
            //�и���ȷ�ظ�
            ServerInfo info=new ServerInfo();
            info.IP="";
            info.port=0;
            info.netType=0;
            info.serverName="";
            byte[] tmp=f.unDataModel(info);//������Ϣ���
            RspPackaget rsp=new RspPackaget();
            rsp.result=tmp;
            byte[]callData=f.unDataModel(rsp);//��ִ��Ϣ���
            clientl.sendData(addr.srcIP, addr.srcPort, callData);
            clientl.close();
            
        }
    }
    ServerBus.objSocket.remove(String.valueOf(req.sessionid));
}
    
    /*
     * �����̷߳��ͻ�������
     * ���������߳̽���
     */
    private  void startThread()
    {
        if(isRuning)
        {
            return;
        }
        cachedThreadPool.execute(new Runnable(){

            @Override
            public void run() {
                
                judpClient client=new judpClient();
             
             while(isRuning)
             {
                 if(CenterConfig.masterCenter!=null&&CenterConfig.masterCenter.action)
                 {
                     ReqPackaget req=cache.poll();
                     byte[] data=f.unDataModel(req);
                     client.sendData(CenterConfig.masterCenter.IP, CenterConfig.masterCenter.port, data);
                 }
                 else
                 {
                     try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                   
                        e.printStackTrace();
                    }
                 }
                 if(cache.isEmpty())
                 {
                     isRuning=false;
                 }
                     
             }
             client.close();
            }
            
        }); 
    }
}
