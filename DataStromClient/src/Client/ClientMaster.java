/**    
 * �ļ�����ClientMaster.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��22��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package Client;

import ClientChoice.WeightRandom;

/**    
 *     
 * ��Ŀ���ƣ�DataStromClient    
 * �����ƣ�ClientMaster    
 * ��������    ׼������
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��22�� ����12:27:48    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��22�� ����12:27:48    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class ClientMaster {
public static volatile String masterIP="192.168.3.139";
public static volatile int  port=3333;
//��һ��Ҫ�ȴ�master����
public static volatile boolean isBack=true;
//ÿ1wȡ��1��
public static WeightRandom random=new WeightRandom(10000,"wc");
}