/**    
 * 文件名：TimerCount.java    
 *    
 * 版本信息：    
 * 日期：2017年6月14日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package DataStrom;

/**    
 *     
 * 项目名称：CacheReset    
 * 类名称：TimerCount    
 * 类描述：    查询中计数
 * 创建人：jinyu    
 * 创建时间：2017年6月14日 上午12:23:30    
 * 修改人：jinyu    
 * 修改时间：2017年6月14日 上午12:23:30    
 * 修改备注：    
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
     * 启动计时器
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
