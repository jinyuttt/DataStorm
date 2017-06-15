/**    
 * �ļ�����ListenerServer.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��10��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package ListernerServer;

import com.google.common.eventbus.AllowConcurrentEvents;
import ComModel.YWModel;
import DataBus.CacheData;
import IServer.IProcessServer;
import NetPackaget.PackagetRandom;
import NetProtocol.judpClient;


/**    
 *     
 * ��Ŀ���ƣ�DataStromServer    
 * �����ƣ�ListenerServer    
 * ��������    
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����1:33:12    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����1:33:12    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class ListenerRequest{
 
    /**
     * ���淵�ص�������Ϣ
     * �ȴ�����ֵ
     */
    CacheData<Long,CallNetModel> cache=new CacheData<Long,CallNetModel>(100000,30,false);
   @AllowConcurrentEvents
public void receviceData(YWModel data)
{
    //����ҵ��
     //���в���
       if(data.reqCall)
       {
           CallNetModel model=new CallNetModel();
           model.srcIP=data.srcIP;
           model.srcPort=data.srcPort;
           model.socket=data.socket;
           model.netType=data.netType;
           model.id=PackagetRandom.getInstanceID(this);
           //hashMap.put(model.id, model);
           cache.put(model.id, model);
           data.data.sessionid=model.id;
       }
       IProcessServer cur= CurrentServer.getProcess(data.data.serverName);
       cur.recRequest(data.data);
}
  @AllowConcurrentEvents
public void reCallData(ResetCallModel recall)
{
      CallNetModel model=cache.getByKey(recall.id);
    if(model!=null)
    {
        if(model.netType!=0)
        { 
            judpClient client=new judpClient();
            client.sendData(model.srcIP, model.srcPort, recall.data);
        }
    }
}
}
    
