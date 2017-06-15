/**    
 * �ļ�����ListenerLongCache.java    
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
 * �����ƣ�ListenerLongCache    
 * ��������    ��long��Ϊkey���洦��
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����4:28:49    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����4:28:49    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class ListenerLongCache extends CacheTimeListenter<Long, CacheModel>{

    @Override
    public void onRemoval(RemovalNotification<Long, CacheModel> data) {
        Long key=   data.getKey();
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
         DBClearTimer.addLong(key);
        
    }

    @Override
    public void putDB(Long key, CacheModel v) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public CacheModel getDB(Long key) {
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