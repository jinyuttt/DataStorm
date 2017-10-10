/**
 * 
 */
package dataStrom.bus.net.judp;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;
/**
 * @author jinyu
 *
 */
public class CacheManager {
private static AtomicLong sessionid=new AtomicLong(0);
private HashMap<Long,Cache> hash=new HashMap<Long,Cache>();
public void addData(long sessionid,byte[][]data)
{
    Cache c=new Cache();
    c.sessionid=sessionid;
    c.add(data);
}
public byte[] getData(long sessionid,int id)
{
    Cache data=  hash.get(sessionid);
    if(data==null)
    {
    return null;
    }
    else
    {
      return    data.getData(id);
    }
    
}
}
