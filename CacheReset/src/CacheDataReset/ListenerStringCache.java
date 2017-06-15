/**    
 * �ļ�����ListenerCache.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��10��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package CacheDataReset;

import com.google.common.cache.RemovalNotification;

import DBTimer.DBClearTimer;
import DataBus.CacheTimeListenter;
import DataJsonSerializer.JsonSerializer;
import JNetSocket.UDPClient;

/**    
 *     
 * ��Ŀ���ƣ�DataStromServer    
 * �����ƣ�ListenerCache    
 * ��������    ��string��Ϊkey����
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����3:49:31    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����3:49:31    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class ListenerStringCache extends CacheTimeListenter<String, CacheModel>{

    @Override
    public void onRemoval(RemovalNotification<String, CacheModel> data) {
     String key=   data.getKey();
     CacheModel v=   data.getValue();
     DBCacheModel model=new DBCacheModel();
      model.remoteHost=v.client.getRemoteHost();
      model.remotePort=v.client.getRemotePort();
      model.localHost=v.client.getLocalHost();
      model.localPort=v.client.getLocalPort();
      model.data=v.data;
      byte[] k=JsonSerializer.serializerObject(key);
      byte[] vbyte=JsonSerializer.serializerObject(v);
      this.put(k, vbyte);
      DBClearTimer.addString(key);
    }

    @Override
    public void putDB(String key, CacheModel v) {
        
    }

    @Override
    public CacheModel getDB(String key) {
       byte[] keys=JsonSerializer.serializerObject(key);
       byte[]  v=this.get(keys);
       DBCacheModel model=(DBCacheModel) JsonSerializer.reserializerObject(v);
       CacheModel cache=new CacheModel();
       cache.data=model.data;
       UDPClient client=new UDPClient();
       client.bindLocal(model.localHost, model.localPort);
       cache.remoteHost=model.remoteHost;
       cache.remotePort=model.remotePort;
       return cache;
       
    }

}