/**    
 * �ļ�����RspPackaget.java    
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
 * �����ƣ�RspPackaget    
 * ��������   �������� �����лظ��������ְ�ȷ��
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����1:52:48    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����1:52:48    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class RspPackaget extends IDataPackaget{
    public RspPackaget()
    {
        this.packagetType=3;
    }
    public  String  serverName;
    public  byte[] result=new byte[0];
    public int packagetID;
}
