/**    
 * �ļ�����JsonSerializer.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��10��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package DataJsonSerializer;

import com.alibaba.fastjson.JSON;

/**    
 *     
 * ��Ŀ���ƣ�Common    
 * �����ƣ�JsonSerializer    
 * ��������    json���л�
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����2:42:28    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����2:42:28    
 * �޸ı�ע��    
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
        byte[] data=JSON.toJSONBytes(obj, null);
        return data;
       //return   JSON.toJSONBytes(obj, null);
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

