/**    
 * �ļ�����DataPackaget.java    
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
 * �����ƣ�DataPackaget    
 * �������� ������������ݰ�
 * ��ͻ��˷���˵Ľ���
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����5:27:55    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����5:27:55    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class DataPackaget extends IDataPackaget {
    public DataPackaget()
    {
        this.packagetType=4;
    }
    public  String  serverName;
    /*
     * 0 ����Ҫ����
     * 1  ��Ҫ����
     */
    public  byte  reqCall;

    /*
     * ת��ʱ������
     */
    public  byte[] data=new byte[0];

    /*
     * ���ݰ�ID
     */
    public int packagetID;
    
}
