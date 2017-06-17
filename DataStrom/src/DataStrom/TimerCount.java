/**    
 * �ļ�����TimerCount.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��14��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package DataStrom;

/**    
 *     
 * ��Ŀ���ƣ�CacheReset    
 * �����ƣ�TimerCount    
 * ��������    ��ѯ�м���
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��14�� ����12:23:30    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��14�� ����12:23:30    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class TimerCount {
    private static volatile  long count=0;
    private static volatile boolean isStart=false;
    public  boolean isStart()
    {
        return isStart;
    }
    public static long  CountTime()
    {
        return count;
    }
   
    /*
     * ������ʱ��
     */
public  synchronized    void   startTimer()
{
    if(isStart)
    {
        return;
    }
    isStart=true;
    final long timeInterval = 1000;  
    Runnable runnable = new Runnable() {  
        public void run() {  
            while (true) {  
                try {  
                    Thread.sleep(timeInterval);  
                    count++;
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    };  
    Thread thread = new Thread(runnable);  
    thread.setDaemon(true);
    thread.setName("TimerCount");
    thread.start();  
}
}
