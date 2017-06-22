/**    
 * �ļ�����StateProcess.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��10��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
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
 * ��Ŀ���ƣ�DataStrom    
 * �����ƣ�StateProcess    
 * ��������  ע�����Ľ��յ�״̬����  
 * ���շ���˵ģ����ڿ��Ʒ���
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����3:05:16    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����3:05:16    
 * �޸ı�ע��    
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
