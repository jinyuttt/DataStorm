/**    
 * �ļ�����TestServer.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��19��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package Test;

import java.io.IOException;

import StromRegister.ProcessRegister;
import StromServer.RegisterServer;
import Util.ServerInfo;

/**    
 *     
 * ��Ŀ���ƣ�DataStromServer    
 * �����ƣ�TestServer    
 * ��������    
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��19�� ����12:00:38    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��19�� ����12:00:38    
 * �޸ı�ע��    
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
