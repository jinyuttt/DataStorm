/**    
 * 文件名：CenterStart.java    
 *    
 * 版本信息：    
 * 日期：2017年6月15日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
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
 * 项目名称：DataStrom    
 * 类名称：CenterStart    
 * 类描述：   启动中心 
 * 创建人：jinyu    
 * 创建时间：2017年6月15日 上午12:22:17    
 * 修改人：jinyu    
 * 修改时间：2017年6月15日 上午12:22:17    
 * 修改备注：    
 * @version     
 *     
 */
public class CenterStart {
    
    /**
     * 中心启动
     */
public void start()
{
    //接收网络数据
    registerServer  register=new registerServer();
    UDPServerSocket server=new UDPServerSocket();
    server.InitServer(CenterConfig.localCenter.IP, CenterConfig.localCenter.port);
    MessageBus.register("udp", register);
    
    MulticastServerSocket m_server=new MulticastServerSocket();
    m_server.InitServer(CenterConfig.localCenter.multIP, CenterConfig.localCenter.multPort);
    MessageBus.register("udpMultcast", register);
    
    //注册请求处理业务
    BusRegister busregister=new BusRegister();
    busregister.start();
    //启动处理
    CenterTimer.startMasterThread();
    CenterTimer.startServerThread();
    //本节点
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
    CenterTimer.addMaster(localNode);//选举自己
  
}
}
