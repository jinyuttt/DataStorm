/**    
 * �ļ�����CacheOrg.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��10��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package CacheDataReset;

import JNetSocket.UDPClient;


/**    
 *     
 * ��Ŀ���ƣ�DataStromServer    
 * �����ƣ�CacheOrg    
 * ��������  ���ݻ��沢��ʱ�־û�
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����4:39:51    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����4:39:51    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class CacheOrg {
public static boolean isLoadDB=true;
public static void put(String key,UDPClient client,byte[]data,String host,int port)
{
    CacheModel cache=new CacheModel();
    cache.data=data;
    cache.client=client;
    cache.remoteHost=host;
    cache.remotePort=port;
    CacheBus.isLoadDB=isLoadDB;
    CacheBus.getInstance().put(key, cache);
}
public static void put(long key,UDPClient client,byte[]data,String host,int port)
{
    CacheModel cache=new CacheModel();
    cache.data=data;
    cache.client=client;
    cache.remoteHost=host;
    cache.remotePort=port;
    CacheBus.isLoadDB=isLoadDB;
    CacheBus.getInstance().put(key, cache);
}
}
