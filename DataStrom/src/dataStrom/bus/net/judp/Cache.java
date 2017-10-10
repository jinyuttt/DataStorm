/**
 * 
 */
package dataStrom.bus.net.judp;

/**
 * @author jinyu
 *
 */
public class Cache {
public long sessionid=0;
private byte[][]cachedata=null;
public void add(byte[][]data)
{
    //
    cachedata=new byte[data.length][];
    for(int i=0;i<data.length;i++)
    {
        byte[]tmp=new byte[data[i].length];
        System.arraycopy(data[i], 0, tmp, 0, tmp.length);
        cachedata[i]=tmp;
    }
}
public byte[] getData(int index)
{
    return cachedata[index];
}
public void clear()
{
    cachedata=null;
}
}
