/**    
 * 文件名：StateProcess.java    
 *    
 * 版本信息：    
 * 日期：2017年6月10日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package DataProcess;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

import DataStrom.ServerBus;
import StromModel.ServerModel;
import StromModel.StromServerNode;
import Util.ServerState;

/**    
 *     
 * 项目名称：DataStrom    
 * 类名称：StateProcess    
 * 类描述：  注册中心接收到状态数据  
 * 接收服务端的，用于控制服务
 * 创建人：jinyu    
 * 创建时间：2017年6月10日 上午3:05:16    
 * 修改人：jinyu    
 * 修改时间：2017年6月10日 上午3:05:16    
 * 修改备注：    
 * @version     
 *     
 */
public class StateProcess {
    @AllowConcurrentEvents
    @Subscribe
public void recState(ServerState state)
{
        ServerModel server=new ServerModel();
        server.IP=state.srcIP;
        server.port=state.srcPort;
        server.name=state.serverName;
        server.falge=state.flage;
        server.master_slave=state.master_slave;
        server.isMaster=state.isMaster;
        StromServerNode node=ServerBus.serverList.get(state.serverName);
        if(node!=null)
        {
           
            node.update(server);
        }
        else
        {
             node=new StromServerNode();
             node.addServer(server, true);
        }
}
}
