/**    
 * �ļ�����Session.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��10��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package RecviceData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import FactoryPackaget.ReturnCode;
import FactoryPackaget.SubNetPackaget;
import JNetSocket.UDPClient;


/**    
 *     
 * ��Ŀ���ƣ�DataStromUtil    
 * �����ƣ�Session    
 * ��������    
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����5:17:26    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����5:17:26    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class Session {
  public Session(int num)
  {
     buffer=new ReceiveBuffer(num,0);
  }
  /*
   * ��ԴIp
   */
public String srcIP;
/*
* ��Դ�˿�
*/
public int srcPort;

/**
 * ����IP
 */
public String localIP;

/*
 * ����IP
 */
public int localPort;
/*
* ͨѶЭ������ 0 tcp  1 udp  2 �鲥 3 �㲥
*/
public int  netType;

/*
* ��ҪsocketͨѶʱ����
* һ��ֱ�ӱ��ַ���˵�socket����tcp�ͻ���
*/
public Object socket;//
public long id;
public String flage;
private ReceiveBuffer buffer;
private AppData currentChunk=null;

/**
 * ��������
 */
public void addData(ReturnCode  data)
{
    if(data.isAck)
    {
        return;
    }
    AppData buf=new AppData(data.PackagetID, data.data);
    setData(buf);
    
}

/*
 * ����
 */
private boolean  setData(AppData data)
{
    return buffer.offer(data);
}
public byte[] read()
{
    ArrayList<byte[]> list=new   ArrayList<byte[]>();
    int len=0;
    while(true)
    {
            updateCurrentChunk(false);//ȡһ������
            while(currentChunk!=null){
                byte[]data=currentChunk.data;
                list.add(data);
                len+=data.length;
                currentChunk=null;
                updateCurrentChunk(true);
            }
           if(buffer.isEmpty())
           {
               //��ȡ���
               byte[] all=new byte[len];
               int offset=0;
               for(int i=0;i<list.size();i++)
               {
                   byte[] tmp=list.get(i);
                   System.arraycopy(tmp, 0, all, offset, tmp.length);
                   offset+=tmp.length;
               }
               list.clear();
               return all;
               }
        
    }
         
}
private void updateCurrentChunk(boolean block)
{
    if(currentChunk!=null)return;
    while(true){
        try{
            if(block){
                 currentChunk=buffer.poll(1, TimeUnit.MILLISECONDS);//����ȡ��
                while (currentChunk==null){
                    //ѭ��ֱ��ȡ��
                    currentChunk=buffer.poll(1000, TimeUnit.MILLISECONDS);
                    if(currentChunk==null)
                    {
                        break;
//                        //���ܶ�����
//                        UDPClient client=new UDPClient();
//                        AckPackaget ack=new AckPackaget();
//                        ack.packagetID=buffer.waitSequenceNumber();
//                        ack.sessionid=this.id;
//                        ack.ackType=2;
//                        byte[]data= SubNetPackaget.createAckPackaget(ack);
//                        client.sendData(this.srcIP, this.srcPort, data);
//                        break;
                        
                    }
                }
            }
            else currentChunk=buffer.poll(100, TimeUnit.MILLISECONDS);
           //����ȡ��ȡ��ִ��
        }catch(InterruptedException ie){
            IOException ex=new IOException();
            ex.initCause(ie);
        }
        return;//����ѭ��
    }
}
}
