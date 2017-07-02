/**    
 * �ļ�����registerServer.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��10��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package RecServer;



import java.util.concurrent.locks.ReentrantReadWriteLock;


import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

import DataStrom.ServerBus;
import EventBus.MessageBus;
import FactoryPackaget.ReturnCode;
import FactoryPackaget.SubNetPackaget;
import Model.MasterModel;
import Model.StromCenterModel;
import NetModel.DataModel;
import NetModel.NetAddress;
import NetPackaget.PackagetRandom;
import StromModel.LogMsg;
import StromModel.ServerModel;
import StromModel.StromServerNode;
import Util.FactoryPackaget;
import Util.IDataPackaget;
import Util.ReqPackaget;
import Util.RspPackaget;
import Util.ServerInfo;
import Util.ServerState;

/**    
 *     
 * ��Ŀ���ƣ�DataStrom    
 * �����ƣ�registerServer    
 * ��������   ע����շ���ˣ��ͻ��˴�������
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����1:36:24    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����1:36:24    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class registerServer {
    ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
    @Subscribe
    @AllowConcurrentEvents
public  void recviceData(DataModel data)
{
        NetAddress addr=new NetAddress();
        addr.srcIP=data.srcIP;
        addr.srcPort=data.srcPort;
        FactoryPackaget factory=new FactoryPackaget();
        ReturnCode r=SubNetPackaget.AnalysisNetPackaget(data.data);
        IDataPackaget packaget= factory.unPackaget(r.data);//��Ҫ������ʵ����
        packaget.sessionid =PackagetRandom.getSequeueID();
        //
        ServerBus.objSocket.put(String.valueOf(packaget.sessionid),addr );
        processData(packaget);
}

    private void processData(IDataPackaget packaget)
    {
        if(packaget.packagetType==0)
        {
            ServerInfo info=(ServerInfo) packaget;
            ServerModel model=new ServerModel();
            model.IP=info.IP;
            model.name=info.serverName;
            model.isMaster=info.isMaster;
            model.master_slave=info.master_slave;
            model.port=info.port;
            ServerBus.map.put(model.name, model);
            StromServerNode node=  ServerBus.serverList.get(model.name);
          if(node==null)
          {
              lock.writeLock().lock();
              if(node==null)
              {
                 node=new StromServerNode();
                 ServerBus.serverList.put(model.name, node);
              }
               node.addServer(model, true);
               lock.writeLock().unlock();
          }
          LogMsg msg=new LogMsg();
          msg.level=0;
           msg.addmsg("�յ�����"+model.name);
           msg.addmsg("IP:"+model.IP);
           msg.addmsg("�����ʶ��"+model.falge);
          MessageBus.post("LogInfo", msg);
            
        }
        else if(packaget.packagetType==1)
        {
            ServerState state=(ServerState)packaget;
            //����״̬�����
            MessageBus.post("state", state);
            ServerBus.objSocket.remove(String.valueOf(state.sessionid));
            
        }
        else if(packaget.packagetType==2)
        {
            ReqPackaget req=(ReqPackaget)packaget;
            if(!req.srcAddr.isEmpty())
            {
                String[] src=req.srcAddr.split("#");

                NetAddress addr=new NetAddress();
                addr.srcIP=src[0];
                addr.srcPort=Integer.valueOf(src[1]);
                //�滻ֱ����Դ
                ServerBus.objSocket.put(String.valueOf(packaget.sessionid),addr );
            }
            //����������
            MessageBus.post("req", req);
            LogMsg msg=new LogMsg();
            msg.level=0;
            msg.msg="�յ��ͻ�������";
            MessageBus.post("LogInfo", msg);
            
        }
        else if(packaget.packagetType==3)
        {
            RspPackaget rsp=(RspPackaget)packaget;
            
            //�������ش���
            MessageBus.post("rsp", rsp);
        }
        else if(packaget.packagetType==5)
        {
            //
            StromCenterModel state=(StromCenterModel)packaget;
            //�������ش���
            MessageBus.post("stromState", state);
            ServerBus.objSocket.remove(String.valueOf(state.sessionid));
        }
        if(packaget.packagetType==4)
        {
            //��������(Ԥ��)
            
        }
        if(packaget.packagetType==6)
        {
            //
            MasterModel state=(MasterModel)packaget;
            //�������ش���
            MessageBus.post("master", state);
            //ServerBus.objSocket.remove(String.valueOf(state.sessionid));
        }
        if(packaget.packagetType==7)
        {
            //
            StromCenterModel state=(StromCenterModel)packaget;
            //�������ش���
            MessageBus.post("stromserverinfo", state);
           // ServerBus.objSocket.remove(String.valueOf(state.sessionid));
        }
        
        
    }
}
