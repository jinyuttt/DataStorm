/**    
 * 文件名：MasterProcess.java    
 *    
 * 版本信息：    
 * 日期：2017年6月15日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package DataProcess;

import com.google.common.eventbus.AllowConcurrentEvents;

import Config.CenterConfig;
import Config.ConfigModel;
import Model.MasterModel;
import RecServer.CenterTimer;


/**    
 *     
 * 项目名称：DataStrom    
 * 类名称：MasterProcess    
 * 类描述：   处理主注册中心信息 
 * 创建人：jinyu    
 * 创建时间：2017年6月15日 下午10:38:55    
 * 修改人：jinyu    
 * 修改时间：2017年6月15日 下午10:38:55    
 * 修改备注：    
 * @version     
 *     
 */
public class MasterProcess {
    @AllowConcurrentEvents
    public void recMasterAsk(MasterModel req)
    {
        if(req.centerByte==1)
        {
            if(CenterConfig.masterCenter==null)
            {
                ConfigModel model=new ConfigModel();
                model.action=req.action;
                model.centerByte=1;
                model.flage=req.flage;
                model.intflage=Integer.valueOf(req.flage);
                model.IP=req.IP;
                model.port=req.port;
                model.multIP=req.multIP;
                model.multPort=req.multPort;
                CenterConfig.masterCenter=model;
            }
            else
            {
                //
            }
        }
        else if(req.centerByte==2)
        {
            ConfigModel model=CenterConfig.localCenter;
            if(model.flage.equals(req.flage)&&model.IP.equals(req.IP)&&model.port==req.port)
            {
                //说明是自己
               //
            }
            else
            {
                //告诉对方已经有主中心
                //发送主服务,如果自己是主中心
                //自己是主中心
                if(model.centerByte==1)
                {
                    CenterTimer.addMaster(model);
                }
            }
        }
    }
}
