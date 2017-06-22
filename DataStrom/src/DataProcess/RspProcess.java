/**    
 * �ļ�����RspProcess.java    
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
import NetModel.NetAddress;
import NetProtocol.judpClient;
import Util.FactoryPackaget;
import Util.RspPackaget;

/**    
 *     
 * ��Ŀ���ƣ�DataStrom    
 * �����ƣ�RspProcess    
 * ��������   ע�����Ľ��յ���ִ���� 
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����3:07:41    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����3:07:41    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class RspProcess {
    FactoryPackaget f=new FactoryPackaget();
    @AllowConcurrentEvents
    @Subscribe
public void recRsponse(RspPackaget rsp)
{
    //�յ������ִ
    //ת���ͻ���
     NetAddress netcall=     ServerBus.objSocket.getByKey(String.valueOf(rsp.sessionid));
     if(netcall!=null)
     {
       byte[]data=f.unDataModel(rsp);
        judpClient client=new judpClient();
       client.sendData(netcall.srcIP, netcall.srcPort, data);
     }
}
}
