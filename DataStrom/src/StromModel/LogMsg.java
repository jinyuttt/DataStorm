/**    
 * �ļ�����LogMsg.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��18��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package StromModel;

/**    
 *     
 * ��Ŀ���ƣ�DataStrom    
 * �����ƣ�LogMsg    
 * ��������    
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��18�� ����11:37:57    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��18�� ����11:37:57    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class LogMsg {
 public LogMsg()
 {
     buf=new StringBuffer();
 }
/**
 * ��Ϣ����
 */
public  int level;

/*
 * ��Ϣ����
 */
public String msg="";
public Object objMsg=null;
private StringBuffer buf=null;
public void addmsg(String msg)
{
    buf.append(msg);
    buf.append(",");
}
public String toString()
{
 return   buf.toString();
}
}