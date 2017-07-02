/**    
 * 文件名：registerServer.java    
 *    
 * 版本信息：    
 * 日期：2017年6月10日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
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
 * 项目名称：DataStrom    
 * 类名称：registerServer    
 * 类描述：   注册接收服务端，客户端处理请求
 * 创建人：jinyu    
 * 创建时间：2017年6月10日 上午1:36:24    
 * 修改人：jinyu    
 * 修改时间：2017年6月10日 上午1:36:24    
 * 修改备注：    
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
        IDataPackaget packaget= factory.unPackaget(r.data);//需要的是真实数据
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
           msg.addmsg("收到服务："+model.name);
           msg.addmsg("IP:"+model.IP);
           msg.addmsg("服务标识："+model.falge);
          MessageBus.post("LogInfo", msg);
            
        }
        else if(packaget.packagetType==1)
        {
            ServerState state=(ServerState)packaget;
            //传给状态服务端
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
                //替换直接来源
                ServerBus.objSocket.put(String.valueOf(packaget.sessionid),addr );
            }
            //传给请求处理
            MessageBus.post("req", req);
            LogMsg msg=new LogMsg();
            msg.level=0;
            msg.msg="收到客户端请求";
            MessageBus.post("LogInfo", msg);
            
        }
        else if(packaget.packagetType==3)
        {
            RspPackaget rsp=(RspPackaget)packaget;
            
            //传给返回处理
            MessageBus.post("rsp", rsp);
        }
        else if(packaget.packagetType==5)
        {
            //
            StromCenterModel state=(StromCenterModel)packaget;
            //传给返回处理
            MessageBus.post("stromState", state);
            ServerBus.objSocket.remove(String.valueOf(state.sessionid));
        }
        if(packaget.packagetType==4)
        {
            //其它数据(预留)
            
        }
        if(packaget.packagetType==6)
        {
            //
            MasterModel state=(MasterModel)packaget;
            //传给返回处理
            MessageBus.post("master", state);
            //ServerBus.objSocket.remove(String.valueOf(state.sessionid));
        }
        if(packaget.packagetType==7)
        {
            //
            StromCenterModel state=(StromCenterModel)packaget;
            //传给返回处理
            MessageBus.post("stromserverinfo", state);
           // ServerBus.objSocket.remove(String.valueOf(state.sessionid));
        }
        
        
    }
}
