/**    
 * �ļ�����YWModel.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��10��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package ComModel;

import Util.IDataPackaget;

/**    
 *     
 * ��Ŀ���ƣ�BillModel    
 * �����ƣ�YWModel    
 * ��������   ҵ��model ��������
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����6:26:09    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����6:26:09    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class YWModel {
    /*
     * ��ԴIp
     */
public String srcIP;
/*
 * ��Դ�˿�
 */
public int srcPort;
/*
 * ����
 */
public IDataPackaget data;

/**
 * �Ƿ���Ҫ�ش�����
 */
public boolean reqCall=false;
/*
 * ͨѶЭ������ 0 tcp  1 udp  2 �鲥 3 �㲥
 */
public int  netType;

/*
 * ��ҪsocketͨѶʱ����
 * һ��ֱ�ӱ��ַ���˵�socket����tcp�ͻ���
 */
public Object socket;//
}
