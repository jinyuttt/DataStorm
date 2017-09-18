/**
 * 
 */
package dataStrom.bus.mq;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @author jinyu
 *  MQ使用的Message
 */
public class MQMessage  extends Message {
 private byte[] mqdata;
  public MQMessage(Message msg)
  {
      this.mqname=msg.mqname;
      this.topic=msg.topic;
      this.cmd=msg.cmd;
      this.data=msg.data;
      this.status=msg.status;
      this.charset=msg.charset;
      this.msgid=msg.msgid;
  }
  
  /**
   * 调用 convertToMsg()转换
   * @param data
   */
  public MQMessage(byte[]data)
  {
      mqdata=data;
  }
  public MQMessage(byte[]data,boolean ismsg)
  {
      if(data==null)
      {
          return;
      }
      mqdata=data;
      if(ismsg)
      {
          Message msg=this.convertToMsg();
          this.cmd=msg.cmd;
          this.data=msg.data;
          this.topic=msg.topic;
          this.mqname=msg.mqname;
      }
  }
  public MQMessage()
  {
      
  }
  public String mqname()
  {
      return this.mqname;
  }
  public String topicname()
  {
      return this.topic;
  }
  public MQCmd cmd() 
  {
      return this.cmd;
  }
  public byte[] getBody()
  {
      return this.data;
  }
  public String getMsg()
  {
      
      return new String(this.data);
  }
  
  /**
   * 数据转byte[]
   * @return
   */
   public byte[] convertToData()
   {
       try {
        byte[]name=mqname.getBytes(charset);
        byte[]topic=this.topic.getBytes(charset);
        byte[]cmd=this.cmd.name().getBytes(charset);
        byte[]msgBody=new byte[name.length+topic.length+cmd.length+data.length+12];
        ByteBuffer buf=ByteBuffer.wrap(msgBody);
        buf.putInt(name.length);
        buf.put(name);
        buf.putInt(topic.length);
        buf.put(topic);
        buf.putInt(cmd.length);
        buf.put(cmd);
       // buf.putInt(data.length);
        buf.put(data);
        buf.flip();
        return   buf.array();
    } catch (UnsupportedEncodingException e) {
     
        e.printStackTrace();
    }
    return null;
       
   }
   public Message convertToMsg()
   {
       if(mqdata==null||mqdata.length==0)
       {
           return null;
       }
       ByteBuffer buf=ByteBuffer.wrap(mqdata);
       Message msg=new Message();
       int nameLen=buf.getInt();
       byte[] name=new byte[nameLen];
       buf.get(name);
       msg.setMQ(new String(name));
       int topicLen=buf.getInt();
       byte[] topic=new byte[topicLen];
       buf.get(topic);
       msg.setTopic(new String(topic));
      int cmdLen=buf.getInt();
      byte[] cmd=new byte[cmdLen];
      buf.get(cmd);
      msg.cmd=MQCmd.valueOf(new String(cmd));
      byte[]msgBody=new byte[buf.limit()-buf.position()];
      buf.get(msgBody);
      msg.setBody(msgBody);
      return msg;
   }
public String getId() {
   
    return msgid;
}
public String getBodyString(String encoding) {
  if(this.data!=null)
  {
      String str;
    try {
        str = new String(data,0,data.length,encoding);
    } catch (UnsupportedEncodingException e) {
        str = new String(data);
    }
      return str;
  }
    return null;
}
}
