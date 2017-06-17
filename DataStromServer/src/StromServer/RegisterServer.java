/**    
 * �ļ�����RegisterServer.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��10��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package StromServer;

import com.google.common.eventbus.AllowConcurrentEvents;

import CacheDataReset.CacheOrg;
import ComModel.YWModel;
import Config.CenterConfig;
import EventBus.MessageBus;
import FactoryPackaget.NetDataPackaget;
import JNetSocket.UDPClient;
import JNetSocket.UDPServerSocket;
import ListernerServer.ListenerRequest;
import Util.DataPackaget;
import Util.FactoryPackaget;
import Util.IDataPackaget;
import Util.ServerInfo;

/**    
 *     
 * ��Ŀ���ƣ�DataStromServer    
 * �����ƣ�RegisterServer    
 * �������������Է���ע��
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����3:25:33    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����3:25:33    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class RegisterServer {
  
    /**
     * ע�������Ϣ
     */
public boolean register(ServerInfo info)
{
    //
    boolean r=startServer(info);
    //
    if(r)
    {
       UDPClient client=new UDPClient();
       FactoryPackaget f=new FactoryPackaget();
       byte[]data=f.unDataModel(info);
       client.sendData(CenterConfig.masterCenter.IP, CenterConfig.masterCenter.port, data);
       CacheOrg.put(info.serverName, client, data, CenterConfig.masterCenter.IP, CenterConfig.masterCenter.port);
       MessageBus.register(String.valueOf(info.port), this);
       //
       ListenerRequest req=new ListenerRequest();
       MessageBus.register(info.serverName, req);;
    }
    return r;
}
@AllowConcurrentEvents
public void recviceData(NetDataPackaget data)
{
   
    //�����ֻ��Ӧ�����
    YWModel model=this.create(data);
    //����ע���Ӧ��server
    //ʵ�ʶ�����ListenerRequest�������Ƕ��ʵ��
    //��ÿ������һ��ListenerRequest����ʵ��
    MessageBus.post(model.data.serverName, model);
}
private YWModel create(NetDataPackaget data)
{
    YWModel  model=new YWModel();
    FactoryPackaget f=new FactoryPackaget();
    IDataPackaget packaget= f.unPackaget(data.netPackaget);
    //�����ֻ��Ӧ�����
    DataPackaget req=(DataPackaget)packaget;
    model.data=req;
    model.netType=data.netType;
    model.socket=data.socket;
    model.srcIP=data.srcIP;
    model.srcPort=data.srcPort;
    model.reqCall=req.reqCall==0?false:true;
    return model;
    
}

/*
 * �����������
 */
private boolean startServer(ServerInfo info)
{
    UDPServerSocket server=new UDPServerSocket();
   boolean r=  server.InitServer(info.IP, info.port);
   return r;
}
}
