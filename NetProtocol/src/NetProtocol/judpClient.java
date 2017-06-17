/**    
 * 文件名：judp.java    
 *    
 * 版本信息：    
 * 日期：2017年6月11日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package NetProtocol;



import java.util.LinkedList;

import CacheDataReset.CacheOrg;
import FactoryPackaget.SubNetPackaget;

import JNetSocket.UDPClient;
import NetPackaget.PackagetRandom;
import NetPackaget.SubPackaget;

/**    
 *     
 * 项目名称：NetProtocol    
 * 类名称：judp    
 * 类描述：   upp客户端
 * 被封装发送 
 * 创建人：jinyu    
 * 创建时间：2017年6月11日 下午7:12:40    
 * 修改人：jinyu    
 * 修改时间：2017年6月11日 下午7:12:40    
 * 修改备注：    
 * @version     
 *     
 */
public class judpClient {
    UDPClient client=new UDPClient();
    boolean isColse=false;
    /**
     * 发送数据
     * 
     */
    public void sendData(String sIP,int sPort,byte[]data)
    {
       LinkedList<byte[]> list=SubPackaget.subData(data);
      if(list!=null)
    {
        long sessionid=PackagetRandom.getSessionID();
        long initseq=PackagetRandom.getSequeueID();
        int num=list.size();
        while(list.size()>0)
        {
             long packagetID=PackagetRandom.getInstanceID(this);
             byte[] sendData=SubNetPackaget.createNetPackaget(sessionid,initseq,packagetID,num,list.removeFirst());
            client.sendData(sIP, sPort, sendData);
            putCache(sessionid,packagetID,sendData,sIP,sPort);
        }
    }
    }
    /*
     * 发送数据
     * 绑定本地地址
     */
    public void sendData(String  localIP,int  localPort, String sIP,int sPort,byte[]data)
    {
        LinkedList<byte[]> list=SubPackaget.subData(data);
        if(list!=null)
        {
          long sessionid=PackagetRandom.getSessionID();
          long initseq=PackagetRandom.getSequeueID();
            int num=list.size();
            while(list.size()>0)
            {
                 long packagetID=PackagetRandom.getInstanceID(this);
                 byte[] sendData=SubNetPackaget.createNetPackaget(sessionid,initseq,packagetID,num,list.removeFirst());
                 client.bindLocal(localIP,localPort);
                 client.sendData(sIP, sPort, sendData);
                putCache(sessionid,packagetID,sendData,sIP,sPort);
            }
        }
      
    }
    
    /**
     * 
     * 保存
    
     */
    private void putCache(long sessionid,long packagetid ,byte[]data,String host,int port)
    {
        String key=String.valueOf(sessionid)+String.valueOf(packagetid);
        CacheOrg.put(key, client, data, host, port);
    }
    
    /**
     * 逻辑上关闭
     */
    public void close()
    {
        isColse=true;
        client.protocolClose();
    }
    /**
     * 返回逻辑关闭
     */
    public boolean isClose()
    {
        return isColse;
    }
    
    /*
     * 接收数据
     */
    public byte[]  getCallBackData()
    {
       return client.getCallBackData();
    }
}
