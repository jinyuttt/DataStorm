/**    
 * �ļ�����RandomFlage.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��15��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package RecServer;


import java.util.Random;
import java.util.UUID;

/**    
 *     
 * ��Ŀ���ƣ�DataStrom    
 * �����ƣ�RandomFlage    
 * ��������    �����ʶ
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��15�� ����11:24:32    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��15�� ����11:24:32    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class RandomFlage {
    /*
     * ����ID��ʶ
     */
public static  int  getFlage()
{
    Random r = new Random(UUID.randomUUID().hashCode());
    return  r.nextInt(1000);
}
}