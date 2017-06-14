/**    
 * 文件名：JsonSerializer.java    
 *    
 * 版本信息：    
 * 日期：2017年6月10日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package DataJsonSerializer;

import com.alibaba.fastjson.JSON;

/**    
 *     
 * 项目名称：Common    
 * 类名称：JsonSerializer    
 * 类描述：    json序列化
 * 创建人：jinyu    
 * 创建时间：2017年6月10日 下午2:42:28    
 * 修改人：jinyu    
 * 修改时间：2017年6月10日 下午2:42:28    
 * 修改备注：    
 * @version     
 *     
 */
public class JsonSerializer {
public  static byte[] serializerObject(Object obj)
{
    if(obj==null)
    {
        return null;
    }
    else
    {
       return   JSON.toJSONBytes(obj, null);
    }
}
public static Object  reserializerObject(byte[]obj)
{
    
        if(obj==null)
        {
            return null;
        }
        else
        {
          return  JSON.parse(obj, null);
        }
    }

 
    
}

