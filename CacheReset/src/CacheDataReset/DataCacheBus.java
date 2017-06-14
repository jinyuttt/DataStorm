/**    
 * 文件名：DataCacheBus.java    
 *    
 * 版本信息：    
 * 日期：2017年6月10日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package CacheDataReset;

import DataBus.CacheData;
import DataBus.CacheTimeListenter;

/**    
 *     
 * 项目名称：DataStromServer    
 * 类名称：DataCacheBus    
 * 类描述：  内存缓存外层封装  
 * 创建人：jinyu    
 * 创建时间：2017年6月10日 下午3:32:09    
 * 修改人：jinyu    
 * 修改时间：2017年6月10日 下午3:32:09    
 * 修改备注：    
 * @version     
 *     
 */
public class DataCacheBus {
    private static DataCacheBus instance=null;
    public  static   boolean isLoadDB=false;
    public static DataCacheBus getInstance()
    {
        if(instance==null)
        {
            instance=new DataCacheBus();
        }
        return instance;
    }
    CacheData<String,CacheModel> cacheFlage=new CacheData<String,CacheModel>(100000, 120,isLoadDB);
    CacheData<Long,CacheModel> cache=new CacheData<Long,CacheModel>(100000, 120,isLoadDB);
    public  void put(String flage,CacheModel model)
    {
        cacheFlage.put(flage, model);
    }
    public  CacheModel get(String key)
    {
      return   cacheFlage.getByKey(key);
    }
    public  void put(long flage,CacheModel model)
    {
        cache.put(flage, model);
    }
    public  CacheModel get(long key)
    {
      return   cache.getByKey(key);
    }
    public void setStringListenter(CacheTimeListenter<String , CacheModel> listener)
    {
        cacheFlage.setListenter(listener);
    }
    public void setLongListenter(CacheTimeListenter<Long , CacheModel> listener)
    {
        cache.setListenter(listener);
    }
    public void remove(String key)
    {
        cacheFlage.remove(key);
    }
    public void remove(long key)
    {
        cache.remove(key);
    }
}
