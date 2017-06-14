/**    
 * 文件名：SessionMap.java    
 *    
 * 版本信息：    
 * 日期：2017年6月12日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package FactoryPackaget;

import java.util.concurrent.ConcurrentHashMap;

/**    
 *     
 * 项目名称：NetProtocol    
 * 类名称：SessionMap    
 * 类描述：    二级存储
 * 创建人：jinyu    
 * 创建时间：2017年6月12日 上午2:11:49    
 * 修改人：jinyu    
 * 修改时间：2017年6月12日 上午2:11:49    
 * 修改备注：    
 * @version     
 *     
 */
public class SessionMap<K,V> {
 private   ConcurrentHashMap<K,V> hashMap=new ConcurrentHashMap<K,V>();
public void put(K key, V value)
{
    hashMap.put(key, value);
}
public V get(K key)
{
  return  hashMap.get(key);
}
}
