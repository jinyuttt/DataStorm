/**    
 * �ļ�����ReqPackaget.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��10��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package Util;

/**    
 *     
 * ��Ŀ���ƣ�DataStromUtil    
 * �����ƣ�ReqPackaget    
 * ��������   �ͻ�������� (��ע�����ķ��͵İ���
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����1:52:33    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����1:52:33    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class ReqPackaget  extends IDataPackaget{
    public ReqPackaget()
    {
        this.packagetType=2;
    }
public  String  serverName;
/*
 * 0 ������ת����
 * 1 ��ȡ�����ַ
 */
public  byte  reqType;

/*
 * ת��ʱ������
 */
public  byte[] args;

/*
 * ���ݰ�ID
 */
public int packagetID;

/**
 * ת��ʱ����Դ��ַ
 */
public String srcAddr="";
}
