/**
 * 
 */
package dataStrom.bus.net;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author jinyu
 * 数据分包
 * 消息 sessionid+id+sendTime+recTime+rsendTime+data
 */
public class PackagetSub {
    public static int  dataSzie=1472;
    private static final int header=12;
    private static  AtomicLong sessionid=new AtomicLong(0);
    
   
   /**
    * 分包
    * @param data
    * @return
    */
public static ArrayList<byte[]> subData(byte[]data)
{
    ArrayList<byte[]>  list=new ArrayList<byte[]>();
    ByteBuffer buf=ByteBuffer.allocate(dataSzie);
    int id=0;
    for(int i=0;i<data.length;i++)
    {
        buf.putLong(sessionid.incrementAndGet());
        if(i+dataSzie-header<data.length)
        {
           buf.put(data, i, dataSzie-header);
           buf.putInt(id);
        }
        else
        {
            buf.put(data, i, data.length-i);
            buf.putInt(id);
        }
        list.add(buf.array());
        id++;
        i+=dataSzie-header;//最后一次就可以跳过
    }
    
    return list;
    
}
}
