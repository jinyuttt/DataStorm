/**
 * 
 */
package dataStrom.bus.net;

import java.util.HashMap;

import dataStrom.bus.Tools.CommTools;

/**
 * @author jinyu
 *
 */
public class NetFactory {
   static  HashMap<String,Class<?>> hash=new  HashMap<String,Class<?>>();
 private  static final String client="_client";
 private  static final String server="_server";
    /*
     * ªÒ»°
     */
public static Server createServer(String name)
{
    String protocol=name+server;
    Class<?> cls= hash.get(protocol);
    if(cls==null)
    {
       cls=  CommTools.getClass(protocol);
       if(cls!=null)
       {
           hash.put(protocol, cls);
       }
    }
    if(cls!=null)
    {
        try {
            Server obj=(Server) cls.newInstance();
            return obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    return null;
    
}
public static Client createClient(String name)
{
    String protocol=name+client;
    Class<?> cls= hash.get(protocol);
    if(cls==null)
    {
       cls=  CommTools.getClass(protocol);
       if(cls!=null)
       {
           hash.put(protocol, cls);
       }
    }
    if(cls!=null)
    {
        try {
            Client obj=(Client) cls.newInstance();
            return obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    return null;
    
}
}
