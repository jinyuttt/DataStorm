/**
 * 
 */
package dataStrom.bus.rpc;

import java.nio.ByteBuffer;

import dataStrom.bus.mq.RPCCode;

/**
 * @author jinyu
 * �������߻�ִ
 */
public class Heart {
public String name="";
public String address="";
public  long taskid=-1;//id��Ϊ-1ʱ���Ǽ��ӣ���Ȼ��������
public  long rpcID=-1;//rpcID ���������У�����Ҳ�����У�׼���ֶ�

/**
 * ����ʱʹ��
 */
public RPCCode result=RPCCode.sucess;//����ʱʹ��
public byte rpcType=0;//0�Ǹ��ؾ��⣬1�����Ӹ���
private String charset="utf-8";

/*
 * ת����������
 */
public byte[] convertToData()
{
    try
    {
    byte[] namebytes=name.getBytes(charset);
    byte[]addrbytes=address.getBytes(charset);
    byte[] rbytes=result.name().getBytes(charset);
    byte[]data=new byte[namebytes.length+addrbytes.length+rbytes.length+8+8+4+4+1];
    ByteBuffer buf=ByteBuffer.wrap(data);
    buf.putLong(taskid);
    buf.putLong(rpcID);
    buf.putInt(namebytes.length);
    buf.put(namebytes);
    buf.putInt(addrbytes.length);
    buf.put(addrbytes);
    buf.put(rpcType);
    buf.put(rbytes);
    buf.flip();
    return buf.array();
    }
    catch(Exception ex)
    {
        
    }
   return null;
}

/*
 * ת��model����
 */
public void convertToModel(byte[] data)
{
    try
    {
 
    ByteBuffer buf=ByteBuffer.wrap(data);
    this.taskid=buf.getLong();
    this.rpcID=buf.getLong();
    int nameLen=buf.getInt();
    byte[] namebytes=new byte[nameLen];
     buf.get(namebytes);
     this.name=new String(namebytes);
     int addrLen=buf.getInt();
     byte[]addrbytes=new byte[addrLen];
     buf.get(addrbytes);
     this.address=new String(addrbytes);
     this.rpcType=buf.get();
     byte[] rbytes=new byte[buf.limit()-buf.position()];
     buf.get(rbytes);
     String code=new String(rbytes);
     this.result=RPCCode.valueOf(code);
     
    }
    catch(Exception ex)
    {
        
    }
}
}
