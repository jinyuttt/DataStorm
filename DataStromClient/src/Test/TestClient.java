/**    
 * �ļ�����TestClient.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��18��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
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
 * ��Ŀ���ƣ�DataStromClient    
 * �����ƣ�TestClient    
 * ��������    
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��18�� ����11:57:49    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��18�� ����11:57:49    
 * �޸ı�ע��    
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
        System.out.println(info.serverName+"��"+info.IP+","+info.port);
        }
        else
        {
            System.out.println("���س�ʱ");
        }

    }

}
