/**
 * 
 */
package dataStrom.bus.mq;

import java.text.SimpleDateFormat;
import java.util.Date;
import dataStrom.bus.net.Session;

/**
 * @author jinyu
 *消费者消息
 *RPC时为注册信息
 */
public class DataStromConsumer {
public  Session session;
public String  mqname;
public String topic;//
public String address="";//RPC时地址，MQ,TOPIC时的远端地址
public String RpcnetType="udp";
/**
 * 注册时间
 */
public long createTime=System.currentTimeMillis();
/**
 * 表示网络
 */
public String keeplive="udp";//表示网络,协议类型


/**
 * 还在生命周期
 */
public boolean lifecycle=true;//还在生命周期

/**
 * 心跳检查
 */
public long time=System.currentTimeMillis();

/**
 * 注册时为rpcid,消费者时分配ID号
 */
public long rpcid=-1;//注册时为rpcid,消费者时分配ID号

public boolean equals(Object obj) {
    if (obj instanceof DataStromConsumer) {
        DataStromConsumer name = (DataStromConsumer) obj;
        return (session.getRemoteAddress().equals(name.session.getRemoteAddress()));
    }
    return super.equals(obj);
}
public String getDate()
{
   
    SimpleDateFormat dateFm = new SimpleDateFormat(DateTime.staticFormat); //格式化当前系统日期
    String dateTime = dateFm.format(new java.util.Date());
    return dateTime;
}
public String getDate(Date date)
{
    SimpleDateFormat dateFm = new SimpleDateFormat(DateTime.staticFormat); //格式化当前系统日期
    String dateTime = dateFm.format(date);
    return dateTime;
}
public String getDate(long date)
{
    Date tmp=new Date(date);
    SimpleDateFormat dateFm = new SimpleDateFormat(DateTime.staticFormat); //格式化当前系统日期
    String dateTime = dateFm.format(tmp);
    return dateTime;
}
}
