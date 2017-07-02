/**    
 * 文件名：ReqProcess.java    
 *    
 * 版本信息：    
 * 日期：2017年6月10日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package DataProcess;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

import Config.CenterConfig;
import DataStrom.ServerBus;
import EventBus.MessageBus;
import NetModel.NetAddress;
import NetPackaget.PackagetRandom;
import NetProtocol.judpClient;
import StromModel.LogMsg;
import StromModel.ServerModel;
import Util.DataPackaget;
import Util.FactoryPackaget;
import Util.ReqPackaget;
import Util.RspPackaget;
import Util.ServerInfo;

/**    
 *     
 * 项目名称：DataStrom    
 * 类名称：ReqProcess    
 * 类描述：    注册中心接收到客户端的请求
 * 创建人：jinyu    
 * 创建时间：2017年6月10日 上午3:06:31    
 * 修改人：jinyu    
 * 修改时间：2017年6月10日 上午3:06:31    
 * 修改备注：    
 * @version     
 *     
 */
public class ReqProcess {
    private FactoryPackaget f=new FactoryPackaget();
    private StromCenterBiil  biil=new StromCenterBiil();
    private   ConcurrentLinkedQueue<ReqPackaget> cache=new ConcurrentLinkedQueue<ReqPackaget>();
  private  ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
  private  volatile boolean isRuning=false;
  @Subscribe
    @AllowConcurrentEvents
public void recRequest(ReqPackaget req)
{
     NetAddress addr= null;
     try
     {
         addr=ServerBus.objSocket.getByKey(String.valueOf(req.sessionid));
     }
     catch(Exception ex)
     {
         System.out.println("没有获取到缓存地址");
     }
    if(CenterConfig.localCenter.centerByte==1)
    {
        //如果自己是master,则处理
     
        if(req.reqType==1)
        {
            //获取服务地址
           //
            ServerModel model= biil.getServerAddr(req.serverName);
            if(addr==null)
            {
                return ;
            }
            judpClient clientl=new judpClient();
            if(model!=null)
            {
                //转换成数据
                    //
                    ServerInfo info=new ServerInfo();
                    info.IP=model.IP;
                    info.port=model.port;
                    info.netType=model.netType;
                    info.serverName=model.name;
                    byte[] tmp=f.unDataModel(info);//服务信息组包
                    RspPackaget rsp=new RspPackaget();
                    rsp.result=tmp;
                    rsp.serverName=model.name;
                    byte[]data=f.unDataModel(rsp);//回执信息组包
                    clientl.sendData(addr.srcIP, addr.srcPort, data);
                    clientl.close();
//                    LogMsg msg=new LogMsg();
//                    msg.level=0;
//                    msg.msg="收到客户端请求";
                 //   MessageBus.post("LogInfo", msg);
                    LogMsg log=new LogMsg();
                    log.level=0;
                    log.msg="接收到服务地址请求:"+model.name+"地址："+addr.srcIP+","+ addr.srcPort;
                    MessageBus.getBus("LogInfo").post(log);
            }
            else
            {
              
                ServerInfo info=new ServerInfo();
                info.IP="";
                info.port=0;
                info.netType=0;
                info.serverName="";
                byte[] tmp=f.unDataModel(info);//服务信息组包
                RspPackaget rsp=new RspPackaget();
                rsp.result=tmp;
                byte[]data=f.unDataModel(rsp);//回执信息组包
                clientl.sendData(addr.srcIP, addr.srcPort, data);
                clientl.close();
                LogMsg log=new LogMsg();
                log.level=0;
                log.msg="没有找到服务信息，回传地址："+addr.srcIP+","+ addr.srcPort;
                MessageBus.getBus("LogInfo").post(log);
            }
        }
        else
        {
            //直接转给服务
            ServerModel model= biil.getServerAddr(req.serverName);
            judpClient clientl=new judpClient();
            if(model!=null)
            {
                  //转换成数据
                    DataPackaget data=new DataPackaget();
                    data.data=req.args;
                    data.sessionid=PackagetRandom.getSequeueID();
                   ServerBus.objSocket.remove(String.valueOf(req.sessionid));//原缓存清除
                   ServerBus.objSocket.put(String.valueOf(data.sessionid),addr);
                    byte[] tmp=f.unDataModel(data);//服务信息组包
                    clientl.sendData(model.IP, model.port, tmp);
                    clientl.close();
                    LogMsg log=new LogMsg();
                    log.msg="传递数据到服务:"+model.name;
                    MessageBus.getBus("LogInfo").post(log);
            }
        }
    }
    else
    {
        //转给master;
        req.srcAddr=addr.srcIP+"#"+addr.srcPort;
        if(CenterConfig.masterCenter!=null&&CenterConfig.masterCenter.action)
        {
            byte[] data=f.unDataModel(req);
            judpClient client=new judpClient();
           client.sendData(CenterConfig.masterCenter.IP, CenterConfig.masterCenter.port, data);
           client.close();
           LogMsg log=new LogMsg();
           log.msg="转发数据给master";
           log.level=1;
           MessageBus.getBus("LogInfo").post(log);
        }
        else
        {
            cache.add(req);
            //存储一定时间
            startThread();
        }
        //
        if(req.rsp==1)
        {
            judpClient clientl=new judpClient();
            //有个明确回复
            ServerInfo info=new ServerInfo();
            info.IP="";
            info.port=0;
            info.netType=0;
            info.serverName="";
            byte[] tmp=f.unDataModel(info);//服务信息组包
            RspPackaget rsp=new RspPackaget();
            rsp.result=tmp;
            byte[]callData=f.unDataModel(rsp);//回执信息组包
            clientl.sendData(addr.srcIP, addr.srcPort, callData);
            clientl.close();
            
        }
    }
    ServerBus.objSocket.remove(String.valueOf(req.sessionid));
}
    
    /*
     * 启动线程发送缓存数据
     * 发送完则线程结束
     */
    private  void startThread()
    {
        if(isRuning)
        {
            return;
        }
        cachedThreadPool.execute(new Runnable(){

            @Override
            public void run() {
                
                judpClient client=new judpClient();
             
             while(isRuning)
             {
                 if(CenterConfig.masterCenter!=null&&CenterConfig.masterCenter.action)
                 {
                     ReqPackaget req=cache.poll();
                     byte[] data=f.unDataModel(req);
                     client.sendData(CenterConfig.masterCenter.IP, CenterConfig.masterCenter.port, data);
                 }
                 else
                 {
                     try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                   
                        e.printStackTrace();
                    }
                 }
                 if(cache.isEmpty())
                 {
                     isRuning=false;
                 }
                     
             }
             client.close();
            }
            
        }); 
    }
}
