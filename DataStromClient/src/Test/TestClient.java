/**    
 * 文件名：TestClient.java    
 *    
 * 版本信息：    
 * 日期：2017年6月18日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package Test;

import Client.StromClient;
import Util.FactoryPackaget;
import Util.ReqPackaget;
import Util.RspPackaget;
import Util.ServerInfo;

/**    
 *     
 * 项目名称：DataStromClient    
 * 类名称：TestClient    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2017年6月18日 下午11:57:49    
 * 修改人：jinyu    
 * 修改时间：2017年6月18日 下午11:57:49    
 * 修改备注：    
 * @version     
 *     
 */
public class TestClient {
  
    public static void main(String[] args) {
        FactoryPackaget f=new FactoryPackaget();
        StromClient client=new StromClient();
        ReqPackaget req=new ReqPackaget();
        req.reqType=1;
        req.serverName="TestServer";
        client.sendRequest(req);
        RspPackaget rsp=   client.recCallData();
        if(rsp!=null)
        {  
        ServerInfo info= (ServerInfo) f.unPackaget(rsp.result);
       if(info!=null)
        System.out.println(info.serverName+"，"+info.IP+","+info.port);
        }
        else
        {
            System.out.println("返回超时");
        }

    }

}
