/**    
 * 文件名：CacheBus.java    
 *    
 * 版本信息：    
 * 日期：2017年6月10日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package CacheDataReset;

import DBTimer.DBClearTimer;
import DBTimer.TimerCount;

/**    
 *     
 * 项目名称：DataStromServer    
 * 类名称：CacheBus    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2017年6月10日 下午3:51:53    
 * 修改人：jinyu    
 * 修改时间：2017年6月10日 下午3:51:53    
 * 修改备注：    
 * @version     
 *     
 */
public class CacheBus {
  private static  DataCacheBus  obj=null;
  public  static   boolean isLoadDB=false;
  static TimerCount timer=null;
  static DBClearTimer dbclear=null;
  public static DataCacheBus getInstance()
  {
      if(obj==null)
      {
          if(isLoadDB)
          {
               timer=new TimerCount();
               timer.startTimer();
               dbclear=new DBClearTimer();
               dbclear.startThread();
          }
          DataCacheBus.isLoadDB=isLoadDB;
          obj=DataCacheBus.getInstance();
          ListenerStringCache listenerStr=new ListenerStringCache();
          ListenerLongCache  listenerLong=new ListenerLongCache();
          obj.setStringListenter(listenerStr);
          obj.setLongListenter(listenerLong);
      }
      return obj;
  }
}
