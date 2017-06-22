/**    
 * 文件名：TestServer.java    
 *    
 * 版本信息：    
 * 日期：2017年6月19日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package Test;

import java.io.IOException;

import StromRegister.ProcessRegister;
import StromServer.RegisterServer;
import Util.ServerInfo;

/**    
 *     
 * 项目名称：DataStromServer    
 * 类名称：TestServer    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2017年6月19日 上午12:00:38    
 * 修改人：jinyu    
 * 修改时间：2017年6月19日 上午12:00:38    
 * 修改备注：    
 * @version     
 *     
 */
public class TestServer {

    /**    
       

    */
    public static void main(String[] args) {
        RegisterServer reg=new RegisterServer();
      ServerInfo info=new ServerInfo();
      info.IP="192.168.3.139";
      info.port=1111;
      info.serverName="TestServer";
     reg.register(info);
     ProcessServer server=new ProcessServer();
    
     ProcessRegister.addServer("TestServer", server);
    try {
        System.in.read();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    }

}
