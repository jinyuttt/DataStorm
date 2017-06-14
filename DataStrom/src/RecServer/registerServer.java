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



import com.google.common.collect.Multiset;
import com.google.common.eventbus.AllowConcurrentEvents;

import DataStrom.ServerBus;
import DataStrom.ServerModel;
import EventBus.MessageBus;
import Model.StromCenterModel;
import NetModel.DataModel;
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
    @AllowConcurrentEvents
public  void recviceData(DataModel data)
{
        FactoryPackaget factory=new FactoryPackaget();
        IDataPackaget packaget= factory.unPackaget(data.data);
        processData(packaget);
}
    @SuppressWarnings("unchecked")
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
        }
        else if(packaget.packagetType==1)
        {
            ServerState state=(ServerState)packaget;
            //传给状态服务端
            MessageBus.post("state", state);
        }
        else if(packaget.packagetType==2)
        {
            ReqPackaget req=(ReqPackaget)packaget;
            //传给请求处理
            MessageBus.post("req", req);
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
        }
        if(packaget.packagetType==4)
        {
            //服务注册
            ServerInfo tmp=(ServerInfo) packaget;
            //
            ServerModel model=new ServerModel();
           model.IP=tmp.IP;
           model.isMaster=tmp.isMaster;
           model.master_slave=tmp.master_slave;
           model.name=tmp.serverName;
           model.netType=tmp.netType;
           model.port=tmp.port;
           Multiset<ServerModel> set=  (Multiset<ServerModel>) ServerBus.map.get(tmp.serverName);
             set.add(model,1);
            
        }
        
    }
}
