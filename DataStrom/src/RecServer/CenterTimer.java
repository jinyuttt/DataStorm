/**    
 * 文件名：CenterTimer.java    
 *    
 * 版本信息：    
 * 日期：2017年6月15日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package RecServer;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.locks.ReentrantLock;

import Config.CenterConfig;
import Config.ConfigModel;
import JNetSocket.MulticastClient;
import JNetSocket.UDPClient;
import Model.MasterModel;
import Model.StromCenterModel;
import Util.FactoryPackaget;
import Util.ServerInfo;

/**    
 *     
 * 项目名称：DataStrom    
 * 类名称：CenterTimer    
 * 类描述：  定时检查
 * 1 发布新添加的服务
 * 2 交互服务信息
 * 3 检查各注册中心状态
 * 4 及时确认注册中心是否停止
 * 5 选举通知主注册中心  
 * 创建人：jinyu    
 * 创建时间：2017年6月15日 上午1:53:48    
 * 修改人：jinyu    
 * 修改时间：2017年6月15日 上午1:53:48    
 * 修改备注：    
 * @version     
 *     
 */
public class CenterTimer {
    private static Thread  stateReset=null;//检查线程
    
    private static ReentrantLock lock=new ReentrantLock();
    private static boolean isRuning;//控制线程启动
    private static LinkedBlockingQueue<ServerInfo> newadd=new LinkedBlockingQueue<ServerInfo>();
   private  static LinkedTransferQueue<ConfigModel> masterVote=new LinkedTransferQueue<ConfigModel>();
    /**
     * 新接受的服务信息
     * 及时通知更新新服务
     */
    public static void addServer(ServerInfo info)
    {
        newadd.offer(info);
    }
    /*
     * 与主中心选举相关的包
     * 直接把本中心配置加入
     */
    public static  void addMaster(ConfigModel model)
    {
        try {
            masterVote.transfer(model);
        } catch (InterruptedException e) {
           
            e.printStackTrace();
        }
    }
    /*
     * 启动线程及时发送本中心接受的新的服务信息
     * 及时更新新服务
     */
    public static void startThread()
    {
            try
            {
            if(stateReset==null)
            {
                stateReset=new   Thread(new Runnable()
                        {
                           @Override
                           public void run() {
                               FactoryPackaget f=new FactoryPackaget();
                               MulticastClient client=new MulticastClient();//组播通讯
                              while(isRuning)
                              {
                               try {
                                   
                                        ServerInfo tmp=newadd.take();
                                        byte[]data= f.unDataModel(tmp);
                                        client.sendData(CenterConfig.localCenter.multIP, CenterConfig.localCenter.port, data);
                                        
                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               }
                               
                              }
                           }
                    
                        });
                stateReset.setDaemon(true);
                stateReset.setName("sendServerState");
                stateReset.start();
                    
                }
            }
            finally
            {
                lock.unlock();
            }
        }
    
    /*
     * 启动线程发送state
     */
    public static void startStateThread()
    {
            try
            {
         
                Thread  state=new   Thread(new Runnable()
                        {
                           @Override
                           public void run() {
                               FactoryPackaget f=new FactoryPackaget();
                               MulticastClient client=new MulticastClient();//组播通讯
                              while(isRuning)
                              {
                                        //发送范围信息及自己的标识
                                        StromCenterModel tmp=new StromCenterModel();
                                        byte[]data= f.unDataModel(tmp);
                                        client.sendData(CenterConfig.localCenter.multIP, CenterConfig.localCenter.port, data);     
                              }
                           }
                    
                        });
                state.setDaemon(true);
                state.setName("centerState");
                state.start();
            }
            finally
            {
              
            }
        }
    
    /*
     * 启动线程及时处理主注册中心
     */
    public static void startMasterThread()
    {
         
                Thread master=new   Thread(new Runnable()
                        {
                           @Override
                           public void run() {
                               FactoryPackaget f=new   FactoryPackaget();
                               MulticastClient client=new MulticastClient();//组播通讯
                               UDPClient udpClient=new UDPClient();
                              while(isRuning)
                              {
                              
                                   ConfigModel  masterinfo=masterVote.remove();
                                  if(masterinfo==null)
                                  {
                                      //自己设置为主中心
                                  }
                                   //
                                   MasterModel modelask=new MasterModel();
                                   modelask.flage=masterinfo.flage;
                                   modelask.action=masterinfo.action;
                                   modelask.centerByte=masterinfo.centerByte;
                                   modelask.IP=masterinfo.IP;
                                   modelask.multIP=masterinfo.multIP;
                                   modelask.multPort=masterinfo.multPort;
                                   modelask.port=masterinfo.port;
                                   modelask.serverName="";
                                   byte[]data=f.unDataModel(modelask);
                                   //点对点
                                   udpClient.sendData(CenterConfig.localCenter.IP, CenterConfig.localCenter.port, data);
                                   //组播通知
                                   client.sendData(CenterConfig.localCenter.multIP, CenterConfig.localCenter.port, data);
                              }
                           }
                    
                        });
                master.setDaemon(true);
                master.setName("sendmasterstate");
                master.start();
                    
                }

    /*
     * 启动线程及时验证处理主中心是否死亡
     */
    public static void startCheckMasterThread()
    {
         
                Thread check=new   Thread(new Runnable()
                        {
                           @Override
                           public void run() {
                               FactoryPackaget f=new   FactoryPackaget();
                               MulticastClient client=new MulticastClient();//组播通讯
                               UDPClient udpClient=new UDPClient();
                              while(isRuning)
                              {
                                  //如果没有接收到主服务；
                                  //主服务的心跳（包括注册服务信息）
                                  //检查刷新
                                   ConfigModel  masterinfo=masterVote.remove();
                                  if(masterinfo==null)
                                  {
                                      //自己设置为主中心
                                  }
                                   //
                                   MasterModel modelask=new MasterModel();
                                   modelask.flage=masterinfo.flage;
                                   modelask.action=masterinfo.action;
                                   modelask.centerByte=masterinfo.centerByte;
                                   modelask.IP=masterinfo.IP;
                                   modelask.multIP=masterinfo.multIP;
                                   modelask.multPort=masterinfo.multPort;
                                   modelask.port=masterinfo.port;
                                   modelask.serverName="";
                                   byte[]data=f.unDataModel(modelask);
                                   //点对点
                                   udpClient.sendData(CenterConfig.localCenter.IP, CenterConfig.localCenter.port, data);
                                   //组播通知
                                   client.sendData(CenterConfig.localCenter.multIP, CenterConfig.localCenter.port, data);
                              }
                           }
                    
                        });
                check.setDaemon(true);
                check.setName("sendmasterstate");
                check.start();
                    
                }

    
        }
    
    
    

