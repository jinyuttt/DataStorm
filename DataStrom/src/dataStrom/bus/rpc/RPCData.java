/**
 * 
 */
package dataStrom.bus.rpc;

import java.util.ArrayList;

/**
 * @author jinyu
 * 心跳数据包
 */
public class RPCData {
public ArrayList<PRCAddress> list=new ArrayList<PRCAddress>();
private int index=0;
private volatile int size=0;
public void add(PRCAddress data)
{
	list.add(data);
	size++;
}
public PRCAddress remove()
{
	if(list.isEmpty())
	{
		return null;
	}
	else
	{
    return	list.remove(0);
	}
}
public PRCAddress getRequest()
{	
	PRCAddress addr= list.get(index%size);
	index++;
   return addr;
}
public int getSize()
{
	return list.size();
}
}
