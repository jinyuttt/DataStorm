/**
 * 
 */
package StromModel;

import DataStrom.TimerCount;

/**
 * @author jinyu
 *
 */
public class ServerModel {
/**
 * ����IP
 */
public String IP;
/**
 * ����˿�
 */
public int port;

/**
 * ��������
 */
public String name;
/*
 * ͨѶ��ʽ
 */
public byte netType;

/*
 * �Ƿ������ӷ���
 */
public boolean master_slave;

/*
 * �Ƿ���������
 */
public boolean isMaster;

/*
 * �Ƿ���
 */
private boolean action=true;

public String falge;

/*
 * �жϴ��ʱ�䣨�룩
 */
private int timeFlash=3;

private long flashCount=0;

/**
 * �жϷ�����
 */
public boolean isAction()
{
    if(TimerCount.CountTime()-flashCount>timeFlash)
    {
        action=false;
    }
    return action;
}
public void update()
{
    action=true;
    flashCount=TimerCount.CountTime();
    
}
public boolean equals(Object obj)
{
    if (this == obj)      //����Ķ���������Լ�����s.equals(s)���϶�����ȵģ�  
        return true;   
    if (obj == null)     //�������Ķ����ǿգ��϶������  
        return false;  
    if (getClass() != obj.getClass())  //�������ͬһ�����͵ģ���Studnet���Animal�࣬  
                                       //Ҳ���ñȽ��ˣ��϶��ǲ���ȵ�  
        return false;  
    ServerModel other = (ServerModel) obj;       
    if (name == null) {  
        if (other.name != null)  
            return false;  
    } else if (!name.equals(other.name))   //���name������ȣ������  
        return false;  
    if(other.IP.equals(IP)&&other.port==port)
    {
        return true;
    }
    else
    {
        return false;  
    }
}

}