/**    
 * �ļ�����CenterStart.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��15��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package RecServer;

import Config.CenterConfig;
import DataProcess.BusRegister;
import EventBus.MessageBus;
import JNetSocket.MulticastServerSocket;
import JNetSocket.UDPServerSocket;
import StromModel.ConfigModel;

/**    
 *     
 * ��Ŀ���ƣ�DataStrom    
 * �����ƣ�CenterStart    
 * ��������   �������� 
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��15�� ����12:22:17    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��15�� ����12:22:17    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class CenterStart {
    
    /**
     * ��������
     */
public void start()
{
    //������������
    registerServer  register=new registerServer();
    UDPServerSocket server=new UDPServerSocket();
    server.InitServer(CenterConfig.localCenter.IP, CenterConfig.localCenter.port);
    MessageBus.register("udp", register);
    
    MulticastServerSocket m_server=new MulticastServerSocket();
    m_server.InitServer(CenterConfig.localCenter.multIP, CenterConfig.localCenter.multPort);
    MessageBus.register("udpMultcast", register);
    
    //ע��������ҵ��
    BusRegister busregister=new BusRegister();
    busregister.start();
    //��������
    CenterTimer.startMasterThread();
    CenterTimer.startServerThread();
    //���ڵ�
    initLocalNode();
    
}
/*
 * 
 */
private void  initLocalNode()
{
    ConfigModel localNode=new ConfigModel();
    localNode.centerByte=2;
    localNode.intflage=RandomFlage.getFlage();
    localNode.flage=String.valueOf( localNode.intflage);
    CenterConfig.localCenter=localNode;
    CenterTimer.addMaster(localNode);//ѡ���Լ�
  
}
}
