/**    
 * �ļ�����ProcessRegister.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��10��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package StromRegister;



import IServer.IProcessServer;
import ListernerServer.CurrentServer;
import ListernerServer.ServerTimer;

/**    
 *     
 * ��Ŀ���ƣ�DataStromServer    
 * �����ƣ�ProcessRegister    
 * ������:����ҵ����,��ӷ����Ӧ�Ĵ�����
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����5:12:35    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����5:12:35    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class ProcessRegister {
  private static volatile boolean isStart=true;
  
  /**
   * ע�������
   */
public  static void addServer(String name,IProcessServer process)
{
    CurrentServer.addServer(name, process);
    if(isStart)
    {
        isStart=false;
        ServerTimer.startThread();
    }
}

/*
 * �Ƴ�������
 */
public static void removeServer(String name)
{
    CurrentServer.removeServer(name);
}

}
