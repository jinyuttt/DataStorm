/**    
 * �ļ�����IDataPackaget.java    
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
 * �����ƣ�IDataPackaget    
 * ��������    
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����2:05:59    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����2:05:59    
 * �޸ı�ע��    
 * @version     
 *     
 */
public abstract class IDataPackaget {
    /**
     * ������
     * 0 ������Ϣ
     * 1 ����״̬
     * 2  �ͻ�������
     * 3 ���ذ�
     * 4 ���ݰ�
     * 5 ������Ϣ��
     * 6 ע�����İ�ѯ��
     * 7 ע�����ķ��񽻻�
     */
public  byte packagetType;
/**
 * ��������
 */
public String serverName;

/**
 * session id ֻ��һ����ʶ
 */
public long sessionid;

/*
 * �Ƿ���ȷ��Ҫ��ִ
 * 0����Ҫ
 * 1��Ҫ
 */
public byte rsp=0;
}
