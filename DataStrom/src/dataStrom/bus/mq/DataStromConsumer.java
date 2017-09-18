/**
 * 
 */
package dataStrom.bus.mq;

import java.text.SimpleDateFormat;
import java.util.Date;
import dataStrom.bus.net.Session;

/**
 * @author jinyu
 *��������Ϣ
 *RPCʱΪע����Ϣ
 */
public class DataStromConsumer {
public  Session session;
public String  mqname;
public String topic;//
public String address="";//RPCʱ��ַ��MQ,TOPICʱ��Զ�˵�ַ
public String RpcnetType="udp";
/**
 * ע��ʱ��
 */
public long createTime=System.currentTimeMillis();
/**
 * ��ʾ����
 */
public String keeplive="udp";//��ʾ����,Э������


/**
 * ������������
 */
public boolean lifecycle=true;//������������

/**
 * �������
 */
public long time=System.currentTimeMillis();

/**
 * ע��ʱΪrpcid,������ʱ����ID��
 */
public long rpcid=-1;//ע��ʱΪrpcid,������ʱ����ID��

public boolean equals(Object obj) {
    if (obj instanceof DataStromConsumer) {
        DataStromConsumer name = (DataStromConsumer) obj;
        return (session.getRemoteAddress().equals(name.session.getRemoteAddress()));
    }
    return super.equals(obj);
}
public String getDate()
{
   
    SimpleDateFormat dateFm = new SimpleDateFormat(DateTime.staticFormat); //��ʽ����ǰϵͳ����
    String dateTime = dateFm.format(new java.util.Date());
    return dateTime;
}
public String getDate(Date date)
{
    SimpleDateFormat dateFm = new SimpleDateFormat(DateTime.staticFormat); //��ʽ����ǰϵͳ����
    String dateTime = dateFm.format(date);
    return dateTime;
}
public String getDate(long date)
{
    Date tmp=new Date(date);
    SimpleDateFormat dateFm = new SimpleDateFormat(DateTime.staticFormat); //��ʽ����ǰϵͳ����
    String dateTime = dateFm.format(tmp);
    return dateTime;
}
}
