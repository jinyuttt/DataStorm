/**    
 * �ļ�����NetDataPackaget.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��12��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package FactoryPackaget;

/**    
 *     
 * ��Ŀ���ƣ�NetProtocol    
 * �����ƣ�NetDataPackaget    
 * ��������    
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��12�� ����3:01:12    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��12�� ����3:01:12    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class NetDataPackaget {
    /*
     * ��ԴIp
     */
  public String srcIP;
  /*
  * ��Դ�˿�
  */
  public int srcPort;

  /**
   * ����IP
   */
  public String localIP;

  /*
   * ����IP
   */
  public int localPort;
  /*
  * ͨѶЭ������ 0 tcp  1 udp  2 �鲥 3 �㲥
  */
  public int  netType;

  /*
  * ��ҪsocketͨѶʱ����
  * һ��ֱ�ӱ��ַ���˵�socket����tcp�ͻ���
  */
  public Object socket;//
  
  /**
   *  ��������������
   */
  public byte[] netPackaget;
}