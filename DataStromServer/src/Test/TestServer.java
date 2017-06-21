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
       
     * main(这里用一句话描述这个方法的作用)    
       
     * TODO(这里描述这个方法适用条件 – 可选)    
       
     * TODO(这里描述这个方法的执行流程 – 可选)    
       
     * TODO(这里描述这个方法的使用方法 – 可选)    
       
     * TODO(这里描述这个方法的注意事项 – 可选)    
       
     * @param   name    
       
     * @param  @return    设定文件    
       
     * @return String    DOM对象    
       
     * @Exception 异常对象    
       
     * @since  CodingExample　Ver(编码范例查看) 1.1    
       
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
