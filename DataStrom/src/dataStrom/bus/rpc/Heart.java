/**
 * 
 */
package dataStrom.bus.rpc;

import java.nio.ByteBuffer;

import dataStrom.bus.mq.RPCCode;

/**
 * @author jinyu
 * 心跳或者回执
 */
public class Heart {
public String name="";
public String address="";
public  long taskid=-1;//id不为-1时就是监视，不然就是心跳
public  long rpcID=-1;//rpcID 心跳不会有，监视也不会有，准备字段

/**
 * 监视时使用
 */
public RPCCode result=RPCCode.sucess;//监视时使用
public byte rpcType=0;//0是负载均衡，1是主从复制
private String charset="utf-8";

/*
 * 转换传输数据
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
 * 转换model数据
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
