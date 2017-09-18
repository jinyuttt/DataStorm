/**
 * 
 */
package dataStrom.bus.mq;

import java.io.UnsupportedEncodingException;

/**
 * @author jinyu
 * 只允许设置不允许获取
 */
public class Message {
    protected String mqname="";
    protected String topic="";
    protected byte[] data=new byte[0];
    protected String charset="utf-8";
    protected String msgid;
    protected  String status;
    public  MQCmd cmd=MQCmd.MQMsg;
public void setMQ(String name)
{
    this.mqname=name;
}
public void setTopic(String name)
{
    this.topic=name;
}
public void setBody(byte[] data)
{
    this.data=data;
}
public void setBody(String data)
{
    try {
        this.data=data.getBytes(charset);
    } catch (UnsupportedEncodingException e) {
        this.data=data.getBytes();
    }
}
public String getEncoding() {
   
    return charset;
}

public void setId(String msgId) {
  
    this.msgid=msgId;
}
public void setStatus(int status) {
    this.status=""+status;
    
}
public void setStatus(String status) {
    this.status=status;
    
}
public boolean isStatus200() {
    if(status!=null&&status.equals("200"))
    {
        return true;
    }
    return false;
}

}
